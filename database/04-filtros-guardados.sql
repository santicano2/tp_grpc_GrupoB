USE empuje_comunitario;

CREATE TABLE IF NOT EXISTS filtros_guardados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo ENUM('DONACIONES', 'EVENTOS') NOT NULL,
    filtros_json TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_filtros_usuario ON filtros_guardados(usuario_id);
CREATE INDEX idx_filtros_tipo ON filtros_guardados(tipo);
