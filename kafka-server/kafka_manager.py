"""
Kafka Manager - Gestiona la conexión y configuracion de Kafka
"""
import os
import logging
from kafka import KafkaProducer, KafkaConsumer
from kafka.admin import KafkaAdminClient, NewTopic
from kafka.errors import TopicAlreadyExistsError
import json
from typing import List, Dict, Any

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class KafkaManager:
    def __init__(self):
        # configuracion de Kafka desde variables de entorno
        self.bootstrap_servers = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'kafka:29092')
        self.organization_id = os.getenv('ORGANIZATION_ID', 'org-001')  # ID de la organizacion
        
        # cliente administrativo para crear topics
        self.admin_client = KafkaAdminClient(
            bootstrap_servers=self.bootstrap_servers.split(','),
            client_id='empuje_admin'
        )
        
        # producer para enviar mensajes
        self.producer = KafkaProducer(
            bootstrap_servers=self.bootstrap_servers.split(','),
            value_serializer=lambda v: json.dumps(v, default=str).encode('utf-8'),
            key_serializer=lambda k: k.encode('utf-8') if k else None
        )
    
    def create_required_topics(self):
        """
        Crea todos los topics necesarios
        """
        topics_to_create = [
            NewTopic(
                name="solicitud-donaciones",
                num_partitions=3,
                replication_factor=1
            ),
            NewTopic(
                name="oferta-donaciones", 
                num_partitions=3,
                replication_factor=1
            ),
            NewTopic(
                name="baja-solicitud-donaciones",
                num_partitions=3, 
                replication_factor=1
            ),
            NewTopic(
                name="eventos-solidarios",
                num_partitions=3,
                replication_factor=1
            ),
            NewTopic(
                name="baja-evento-solidario",
                num_partitions=3,
                replication_factor=1
            )
        ]
        
        # topics dinamicos - se crean cuando se necesiten
        for org_id in ['org-001', 'org-002', 'org-003']:
            topics_to_create.append(
                NewTopic(
                    name=f"transferencia-donaciones-{org_id}",
                    num_partitions=1,
                    replication_factor=1
                )
            )
            topics_to_create.append(
                NewTopic(
                    name=f"adhesion-evento-{org_id}",
                    num_partitions=1, 
                    replication_factor=1
                )
            )
        
        # intentar crear todos los topics
        created_topics = []
        for topic in topics_to_create:
            try:
                self.admin_client.create_topics([topic], validate_only=False)
                created_topics.append(topic.name)
                logger.info(f"Topic creado: {topic.name}")
            except TopicAlreadyExistsError:
                logger.info(f"Topic ya existe: {topic.name}")
            except Exception as e:
                logger.error(f"Error creando topic {topic.name}: {e}")
        
        return created_topics
    
    def create_dynamic_topic(self, topic_name: str):
        """
        Crea un topic dinamico si no existe
        """
        try:
            topic = NewTopic(
                name=topic_name,
                num_partitions=1,
                replication_factor=1
            )
            self.admin_client.create_topics([topic], validate_only=False)
            logger.info(f"Topic dinámico creado: {topic_name}")
            return True
        except TopicAlreadyExistsError:
            logger.info(f"Topic dinámico ya existe: {topic_name}")
            return True
        except Exception as e:
            logger.error(f"Error creando topic dinámico {topic_name}: {e}")
            return False
    
    def send_message(self, topic: str, message: Dict[Any, Any], key: str = None):
        """
        Envía un mensaje a un topic especifico
        """
        try:
            # asegurar que el topic dinamico existe
            if topic.startswith('transferencia-donaciones-') or topic.startswith('adhesion-evento-'):
                self.create_dynamic_topic(topic)
            
            future = self.producer.send(topic, value=message, key=key)
            result = future.get(timeout=10)
            logger.info(f"Mensaje enviado a {topic}: {result}")
            return True
        except Exception as e:
            logger.error(f"Error enviando mensaje a {topic}: {e}")
            return False
    
    def get_consumer(self, topics: List[str], group_id: str = None) -> KafkaConsumer:
        """
        Crea un consumidor para escuchar topics especificos
        """
        if group_id is None:
            group_id = f"empuje_{self.organization_id}"
        
        consumer = KafkaConsumer(
            *topics,
            bootstrap_servers=self.bootstrap_servers.split(','),
            group_id=group_id,
            value_deserializer=lambda m: json.loads(m.decode('utf-8')) if m else None,
            key_deserializer=lambda k: k.decode('utf-8') if k else None,
            auto_offset_reset='latest',  # mensajes nuevos
            enable_auto_commit=True
        )
        
        return consumer
    
    def list_topics(self):
        """
        Lista todos los topics disponibles
        """
        try:
            metadata = self.admin_client.describe_topics()
            topics = list(metadata.keys())
            logger.info(f"Topics disponibles: {topics}")
            return topics
        except Exception as e:
            logger.error(f"Error listando topics: {e}")
            return []
    
    def close(self):
        """
        Cierra las conexiones
        """
        if hasattr(self, 'producer'):
            self.producer.close()
        if hasattr(self, 'admin_client'):
            self.admin_client.close()

# no instanciar globalmente - se crea en el servidor
kafka_manager = None