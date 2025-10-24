# tp_grpc_GrupoB

Trabajo práctico sobre RPC de la materia Desarrollo de Software en Sistemas Distribuidos. UNLa

Sistema de gestión para ONG "Empuje Comunitario" desarrollado con **gRPC**, **Kafka**, **GraphQL**, **REST** y **SOAP**.

## Integrantes

- Carlos Iglesias
- Nicolas Alejandro Romero Ortiz
- Anahí Maitén Mansilla
- Santiago Martin Cano

## Características principales

### TP1 - Base gRPC

- **Gestión de usuarios** con 4 roles (Presidente, Vocal, Coordinador, Voluntario)
- **Inventario de donaciones** por categorías (ROPA, ALIMENTOS, JUGUETES, UTILES_ESCOLARES)
- **Eventos solidarios** con gestión de participantes y distribución de donaciones
- **Autenticación y autorización** basada en roles
- **Microservicios gRPC** (Python) con API Gateway (Spring Boot)

### TP2 - Integración con Kafka

- **Sistema de mensajería** para red colaborativa de ONGs
- **Solicitudes de donaciones** entre organizaciones
- **Transferencias de donaciones** con actualización automática de inventarios
- **Ofertas de donaciones** publicadas en la red
- **Eventos solidarios externos** con adhesión de voluntarios de otras ONGs

### TP3 - Web services - REST, GraphQL y SOAP

- **Informes GraphQL** de donaciones y participación en eventos con filtros personalizables guardables
- **Exportación Excel** (REST) de donaciones con filtros opcionales
- **Cliente SOAP** para consultar presidentes y organizaciones de la red
- **Documentación Swagger/OpenAPI** para endpoints REST

## Tecnologías utilizadas

### Backend

- **gRPC** (Python 3.12) - Comunicación entre microservicios
- **Spring Boot 3.3.5** - API Gateway con JAX-WS para SOAP
- **Apache Kafka** - Sistema de mensajería asíncrono
- **GraphQL** (Python + Strawberry) - Consultas flexibles y filtros
- **REST** (Spring Boot) - Endpoints con Swagger/OpenAPI
- **SOAP Client** (JAX-WS 4.0.0) - Integración con red externa de ONGs
- **MySQL 8** - Base de datos relacional
- **Apache POI** - Generación de reportes Excel

### Frontend

- **React 19.1** + **Vite** - Interfaz de usuario moderna
- **Apollo Client** - Cliente GraphQL

### DevOps

- **Docker** y **Docker Compose** - Contenedorización de servicios
- **Maven** - Gestión de dependencias Java y generación de código SOAP
- **pip** - Gestión de dependencias Python

## Arquitectura del sistema

```
                                    ┌─────────────────────────┐
                                    │   Frontend (React)      │
                                    │        :5173            │
                                    └───────────┬─────────────┘
                                                │
                    ┌───────────────────────────┼───────────────────────────┐
                    │                           │                           │
            ┌───────▼──────────┐        ┌──────▼──────────┐        ┌──────▼──────────┐
            │  API Gateway     │        │  GraphQL Server │        │  Kafka Server   │
            │  (Spring Boot)   │◄───────┤    (Python)     │        │    (Python)     │
            │     :8080        │        │      :8000      │        │     :9092       │
            └─────────┬────────┘        └─────────────────┘        └─────────────────┘
                      │
         ┌────────────┼────────────┐
         │            │            │
   ┌─────▼─────┐  ┌──▼──────┐  ┌──▼──────────┐
   │ gRPC      │  │  SOAP   │  │   MySQL     │
   │ Server    │  │ Client  │  │   :3307     │
   │ (Python)  │  │ (JAX-WS)│  └─────────────┘
   │  :50051   │  │ External│
   └───────────┘  └─────────┘
```

## Acceso a los servicios

- **Frontend**: http://localhost:5173
- **API Gateway**: http://localhost:8080
- **GraphQL Server**: http://localhost:8000/graphql
- **Servidor gRPC**: localhost:50051
- **Kafka**: localhost:9092
- **phpMyAdmin**: http://localhost:8081
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Swagger para pruebas REST**: http://localhost:8082/swagger-ui.html

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

1. **Instalar Docker y Docker Compose** en tu sistema.

2. **Iniciar todos los servicios**:

   ```bash
   docker-compose up -d
   ```

3. **Iniciar FrontEnd**:

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. **Detener el sistema**:
   ```bash
   docker-compose down
   ```

### Acceso a los servicios

- **Frontend**: http://localhost:5173
- **API Gateway**: http://localhost:8080
- **GraphQL Server**: http://localhost:8000/graphql
- **Servidor gRPC**: localhost:50051
- **Kafka**: localhost:9092
- **phpMyAdmin**: http://localhost:8081

## Documentación de APIs

- **Swagger UI (REST)**: http://localhost:8080/swagger-ui.html / http://localhost:8082/swagger-ui.html
  - Documentación interactiva de endpoints REST
  - Incluye: Filtros guardados, Reportes Excel, Consulta Red de ONGs (SOAP)
- **GraphQL Playground**: http://localhost:8000/graphql
  - Interfaz para consultas GraphQL
  - Incluye: Informes de donaciones e informes de participación en eventos

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
