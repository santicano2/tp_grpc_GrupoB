# tp_grpc_GrupoB

Trabajo práctico sobre RPC de la materia Desarrollo de Software en Sistemas Distribuidos. UNLa

## Integrantes

- Carlos Iglesias
- Nicolas Alejandro Romero Ortiz
- Anahí Maitén Mansilla
- Santiago Martin Cano

## Prerrequisitos

### Para Docker (Opción recomendada)

- Docker Desktop 4.0+
- Docker Compose
- Puertos disponibles: 3307, 8080, 8081, 50051, 5173

### Para desarrollo manual

- **Java**: 17 o superior
- **Node.js**: 16+ con npm
- **Python**: 3.9+
- **Maven**: Incluido en el proyecto (Maven Wrapper)
- **MySQL**: 8.0+ (o usar Docker solo para la BD)

## Cómo iniciar el sistema completo

### Opción 1: Con Docker (Recomendado)

1. **Instalar Docker y Docker Compose** en tu sistema.

2. **Iniciar todos los servicios**:

   ```bash
   docker-compose up -d
   ```

3. **Detener el sistema**:
   ```bash
   docker-compose down
   ```

### Opción 2: Ejecución manual

#### 1. Base de datos MySQL

```bash
# Iniciar solo la base de datos con Docker
docker-compose up mysql phpmyadmin -d
```

#### 2. Servidor gRPC (Python)

```bash
cd server
python -m venv .venv
# Windows:
.venv\Scripts\activate
# Linux/Mac:
# source .venv/bin/activate

pip install -r requirements.txt
python server/server.py
```

#### 3. API Gateway (Spring Boot)

```bash
cd API
./mvnw compile        # Generar clases gRPC
./mvnw spring-boot:run
```

#### 4. Frontend (React + Vite)

```bash
cd frontend
npm install
npm run dev
```

### Arquitectura del sistema

```
Frontend (React)    →    API Gateway (Spring Boot)    →    Servidor gRPC (Python)    →    MySQL
    :5173                       :8080                          :50051                    :3307
```

### Acceso a los servicios

- **Frontend**: http://localhost:5173
- **API Gateway**: http://localhost:8080
- **Servidor gRPC**: localhost:50051
- **phpMyAdmin**: http://localhost:8081

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

### Problemas comunes

#### Error en API Gateway: Maven compilation fails

```bash
cd API
./mvnw clean compile   # Limpiar y recompilar
```
