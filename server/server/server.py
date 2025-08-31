import grpc
from concurrent import futures
from datetime import datetime, timezone
import secrets

from .auth import (
    can_manage_users, can_manage_inventory, can_manage_events, can_participate_events
)
from .utils import now_utc, hash_password, verify_password
from .storage import db, User as UserModel, Donation as DonationModel, Event as EventModel
# Import generated stubs after protoc generation
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
        # generar clave random y hashear
        plain = secrets.token_urlsafe(10)
        pw_hash, salt = hash_password(plain)
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
            pw_salt=salt,
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
        u = db.users.get(request.id)
        if not u:
            context.abort(grpc.StatusCode.NOT_FOUND, "Usuario inexistente")
        # No se modifica clave
        u.username = request.username or u.username
        u.name = request.name or u.name
        u.lastname = request.lastname or u.lastname
        u.phone = request.phone or u.phone
        u.email = request.email or u.email
        u.role = users_pb2.Role.Name(request.role) if request.role is not None else u.role
        u.active = request.active if request.active is not None else u.active
        return users_pb2.User(
            id=u.id, username=u.username, name=u.name, lastname=u.lastname,
            phone=u.phone, email=u.email, role=users_pb2.Role.Value(u.role), active=u.active
        )

    def DeactivateUser(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_users(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        u = db.users.get(request.id)
        if not u:
            context.abort(grpc.StatusCode.NOT_FOUND, "Usuario inexistente")
        u.active = False
        return users_pb2.User(
            id=u.id, username=u.username, name=u.name, lastname=u.lastname,
            phone=u.phone, email=u.email, role=users_pb2.Role.Value(u.role), active=u.active
        )

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
        d = db.donations.get(request.id)
        if not d or d.deleted:
            context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")
        if request.quantity < 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Cantidad no puede ser negativa")
        d.description = request.description or d.description
        d.quantity = request.quantity if request.quantity is not None else d.quantity
        d.updated_at = now_utc().isoformat()
        d.updated_by = actor.username
        return inventory_pb2.DonationItem(
            id=d.id, category=inventory_pb2.Category.Value(d.category),
            description=d.description, quantity=d.quantity, deleted=d.deleted,
            created_at=d.created_at, created_by=d.created_by,
            updated_at=d.updated_at or "", updated_by=d.updated_by or ""
        )

    def DeleteDonation(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_inventory(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        d = db.donations.get(request.id)
        if not d or d.deleted:
            context.abort(grpc.StatusCode.NOT_FOUND, "Donación inexistente")
        d.deleted = True
        d.updated_at = now_utc().isoformat()
        d.updated_by = actor.username
        return inventory_pb2.DonationItem(
            id=d.id, category=inventory_pb2.Category.Value(d.category),
            description=d.description, quantity=d.quantity, deleted=d.deleted,
            created_at=d.created_at, created_by=d.created_by,
            updated_at=d.updated_at or "", updated_by=d.updated_by or ""
        )

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
        e = db.events.get(request.id)
        if not e:
            context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
        if request.when_iso:
            new_when = datetime.fromisoformat(request.when_iso)
            e.when_iso = request.when_iso
        if request.name: e.name = request.name
        if request.description: e.description = request.description
        return events_pb2.Event(id=e.id, name=e.name, description=e.description, when_iso=e.when_iso, members=e.members)

    def DeleteFutureEvent(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_events(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        e = db.events.get(request.id)
        if not e:
            context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
        when = datetime.fromisoformat(e.when_iso)
        if when <= now_utc():
            context.abort(grpc.StatusCode.FAILED_PRECONDITION, "Solo se puede eliminar eventos a futuro")
        # eliminación física
        del db.events[e.id]
        return events_pb2.google_dot_protobuf_dot_empty__pb2.Empty()

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
        e = db.events.get(request.id)
        if not e:
            context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
        if request.add:
            if request.username not in e.members:
                e.members.append(request.username)
        else:
            e.members = [m for m in e.members if m != request.username]
        return events_pb2.Event(id=e.id, name=e.name, description=e.description, when_iso=e.when_iso, members=e.members)

    def RegisterDistributions(self, request, context):
        actor = require_user(request.actor_username)
        if not actor or not can_manage_events(actor.role):
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "No autorizado")
        e = db.events.get(request.id)
        if not e:
            context.abort(grpc.StatusCode.NOT_FOUND, "Evento inexistente")
        when = datetime.fromisoformat(e.when_iso)
        if when > now_utc():
            context.abort(grpc.StatusCode.FAILED_PRECONDITION, "Solo eventos pasados pueden registrar distribuciones")
        # Descontar inventario
        for dist in request.donations:
            # Buscar por categoría (simple)
            # Restar de la primera donación activa con misma categoría
            for d in db.donations.values():
                if d.deleted: 
                    continue
                if d.category == dist.category.upper():
                    if d.quantity < dist.quantity:
                        context.abort(grpc.StatusCode.FAILED_PRECONDITION, f"Inventario insuficiente para {dist.category}")
                    d.quantity -= dist.quantity
                    d.updated_at = now_utc().isoformat()
                    d.updated_by = actor.username
                    break
        return events_pb2.Event(id=e.id, name=e.name, description=e.description, when_iso=e.when_iso, members=e.members)

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
