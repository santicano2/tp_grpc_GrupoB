USE empuje_comunitario;

-- Todas las claves son "password123" para testing (las claves estan hasheadas con bcrypt)
INSERT INTO usuarios (nombre_usuario, nombre, apellido, telefono, clave, email, rol, activo) VALUES
('presidente1', 'Maria', 'Gonzalez', '+54911234567', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'maria.gonzalez@empujecomunitario.org', 'PRESIDENTE', TRUE),
('vocal1', 'Juan', 'Perez', '+54911234568', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'juan.perez@empujecomunitario.org', 'VOCAL', TRUE),
('vocal2', 'Ana', 'Martinez', '+54911234569', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'ana.martinez@empujecomunitario.org', 'VOCAL', TRUE),
('coordinador1', 'Carlos', 'Lopez', '+54911234570', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'carlos.lopez@empujecomunitario.org', 'COORDINADOR', TRUE),
('coordinador2', 'Laura', 'Rodriguez', '+54911234571', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'laura.rodriguez@empujecomunitario.org', 'COORDINADOR', TRUE),
('voluntario1', 'Pedro', 'Sanchez', '+54911234572', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'pedro.sanchez@gmail.com', 'VOLUNTARIO', TRUE),
('voluntario2', 'Sofia', 'Garcia', '+54911234573', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'sofia.garcia@gmail.com', 'VOLUNTARIO', TRUE),
('voluntario3', 'Miguel', 'Torres', '+54911234574', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'miguel.torres@gmail.com', 'VOLUNTARIO', TRUE),
('voluntario4', 'Carmen', 'Jimenez', '+54911234575', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'carmen.jimenez@gmail.com', 'VOLUNTARIO', FALSE),
('coordinador3', 'Roberto', 'Fernandez', '+54911234576', '$2a$12$BuN9BmNHfuZrsox4MHEkB.uyzJVydU2nYGAQx9BQw8qq27cHPQc3a', 'roberto.fernandez@empujecomunitario.org', 'COORDINADOR', FALSE);

INSERT INTO inventario (categoria, descripcion, cantidad, usuario_alta) VALUES
-- ROPA
('ROPA', 'Remeras de algodon talle M', 25, 2),
('ROPA', 'Pantalones jeans varios talles', 18, 2),
('ROPA', 'Camperas de abrigo', 12, 3),
('ROPA', 'Zapatillas deportivas', 8, 2),
('ROPA', 'Vestidos para ninas', 15, 3),
('ROPA', 'Buzos con capucha', 20, 2),

-- ALIMENTOS
('ALIMENTOS', 'Arroz 1kg', 50, 2),
('ALIMENTOS', 'Fideos secos', 40, 3),
('ALIMENTOS', 'Conservas de tomate', 30, 2),
('ALIMENTOS', 'Aceite de girasol 900ml', 25, 3),
('ALIMENTOS', 'Leche en polvo', 20, 2),
('ALIMENTOS', 'Azucar 1kg', 35, 3),
('ALIMENTOS', 'Galletitas dulces', 60, 2),
('ALIMENTOS', 'Yerba mate 500g', 28, 3),

-- JUGUETES
('JUGUETES', 'Pelotas de futbol', 10, 2),
('JUGUETES', 'Munecas de plastico', 15, 3),
('JUGUETES', 'Autos de juguete', 20, 2),
('JUGUETES', 'Rompecabezas infantiles', 12, 3),
('JUGUETES', 'Libros de cuentos', 25, 2),
('JUGUETES', 'Juegos de mesa', 8, 3),
('JUGUETES', 'Peluches varios', 18, 2),

-- UTILES ESCOLARES
('UTILES_ESCOLARES', 'Cuadernos A4 rayados', 100, 2),
('UTILES_ESCOLARES', 'Lapices de grafito', 150, 3),
('UTILES_ESCOLARES', 'Lapices de colores (cajas x12)', 45, 2),
('UTILES_ESCOLARES', 'Biromes azules', 80, 3),
('UTILES_ESCOLARES', 'Gomas de borrar', 60, 2),
('UTILES_ESCOLARES', 'Reglas de 20cm', 40, 3),
('UTILES_ESCOLARES', 'Cartucheras de tela', 35, 2),
('UTILES_ESCOLARES', 'Mochilas escolares', 22, 3),
('UTILES_ESCOLARES', 'Marcadores fluorescentes', 50, 2);

INSERT INTO eventos (id, nombre, descripcion, fecha_evento, usuario_creacion) VALUES
(1, 'Visita Escuela N 25', 'Entrega de utiles escolares y actividades recreativas con los ninos de primaria', '2025-09-15 14:00:00', 4),
(2, 'Hogar de Ancianos San Jose', 'Actividades de entretenimiento y compania para los residentes', '2025-09-20 10:00:00', 5),
(3, 'Centro Comunitario Barrio Sur', 'Distribucion de alimentos y ropa para familias necesitadas', '2025-09-25 16:00:00', 4),
(4, 'Jardin de Infantes Arcoiris', 'Entrega de juguetes y libros de cuentos', '2025-10-02 15:30:00', 5),
(5, 'Hospital Pediatrico', 'Visita a ninos internados con juguetes y actividades ludicas', '2025-10-08 11:00:00', 4),
(6, 'Comedor Comunitario La Esperanza', 'Colaboracion en la preparacion y servicio de alimentos', '2025-10-15 12:00:00', 5),
(7, 'Escuela Tecnica N 12', 'Charla motivacional y entrega de utiles para estudiantes', '2025-10-22 09:00:00', 4),
(8, 'Hogar de Ninos Santa Maria', 'Actividades recreativas y entrega de ropa', '2025-11-05 14:30:00', 5),
(9, 'Campana de Donacion de Ropa', 'Recoleccion masiva de ropa en desuso para familias necesitadas', '2025-08-15 09:00:00', 4),
(10, 'Feria de Salud Barrio Norte', 'Controles medicos gratuitos y charlas sobre prevencion', '2025-08-20 08:00:00', 5),
(11, 'Merienda Solidaria Plaza Central', 'Distribucion de merienda para ninos en situacion de calle', '2025-08-25 16:00:00', 4),
(12, 'Taller de Oficios Centro Juvenil', 'Ensenanza de carpinteria basica para jovenes', '2025-09-01 14:00:00', 5),
(13, 'Limpieza Rio San Juan', 'Jornada de limpieza y concientizacion ambiental', '2025-09-05 07:00:00', 4),
(14, 'Entrega de Viandas Hospital Municipal', 'Preparacion y entrega de comida para familiares de pacientes', '2025-09-10 12:00:00', 5);

-- Evento 1: Visita Escuela N° 25
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(1, 4, 4), -- coordinador1 se asigna
(1, 6, 4), -- voluntario1 asignado por coordinador1
(1, 7, 4), -- voluntario2 asignado por coordinador1
(1, 8, 4); -- voluntario3 asignado por coordinador1

-- Evento 2: Hogar de Ancianos San Jose
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(2, 5, 5), -- coordinador2 se asigna
(2, 6, 5), -- voluntario1 asignado por coordinador2
(2, 7, 5); -- voluntario2 asignado por coordinador2

-- Evento 3: Centro Comunitario Barrio Sur
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(3, 4, 4), -- coordinador1 se asigna
(3, 1, 1), -- presidente se asigna
(3, 7, 4), -- voluntario2 asignado por coordinador1
(3, 8, 4); -- voluntario3 asignado por coordinador1

-- Evento 4: Jardin de Infantes Arcoiris
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(4, 5, 5), -- coordinador2 se asigna
(4, 6, 6), -- voluntario1 se asigna a si mismo
(4, 8, 8); -- voluntario3 se asigna a si mismo

-- Evento 5: Hospital Pediatrico
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(5, 4, 4), -- coordinador1 se asigna
(5, 5, 1), -- coordinador2 asignado por presidente
(5, 6, 4), -- voluntario1 asignado por coordinador1
(5, 7, 4), -- voluntario2 asignado por coordinador1
(5, 8, 4); -- voluntario3 asignado por coordinador1

-- Evento 6: Comedor Comunitario La Esperanza
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(6, 5, 5), -- coordinador2 se asigna
(6, 1, 1), -- presidente se asigna
(6, 7, 7), -- voluntario2 se asigna a si mismo
(6, 8, 7); -- voluntario3 asignado por voluntario2 (esto no deberia ser posible segun las reglas, pero para pruebas)

-- Evento 7: Escuela Tecnica N° 12
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(7, 4, 4), -- coordinador1 se asigna
(7, 6, 6), -- voluntario1 se asigna a si mismo
(7, 8, 6); -- voluntario3 asignado por voluntario1 (esto no deberia ser posible segun las reglas)

-- Evento 8: Hogar de Ninos Santa Maria
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(8, 5, 5), -- coordinador2 se asigna
(8, 1, 1), -- presidente se asigna
(8, 6, 5), -- voluntario1 asignado por coordinador2
(8, 7, 5), -- voluntario2 asignado por coordinador2
(8, 8, 8); -- voluntario3 se asigna a si mismo

-- Eventos pasados participaciones
-- Evento 9: Campana de Donacion de Ropa
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(9, 4, 4), -- coordinador1 se asigna
(9, 2, 2), -- vocal1 se asigna
(9, 6, 4), -- voluntario1 asignado por coordinador1
(9, 7, 4); -- voluntario2 asignado por coordinador1

-- Evento 10: Feria de Salud Barrio Norte
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(10, 5, 5), -- coordinador2 se asigna
(10, 3, 3), -- vocal2 se asigna
(10, 8, 5); -- voluntario3 asignado por coordinador2

-- Evento 11: Merienda Solidaria Plaza Central
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(11, 4, 4), -- coordinador1 se asigna
(11, 6, 6), -- voluntario1 se asigna
(11, 7, 6), -- voluntario2 asignado por voluntario1
(11, 8, 4); -- voluntario3 asignado por coordinador1

-- Evento 12: Taller de Oficios Centro Juvenil
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(12, 5, 5), -- coordinador2 se asigna
(12, 1, 1), -- presidente se asigna
(12, 6, 5); -- voluntario1 asignado por coordinador2

-- Evento 13: Limpieza Rio San Juan
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(13, 4, 4), -- coordinador1 se asigna
(13, 5, 4), -- coordinador2 asignado por coordinador1
(13, 6, 4), -- voluntario1 asignado por coordinador1
(13, 7, 4), -- voluntario2 asignado por coordinador1
(13, 8, 4); -- voluntario3 asignado por coordinador1

-- Evento 14: Entrega de Viandas Hospital Municipal
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(14, 5, 5), -- coordinador2 se asigna
(14, 2, 2), -- vocal1 se asigna
(14, 7, 5), -- voluntario2 asignado por coordinador2
(14, 8, 5); -- voluntario3 asignado por coordinador2

SELECT 'USUARIOS' as tabla, COUNT(*) as cantidad FROM usuarios
UNION ALL
SELECT 'INVENTARIO', COUNT(*) FROM inventario
UNION ALL
SELECT 'EVENTOS', COUNT(*) FROM eventos
UNION ALL
SELECT 'PARTICIPACIONES', COUNT(*) FROM evento_participaciones;

-- Trigger para validar fechas futuras en nuevos eventos
DELIMITER //
CREATE TRIGGER validate_evento_fecha_insert
    BEFORE INSERT ON eventos
    FOR EACH ROW
BEGIN
    IF NEW.fecha_evento <= NOW() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La fecha del evento debe ser futura';
    END IF;
END//
DELIMITER ;