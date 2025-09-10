# API Gateway - Spring Boot

Esta es una API Gateway desarrollada con Spring Boot que actúa como intermediario entre el frontend y los servicios gRPC del servidor Python.

## Requisitos previos

- Java 17 o superior
- Maven (incluido en el proyecto con Maven Wrapper)
- Servidor gRPC Python ejecutándose

## Instalación y ejecución

### 1. Compilar el proyecto y generar clases gRPC

```bash
./mvnw compile
```

### 2. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación se iniciará en el puerto 8080 por defecto.

### 3. Verificar que está funcionando

Acceder a: `http://localhost:8080`

## Endpoints disponibles

### Usuarios

#### POST /usuarios/login

Autentica un usuario en el sistema.

**Parámetros:**

- `login` (string): Nombre de usuario o email
- `password` (string): Contraseña

**Respuesta:**

```json
{
  "ok": true,
  "message": "Login exitoso",
  "username": "usuario",
  "name": "Nombre",
  "lastname": "Apellido",
  "email": "usuario@email.com",
  "role": "VOCAL"
}
```

#### POST /usuarios/crear

Crea un nuevo usuario en el sistema.

**Parámetros:**

- `actor` (string): Usuario que realiza la acción
- `username` (string): Nombre de usuario
- `nombre` (string): Nombre
- `apellido` (string): Apellido
- `email` (string): Email
- `rol` (enum): Rol del usuario (PRESIDENTE, VICEPRESIDENTE, TESORERO, SECRETARIO, VOCAL)

### Eventos

#### GET /eventos/listar

Obtiene la lista de todos los eventos.

**Respuesta:**

```json
[
  {
    "id": 1,
    "nombre": "Visita Escuela N° 25",
    "descripcion": "Entrega de utiles escolares y actividades recreativas",
    "fechaEvento": "15/9/2025 14:00:00",
    "usuarioCreacion": 0,
    "modificadoPor": 0,
    "fechaModificacion": ""
  }
]
```

#### POST /eventos/crear

Crea un nuevo evento.

**Parámetros:**

- `actor` (string): Usuario que crea el evento
- `nombre` (string): Nombre del evento
- `descripcion` (string): Descripción del evento
- `whenIso` (string): Fecha y hora en formato ISO

#### POST /eventos/asignar

Asigna o remueve un miembro de un evento.

**Parámetros:**

- `eventoId` (int): ID del evento
- `actor` (string): Usuario que realiza la acción
- `username` (string): Usuario a asignar/remover
- `add` (boolean): true para asignar, false para remover

### Donaciones

#### GET /donaciones/listar

Obtiene la lista de todas las donaciones.

**Respuesta:**

```json
[
  {
    "id": 1,
    "category": "ROPA",
    "description": "Remeras de algodón talle M",
    "quantity": 25,
    "deleted": false,
    "createdAt": "4/9/2025 19:53:23",
    "createdBy": "vocal1",
    "updatedAt": "4/9/2025 19:53:23",
    "updatedBy": ""
  }
]
```

#### POST /donaciones/crear

Crea una nueva donación.

**Parámetros:**

- `actor` (string): Usuario que registra la donación
- `categoria` (string): Categoría de la donación (ROPA, ALIMENTOS, JUGUETES, UTILES_ESCOLARES)
- `descripcion` (string): Descripción del item
- `cantidad` (int): Cantidad de items

## Categorías de donaciones disponibles

- `ROPA`: Prendas de vestir
- `ALIMENTOS`: Comida y bebidas
- `JUGUETES`: Juguetes y material lúdico
- `UTILES_ESCOLARES`: Material escolar y educativo

## Roles de usuario disponibles

- `PRESIDENTE`: Máximo nivel de autorización
- `VICEPRESIDENTE`: Segundo nivel de autorización
- `TESORERO`: Gestión financiera
- `SECRETARIO`: Gestión administrativa
- `VOCAL`: Nivel básico de participación

## Tecnologías utilizadas

- Spring Boot 3.5.5
- Java 17
- gRPC 1.62.2
- Maven

## Endpoints pendientes de implementar

Los siguientes endpoints están disponibles en los servicios gRPC pero aún no tienen implementación en los controladores REST:

### Usuarios (pendientes)

- **GET /usuarios/listar** - Listar todos los usuarios
- **PUT /usuarios/actualizar** - Actualizar datos de un usuario
- **DELETE /usuarios/desactivar** - Desactivar un usuario

### Eventos (pendientes)

- **PUT /eventos/actualizar** - Actualizar un evento existente
- **DELETE /eventos/eliminar** - Eliminar un evento futuro
- **POST /eventos/registrar-distribuciones** - Registrar distribuciones de donaciones en un evento

### Donaciones (pendientes)

- **PUT /donaciones/actualizar** - Actualizar una donación existente
- **DELETE /donaciones/eliminar** - Eliminar (baja lógica) una donación

### Parámetros esperados para endpoints pendientes

#### PUT /usuarios/actualizar

- `actor` (string): Usuario que realiza la acción
- `id` (int): ID del usuario a actualizar
- `username` (string): Nuevo nombre de usuario
- `nombre` (string): Nuevo nombre
- `apellido` (string): Nuevo apellido
- `telefono` (string): Nuevo teléfono
- `email` (string): Nuevo email
- `rol` (enum): Nuevo rol
- `activo` (boolean): Estado activo/inactivo

#### PUT /eventos/actualizar

- `actor` (string): Usuario que realiza la acción
- `id` (int): ID del evento a actualizar
- `nombre` (string): Nuevo nombre
- `descripcion` (string): Nueva descripción
- `whenIso` (string): Nueva fecha en formato ISO

#### POST /eventos/registrar-distribuciones

- `actor` (string): Usuario que realiza la acción
- `eventoId` (int): ID del evento
- `distribuciones` (array): Lista de objetos con `categoria` y `cantidad`

#### PUT /donaciones/actualizar

- `actor` (string): Usuario que realiza la acción
- `id` (int): ID de la donación a actualizar
- `descripcion` (string): Nueva descripción
- `cantidad` (int): Nueva cantidad
