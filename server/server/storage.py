from dataclasses import dataclass, field
from typing import Dict, List, Optional
from .auth import Role
from .utils import now_utc

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
    when_iso: str  # ISO 8601
    members: List[str] = field(default_factory=list)  # usernames
    deleted: bool = False  # for future events only (physical delete on API, but we mark then drop)

class MemoryDB:
    def __init__(self):
        self._uid = 1
        self._did = 1
        self._eid = 1
        self.users: Dict[int, User] = {}
        self.users_by_username: Dict[str, int] = {}
        self.users_by_email: Dict[str, int] = {}
        self.donations: Dict[int, Donation] = {}
        self.events: Dict[int, Event] = {}

    # Users
    def create_user(self, u: User):
        if u.username in self.users_by_username:
            raise ValueError("Nombre de usuario ya existe")
        if u.email in self.users_by_email:
            raise ValueError("Email ya existe")
        u.id = self._uid; self._uid += 1
        self.users[u.id] = u
        self.users_by_username[u.username] = u.id
        self.users_by_email[u.email] = u.id
        return u

    def find_user_by_login(self, login: str) -> Optional[User]:
        if login in self.users_by_username:
            return self.users[self.users_by_username[login]]
        # email lookup
        if login in self.users_by_email:
            return self.users[self.users_by_email[login]]
        return None

    # Donations
    def create_donation(self, d: Donation):
        d.id = self._did; self._did += 1
        self.donations[d.id] = d
        return d

    # Events
    def create_event(self, e: Event):
        e.id = self._eid; self._eid += 1
        self.events[e.id] = e
        return e

db = MemoryDB()
