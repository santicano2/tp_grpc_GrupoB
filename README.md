# tp_grpc_GrupoB

Trabajo práctico sobre RPC de la materia Desarrollo de Software en Sistemas Distribuidos. UNLa

## Integrantes

- Carlos Iglesias
- Nicolas Alejandro Romero Ortiz
- Anahí Maitén Mansilla
- Santiago Martin Cano

## Cómo iniciar el servidor

1. Instalar Docker y Docker Compose en tu sistema.

```sh
docker-compose up --build ## Iniciar servidor
docker-compose logs 			## Ver logs del server
docker-compose down				## Detener servidor
```

El servidor gRPC queda expuesto en el puerto 50051.

## Probar la API gRPC con Postman

1. **Crear nueva gRPC Request**:

   - En Postman: **New > gRPC Request**
   - **Server URL**: `localhost:50051`

2. **Import Proto Files**:

   - Click en **"Import .proto files"**
   - Seleccionar archivos desde `server/protos/`:
     - `users.proto`
     - `inventory.proto`
     - `events.proto`

3. **Usar los servicios**:

   - **Select Service**: `ong.users.UsuariosService`
   - **Select Method**: `Login`
   - **Message**: `{"login": "presidente1", "password": "password123"}`
   - **Send**: Probar

4. **Servicios disponibles**:

   - `ong.users.UsuariosService` - Gestión de usuarios
   - `ong.inventory.DonacionesService` - Gestión de inventario
   - `ong.events.EventosService` - Gestión de eventos

5. **Usuarios de prueba**:
   - `presidente1` (PRESIDENTE)
   - `vocal1`, `vocal2` (VOCAL)
   - `coordinador1`, `coordinador2`, `coordinador3` (COORDINADOR)
   - `voluntario1`, `voluntario2`, `voluntario3`, `voluntario4` (VOLUNTARIO)
   - **Contraseña para todos**: `password123`

### Métodos disponibles:

#### UsuariosService

- `ListUsers` - Listar usuarios
- `Login` - Autenticar usuario
- `CreateUser` - Crear usuario (PRESIDENTE/VOCAL)
- `UpdateUser` - Actualizar usuario (PRESIDENTE/VOCAL)
- `DeactivateUser` - Desactivar usuario (PRESIDENTE/VOCAL)

#### DonacionesService (Inventario)

- `ListDonations` - Listar inventario
- `CreateDonation` - Crear donación (PRESIDENTE/VOCAL/COORDINADOR)
- `UpdateDonation` - Actualizar donación (PRESIDENTE/VOCAL/COORDINADOR)
- `DeleteDonation` - Eliminar donación (PRESIDENTE/VOCAL/COORDINADOR)

#### EventosService

- `ListEvents` - Listar eventos
- `CreateEvent` - Crear evento (PRESIDENTE/VOCAL/COORDINADOR)
- `UpdateEvent` - Actualizar evento (PRESIDENTE/VOCAL/COORDINADOR)
- `AssignOrRemoveMember` - Gestionar participantes (PRESIDENTE/VOCAL/COORDINADOR)
- `RegisterDistributions` - Registrar distribuciones (PRESIDENTE/VOCAL/COORDINADOR)
- `DeleteFutureEvent` - Eliminar evento futuro (PRESIDENTE/VOCAL/COORDINADOR)
