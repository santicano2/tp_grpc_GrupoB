import logging
import mysql.connector
from mysql.connector import Error
from contextlib import contextmanager
from typing import Optional, Any, Dict, List, Tuple
from .db_config import DatabaseConfig, DEFAULT_CONFIG

logger = logging.getLogger(__name__)

class DatabaseManager:
    """Gestor de conexión a MySQL"""
    
    def __init__(self, config: Optional[DatabaseConfig] = None):
        self.config = config or DEFAULT_CONFIG
        self._connection_pool = None
        self._initialize_pool()
    
    def _initialize_pool(self):
        try:
            from mysql.connector import pooling
            
            pool_config = {
                'pool_name': 'empuje_pool',
                'pool_size': 10,
                'pool_reset_session': True,
                'host': self.config.host,
                'port': self.config.port,
                'user': self.config.user,
                'password': self.config.password,
                'database': self.config.database,
                'charset': self.config.charset,
                'autocommit': False,
                'time_zone': '-03:00'
            }
            
            self._connection_pool = pooling.MySQLConnectionPool(**pool_config)
            logger.info("Pool de conexiones MySQL inicializado correctamente")
            
        except Error as e:
            logger.error(f"Error al inicializar pool de conexiones: {e}")
            raise
    
    @contextmanager
    def get_connection(self):
        """Context manager para obtener una conexión del pool"""
        connection = None
        try:
            connection = self._connection_pool.get_connection()
            yield connection
        except Error as e:
            if connection:
                connection.rollback()
            logger.error(f"Error en conexión a base de datos: {e}")
            raise
        finally:
            if connection and connection.is_connected():
                connection.close()
    
    def execute_query(self, query: str, params: Optional[Tuple] = None) -> List[Dict[str, Any]]:
        """Ejecutar una consulta SELECT y retornar resultados"""
        with self.get_connection() as conn:
            cursor = conn.cursor(dictionary=True)
            try:
                cursor.execute(query, params or ())
                results = cursor.fetchall()
                return results
            finally:
                cursor.close()
    
    def execute_update(self, query: str, params: Optional[Tuple] = None) -> int:
        """Ejecutar una consulta de actualización (INSERT, UPDATE, DELETE)"""
        with self.get_connection() as conn:
            cursor = conn.cursor()
            try:
                cursor.execute(query, params or ())
                conn.commit()
                return cursor.rowcount
            except Error as e:
                conn.rollback()
                logger.error(f"Error en execute_update: {e}")
                raise
            finally:
                cursor.close()
    
    def execute_insert(self, query: str, params: Optional[Tuple] = None) -> int:
        """Ejecutar un INSERT y retornar el ID insertado"""
        with self.get_connection() as conn:
            cursor = conn.cursor()
            try:
                cursor.execute(query, params or ())
                conn.commit()
                return cursor.lastrowid
            except Error as e:
                conn.rollback()
                logger.error(f"Error en execute_insert: {e}")
                raise
            finally:
                cursor.close()
    
    def execute_transaction(self, queries_with_params: List[Tuple[str, Optional[Tuple]]]) -> bool:
        """Ejecutar múltiples consultas en una transacción"""
        with self.get_connection() as conn:
            cursor = conn.cursor()
            try:
                for query, params in queries_with_params:
                    cursor.execute(query, params or ())
                conn.commit()
                return True
            except Error as e:
                conn.rollback()
                logger.error(f"Error en transacción: {e}")
                raise
            finally:
                cursor.close()
    
    def test_connection(self) -> bool:
        """Probar la conexión a la base de datos"""
        try:
            with self.get_connection() as conn:
                cursor = conn.cursor()
                cursor.execute("SELECT 1")
                result = cursor.fetchone()
                cursor.close()
                return result is not None
        except Error as e:
            logger.error(f"Error al probar conexión: {e}")
            return False

db_manager = DatabaseManager()
