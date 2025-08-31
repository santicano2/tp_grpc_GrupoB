from enum import Enum

class Role(str, Enum):
    PRESIDENTE = "PRESIDENTE"
    VOCAL = "VOCAL"
    COORDINADOR = "COORDINADOR"
    VOLUNTARIO = "VOLUNTARIO"

def can_manage_users(role: str) -> bool:
    return role == Role.PRESIDENTE

def can_manage_inventory(role: str) -> bool:
    return role in {Role.PRESIDENTE, Role.VOCAL}

def can_manage_events(role: str) -> bool:
    return role in {Role.PRESIDENTE, Role.COORDINADOR}

def can_participate_events(role: str) -> bool:
    return role in {Role.PRESIDENTE, Role.COORDINADOR, Role.VOLUNTARIO}
