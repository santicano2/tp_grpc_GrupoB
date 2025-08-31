# gRPC Server (Python) — ONG Empuje Comunitario

Este proyecto implementa **tres microservicios gRPC en Python** para el TP RPC:

- `UsuariosService` (gestión de usuarios, login)
- `DonacionesService` (inventario)
- `EventosService` (eventos solidarios)

Incluye **validaciones según la consigna**, auditoría básica, y **chequeo de roles**.
Almacenamiento **en memoria** para simplificar las pruebas (se puede reemplazar por DB).

## Estructura

```
protos/
  users.proto
  inventory.proto
  events.proto

server/
  server.py
  storage.py
  auth.py
  utils.py
  __init__.py
requirements.txt
README.md
```

## Cómo generar los stubs y correr

1. Crear venv e instalar dependencias:
   ```bash
   python -m venv .venv
   . .venv/bin/activate  # Windows: .venv\Scripts\activate
   pip install -r requirements.txt
   ```

2. Generar stubs gRPC:
   ```bash
   python -m grpc_tools.protoc -I=protos      --python_out=server --grpc_python_out=server      protos/users.proto protos/inventory.proto protos/events.proto
   ```

3. Ejecutar el servidor:
   ```bash
   python server/server.py
   ```

El servidor levanta en `localhost:50051` por defecto.

## Notas de implementación

- **Password**: se guarda **hash (SHA-256 + salt)** y se genera al alta con `secrets`. Se imprime en consola simulando envío por e‑mail.
- **Baja de usuario**: lógica (flag `active = false`).
- **Inventario**: categorías restringidas (`ROPA, ALIMENTOS, JUGUETES, UTILES_ESCOLARES`), cantidad no negativa, auditoría de altas/modificaciones/baja.
- **Eventos**: fecha/hora **debe ser futura** al crear; baja sólo para futuros; asignar/quitar miembros respeta roles; si el evento es pasado, se puede registrar **donaciones repartidas** (y descuenta inventario).
- **Autorización por rol**: el cliente debe enviar `actor_username` en cada request; el servidor verifica permisos según el **rol del actor** y el estado `activo`. Roles: `PRESIDENTE, VOCAL, COORDINADOR, VOLUNTARIO`.

## Siguiente paso

- Conectar tu **API Gateway en Java** a este server gRPC usando los `.proto`.
- Si necesitás persistencia, reemplazar `storage.py` por una capa SQLite/PostgreSQL.
