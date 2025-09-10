import logging
from dataclasses import dataclass, field
from typing import Dict, List, Optional
from datetime import datetime
from .auth import Role
from .utils import now_utc
from .database import db_manager
import bcrypt
import secrets
import string

logger = logging.getLogger(__name__)

@dataclass
class User:
    id: int
    username: str
    name: str
    lastname: str
    phone: str | None
    email: str
    role: str
    active: bool
    pw_hash: str
    pw_salt: str

@dataclass
class Donation:
    id: int
    category: str
    description: str
    quantity: int
    deleted: bool
    created_at: str
    created_by: str
    updated_at: str | None = None
    updated_by: str | None = None

@dataclass
class Event:
    id: int
    name: str
    description: str
    when_iso: str
    members: List[str] = field(default_factory=list)
    deleted: bool = False

class DatabaseStorage:    
    def __init__(self):
        self.db = db_manager
        self._user_cache = {}
        self._users_by_username = {}
        self._users_by_email = {}
    
    def _hash_password(self, password: str) -> str:
        """Encriptar contraseña con bcrypt"""
        salt = bcrypt.gensalt()
        hashed = bcrypt.hashpw(password.encode('utf-8'), salt)
        return hashed.decode('utf-8')
    
    def _verify_password(self, password: str, hashed: str) -> bool:
        """Verificar contraseña"""
        return bcrypt.checkpw(password.encode('utf-8'), hashed.encode('utf-8'))
    
    def _generate_password(self, length: int = 8) -> str:
        """Generar contraseña aleatoria"""
        characters = string.ascii_letters + string.digits + "!@#$%&*"
        return ''.join(secrets.choice(characters) for _ in range(length))
    
    def _clear_user_cache(self):
        self._user_cache = {}
        self._users_by_username = {}
        self._users_by_email = {}
    
    # ===== USUARIOS =====
    def create_user(self, u: User):
        """Crear usuario"""
        try:
            # Verificar duplicados
            existing_user = self.db.execute_query(
                "SELECT id FROM usuarios WHERE nombre_usuario = %s OR email = %s",
                (u.username, u.email)
            )
            if existing_user:
                raise ValueError("Nombre de usuario o email ya existe")
            
            # Si no tiene contraseña, generar una
            if not u.pw_hash:
                plain_password = self._generate_password()
                u.pw_hash = self._hash_password(plain_password)
                print(f"[EMAIL] Enviando clave a {u.email}: {plain_password}")
            
            # Insertar en base de datos
            query = """
                INSERT INTO usuarios (nombre_usuario, nombre, apellido, telefono, clave, email, rol, activo)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            """
            user_id = self.db.execute_insert(query, (
                u.username, u.name, u.lastname, u.phone, u.pw_hash, u.email, u.role, u.active
            ))
            
            u.id = user_id
            self._clear_user_cache()
            return u
            
        except Exception as e:
            raise ValueError(str(e))
    
    def find_user_by_login(self, login: str) -> Optional[User]:
        """Buscar usuario por username o email"""
        try:
            if login in self._users_by_username:
                return self._user_cache.get(self._users_by_username[login])
            if login in self._users_by_email:
                return self._user_cache.get(self._users_by_email[login])
            
            
            query = """
                SELECT * FROM usuarios 
                WHERE (nombre_usuario = %s OR email = %s) AND activo = 1
            """
            results = self.db.execute_query(query, (login, login))
            
            if results:
                row = results[0]
                print(results)
                user = User(
                    id=row['id'],
                    username=row['nombre_usuario'],
                    name=row['nombre'],
                    lastname=row['apellido'],
                    phone=row['telefono'],
                    email=row['email'],
                    role=row['rol'],
                    active=row['activo'],
                    pw_hash=row['clave'],
                    pw_salt=""
                )
                
                self._user_cache[user.id] = user
                self._users_by_username[user.username] = user.id
                self._users_by_email[user.email] = user.id
                
                return user
            return None
        except Exception:
            return None
    
    @property
    def users(self) -> Dict[int, User]:
        """Obtener todos los usuarios como diccionario"""
        try:
            query = "SELECT * FROM usuarios"
            results = self.db.execute_query(query)
            
            users_dict = {}
            for row in results:
                user = User(
                    id=row['id'],
                    username=row['nombre_usuario'],
                    name=row['nombre'],
                    lastname=row['apellido'],
                    phone=row['telefono'],
                    email=row['email'],
                    role=row['rol'],
                    active=row['activo'],
                    pw_hash=row['clave'],
                    pw_salt=""
                )
                users_dict[user.id] = user
            
            return users_dict
        except Exception:
            return {}
    
    # ===== DONACIONES =====
    def create_donation(self, d: Donation):
        """Crear donación"""
        try:
            # Obtener usuario por username para el campo usuario_alta
            creator = self.find_user_by_login(d.created_by)
            creator_id = creator.id if creator else 1
            
            print(f"[DEBUG] Insertando donación con categoría: '{d.category}'")
            query = """
                INSERT INTO inventario (categoria, descripcion, cantidad, usuario_alta)
                VALUES (%s, %s, %s, %s)
            """
            item_id = self.db.execute_insert(query, (
                d.category, d.description, d.quantity, creator_id
            ))
            
            d.id = item_id
            return d
            
        except Exception as e:
            raise ValueError(str(e))
    
    @property
    def donations(self) -> Dict[int, Donation]:
        """Obtener todas las donaciones como diccionario"""
        try:
            query = """
                SELECT i.*, 
                       u1.nombre_usuario as created_by_username,
                       u2.nombre_usuario as updated_by_username
                FROM inventario i
                LEFT JOIN usuarios u1 ON i.usuario_alta = u1.id
                LEFT JOIN usuarios u2 ON i.usuario_modificacion = u2.id
            """
            results = self.db.execute_query(query)
            
            donations_dict = {}
            for row in results:
                donation = Donation(
                    id=row['id'],
                    category=row['categoria'],
                    description=row['descripcion'] or "",
                    quantity=row['cantidad'],
                    deleted=row['eliminado'],
                    created_at=row['fecha_alta'].isoformat() if row['fecha_alta'] else now_utc(),
                    created_by=row['created_by_username'] or "",
                    updated_at=row['fecha_modificacion'].isoformat() if row['fecha_modificacion'] else None,
                    updated_by=row['updated_by_username'] or None
                )
                donations_dict[donation.id] = donation
            
            return donations_dict
        except Exception:
            return {}
    
    # ===== EVENTOS =====
    def create_event(self, e: Event):
        """Crear evento"""
        try:
            fecha_evento = datetime.fromisoformat(e.when_iso.replace('Z', '+00:00'))
            
            query = """
                INSERT INTO eventos (nombre, descripcion, fecha_evento, usuario_creacion)
                VALUES (%s, %s, %s, %s)
            """
            event_id = self.db.execute_insert(query, (
                e.name, e.description, fecha_evento, 1
            ))
            
            e.id = event_id
            
            # Agregar participantes si los hay
            for username in e.members:
                user = self.find_user_by_login(username)
                if user:
                    try:
                        participant_query = """
                            INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por)
                            VALUES (%s, %s, %s)
                        """
                        self.db.execute_insert(participant_query, (event_id, user.id, 1))
                    except:
                        pass
            
            return e
            
        except Exception as e:
            raise ValueError(str(e))
    
    @property
    def events(self) -> Dict[int, Event]:
        """Obtener todos los eventos como diccionario"""
        try:
            # Obtener eventos
            events_query = "SELECT * FROM eventos"
            events_results = self.db.execute_query(events_query)
            
            # Obtener participaciones
            participants_query = """
                SELECT ep.evento_id, u.nombre_usuario
                FROM evento_participaciones ep
                JOIN usuarios u ON ep.usuario_id = u.id
            """
            participants_results = self.db.execute_query(participants_query)
            
            # Agrupar participantes por evento
            event_participants = {}
            for row in participants_results:
                event_id = row['evento_id']
                if event_id not in event_participants:
                    event_participants[event_id] = []
                event_participants[event_id].append(row['nombre_usuario'])
            
            # Crear diccionario de eventos
            events_dict = {}
            for row in events_results:
                event = Event(
                    id=row['id'],
                    name=row['nombre'],
                    description=row['descripcion'] or "",
                    when_iso=row['fecha_evento'].isoformat(),
                    members=event_participants.get(row['id'], []),
                    deleted=False
                )
                events_dict[event.id] = event
            
            return events_dict
        except Exception:
            return {}

db = DatabaseStorage()
