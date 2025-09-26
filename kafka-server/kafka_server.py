"""
Servidor Kafka - Maneja toda la comunicacion con otras ONGs
"""
import asyncio
import json
import logging
import os
import threading
from datetime import datetime
from typing import Dict, List, Any

import uvicorn
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import mysql.connector
from mysql.connector import Error

from kafka_manager import KafkaManager

# variable global para el manager
kafka_manager = None

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# configuracion de la aplicacion
app = FastAPI(title="Kafka Server - ONG Empuje Comunitario", version="1.0.0")

# configuracion de base de datos
DB_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'port': int(os.getenv('DB_PORT', '3307')),
    'user': os.getenv('DB_USER', 'empuje_user'),
    'password': os.getenv('DB_PASSWORD', 'empuje_password'),
    'database': os.getenv('DB_NAME', 'empuje_comunitario'),
}

ORGANIZATION_ID = os.getenv('ORGANIZATION_ID', 'org-empuje-001')

# modelos pydantic para API REST
class DonationRequest(BaseModel):
    category: str
    description: str

class SolicitudDonacion(BaseModel):
    id_solicitud: str
    donaciones: List[DonationRequest]

class OfertaDonacion(BaseModel):
    id_oferta: str
    donaciones: List[Dict[str, Any]]  # incluye cantidad

class EventoSolidario(BaseModel):
    nombre: str
    descripcion: str
    fecha_hora: str

# base de datos helper
def get_db_connection():
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        return connection
    except Error as e:
        logger.error(f"Error conectando a la base de datos: {e}")
        return None

# =================== ENDPOINTS API REST ===================

@app.get("/")
async def root():
    return {
        "message": "Kafka Server - ONG Empuje Comunitario",
        "organization_id": ORGANIZATION_ID,
        "status": "running"
    }

@app.get("/topics")
async def list_topics():
    """Lista todos los topics disponibles"""
    if kafka_manager is None:
        raise HTTPException(status_code=503, detail="Kafka no inicializado")
    topics = kafka_manager.list_topics()
    return {"topics": topics}

@app.post("/solicitar-donaciones")
async def solicitar_donaciones(solicitud: SolicitudDonacion):
    """Publica una solicitud de donaciones"""
    try:
        mensaje = {
            "id_organizacion_solicitante": ORGANIZATION_ID,
            "id_solicitud": solicitud.id_solicitud,
            "timestamp": datetime.now().isoformat(),
            "donaciones": [
                {
                    "categoria": d.category,
                    "descripcion": d.description
                } for d in solicitud.donaciones
            ]
        }
        
        if kafka_manager is None:
            raise HTTPException(status_code=503, detail="Kafka no inicializado")
            
        success = kafka_manager.send_message(
            topic="solicitud-donaciones",
            message=mensaje,
            key=f"{ORGANIZATION_ID}_{solicitud.id_solicitud}"
        )
        
        if success:
            return {"status": "success", "message": "Solicitud publicada correctamente"}
        else:
            raise HTTPException(status_code=500, detail="Error enviando solicitud")
            
    except Exception as e:
        logger.error(f"Error en solicitar_donaciones: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/ofrecer-donaciones")
async def ofrecer_donaciones(oferta: OfertaDonacion):
    """Publica una oferta de donaciones"""
    try:
        mensaje = {
            "id_oferta": oferta.id_oferta,
            "id_organizacion_donante": ORGANIZATION_ID,
            "timestamp": datetime.now().isoformat(),
            "donaciones": oferta.donaciones
        }
        
        success = kafka_manager.send_message(
            topic="oferta-donaciones",
            message=mensaje,
            key=f"{ORGANIZATION_ID}_{oferta.id_oferta}"
        )
        
        if success:
            return {"status": "success", "message": "Oferta publicada correctamente"}
        else:
            raise HTTPException(status_code=500, detail="Error enviando oferta")
            
    except Exception as e:
        logger.error(f"Error en ofrecer_donaciones: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/publicar-evento")
async def publicar_evento(evento: EventoSolidario):
    """Publica un evento solidario"""
    try:
        # obtener ID del evento desde la base de datos asumiendo que ya existe
        # por ahora usamos un ID temporal
        event_id = f"event_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
        
        mensaje = {
            "id_organizacion": ORGANIZATION_ID,
            "id_evento": event_id,
            "nombre": evento.nombre,
            "descripcion": evento.descripcion,
            "fecha_hora": evento.fecha_hora,
            "timestamp": datetime.now().isoformat()
        }
        
        success = kafka_manager.send_message(
            topic="eventos-solidarios",
            message=mensaje,
            key=f"{ORGANIZATION_ID}_{event_id}"
        )
        
        if success:
            return {"status": "success", "message": "Evento publicado correctamente"}
        else:
            raise HTTPException(status_code=500, detail="Error enviando evento")
            
    except Exception as e:
        logger.error(f"Error en publicar_evento: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/solicitudes-externas")
async def get_solicitudes_externas():
    """Obtiene las solicitudes de donaciones de otras organizaciones"""
    # TODO: implementar consulta a base de datos local donde se guardan las solicitudes externas
    return {"solicitudes": []}

@app.get("/ofertas-externas")
async def get_ofertas_externas():
    """Obtiene las ofertas de donaciones de otras organizaciones"""
    # TODO: implementar consulta a base de datos local donde se guardan las ofertas externas
    return {"ofertas": []}

@app.get("/eventos-externos")
async def get_eventos_externos():
    """Obtiene los eventos de otras organizaciones"""
    # TODO: implementar consulta a base de datos local donde se guardan los eventos externos
    return {"eventos": []}

# =================== CONSUMIDORES KAFKA ===================

def process_solicitud_donaciones(message):
    """Procesa mensajes del topic solicitud-donaciones"""
    try:
        data = message.value
        org_id = data.get('id_organizacion_solicitante')
        
        # no procesar nuestros propios mensajes
        if org_id == ORGANIZATION_ID:
            return
            
        logger.info(f"Nueva solicitud de donacion de {org_id}: {data}")
        
        # TODO: guardar en base de datos local
        # save_external_donation_request(data)
        
    except Exception as e:
        logger.error(f"Error procesando solicitud de donacion: {e}")

def process_oferta_donaciones(message):
    """Procesa mensajes del topic oferta-donaciones"""
    try:
        data = message.value
        org_id = data.get('id_organizacion_donante')
        
        # no procesar nuestros propios mensajes
        if org_id == ORGANIZATION_ID:
            return
            
        logger.info(f"Nueva oferta de donacion de {org_id}: {data}")
        
        # TODO: guardar en base de datos local
        # save_external_donation_offer(data)
        
    except Exception as e:
        logger.error(f"Error procesando oferta de donacion: {e}")

def process_eventos_solidarios(message):
    """Procesa mensajes del topic eventos-solidarios"""
    try:
        data = message.value
        org_id = data.get('id_organizacion')
        
        # No procesar nuestros propios mensajes
        if org_id == ORGANIZATION_ID:
            return
            
        logger.info(f"Nuevo evento solidario de {org_id}: {data}")

        # TODO: guardar en base de datos local
        # save_external_event(data)
        
    except Exception as e:
        logger.error(f"Error procesando evento solidario: {e}")

def process_baja_solicitud(message):
    """Procesa mensajes del topic baja-solicitud-donaciones"""
    try:
        data = message.value
        org_id = data.get('id_organizacion_solicitante')
        solicitud_id = data.get('id_solicitud')
        
        logger.info(f"Baja de solicitud de {org_id}: {solicitud_id}")

        # TODO: marcar como eliminada en base de datos local
        # mark_external_request_as_deleted(org_id, solicitud_id)
        
    except Exception as e:
        logger.error(f"Error procesando baja de solicitud: {e}")

def process_baja_evento(message):
    """Procesa mensajes del topic baja-evento-solidario"""
    try:
        data = message.value
        org_id = data.get('id_organizacion')
        evento_id = data.get('id_evento')
        
        logger.info(f"Baja de evento de {org_id}: {evento_id}")

        # TODO: marcar como eliminado en base de datos local
        # mark_external_event_as_deleted(org_id, evento_id)
        
    except Exception as e:
        logger.error(f"Error procesando baja de evento: {e}")

def start_kafka_consumers():
    """Inicia los consumidores de Kafka en hilos separados"""
    
    def consume_topic(topics, processor, group_suffix=""):
        """Función generica para consumir un topic"""
        try:
            consumer = kafka_manager.get_consumer(
                topics=topics,
                group_id=f"empuje_{ORGANIZATION_ID}_{group_suffix}"
            )
            
            logger.info(f"Consumidor iniciado para topics: {topics}")
            
            for message in consumer:
                if message.value:
                    processor(message)
                    
        except Exception as e:
            logger.error(f"Error en consumidor {topics}: {e}")
    
    # iniciar consumidores en hilos separados
    consumers = [
        (["solicitud-donaciones"], process_solicitud_donaciones, "solicitudes"),
        (["oferta-donaciones"], process_oferta_donaciones, "ofertas"),
        (["eventos-solidarios"], process_eventos_solidarios, "eventos"),
        (["baja-solicitud-donaciones"], process_baja_solicitud, "baja_solicitudes"),
        (["baja-evento-solidario"], process_baja_evento, "baja_eventos"),
    ]
    
    for topics, processor, group_suffix in consumers:
        thread = threading.Thread(
            target=consume_topic,
            args=(topics, processor, group_suffix),
            daemon=True
        )
        thread.start()
    
    logger.info("Todos los consumidores iniciados")

# =================== INICIALIZACION ===================

@app.on_event("startup")
async def startup_event():
    """Inicializa Kafka al arrancar el servidor"""
    global kafka_manager
    logger.info("Iniciando servidor Kafka...")
    
    try:
        # inicializar el manager de Kafka
        kafka_manager = KafkaManager()
        logger.info("KafkaManager inicializado")
        
        # crear topics necesarios
        created_topics = kafka_manager.create_required_topics()
        logger.info(f"Topics creados/verificados: {len(created_topics)}")
        
        # iniciar consumidores
        start_kafka_consumers()
        
        logger.info("Servidor Kafka iniciado correctamente")
        
    except Exception as e:
        logger.error(f"Error iniciando servidor Kafka: {e}")
        raise

@app.on_event("shutdown")
async def shutdown_event():
    """Limpia recursos al cerrar el servidor"""
    logger.info("Cerrando servidor Kafka...")
    kafka_manager.close()

if __name__ == "__main__":
    # ejecutar servidor
    uvicorn.run(
        "kafka_server:app",
        host="0.0.0.0",
        port=8090,
        log_level="info",
        reload=False
    )