import grpc
from concurrent import futures
from datetime import datetime, timezone
import secrets

from .auth import (
    can_manage_users, can_manage_inventory, can_manage_events, can_participate_events
)
from .utils import now_utc, hash_password, verify_password, hash_password_bcrypt
from .storage import db, User as UserModel, Donation as DonationModel, Event as EventModel
from . import users_pb2, users_pb2_grpc
from . import inventory_pb2, inventory_pb2_grpc
from . import events_pb2, events_pb2_grpc

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
        u = UserModel(
            id=0,
            username=request.username,
            name=request.name,
            lastname=request.lastname,
            phone=request.phone or "",
            email=request.email,
            role=users_pb2.Role.Name(request.role),
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
                role=request.role, active=created.active
            ),
            plain_password=plain
        )

    def UpdateUser(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_users(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        # Obtener usuario actual de la base de datos
        try:
            query = "SELECT * FROM usuarios WHERE id = %s"
            results = db.db.execute_query(query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Usuario inexistente")
            
            # Construir datos de actualización
            update_data = {}
            if request.username:
                update_data['nombre_usuario'] = request.username
            if request.name:
                update_data['nombre'] = request.name
            if request.lastname:
                update_data['apellido'] = request.lastname
            if request.phone:
                update_data['telefono'] = request.phone
            if request.email:
                update_data['email'] = request.email
            if request.role is not None:
                update_data['rol'] = users_pb2.Role.Name(request.role)
            if hasattr(request, 'active') and request.active is not None:
                update_data['activo'] = request.active
            
            # Actualizar en base de datos
            if update_data:
                update_fields = []
                params = []
                for field, value in update_data.items():
                    update_fields.append(f"{field} = %s")
                    params.append(value)
                
                params.append(request.id)
                update_query = f"UPDATE usuarios SET {', '.join(update_fields)} WHERE id = %s"
                db.db.execute_update(update_query, tuple(params))
            
            # Obtener usuario actualizado
            db._clear_user_cache()
            updated_results = db.db.execute_query(query, (request.id,))
            row = updated_results[0]
            
            return users_pb2.User(
                id=row['id'], 
                username=row['nombre_usuario'], 
                name=row['nombre'], 
                lastname=row['apellido'],
                phone=row['telefono'] or "", 
                email=row['email'], 
                role=users_pb2.Role.Value(row['rol']), 
                active=row['activo']
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al actualizar usuario: {str(e)}")

    def DeactivateUser(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_users(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        try:
            # Desactivar usuario
            query = "UPDATE usuarios SET activo = 0 WHERE id = %s"
            rows_affected = db.db.execute_update(query, (request.id,))
            
            if rows_affected == 0:
                context.abort(grpc.StatusCode.NOT_FOUND, "Usuario inexistente")
            
            # Remover de eventos futuros
            remove_query = """
                DELETE FROM evento_participaciones 
                WHERE usuario_id = %s 
                AND evento_id IN (SELECT id FROM eventos WHERE fecha_evento > NOW())
            """
            db.db.execute_update(remove_query, (request.id,))
            
            # Obtener usuario actualizado
            db._clear_user_cache()
            user_query = "SELECT * FROM usuarios WHERE id = %s"
            results = db.db.execute_query(user_query, (request.id,))
            row = results[0]
            
            return users_pb2.User(
                id=row['id'], 
                username=row['nombre_usuario'], 
                name=row['nombre'], 
                lastname=row['apellido'],
                phone=row['telefono'] or "", 
                email=row['email'], 
                role=users_pb2.Role.Value(row['rol']), 
                active=row['activo']
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al desactivar usuario: {str(e)}")

    def ListUsers(self, request, context):
        users = []
        for u in db.users.values():
            users.append(users_pb2.User(
                id=u.id, username=u.username, name=u.name, lastname=u.lastname,
                phone=u.phone, email=u.email, role=users_pb2.Role.Value(u.role), active=u.active
            ))
        return users_pb2.UserList(users=users)

    def Login(self, request, context):
        user = db.find_user_by_login(request.login)
        if not user:
            return users_pb2.LoginResponse(ok=False, message="Usuario/email inexistente")
        if not user.active:
            return users_pb2.LoginResponse(ok=False, message="Usuario inactivo")
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
            check_query = "SELECT * FROM inventario WHERE id = %s AND eliminado = 0"
            results = db.db.execute_query(check_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")
            
            if request.quantity < 0:
                context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Cantidad no puede ser negativa")
            
            # Actualizar donación
            update_data = {}
            if request.description is not None:
                update_data['descripcion'] = request.description
            if request.quantity is not None:
                update_data['cantidad'] = request.quantity
            
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
                
                update_query = f"UPDATE inventario SET {', '.join(update_fields)} WHERE id = %s"
                db.db.execute_update(update_query, tuple(params))
            
            # Obtener donación actualizada
            updated_results = db.db.execute_query(check_query, (request.id,))
            row = updated_results[0]
            
            return inventory_pb2.DonationItem(
                id=row['id'], 
                category=inventory_pb2.Category.Value(row['categoria']),
                description=row['descripcion'] or "", 
                quantity=row['cantidad'], 
                deleted=row['eliminado'],
                created_at=row['fecha_alta'].isoformat() if row['fecha_alta'] else "",
                created_by=actor.username,  # Simplificado
                updated_at=row['fecha_modificacion'].isoformat() if row['fecha_modificacion'] else "",
                updated_by=actor.username
            )
            
        except Exception as e:
            context.abort(grpc.StatusCode.INTERNAL, f"Error al actualizar donación: {str(e)}")

    def DeleteDonation(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_inventory(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        
        try:
            # Verificar que la donación existe y no está eliminada
            check_query = "SELECT * FROM inventario WHERE id = %s AND eliminado = 0"
            results = db.db.execute_query(check_query, (request.id,))
            if not results:
                context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")
            
            # Eliminar
            delete_query = "UPDATE inventario SET eliminado = 1, usuario_modificacion = %s WHERE id = %s"
            db.db.execute_update(delete_query, (actor.id, request.id))
            
            # Obtener donación actualizada
            updated_results = db.db.execute_query("SELECT * FROM inventario WHERE id = %s", (request.id,))
            row = updated_results[0]
            
            return inventory_pb2.DonationItem(
                id=row['id'], 
                category=inventory_pb2.Category.Value(row['categoria']),
                description=row['descripcion'] or "", 
                quantity=row['cantidad'], 
                deleted=row['eliminado'],
                created_at=row['fecha_alta'].isoformat() if row['fecha_alta'] else "",
                created_by=actor.username,  # Simplificado
                updated_at=row['fecha_modificacion'].isoformat() if row['fecha_modificacion'] else "",
                updated_by=actor.username
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
                new_when = datetime.fromisoformat(request.when_iso.replace('Z', '+00:00'))
                update_data['fecha_evento'] = new_when
            
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
            
            return events_pb2.google_dot_protobuf_dot_empty__pb2.Empty()
            
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
