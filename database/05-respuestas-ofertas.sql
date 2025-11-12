CREATE TABLE IF NOT EXISTS respuestas_ofertas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_oferta BIGINT NOT NULL,
    id_organizacion_solicitante VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    cantidad VARCHAR(50) NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    procesada BOOLEAN DEFAULT FALSE,
    INDEX idx_oferta (id_oferta),
    INDEX idx_org (id_organizacion_solicitante),
    INDEX idx_fecha (fecha_solicitud)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
