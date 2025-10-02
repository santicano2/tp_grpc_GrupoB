## Servicios Disponibles

- **MySQL**: `localhost:3307`

  - Usuario: `empuje_user`
  - Contraseña: `empuje_password`
  - Base de datos: `empuje_comunitario`

- **PHPMyAdmin**: http://localhost:8081

  - Para administrar la base de datos visualmente

- **Servidor gRPC**: `localhost:50051`
  - Maneja usuarios, inventario y eventos (funcionalidades del TP1)
  - Servicios: UsuariosService, DonacionesService, EventosService

### **(Kafka)**

- **Zookeeper**: `localhost:2181`
- **Kafka**: `localhost:9092` (externo) / `kafka:29092` (interno)
- **Servidor Kafka**: http://localhost:8090
  - API REST para interactuar con Kafka
  - Maneja productores y consumidores

## Topics de Kafka Creados

### Topics Principales:

- `solicitud-donaciones` - Para solicitar donaciones entre ONGs
- `oferta-donaciones` - Para ofrecer donaciones
- `baja-solicitud-donaciones` - Para cancelar solicitudes
- `eventos-solidarios` - Para publicar eventos
- `baja-evento-solidario` - Para cancelar eventos

### Topics Dinámicos:

- `transferencia-donaciones-{org-id}` - Para transferir donaciones específicas
- `adhesion-evento-{org-id}` - Para adherirse a eventos de otras ONGs

### Levantar todo el stack:

```bash
docker-compose up -d
```

### Ver logs de un servicio específico:

```bash
docker logs grpc_server      # Servidor gRPC
docker logs kafka_server     # Servidor Kafka
docker logs empuje_kafka     # Kafka
```

### Verificar topics en Kafka:

```bash
docker exec empuje_kafka kafka-topics --list --bootstrap-server kafka:29092
```

### Probar API del servidor Kafka:

```bash
curl http://localhost:8090                 # Estado del servidor
curl http://localhost:8090/health          # Health check
curl http://localhost:8090/topics          # Listar topics
```

## Arquitectura

```
Frontend (React)
    ↓
API Gateway (Java)
    ↓                    ↓
gRPC Server          Kafka Server
(Python)             (Python + FastAPI)
    ↓                    ↓
MySQL                Kafka Cluster
                     (Zookeeper + Kafka)
```

## Endpoints del Servidor Kafka

### Estado y Monitoreo

- `GET /` - Estado del servidor
- `GET /health` - Health check completo
- `GET /topics` - Listar topics disponibles

### Publicar Mensajes

- `POST /solicitar-donaciones` - Solicitar donaciones a otras ONGs
- `POST /ofrecer-donaciones` - Ofrecer donaciones disponibles
- `POST /publicar-evento` - Publicar evento solidario

### Consultar Mensajes

- `GET /solicitudes-externas` - Ver solicitudes de otras ONGs
- `GET /ofertas-externas` - Ver ofertas de otras ONGs
- `GET /eventos-externos` - Ver eventos de otras ONGs

### Acciones

- `POST /adherir-evento/{evento_id}` - Adherirse a evento de otra ONG

## ID de Organización

- **ID actual**: `org-empuje-001`
- Se puede configurar via variable de entorno `ORGANIZATION_ID`
