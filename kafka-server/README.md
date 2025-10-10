# Kafka Server - TP2 Empuje Comunitario

## Descripción

Servidor dedicado para manejar toda la comunicación de Kafka. Funciona como intermediario entre el API Gateway y Kafka, proporcionando una API REST para enviar y recibir mensajes entre ONGs.

## Características

- **API REST** para interactuar con Kafka
- **Productores** automáticos para enviar mensajes
- **Consumidores** en tiempo real para recibir mensajes
- **Auto-creación** de topics dinámicos
- **Manejo de errores** y reconexión automática

## Arquitectura

```
API Gateway → Kafka Server (Puerto 8090) → Kafka Broker
                    ↓
              Consumidores activos escuchando mensajes
```

## Topics Implementados

### Topics Fijos Creados:

- `solicitud-donaciones`
- `oferta-donaciones`
- `baja-solicitud-donaciones`
- `eventos-solidarios`
- `baja-evento-solidario`

### Topics Dinámicos (se crean automáticamente):

- `transferencia-donaciones-{org-id}`
- `adhesion-evento-{org-id}`

## Configuración

### Variables de Entorno:

- `KAFKA_BOOTSTRAP_SERVERS`: Servidores Kafka (default: `kafka:29092`)
- `ORGANIZATION_ID`: ID de la organización (default: `org-empuje-001`)
- `DB_HOST`: Host de MySQL (default: `mysql`)
- `DB_PORT`: Puerto de MySQL (default: `3306`)
- `DB_USER`: Usuario de BD (default: `empuje_user`)
- `DB_PASSWORD`: Contraseña de BD (default: `empuje_password`)
- `DB_NAME`: Nombre de BD (default: `empuje_comunitario`)

## API Endpoints

### Estado y Configuración:

- `GET /` - Estado del servidor
- `GET /topics` - Listar todos los topics disponibles

### Productores (Enviar mensajes):

- `POST /solicitar-donaciones` - Solicitar donaciones a la red
- `POST /ofrecer-donaciones` - Ofrecer donaciones a la red
- `POST /publicar-evento` - Publicar evento solidario

### Consumidores (Ver mensajes recibidos):

- `GET /solicitudes-externas` - Ver solicitudes de otras ONGs
- `GET /ofertas-externas` - Ver ofertas de otras ONGs
- `GET /eventos-externos` - Ver eventos de otras ONGs

## Ejemplos de Uso

### 1. Solicitar Donaciones:

```bash
curl -X POST http://localhost:8090/solicitar-donaciones \
  -H "Content-Type: application/json" \
  -d '{
    "id_solicitud": "SOL-001",
    "donaciones": [
      {"category": "ALIMENTOS", "description": "Leche en polvo"},
      {"category": "ROPA", "description": "Abrigos de invierno"}
    ]
  }'
```

### 2. Ofrecer Donaciones:

```bash
curl -X POST http://localhost:8090/ofrecer-donaciones \
  -H "Content-Type: application/json" \
  -d '{
    "id_oferta": "OFE-001",
    "donaciones": [
      {"categoria": "ALIMENTOS", "descripcion": "Arroz", "cantidad": "50kg"},
      {"categoria": "JUGUETES", "descripcion": "Peluches", "cantidad": "20 unidades"}
    ]
  }'
```

### 3. Publicar Evento:

```bash
curl -X POST http://localhost:8090/publicar-evento \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Visita Hospital Infantil",
    "descripcion": "Actividad recreativa con los niños",
    "fecha_hora": "2025-10-15T14:00:00"
  }'
```

### 4. Ver Estado:

```bash
curl http://localhost:8090/
```

## Consumidores Automáticos

El servidor tiene **5 consumidores** corriendo automáticamente en hilos separados:

1. **Consumidor de Solicitudes** - Escucha `solicitud-donaciones`
2. **Consumidor de Ofertas** - Escucha `oferta-donaciones`
3. **Consumidor de Eventos** - Escucha `eventos-solidarios`
4. **Consumidor de Bajas Solicitudes** - Escucha `baja-solicitud-donaciones`
5. **Consumidor de Bajas Eventos** - Escucha `baja-evento-solidario`

Cada consumidor:

- Filtra mensajes propios
- Actualiza la base de datos local
- Maneja errores automáticamente

## Desarrollo

### Estructura del Código:

```
kafka-server/
├── kafka_manager.py      # Manejo de Kafka (admin, producer, consumer)
├── kafka_server.py       # Servidor FastAPI principal
├── requirements.txt      # Dependencias Python
├── Dockerfile           # Imagen Docker
└── README.md           # Esta documentación
```

### Dependencias Principales:

- `kafka-python==2.0.2` - Cliente Kafka
- `fastapi==0.104.1` - Framework web
- `uvicorn==0.24.0` - Servidor ASGI
- `mysql-connector-python==8.2.0` - Conexión MySQL

### Comandos Útiles:

```bash
# Ver topics en Kafka
docker exec empuje_kafka kafka-topics --list --bootstrap-server kafka:29092

# Ver grupos de consumidores
docker exec empuje_kafka kafka-consumer-groups --list --bootstrap-server kafka:29092

# Ver mensajes en un topic
docker exec empuje_kafka kafka-console-consumer --topic solicitud-donaciones --bootstrap-server kafka:29092 --from-beginning
```
