import grpc
from concurrent import futures
from datetime import datetime, timezone
import secrets
import logging

from .auth import (
    can_manage_users, can_manage_inventory, can_manage_events, can_participate_events
)
from .utils import now_utc, hash_password, verify_password, hash_password_bcrypt
from .storage import db, User as UserModel, Donation as DonationModel, Event as EventModel
from . import users_pb2, users_pb2_grpc
from . import inventory_pb2, inventory_pb2_grpc
from . import events_pb2, events_pb2_grpc
from google.protobuf.empty_pb2 import Empty

# Helpers
def require_user(actor_username: str) -> UserModel | None:
    u = db.find_user_by_login(actor_username)
    if not u or not u.active:
        return None
    return u

# ---- UsuariosService ----
class UsuariosService(users_pb2_grpc.UsuariosServiceServicer):
    def CreateUser(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_users(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        if not request.username or not request.name or not request.lastname or not request.email:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Campos obligatorios faltantes")
        # generar clave random y hashear con bcrypt
        plain = secrets.token_urlsafe(10)
        pw_hash = hash_password_bcrypt(plain)  # usar bcrypt
        
        # convertir role a int (aceptar tanto numeric como string)
        try:
            # si request.role es int o convertible a int
            role_int = int(request.role)
        except Exception:
            try:
                # si request.role es string con nombre del enum
                role_int = users_pb2.Role.Value(request.role)
            except Exception as e:
                context.abort(grpc.StatusCode.INVALID_ARGUMENT, f"Rol inválido: {str(e)}")

        u = UserModel(
            id=0,
            username=request.username,
            name=request.name,
            lastname=request.lastname,
            phone=request.phone or "",
            email=request.email,
            role=role_int,
            active=True,
            pw_hash=pw_hash,
            pw_salt="",  # bcrypt no usa salt separado
        )
        try:
            created = db.create_user(u)
        except ValueError as e:
            context.abort(grpc.StatusCode.ALREADY_EXISTS, str(e))
        # Simular envío de email
        print(f"[EMAIL] Enviando clave a {created.email}: {plain}")
        
        return users_pb2.CreateUserResponse(
            user=users_pb2.User(
                id=created.id, username=created.username, name=created.name,
                lastname=created.lastname, phone=created.phone, email=created.email,
                role=int(created.role), active=created.active
            ),
            plain_password=plain
        )

    def UpdateUser(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_users(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")

        # Validar ID
        results = db.db.execute_query("SELECT * FROM usuarios WHERE id = %s", (request.id,))
        if not results:
            context.abort(grpc.StatusCode.NOT_FOUND, "Usuario inexistente")
        row = results[0]

        update_data = {}
        if request.username: update_data['nombre_usuario'] = request.username
        if request.name: update_data['nombre'] = request.name
        if request.lastname: update_data['apellido'] = request.lastname
        if request.phone: update_data['telefono'] = request.phone
        if request.email: update_data['email'] = request.email
        if request.HasField('role'): 
            # Convert numeric enum value to string name for database ENUM column
            role_name = users_pb2.Role.Name(request.role)
            update_data['rol'] = role_name
        if request.HasField('active'): update_data['activo'] = bool(request.active)

        if update_data:
            set_clause = ", ".join([f"{k} = %s" for k in update_data.keys()])
            params = list(update_data.values()) + [request.id]
            db.db.execute_update(f"UPDATE usuarios SET {set_clause} WHERE id = %s", tuple(params))
            # db.db.commit() - Removed: execute_update already handles commit internally

        # devolver usuario actualizado
        updated_row = db.db.execute_query("SELECT * FROM usuarios WHERE id = %s", (request.id,))[0]
        print(f"DEBUG: updated_row['rol'] = {updated_row['rol']}, type = {type(updated_row['rol'])}")
        
        # Convertir rol a entero si es string
        rol_value = updated_row['rol']
        if isinstance(rol_value, str):
            try:
                rol_value = users_pb2.Role.Value(rol_value)
            except ValueError:
                # Si no es un nombre válido de enum, intentar conversión directa
                rol_value = int(rol_value)
        else:
            rol_value = int(rol_value)
        
        return users_pb2.User(
            id=updated_row['id'],
            username=updated_row['nombre_usuario'],
            name=updated_row['nombre'],
            lastname=updated_row['apellido'],
            phone=updated_row['telefono'] or "",
            email=updated_row['email'],
            role=rol_value,
            active=bool(updated_row['activo'])
        )


    def DeactivateUser(self, request, context):
        print("=== DEACTIVATE USER REQUEST ===")
        print(f"Actor username: {request.actor_username}")
        print(f"User ID: {request.id}")
        
        actor = require_user(request.actor_username)
        print(f"Actor found: {actor}")
        if not actor or not can_manage_users(actor.role):
            print("Permission denied - actor not authorized")
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")

        try:
            print("Checking if user exists...")
            results = db.db.execute_query("SELECT * FROM usuarios WHERE id = %s", (request.id,))
            if not results:
                print("User not found")
                context.abort(grpc.StatusCode.NOT_FOUND, "Usuario inexistente")
            user_row = results[0]
            print(f"User found: {user_row}")

            print("Updating user status to inactive...")
            db.db.execute_update("UPDATE usuarios SET activo = 0 WHERE id = %s", (request.id,))
            print("User deactivated successfully")

            print("Removing future event participations...")
            remove_query = """
                DELETE FROM evento_participaciones 
                WHERE usuario_id = %s 
                AND evento_id IN (SELECT id FROM eventos WHERE fecha_evento > NOW())
            """
            db.db.execute_update(remove_query, (request.id,))
            print("Event participations removed")

            print("Fetching updated user...")
            updated = db.db.execute_query("SELECT * FROM usuarios WHERE id = %s", (request.id,))[0]
            print(f"Updated user: {updated}")
            
            # Handle role properly - convert ENUM string to protobuf enum value
            role_value = updated['rol']
            print(f"Role value from DB: {role_value} (type: {type(role_value)})")
            if isinstance(role_value, str):
                role_enum = users_pb2.Role.Value(role_value)
                print(f"Converted role to enum: {role_enum}")
            else:
                role_enum = int(role_value)
                print(f"Role as int: {role_enum}")
            
            response_user = users_pb2.User(
                id=updated['id'],
                username=updated['nombre_usuario'],
                name=updated['nombre'],
                lastname=updated['apellido'],
                phone=updated['telefono'] or "",
                email=updated['email'],
                role=role_enum,
                active=bool(updated['activo'])
            )
            print(f"Returning user: {response_user}")
            return response_user

        except Exception as e:
            print(f"Exception in DeactivateUser: {str(e)}")
            import traceback
            traceback.print_exc()
            context.abort(grpc.StatusCode.INTERNAL, f"Error al desactivar usuario: {str(e)}")

    def ListUsers(self, request, context):
        users = []
        query = "SELECT * FROM usuarios"
        results = db.db.execute_query(query)
        for row in results:
            users.append(users_pb2.User(
                id=row['id'],
                username=row['nombre_usuario'],
                name=row['nombre'],
                lastname=row['apellido'],
                phone=row['telefono'] or "",
                email=row['email'],
                role=users_pb2.Role.Value(row['rol']),
                active=row['activo']
            ))
        return users_pb2.UserList(users=users)

    def Login(self, request, context):
        user = db.find_user_by_login(request.login)
        if not user:
            return users_pb2.LoginResponse(ok=False, message="Usuario/email inexistente")
        if not user.active:
            return users_pb2.LoginResponse(ok=False, message="Usuario inactivo")
        print(request.password, user.pw_hash, user.pw_salt)
        if not verify_password(request.password, user.pw_hash, user.pw_salt):
            return users_pb2.LoginResponse(ok=False, message="Credenciales incorrectas")
        return users_pb2.LoginResponse(
            ok=True, message="Login ok",
            user=users_pb2.User(
                id=user.id, username=user.username, name=user.name, lastname=user.lastname,
                phone=user.phone, email=user.email, role=users_pb2.Role.Value(user.role), active=user.active
            )
        )

# ---- DonacionesService ----
ALLOWED_CATEGORIES = {"ROPA", "ALIMENTOS", "JUGUETES", "UTILES_ESCOLARES"}

class DonacionesService(inventory_pb2_grpc.DonacionesServiceServicer):
    def CreateDonation(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_inventory(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        cat = inventory_pb2.Category.Name(request.category)
        if cat not in ALLOWED_CATEGORIES:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Categoría inválida")
        if request.quantity < 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Cantidad no puede ser negativa")
        d = DonationModel(
            id=0, category=cat, description=request.description, quantity=request.quantity,
            deleted=False, created_at=now_utc().isoformat(), created_by=actor.username
        )
        created = db.create_donation(d)
        return inventory_pb2.DonationItem(
            id=created.id, category=request.category, description=created.description,
            quantity=created.quantity, deleted=created.deleted, created_at=created.created_at,
            created_by=created.created_by
        )

    def UpdateDonation(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_inventory(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        try:
            # Verificar que la donación existe y no está eliminada
            select_query = "SELECT * FROM inventario WHERE id = %s"
            results = db.db.execute_query(select_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")
            row = results[0]
            if row.get('eliminado'):
                context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")

            if request.quantity is not None and request.quantity < 0:
                context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Cantidad no puede ser negativa")

            # Actualizar donación
            update_data = {}
            if request.description is not None and request.description != row.get('descripcion'):
                update_data['descripcion'] = request.description
            if request.quantity is not None and int(request.quantity) != int(row.get('cantidad') or 0):
                update_data['cantidad'] = int(request.quantity)
            
            if update_data:
                update_fields = []
                params = []
                for field, value in update_data.items():
                    update_fields.append(f"{field} = %s")
                    params.append(value)
                
                # Agregar auditoría
                update_fields.append("usuario_modificacion = %s")
                update_fields.append("fecha_modificacion = NOW()")
                params.append(actor.id)
                params.append(request.id)
                
                update_query = f"UPDATE inventario SET {', '.join(update_fields)} WHERE id = %s"
                db.db.execute_update(update_query, tuple(params))
                db.db.commit()

            # Obtener donación actualizada usando el sistema de almacenamiento
            updated_donations = db.donations
            updated_donation = updated_donations[request.id]
            
            return inventory_pb2.DonationItem(
                id=updated_donation.id, 
                category=inventory_pb2.Category.Value(updated_donation.category),
                description=updated_donation.description, 
                quantity=updated_donation.quantity, 
                deleted=updated_donation.deleted,
                created_at=updated_donation.created_at,
                created_by=updated_donation.created_by,
                updated_at=updated_donation.updated_at or "",
                updated_by=updated_donation.updated_by or ""
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al actualizar donación: {str(e)}")

    def DeleteDonation(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_inventory(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        try:
            select_query = "SELECT * FROM inventario WHERE id = %s"
            results = db.db.execute_query(select_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")
            row = results[0]
            if row.get('eliminado'):
                context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")

            delete_query = "UPDATE inventario SET eliminado = 1, usuario_modificacion = %s, fecha_modificacion = NOW() WHERE id = %s"
            db.db.execute_update(delete_query, (actor.id, request.id))
            db.db.commit()
            
            # Obtener la donación actualizada usando el sistema de almacenamiento
            updated_donations = db.donations
            updated_donation = updated_donations[request.id]
            
            return inventory_pb2.DonationItem(
                id=updated_donation.id, 
                category=inventory_pb2.Category.Value(updated_donation.category),
                description=updated_donation.description, 
                quantity=updated_donation.quantity, 
                deleted=updated_donation.deleted,
                created_at=updated_donation.created_at,
                created_by=updated_donation.created_by,
                updated_at=updated_donation.updated_at or "",
                updated_by=updated_donation.updated_by or ""
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al eliminar donación: {str(e)}")

    def ListDonations(self, request, context):
        items = []
        for d in db.donations.values():
            items.append(inventory_pb2.DonationItem(
                id=d.id, category=inventory_pb2.Category.Value(d.category),
                description=d.description, quantity=d.quantity, deleted=d.deleted,
                created_at=d.created_at, created_by=d.created_by,
                updated_at=d.updated_at or "", updated_by=d.updated_by or ""
            ))
        return inventory_pb2.DonationList(items=items)

# ---- EventosService ----
class EventosService(events_pb2_grpc.EventosServiceServicer):
    def CreateEvent(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_events(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        when = datetime.fromisoformat(request.when_iso)
        # si la fecha no tiene zona horaria, asumir UTC
        if when.tzinfo is None:
            when = when.replace(tzinfo=timezone.utc)
        if when <= now_utc():
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "El evento debe ser a futuro")
        e = EventModel(id=0, name=request.name, description=request.description, when_iso=request.when_iso)
        db.create_event(e)
        return events_pb2.Event(id=e.id, name=e.name, description=e.description, when_iso=e.when_iso, members=e.members)

    def UpdateEvent(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_events(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        try:
            # Verificar que el evento existe
            check_query = "SELECT * FROM eventos WHERE id = %s"
            results = db.db.execute_query(check_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
            
            # Construir actualización
            update_data = {}
            if request.name:
                update_data['nombre'] = request.name
            if request.description:
                update_data['descripcion'] = request.description
            if request.when_iso:
                try:
                    s2 = request.when_iso.replace("Z", "+00:00")
                    new_when = datetime.fromisoformat(s2)
                    if new_when.tzinfo is None:
                        new_when = new_when.replace(tzinfo=timezone.utc)
                    update_data['fecha_evento'] = new_when
                except Exception as e:
                    context.abort(grpc.StatusCode.INVALID_ARGUMENT, f"Fecha inválida: {str(e)}")

            
            if update_data:
                update_fields = []
                params = []
                for field, value in update_data.items():
                    update_fields.append(f"{field} = %s")
                    params.append(value)
                
                # Agregar auditoría
                update_fields.append("usuario_modificacion = %s")
                params.append(actor.id)
                params.append(request.id)
                
                update_query = f"UPDATE eventos SET {', '.join(update_fields)} WHERE id = %s"
                db.db.execute_update(update_query, tuple(params))
                db.db.commit()           
                
            # Obtener evento actualizado con participantes
            updated_results = db.db.execute_query(check_query, (request.id,))
            event_row = updated_results[0]
            
            # Obtener participantes
            participants_query = """
                SELECT u.nombre_usuario
                FROM evento_participaciones ep
                JOIN usuarios u ON ep.usuario_id = u.id
                WHERE ep.evento_id = %s
            """
            participants_results = db.db.execute_query(participants_query, (request.id,))
            members = [row['nombre_usuario'] for row in participants_results]
            
            return events_pb2.Event(
                id=event_row['id'], 
                name=event_row['nombre'], 
                description=event_row['descripcion'] or "", 
                when_iso=event_row['fecha_evento'].isoformat(), 
                members=members
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al actualizar evento: {str(e)}")

    def DeleteFutureEvent(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_events(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        try:
            # Verificar que el evento existe y es futuro
            check_query = "SELECT * FROM eventos WHERE id = %s"
            results = db.db.execute_query(check_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
            
            event_row = results[0]
            when = event_row['fecha_evento']
            if when <= now_utc():
                context.abort(grpc.StatusCode.FAILED_PRECONDITION, "Solo se puede eliminar eventos a futuro")
            
            # Eliminar participaciones primero
            delete_participations = "DELETE FROM evento_participaciones WHERE evento_id = %s"
            db.db.execute_update(delete_participations, (request.id,))
            
            # Eliminar evento
            delete_event = "DELETE FROM eventos WHERE id = %s"
            db.db.execute_update(delete_event, (request.id,))
            db.db.commit()
            
            if hasattr(db, "_reload_events_cache"):
                try:
                    db._reload_events_cache()
                except Exception:
                    logger.debug("No se pudo recargar cache de eventos")
                    
            return Empty
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al eliminar evento: {str(e)}")

    def AssignOrRemoveMember(self, request, context):
        actor = require_user(request.actor_username)
        if not actor:
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        target = db.find_user_by_login(request.username)
        if not target or not target.active:
            context.abort(grpc.StatusCode.NOT_FOUND, "Miembro inexistente o inactivo")
        
        # Permisos
        if request.username == actor.username:
            # voluntario puede agregarse/quitarse a sí mismo
            if not can_participate_events(actor.role):
                context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado para participar")
        else:
            if not can_manage_events(actor.role):
                context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado para asignar a otros")
        
        try:
            # Verificar que el evento existe
            check_query = "SELECT * FROM eventos WHERE id = %s"
            results = db.db.execute_query(check_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
            
            event_row = results[0]
            
            if request.add:
                # Agregar participante
                try:
                    add_query = """
                        INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por)
                        VALUES (%s, %s, %s)
                    """
                    db.db.execute_insert(add_query, (request.id, target.id, actor.id))
                except:
                    pass  # Ignorar si ya existe
            else:
                # Remover participante
                remove_query = """
                    DELETE FROM evento_participaciones 
                    WHERE evento_id = %s AND usuario_id = %s
                """
                db.db.execute_update(remove_query, (request.id, target.id))
            
            # Obtener participantes actualizados
            participants_query = """
                SELECT u.nombre_usuario
                FROM evento_participaciones ep
                JOIN usuarios u ON ep.usuario_id = u.id
                WHERE ep.evento_id = %s
            """
            participants_results = db.db.execute_query(participants_query, (request.id,))
            members = [row['nombre_usuario'] for row in participants_results]
            
            return events_pb2.Event(
                id=event_row['id'], 
                name=event_row['nombre'], 
                description=event_row['descripcion'] or "", 
                when_iso=event_row['fecha_evento'].isoformat(), 
                members=members
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al asignar/remover miembro: {str(e)}")

    def RegisterDistributions(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_events(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        try:
            # Verificar que el evento existe
            check_query = "SELECT * FROM eventos WHERE id = %s"
            results = db.db.execute_query(check_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
            
            event_row = results[0]
            when = event_row['fecha_evento']
            if when > now_utc():
                context.abort(grpc.StatusCode.FAILED_PRECONDITION, "Solo eventos pasados pueden registrar distribuciones")
            
            # Procesar cada donación
            for dist in request.donations:
                category = dist.category.upper()
                quantity_needed = dist.quantity
                
                # Buscar items disponibles de esa categoría con suficiente stock
                available_query = """
                    SELECT id, cantidad FROM inventario 
                    WHERE categoria = %s AND eliminado = 0 AND cantidad >= %s
                    ORDER BY fecha_alta ASC
                    LIMIT 1
                """
                available_results = db.db.execute_query(available_query, (category, quantity_needed))
                
                if not available_results:
                    context.abort(grpc.StatusCode.FAILED_PRECONDITION, f"Inventario insuficiente para {category}")
                
                item_row = available_results[0]
                item_id = item_row['id']
                
                # Registrar la donación repartida
                register_query = """
                    INSERT INTO evento_donaciones (evento_id, inventario_id, cantidad_repartida, registrado_por)
                    VALUES (%s, %s, %s, %s)
                """
                db.db.execute_insert(register_query, (request.id, item_id, quantity_needed, actor.id))
                
            
            # Obtener evento actualizado con participantes
            participants_query = """
                SELECT u.nombre_usuario
                FROM evento_participaciones ep
                JOIN usuarios u ON ep.usuario_id = u.id
                WHERE ep.evento_id = %s
            """
            participants_results = db.db.execute_query(participants_query, (request.id,))
            members = [row['nombre_usuario'] for row in participants_results]
            
            return events_pb2.Event(
                id=event_row['id'], 
                name=event_row['nombre'], 
                description=event_row['descripcion'] or "", 
                when_iso=event_row['fecha_evento'].isoformat(), 
                members=members
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al registrar distribuciones: {str(e)}")

    def ListEvents(self, request, context):
        items = []
        for e in db.events.values():
            items.append(events_pb2.Event(id=e.id, name=e.name, description=e.description, when_iso=e.when_iso, members=e.members))
        return events_pb2.EventList(events=items)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    users_pb2_grpc.add_UsuariosServiceServicer_to_server(UsuariosService(), server)
    inventory_pb2_grpc.add_DonacionesServiceServicer_to_server(DonacionesService(), server)
    events_pb2_grpc.add_EventosServiceServicer_to_server(EventosService(), server)
    server.add_insecure_port("[::]:50051")
    print("gRPC server listening on :50051")
    server.start()
    server.wait_for_termination()

if __name__ == "__main__":
    serve()
