# Docker

## Prerequisitos

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Puerto 3307, 8080 y 50051 disponibles en tu sistema

## Arquitectura de Servicios

El proyecto está compuesto por 3 servicios principales:

### 1. MySQL Database (`mysql`)

- **Imagen**: `mysql:8.0`
- **Container**: `empuje_mysql`
- **Puerto**: `3307:3306` (puerto externo 3307 para evitar conflictos)
- **Base de datos**: `empuje_comunitario`
- **Usuario**: `empuje_user`
- **Contraseña**: `empuje_password`

### 2. phpMyAdmin (`phpmyadmin`)

- **Imagen**: `phpmyadmin/phpmyadmin`
- **Container**: `empuje_phpmyadmin`
- **Puerto**: `8080:80`
- **URL**: http://localhost:8080

### 3. gRPC Server (`server`)

- **Build**: `./server/Dockerfile`
- **Container**: `grpc_server`
- **Puerto**: `50051:50051`
- **Lenguaje**: Python 3.11

## Comandos

### Iniciar todos los servicios

```bash
docker-compose up -d
```

### Ver estado de los contenedores

```bash
docker-compose ps
```

### Ver logs de todos los servicios

```bash
docker-compose logs
```

### Ver logs de un servicio específico

```bash
docker-compose logs server
docker-compose logs mysql
docker-compose logs phpmyadmin
```

### Detener todos los servicios

```bash
docker-compose down
```

### Detener y eliminar volúmenes (BORRA DATOS)

```bash
docker-compose down -v
```

### Reconstruir contenedores

```bash
docker-compose build
```

## Base de Datos

Los scripts SQL se ejecutan automáticamente cuando se crea la base de datos por primera vez:

1. **`01-db.sql`** - Estructura de tablas
2. **`02-data.sql`** - Datos iniciales (usuarios, inventario, eventos)

### Acceso a la Base de Datos

#### Via phpMyAdmin (Recomendado)

- **URL**: http://localhost:8080
- **Usuario**: `empuje_user`
- **Contraseña**: `empuje_password`

#### Via MySQL Client

```bash
# Desde fuera del contenedor
mysql -h localhost -P 3307 -u empuje_user -p empuje_comunitario

# Desde dentro del contenedor
docker exec -it empuje_mysql mysql -u empuje_user -p empuje_comunitario
```

### Verificar Datos Cargados

```bash
# Contar usuarios
docker exec empuje_mysql mysql -u empuje_user -pempuje_password empuje_comunitario -e "SELECT COUNT(*) as total_usuarios FROM usuarios;"

# Contar inventario
docker exec empuje_mysql mysql -u empuje_user -pempuje_password empuje_comunitario -e "SELECT COUNT(*) as total_inventario FROM inventario;"

# Contar eventos
docker exec empuje_mysql mysql -u empuje_user -pempuje_password empuje_comunitario -e "SELECT COUNT(*) as total_eventos FROM eventos;"
```

### Variables de Entorno

Las siguientes variables están configuradas en `docker-compose.yml`:

```yaml
# MySQL
MYSQL_ROOT_PASSWORD: root_password
MYSQL_DATABASE: empuje_comunitario
MYSQL_USER: empuje_user
MYSQL_PASSWORD: empuje_password

# gRPC Server
DB_HOST: mysql
DB_PORT: 3306
DB_USER: empuje_user
DB_PASSWORD: empuje_password
DB_NAME: empuje_comunitario
```

### Volúmenes

- **`mysql_data`**: Persistencia de datos MySQL
- **`./server:/app`**: Desarrollo en vivo del servidor
- **`./database:/docker-entrypoint-initdb.d`**: Scripts de inicialización

### Health Checks

MySQL incluye un health check para asegurar que esté listo antes de iniciar el servidor:

```yaml
healthcheck:
  test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
  timeout: 20s
  retries: 10
```

## Datos de Prueba

### Usuarios por Rol

- **Presidente**: 1 usuario (`presidente1`)
- **Vocal**: 2 usuarios (`vocal1`, `vocal2`)
- **Coordinador**: 3 usuarios (`coordinador1-3`)
- **Voluntario**: 4 usuarios (`voluntario1-4`)

**Contraseña para todos**: `password123`

### Inventario

- **Ropa**: 6 items (remeras, pantalones, camperas, etc.)
- **Alimentos**: 12 items (arroz, fideos, conservas, etc.)
- **Juguetes**: 6 items (pelotas, muñecas, libros, etc.)
- **Útiles Escolares**: 6 items (cuadernos, lápices, etc.)

### Eventos

- 8 eventos de ejemplo con fechas futuras
- Diferentes tipos: donaciones, actividades recreativas, etc.
