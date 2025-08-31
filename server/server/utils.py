import secrets, hashlib, datetime

def now_utc():
    return datetime.datetime.utcnow().replace(tzinfo=datetime.timezone.utc)

def hash_password(plain: str, salt: str = None) -> tuple[str, str]:
    if salt is None:
        salt = secrets.token_hex(16)
    h = hashlib.sha256((salt + plain).encode("utf-8")).hexdigest()
    return h, salt

def verify_password(plain: str, h: str, salt: str) -> bool:
    return hash_password(plain, salt)[0] == h
