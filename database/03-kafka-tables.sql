USE empuje_comunitario;

CREATE TABLE organizaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_organizacion VARCHAR(100) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    activa BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE solicitudes_externas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud VARCHAR(255) NOT NULL,
    id_organizacion_solicitante VARCHAR(100) NOT NULL,
    categoria ENUM('ROPA', 'ALIMENTOS', 'JUGUETES', 'UTILES_ESCOLARES') NOT NULL,
    descripcion TEXT,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    fecha_baja TIMESTAMP NULL,
    procesada BOOLEAN DEFAULT FALSE,
    INDEX idx_solicitud_org (id_organizacion_solicitante),
    INDEX idx_solicitud_activa (activa),
    INDEX idx_solicitud_categoria (categoria),
    UNIQUE KEY unique_solicitud (id_solicitud, id_organizacion_solicitante)
);

CREATE TABLE ofertas_externas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_oferta VARCHAR(255) NOT NULL,
    id_organizacion_donante VARCHAR(100) NOT NULL,
    categoria ENUM('ROPA', 'ALIMENTOS', 'JUGUETES', 'UTILES_ESCOLARES') NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    fecha_oferta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    fecha_baja TIMESTAMP NULL,
    INDEX idx_oferta_org (id_organizacion_donante),
    INDEX idx_oferta_activa (activa),
    INDEX idx_oferta_categoria (categoria),
    UNIQUE KEY unique_oferta (id_oferta, id_organizacion_donante)
);

CREATE TABLE eventos_externos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_evento VARCHAR(255) NOT NULL,
    id_organizacion VARCHAR(100) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_evento DATETIME NOT NULL,
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    fecha_baja TIMESTAMP NULL,
    INDEX idx_evento_org (id_organizacion),
    INDEX idx_evento_fecha (fecha_evento),
    INDEX idx_evento_activo (activo),
    UNIQUE KEY unique_evento (id_evento, id_organizacion)
);

CREATE TABLE adhesiones_eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_evento_externo INT NOT NULL,
    id_voluntario INT NOT NULL,
    fecha_adhesion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notificado BOOLEAN DEFAULT FALSE,
    activa BOOLEAN DEFAULT TRUE,
    fecha_baja TIMESTAMP NULL,
    FOREIGN KEY (id_evento_externo) REFERENCES eventos_externos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_voluntario) REFERENCES usuarios(id),
    INDEX idx_adhesion_evento (id_evento_externo),
    INDEX idx_adhesion_voluntario (id_voluntario),
    UNIQUE KEY unique_adhesion (id_evento_externo, id_voluntario)
);

CREATE TABLE transferencias_donaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud VARCHAR(255) NOT NULL,
    id_organizacion_solicitante VARCHAR(100) NOT NULL,
    id_organizacion_donante VARCHAR(100) NOT NULL,
    categoria ENUM('ROPA', 'ALIMENTOS', 'JUGUETES', 'UTILES_ESCOLARES') NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    tipo_transferencia ENUM('ENVIADA', 'RECIBIDA') NOT NULL,
    fecha_transferencia TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    inventario_id INT NULL,
    procesada BOOLEAN DEFAULT FALSE,
    usuario_proceso INT NULL,
    FOREIGN KEY (inventario_id) REFERENCES inventario(id),
    FOREIGN KEY (usuario_proceso) REFERENCES usuarios(id),
    INDEX idx_transfer_solicitud (id_solicitud),
    INDEX idx_transfer_tipo (tipo_transferencia),
    INDEX idx_transfer_fecha (fecha_transferencia)
);

CREATE TABLE mensajes_kafka_procesados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    topic VARCHAR(255) NOT NULL,
    mensaje_id VARCHAR(255) NOT NULL,
    id_organizacion VARCHAR(100) NOT NULL,
    contenido_hash VARCHAR(64) NOT NULL,
    fecha_procesamiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_operacion ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
    INDEX idx_mensaje_topic (topic),
    INDEX idx_mensaje_org (id_organizacion),
    INDEX idx_mensaje_fecha (fecha_procesamiento),
    UNIQUE KEY unique_mensaje (topic, mensaje_id, id_organizacion, contenido_hash)
);

CREATE INDEX idx_solicitudes_categoria_activa ON solicitudes_externas(categoria, activa);
CREATE INDEX idx_ofertas_categoria_activa ON ofertas_externas(categoria, activa);

CREATE INDEX idx_eventos_externos_fecha_activo ON eventos_externos(fecha_evento, activo);

-- ============================================
-- DATOS INICIALES
-- ============================================

INSERT INTO organizaciones (id_organizacion, nombre, descripcion) VALUES 
('empuje_comunitario', 'Empuje Comunitario', 'Nuestra organizacion base - ONG principal del sistema');

INSERT INTO organizaciones (id_organizacion, nombre, descripcion) VALUES 
('ayuda_vecinal', 'Ayuda Vecinal', 'ONG de asistencia comunitaria del barrio norte'),
('manos_solidarias', 'Manos Solidarias', 'Organizacion enfocada en distribucion de alimentos'),
('futuro_mejor', 'Futuro Mejor', 'ONG dedicada a la educacion y utiles escolares');

-- trigger para actualizar inventario cuando se procesa una transferencia enviada
DELIMITER //
CREATE TRIGGER update_inventario_after_transfer_enviada
    AFTER UPDATE ON transferencias_donaciones
    FOR EACH ROW
BEGIN
    -- solo si cambio de no procesada a procesada Y es una transferencia enviada
    IF OLD.procesada = FALSE AND NEW.procesada = TRUE AND NEW.tipo_transferencia = 'ENVIADA' AND NEW.inventario_id IS NOT NULL THEN
        UPDATE inventario 
        SET cantidad = cantidad - NEW.cantidad,
            fecha_modificacion = NOW(),
            usuario_modificacion = NEW.usuario_proceso
        WHERE id = NEW.inventario_id;
        
        -- Verificar que no quede cantidad negativa
        IF (SELECT cantidad FROM inventario WHERE id = NEW.inventario_id) < 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No hay suficiente cantidad en inventario para la transferencia';
        END IF;
    END IF;
END//

-- Trigger para actualizar inventario cuando se procesa una transferencia recibida
CREATE TRIGGER update_inventario_after_transfer_recibida
    AFTER UPDATE ON transferencias_donaciones
    FOR EACH ROW
BEGIN
    -- Solo si cambio de no procesada a procesada Y es una transferencia recibida
    IF OLD.procesada = FALSE AND NEW.procesada = TRUE AND NEW.tipo_transferencia = 'RECIBIDA' THEN
        -- Buscar si ya existe un item similar en inventario
        SET @existing_id = (
            SELECT id FROM inventario 
            WHERE categoria = NEW.categoria 
            AND descripcion = NEW.descripcion 
            AND eliminado = FALSE 
            LIMIT 1
        );
        
        IF @existing_id IS NOT NULL THEN
            -- Actualizar cantidad existente
            UPDATE inventario 
            SET cantidad = cantidad + NEW.cantidad,
                fecha_modificacion = NOW(),
                usuario_modificacion = NEW.usuario_proceso
            WHERE id = @existing_id;
        ELSE
            -- Crear nuevo item en inventario
            INSERT INTO inventario (categoria, descripcion, cantidad, usuario_alta)
            VALUES (NEW.categoria, NEW.descripcion, NEW.cantidad, NEW.usuario_proceso);
        END IF;
    END IF;
END//
DELIMITER ;