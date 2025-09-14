CREATE DATABASE IF NOT EXISTS empuje_comunitario;
USE empuje_comunitario;

CREATE TABLE usuarios (
	id INT AUTO_INCREMENT PRIMARY KEY,
	nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	apellido VARCHAR(100) NOT NULL,
	telefono VARCHAR(20),
	clave VARCHAR(255) NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	rol ENUM('PRESIDENTE', 'VOCAL', 'COORDINADOR', 'VOLUNTARIO') NOT NULL,
	activo BOOLEAN DEFAULT TRUE,
	fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria ENUM('ROPA', 'ALIMENTOS', 'JUGUETES', 'UTILES_ESCOLARES') NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL DEFAULT 0 CHECK (cantidad >= 0),
    eliminado BOOLEAN DEFAULT FALSE,
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_alta INT,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    usuario_modificacion INT,
    FOREIGN KEY (usuario_alta) REFERENCES usuarios(id),
    FOREIGN KEY (usuario_modificacion) REFERENCES usuarios(id)
);

CREATE TABLE eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_evento DATETIME NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    usuario_creacion INT,
    usuario_modificacion INT,
    FOREIGN KEY (usuario_creacion) REFERENCES usuarios(id),
    FOREIGN KEY (usuario_modificacion) REFERENCES usuarios(id)
);

-- muchos a muchos
CREATE TABLE evento_participaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    evento_id INT NOT NULL,
    usuario_id INT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    asignado_por INT,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (asignado_por) REFERENCES usuarios(id),
    UNIQUE KEY unique_participacion (evento_id, usuario_id)
);

CREATE TABLE evento_donaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    evento_id INT NOT NULL,
    inventario_id INT NOT NULL,
    cantidad_repartida INT NOT NULL CHECK (cantidad_repartida > 0),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registrado_por INT NOT NULL,
    FOREIGN KEY (evento_id) REFERENCES eventos(id),
    FOREIGN KEY (inventario_id) REFERENCES inventario(id),
    FOREIGN KEY (registrado_por) REFERENCES usuarios(id)
);

CREATE INDEX idx_usuarios_activo ON usuarios(activo);
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_inventario_categoria ON inventario(categoria);
CREATE INDEX idx_inventario_eliminado ON inventario(eliminado);
CREATE INDEX idx_eventos_fecha ON eventos(fecha_evento);
CREATE INDEX idx_participaciones_evento ON evento_participaciones(evento_id);
CREATE INDEX idx_participaciones_usuario ON evento_participaciones(usuario_id);

-- actualizar inventario cuando se registran donaciones repartidas
DELIMITER //
CREATE TRIGGER update_inventario_after_donacion
    AFTER INSERT ON evento_donaciones
    FOR EACH ROW
BEGIN
    UPDATE inventario 
    SET cantidad = cantidad - NEW.cantidad_repartida,
        fecha_modificacion = NOW(),
        usuario_modificacion = NEW.registrado_por
    WHERE id = NEW.inventario_id;
    
    -- Verificar que no quede cantidad negativa
    IF (SELECT cantidad FROM inventario WHERE id = NEW.inventario_id) < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No hay suficiente cantidad en inventario';
    END IF;
END//
DELIMITER ;