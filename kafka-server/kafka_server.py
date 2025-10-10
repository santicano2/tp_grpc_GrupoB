"""
Servidor Kafka - Maneja toda la comunicacion con otras ONGs
"""
import asyncio
import json
import logging
import os
import re
import threading
from datetime import datetime
from typing import Dict, List, Any

import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
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

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://localhost:3000"],  # Frontend
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

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

# =================== FUNCIONES DE BASE DE DATOS ===================

def save_external_donation_request(data):
    """Guarda una solicitud de donacion externa en la BD"""
    try:
        connection = get_db_connection()
        if not connection:
            return False
            
        cursor = connection.cursor()
        
        org_id = data.get('idOrganizacion') or data.get('id_organizacion_solicitante')
        solicitud_id = data.get('idSolicitud') or data.get('id_solicitud')
        categoria = data.get('categoria')
        descripcion = data.get('descripcion')
        
        # El mensaje puede venir como un solo objeto o con lista de donaciones
        donaciones = data.get('donaciones', [data] if categoria else [])
        
        for donacion in donaciones:
            query = """
                INSERT INTO solicitudes_externas 
                (id_solicitud, id_organizacion_solicitante, categoria, descripcion, activa)
                VALUES (%s, %s, %s, %s, %s)
                ON DUPLICATE KEY UPDATE
                descripcion = VALUES(descripcion),
                activa = TRUE,
                fecha_solicitud = CURRENT_TIMESTAMP
            """
            
            cat = donacion.get('categoria') if isinstance(donacion, dict) else categoria
            desc = donacion.get('descripcion') if isinstance(donacion, dict) else descripcion
            
            cursor.execute(query, (
                solicitud_id,
                org_id,
                cat,
                desc,
                True
            ))
        
        connection.commit()
        logger.info(f"Solicitud externa guardada: {org_id}/{solicitud_id}")
        return True
        
    except Error as e:
        logger.error(f"Error guardando solicitud externa: {e}")
        if connection:
            connection.rollback()
        return False
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

def save_external_donation_offer(data):
    """Guarda una oferta de donacion externa en la BD"""
    try:
        connection = get_db_connection()
        if not connection:
            return False
            
        cursor = connection.cursor()
        
        org_id = data.get('idOrganizacionDonante') or data.get('id_organizacion_donante')
        oferta_id = data.get('idOferta') or data.get('id_oferta')
        donaciones = data.get('donaciones', [])
        
        for donacion in donaciones:
            # Extraer solo el número de la cantidad (ej: "2kg" -> 2)
            cantidad_str = str(donacion.get('cantidad', '0'))
            # Extraer solo dígitos del inicio
            import re
            cantidad_match = re.match(r'(\d+)', cantidad_str)
            cantidad = int(cantidad_match.group(1)) if cantidad_match else 0
            
            query = """
                INSERT INTO ofertas_externas 
                (id_oferta, id_organizacion_donante, categoria, descripcion, cantidad, activa)
                VALUES (%s, %s, %s, %s, %s, %s)
                ON DUPLICATE KEY UPDATE
                descripcion = VALUES(descripcion),
                cantidad = VALUES(cantidad),
                activa = TRUE,
                fecha_oferta = CURRENT_TIMESTAMP
            """
            
            cursor.execute(query, (
                oferta_id,
                org_id,
                donacion.get('categoria'),
                donacion.get('descripcion'),
                cantidad,
                True
            ))
        
        connection.commit()
        logger.info(f"Oferta externa guardada: {org_id}/{oferta_id}")
        return True
        
    except Error as e:
        logger.error(f"Error guardando oferta externa: {e}")
        if connection:
            connection.rollback()
        return False
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()
def save_external_event(data):
    """Guarda un evento solidario externo en la BD"""
    try:
        connection = get_db_connection()
        if not connection:
            return False
            
        cursor = connection.cursor()
        
        evento_id = data.get('idEvento') or data.get('id_evento')
        org_id = data.get('idOrganizacion') or data.get('id_organizacion')
        nombre = data.get('nombre')
        descripcion = data.get('descripcion')
        fecha_hora_str = data.get('fechaHora') or data.get('fecha_hora')
        
        query = """
            INSERT INTO eventos_externos 
            (id_evento, id_organizacion, nombre, descripcion, fecha_evento, activo)
            VALUES (%s, %s, %s, %s, %s, %s)
            ON DUPLICATE KEY UPDATE
            nombre = VALUES(nombre),
            descripcion = VALUES(descripcion),
            fecha_evento = VALUES(fecha_evento),
            activo = TRUE,
            fecha_publicacion = CURRENT_TIMESTAMP
        """
        
        # Parsear fecha/hora
        try:
            fecha_evento = datetime.fromisoformat(fecha_hora_str.replace('Z', '+00:00'))
        except:
            # Si falla, usar la fecha como string o fecha actual
            fecha_evento = fecha_hora_str if fecha_hora_str else datetime.now()
        
        cursor.execute(query, (
            evento_id,
            org_id,
            nombre,
            descripcion,
            fecha_evento,
            True
        ))
        
        connection.commit()
        logger.info(f"Evento externo guardado: {org_id}/{evento_id}")
        return True
        
    except Error as e:
        logger.error(f"Error guardando evento externo: {e}")
        if connection:
            connection.rollback()
        return False
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

def mark_external_request_as_deleted(org_id, solicitud_id):
    """Marca una solicitud externa como dada de baja"""
    try:
        connection = get_db_connection()
        if not connection:
            return False
            
        cursor = connection.cursor()
        
        query = """
            UPDATE solicitudes_externas 
            SET activa = FALSE, fecha_baja = CURRENT_TIMESTAMP
            WHERE id_organizacion_solicitante = %s AND id_solicitud = %s
        """
        
        cursor.execute(query, (org_id, solicitud_id))
        connection.commit()
        
        affected_rows = cursor.rowcount
        logger.info(f"Solicitudes marcadas como eliminadas: {affected_rows}")
        return True
        
    except Error as e:
        logger.error(f"Error marcando solicitud como eliminada: {e}")
        if connection:
            connection.rollback()
        return False
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

def mark_external_event_as_deleted(org_id, evento_id):
    """Marca un evento externo como dado de baja"""
    try:
        connection = get_db_connection()
        if not connection:
            return False
            
        cursor = connection.cursor()
        
        query = """
            UPDATE eventos_externos 
            SET activo = FALSE, fecha_baja = CURRENT_TIMESTAMP
            WHERE id_organizacion = %s AND id_evento = %s
        """
        
        cursor.execute(query, (org_id, evento_id))
        connection.commit()
        
        affected_rows = cursor.rowcount
        logger.info(f"Eventos marcados como eliminados: {affected_rows}")
        return True
        
    except Error as e:
        logger.error(f"Error marcando evento como eliminado: {e}")
        if connection:
            connection.rollback()
        return False
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

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
async def get_solicitudes_externas(exclude_org: str = None):
    """Obtiene las solicitudes de donaciones de otras organizaciones
    
    Args:
        exclude_org: ID de la organización a excluir (normalmente la propia)
    """
    try:
        connection = get_db_connection()
        if not connection:
            raise HTTPException(status_code=500, detail="Error de conexion a BD")
            
        cursor = connection.cursor(dictionary=True)
        
        # Construir query con filtro opcional
        query = """
            SELECT s.*, 
                   COALESCE(org1.nombre, org2.nombre) as nombre_organizacion
            FROM solicitudes_externas s
            LEFT JOIN organizaciones org1 ON s.id_organizacion_solicitante = org1.id_organizacion
            LEFT JOIN organizaciones org2 ON s.id_organizacion_solicitante = org2.id
            WHERE s.activa = TRUE
        """
        
        # Agregar filtro para excluir organización propia
        if exclude_org:
            query += f" AND s.id_organizacion_solicitante != '{exclude_org}'"
        
        query += " ORDER BY s.fecha_solicitud DESC"
        
        cursor.execute(query)
        solicitudes_db = cursor.fetchall()
        
        # Agrupar por id_solicitud (pueden haber múltiples donaciones por solicitud)
        solicitudes_agrupadas = {}
        for sol in solicitudes_db:
            id_sol = str(sol.get("id_solicitud"))
            
            if id_sol not in solicitudes_agrupadas:
                solicitudes_agrupadas[id_sol] = {
                    "id": sol.get("id"),
                    "idSolicitud": id_sol,
                    "idOrganizacion": sol.get("id_organizacion_solicitante"),
                    "activa": sol.get("activa"),
                    "fechaSolicitud": sol.get("fecha_solicitud").isoformat() if sol.get("fecha_solicitud") else None,
                    "nombreOrganizacion": sol.get("nombre_organizacion"),
                    "donaciones": []
                }
            
            # Agregar donación al array
            solicitudes_agrupadas[id_sol]["donaciones"].append({
                "categoria": sol.get("categoria"),
                "descripcion": sol.get("descripcion")
            })
        
        # Convertir el dict a lista
        solicitudes_formateadas = list(solicitudes_agrupadas.values())
        
        return {"solicitudes": solicitudes_formateadas}
        
    except Exception as e:
        logger.error(f"Error obteniendo solicitudes externas: {e}")
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

@app.get("/ofertas-externas")
async def get_ofertas_externas(exclude_org: str = None):
    """Obtiene las ofertas de donaciones de otras organizaciones (agrupadas por oferta con sus donaciones)
    
    Args:
        exclude_org: ID de la organización a excluir (normalmente la propia)
    """
    try:
        connection = get_db_connection()
        if not connection:
            raise HTTPException(status_code=500, detail="Error de conexion a BD")
            
        cursor = connection.cursor(dictionary=True)
        
        # Consulta que trae ofertas con sus donaciones
        query = """
            SELECT 
                o.id_oferta,
                o.id_organizacion_donante,
                COALESCE(org1.nombre, org2.nombre) as nombre_organizacion,
                o.categoria,
                o.descripcion,
                o.cantidad,
                o.fecha_oferta,
                o.activa
            FROM ofertas_externas o
            LEFT JOIN organizaciones org1 ON o.id_organizacion_donante = org1.id_organizacion
            LEFT JOIN organizaciones org2 ON o.id_organizacion_donante = org2.id
            WHERE o.activa = TRUE
        """
        
        # Agregar filtro para excluir organización propia
        if exclude_org:
            query += f" AND o.id_organizacion_donante != '{exclude_org}'"
        
        query += " ORDER BY o.fecha_oferta DESC, o.id_oferta, o.categoria"
        
        cursor.execute(query)
        rows = cursor.fetchall()
        
        # Agrupar por oferta y crear array de donaciones
        ofertas_dict = {}
        for row in rows:
            oferta_id = row['id_oferta']
            
            if oferta_id not in ofertas_dict:
                ofertas_dict[oferta_id] = {
                    'idOferta': str(oferta_id),
                    'idOrganizacion': row['id_organizacion_donante'],
                    'nombreOrganizacion': row['nombre_organizacion'],
                    'fechaOferta': row['fecha_oferta'].isoformat() if row['fecha_oferta'] else None,
                    'activa': bool(row['activa']),
                    'donaciones': []
                }
            
            # Agregar donacion
            ofertas_dict[oferta_id]['donaciones'].append({
                'categoria': row['categoria'],
                'descripcion': row['descripcion'],
                'cantidad': row['cantidad']
            })
        
        ofertas_list = list(ofertas_dict.values())
        
        return {"ofertas": ofertas_list}
        
    except Exception as e:
        logger.error(f"Error obteniendo ofertas externas: {e}")
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

@app.get("/eventos-externos")
async def get_eventos_externos(exclude_org: str = None):
    """Obtiene los eventos de otras organizaciones
    
    Args:
        exclude_org: ID de la organización a excluir (normalmente la propia)
    """
    try:
        connection = get_db_connection()
        if not connection:
            raise HTTPException(status_code=500, detail="Error de conexion a BD")
            
        cursor = connection.cursor(dictionary=True)
        
        query = """
            SELECT e.*, 
                   COALESCE(org1.nombre, org2.nombre) as nombre_organizacion
            FROM eventos_externos e
            LEFT JOIN organizaciones org1 ON e.id_organizacion = org1.id_organizacion
            LEFT JOIN organizaciones org2 ON e.id_organizacion = org2.id
            WHERE e.activo = TRUE AND e.fecha_evento >= NOW()
        """
        
        # Agregar filtro para excluir organización propia
        if exclude_org:
            query += f" AND e.id_organizacion != '{exclude_org}'"
        
        query += " ORDER BY e.fecha_evento ASC"
        
        cursor.execute(query)
        rows = cursor.fetchall()
        
        # Transformar a camelCase para el frontend
        eventos_list = []
        for row in rows:
            eventos_list.append({
                'idEvento': str(row['id_evento']),
                'idOrganizacion': row['id_organizacion'],
                'nombreOrganizacion': row['nombre_organizacion'],
                'nombre': row['nombre'],
                'descripcion': row['descripcion'],
                'fechaEvento': row['fecha_evento'].isoformat() if row['fecha_evento'] else None,
                'activo': bool(row['activo'])
            })
        
        return {"eventos": eventos_list}
        
    except Exception as e:
        logger.error(f"Error obteniendo eventos externos: {e}")
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

@app.get("/transferencias-recibidas")
async def get_transferencias_recibidas():
    """Obtiene las transferencias de donaciones recibidas (agrupadas por solicitud)"""
    try:
        connection = get_db_connection()
        if not connection:
            raise HTTPException(status_code=500, detail="Error de conexion a BD")
            
        cursor = connection.cursor(dictionary=True)
        
        query = """
            SELECT 
                t.id_solicitud,
                t.id_organizacion_solicitante,
                t.id_organizacion_donante,
                COALESCE(org1.nombre, org2.nombre) as nombre_org_donante,
                t.categoria,
                t.descripcion,
                t.cantidad,
                t.fecha_transferencia,
                t.tipo_transferencia
            FROM transferencias_donaciones t
            LEFT JOIN organizaciones org1 ON t.id_organizacion_donante = org1.id_organizacion
            LEFT JOIN organizaciones org2 ON t.id_organizacion_donante = org2.id
            WHERE t.tipo_transferencia = 'ENVIADA'
            ORDER BY t.fecha_transferencia DESC, t.id_solicitud
        """
        
        cursor.execute(query)
        rows = cursor.fetchall()
        
        # Agrupar por solicitud
        transferencias_dict = {}
        for row in rows:
            solicitud_id = str(row['id_solicitud'])
            
            if solicitud_id not in transferencias_dict:
                transferencias_dict[solicitud_id] = {
                    'idSolicitud': solicitud_id,
                    'idOrganizacionSolicitante': row['id_organizacion_solicitante'],
                    'idOrganizacionDonante': row['id_organizacion_donante'],
                    'nombreOrganizacionDonante': row['nombre_org_donante'],
                    'fechaTransferencia': row['fecha_transferencia'].isoformat() if row['fecha_transferencia'] else None,
                    'donaciones': []
                }
            
            # Agregar donacion
            transferencias_dict[solicitud_id]['donaciones'].append({
                'categoria': row['categoria'],
                'descripcion': row['descripcion'],
                'cantidad': row['cantidad']
            })
        
        transferencias_list = list(transferencias_dict.values())
        
        return {"transferencias": transferencias_list}
        
    except Exception as e:
        logger.error(f"Error obteniendo transferencias recibidas: {e}")
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

# =================== CONSUMIDORES KAFKA ===================

def process_solicitud_donaciones(message):
    """Procesa mensajes del topic solicitud-donaciones"""
    try:
        data = message.value
        org_id = data.get('idOrganizacion') or data.get('id_organizacion_solicitante')
        
        # no procesar nuestros propios mensajes
        if org_id == ORGANIZATION_ID:
            return
            
        logger.info(f"Nueva solicitud de donacion de {org_id}: {data}")
        
        # guardar en base de datos
        save_external_donation_request(data)
        
    except Exception as e:
        logger.error(f"Error procesando solicitud de donacion: {e}")

def process_oferta_donaciones(message):
    """Procesa mensajes del topic oferta-donaciones"""
    try:
        data = message.value
        org_id = data.get('idOrganizacionDonante') or data.get('id_organizacion_donante')
        
        # no procesar nuestros propios mensajes
        if org_id == ORGANIZATION_ID:
            return
            
        logger.info(f"Nueva oferta de donacion de {org_id}: {data}")
        
        # guardar en base de datos
        save_external_donation_offer(data)
        
    except Exception as e:
        logger.error(f"Error procesando oferta de donacion: {e}")

def process_eventos_solidarios(message):
    """Procesa mensajes del topic eventos-solidarios"""
    try:
        data = message.value
        org_id = data.get('idOrganizacion') or data.get('id_organizacion')
        
        # no procesar nuestros propios mensajes
        if org_id == ORGANIZATION_ID:
            return
            
        logger.info(f"Nuevo evento solidario de {org_id}: {data}")

        # guardar en base de datos
        save_external_event(data)
        
    except Exception as e:
        logger.error(f"Error procesando evento solidario: {e}")

def process_baja_solicitud(message):
    """Procesa mensajes del topic baja-solicitud-donaciones"""
    try:
        data = message.value
        org_id = data.get('idOrganizacionSolicitante') or data.get('id_organizacion_solicitante')
        solicitud_id = data.get('idSolicitud') or data.get('id_solicitud')
        
        logger.info(f"Baja de solicitud de {org_id}: {solicitud_id}")

        # marcar como eliminada en base de datos
        mark_external_request_as_deleted(org_id, solicitud_id)
        
    except Exception as e:
        logger.error(f"Error procesando baja de solicitud: {e}")

def process_baja_evento(message):
    """Procesa mensajes del topic baja-evento-solidario"""
    try:
        data = message.value
        org_id = data.get('idOrganizacion') or data.get('id_organizacion')
        evento_id = data.get('idEvento') or data.get('id_evento')
        
        logger.info(f"Baja de evento de {org_id}: {evento_id}")

        # marcar como eliminado en base de datos
        mark_external_event_as_deleted(org_id, evento_id)
        
    except Exception as e:
        logger.error(f"Error procesando baja de evento: {e}")

def process_transferencia_donaciones(message):
    """Procesa mensajes del topic transferencia-donaciones-{id_solicitud}"""
    try:
        logger.info(f"[TRANSFERENCIA] Recibido mensaje en topic {message.topic}")
        logger.info(f"[TRANSFERENCIA] Contenido raw: {message.value}")
        
        data = message.value
        id_solicitud = data.get('idSolicitud') or data.get('id_solicitud')
        id_org_solicitante = data.get('idOrganizacionSolicitante') or data.get('id_organizacion_solicitante')
        id_org_donante = data.get('idOrganizacionDonante') or data.get('id_organizacion_donante')
        donaciones = data.get('donaciones', [])
        
        logger.info(f"[TRANSFERENCIA] Procesando transferencia para solicitud {id_solicitud} de org {id_org_donante} a org {id_org_solicitante} con {len(donaciones)} donaciones")
        
        # Guardar cada donacion como una transferencia
        connection = get_db_connection()
        if not connection:
            logger.error("No se pudo conectar a la BD para guardar transferencia")
            return
            
        try:
            cursor = connection.cursor()
            
            for donacion in donaciones:
                # Extraer cantidad
                import re
                cantidad_str = str(donacion.get('cantidad', '0'))
                cantidad_match = re.match(r'(\d+)', cantidad_str)
                cantidad = int(cantidad_match.group(1)) if cantidad_match else 0
                
                query = """
                    INSERT INTO transferencias_donaciones 
                    (id_solicitud, id_organizacion_solicitante, id_organizacion_donante, 
                     categoria, descripcion, cantidad, tipo_transferencia)
                    VALUES (%s, %s, %s, %s, %s, %s, %s)
                """
                
                cursor.execute(query, (
                    id_solicitud,
                    id_org_solicitante,
                    id_org_donante,
                    donacion.get('categoria'),
                    donacion.get('descripcion'),
                    cantidad,
                    'ENVIADA'
                ))
            
            connection.commit()
            logger.info(f"Transferencia guardada: {len(donaciones)} donaciones")
            
        except Error as e:
            logger.error(f"Error guardando transferencia: {e}")
            if connection:
                connection.rollback()
        finally:
            if connection and connection.is_connected():
                cursor.close()
                connection.close()
        
    except Exception as e:
        logger.error(f"Error procesando transferencia: {e}")

def process_adhesion_evento(message):
    """Procesa mensajes del topic adhesion-evento-{id_organizacion}"""
    try:
        data = message.value
        id_evento = data.get('idEvento') or data.get('id_evento')
        id_organizacion = data.get('idOrganizacion') or data.get('id_organizacion')
        id_voluntario = data.get('idVoluntario') or data.get('id_voluntario')
        nombre = data.get('nombre')
        apellido = data.get('apellido')
        email = data.get('email')
        telefono = data.get('telefono')
        
        logger.info(f"Adhesión al evento {id_evento} de {nombre} {apellido} (org: {id_organizacion})")
        
        connection = get_db_connection()
        if not connection:
            logger.error("No se pudo conectar a la BD para procesar adhesión")
            return
            
        try:
            cursor = connection.cursor(dictionary=True)
            
            # Buscar el evento externo por id_evento
            query_evento = """
                SELECT id FROM eventos_externos 
                WHERE id_evento = %s AND activo = TRUE
                LIMIT 1
            """
            cursor.execute(query_evento, (id_evento,))
            evento = cursor.fetchone()
            
            if not evento:
                logger.warning(f"Evento {id_evento} no encontrado o inactivo")
                return
            
            id_evento_interno = evento['id']
            
            # Guardar la adhesión
            query = """
                INSERT INTO adhesiones_eventos 
                (id_evento_externo, id_voluntario, activa)
                VALUES (%s, %s, TRUE)
                ON DUPLICATE KEY UPDATE 
                    activa = TRUE,
                    fecha_adhesion = CURRENT_TIMESTAMP
            """
            
            cursor.execute(query, (id_evento_interno, id_voluntario))
            connection.commit()
            
            logger.info(f"Adhesión guardada: evento {id_evento} (interno: {id_evento_interno}), voluntario {id_voluntario}")
            
        except Error as e:
            logger.error(f"Error guardando adhesión: {e}")
            if connection:
                connection.rollback()
        finally:
            if connection and connection.is_connected():
                cursor.close()
                connection.close()
        
    except Exception as e:
        logger.error(f"Error procesando adhesión: {e}")

def start_kafka_consumers():
    """Inicia los consumidores de Kafka en hilos separados"""
    
    def consume_topic(topics=None, processor=None, group_suffix="", topic_pattern=None):
        """Función generica para consumir un topic o patrón"""
        try:
            if topic_pattern:
                consumer = kafka_manager.get_consumer(
                    topic_pattern=topic_pattern,
                    group_id=f"empuje_{ORGANIZATION_ID}_{group_suffix}"
                )
                logger.info(f"Consumidor iniciado para patrón: {topic_pattern}")
            else:
                consumer = kafka_manager.get_consumer(
                    topics=topics,
                    group_id=f"empuje_{ORGANIZATION_ID}_{group_suffix}"
                )
                logger.info(f"Consumidor iniciado para topics: {topics}")
            
            for message in consumer:
                if message.value:
                    processor(message)
                    
        except Exception as e:
            logger.error(f"Error en consumidor: {e}")
    
    # iniciar consumidores en hilos separados
    # (topics, processor, group_suffix, topic_pattern)
    consumers = [
        (["solicitud-donaciones"], process_solicitud_donaciones, "solicitudes", None),
        (["oferta-donaciones"], process_oferta_donaciones, "ofertas", None),
        (["eventos-solidarios"], process_eventos_solidarios, "eventos", None),
        (["baja-solicitud-donaciones"], process_baja_solicitud, "baja_solicitudes", None),
        (["baja-evento-solidario"], process_baja_evento, "baja_eventos", None),
        # Pattern para capturar todos los topics de transferencias (transferencia-donaciones-*)
        (None, process_transferencia_donaciones, "transferencias", r'^transferencia-donaciones-.*'),
        # Pattern para capturar todos los topics de adhesiones (adhesion-evento-*)
        (None, process_adhesion_evento, "adhesiones", r'^adhesion-evento-.*'),
    ]
    
    for topics, processor, group_suffix, pattern in consumers:
        thread = threading.Thread(
            target=consume_topic,
            args=(topics, processor, group_suffix, pattern),
            daemon=True
        )
        thread.start()
    
    logger.info("Todos los consumidores iniciados")

# =================== ENDPOINTS HELPERS ===================

@app.post("/adherir-evento/{evento_id}")
async def adherir_evento(evento_id: int, voluntario_data: dict):
    """Permite que un voluntario se adhiera a un evento externo"""
    try:
        connection = get_db_connection()
        if not connection:
            raise HTTPException(status_code=500, detail="Error de conexion a BD")
            
        cursor = connection.cursor()
        
        # verifica que el evento existe y esta activo
        check_query = "SELECT id, id_organizacion FROM eventos_externos WHERE id = %s AND activo = TRUE"
        cursor.execute(check_query, (evento_id,))
        evento = cursor.fetchone()
        
        if not evento:
            raise HTTPException(status_code=404, detail="Evento no encontrado o inactivo")
            
        insert_query = """
            INSERT INTO adhesiones_eventos (id_evento_externo, id_voluntario, activa)
            VALUES (%s, %s, %s)
            ON DUPLICATE KEY UPDATE activa = TRUE, fecha_adhesion = CURRENT_TIMESTAMP
        """
        
        cursor.execute(insert_query, (evento_id, voluntario_data.get('id_voluntario'), True))
        connection.commit()
        
        # enviar notificacion por Kafka al organizador
        org_destino = evento[1]
        mensaje_adhesion = {
            "id_evento": str(evento_id),
            "voluntario": {
                "id_organizacion": ORGANIZATION_ID,
                "id_voluntario": voluntario_data.get('id_voluntario'),
                "nombre": voluntario_data.get('nombre'),
                "apellido": voluntario_data.get('apellido'),
                "telefono": voluntario_data.get('telefono'),
                "email": voluntario_data.get('email')
            },
            "timestamp": datetime.now().isoformat()
        }
        
        # enviar a topic especifico de la organizacion (sin barras)
        topic_adhesion = f"adhesion-evento-{org_destino}"
        
        # crear topic dinamico si no existe
        if not kafka_manager.create_dynamic_topic(topic_adhesion):
            logger.warning(f"No se pudo crear topic dinamico: {topic_adhesion}")
        
        success = kafka_manager.send_message(
            topic=topic_adhesion,
            message=mensaje_adhesion,
            key=f"{ORGANIZATION_ID}_{evento_id}_{voluntario_data.get('id_voluntario')}"
        )
        
        if success:
            return {"status": "success", "message": "Adhesion registrada y notificacion enviada"}
        else:
            raise HTTPException(status_code=500, detail="Error enviando notificacion")
            
    except Exception as e:
        logger.error(f"Error en adherir_evento: {e}")
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

@app.get("/health")
async def health_check():
    """Endpoint de verificacion de salud"""
    try:
        # verificar conexion a BD
        connection = get_db_connection()
        if connection:
            connection.close()
            db_status = "ok"
        else:
            db_status = "error"
            
        # verificar Kafka
        kafka_status = "ok" if kafka_manager else "error"
        
        return {
            "status": "healthy" if db_status == "ok" and kafka_status == "ok" else "unhealthy",
            "database": db_status,
            "kafka": kafka_status,
            "organization_id": ORGANIZATION_ID,
            "timestamp": datetime.now().isoformat()
        }
    except Exception as e:
        logger.error(f"Error en health check: {e}")
        return {"status": "unhealthy", "error": str(e)}

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