USE empuje_comunitario;

-- Todas las claves son "password123" para testing (las claves están hasheadas con bcrypt)
INSERT INTO usuarios (nombre_usuario, nombre, apellido, telefono, clave, email, rol, activo) VALUES
('presidente1', 'María', 'González', '+54911234567', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'maria.gonzalez@empujecomunitario.org', 'PRESIDENTE', TRUE),
('vocal1', 'Juan', 'Pérez', '+54911234568', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'juan.perez@empujecomunitario.org', 'VOCAL', TRUE),
('vocal2', 'Ana', 'Martínez', '+54911234569', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'ana.martinez@empujecomunitario.org', 'VOCAL', TRUE),
('coordinador1', 'Carlos', 'López', '+54911234570', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'carlos.lopez@empujecomunitario.org', 'COORDINADOR', TRUE),
('coordinador2', 'Laura', 'Rodríguez', '+54911234571', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'laura.rodriguez@empujecomunitario.org', 'COORDINADOR', TRUE),
('voluntario1', 'Pedro', 'Sánchez', '+54911234572', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'pedro.sanchez@gmail.com', 'VOLUNTARIO', TRUE),
('voluntario2', 'Sofía', 'García', '+54911234573', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'sofia.garcia@gmail.com', 'VOLUNTARIO', TRUE),
('voluntario3', 'Miguel', 'Torres', '+54911234574', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'miguel.torres@gmail.com', 'VOLUNTARIO', TRUE),
('voluntario4', 'Carmen', 'Jiménez', '+54911234575', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'carmen.jimenez@gmail.com', 'VOLUNTARIO', FALSE),
('coordinador3', 'Roberto', 'Fernández', '+54911234576', '$2b$12$LQv3c1yqBwlVHpPjrCv0qeD5aOEjNVtqRCk8Y.ZH.7P1Q8R9F0K2O', 'roberto.fernandez@empujecomunitario.org', 'COORDINADOR', FALSE);

INSERT INTO inventario (categoria, descripcion, cantidad, usuario_alta) VALUES
-- ROPA
('ROPA', 'Remeras de algodón talle M', 25, 2),
('ROPA', 'Pantalones jeans varios talles', 18, 2),
('ROPA', 'Camperas de abrigo', 12, 3),
('ROPA', 'Zapatillas deportivas', 8, 2),
('ROPA', 'Vestidos para niñas', 15, 3),
('ROPA', 'Buzos con capucha', 20, 2),

-- ALIMENTOS
('ALIMENTOS', 'Arroz 1kg', 50, 2),
('ALIMENTOS', 'Fideos secos', 40, 3),
('ALIMENTOS', 'Conservas de tomate', 30, 2),
('ALIMENTOS', 'Aceite de girasol 900ml', 25, 3),
('ALIMENTOS', 'Leche en polvo', 20, 2),
('ALIMENTOS', 'Azúcar 1kg', 35, 3),
('ALIMENTOS', 'Galletitas dulces', 60, 2),
('ALIMENTOS', 'Yerba mate 500g', 28, 3),

-- JUGUETES
('JUGUETES', 'Pelotas de fútbol', 10, 2),
('JUGUETES', 'Muñecas de plástico', 15, 3),
('JUGUETES', 'Autos de juguete', 20, 2),
('JUGUETES', 'Rompecabezas infantiles', 12, 3),
('JUGUETES', 'Libros de cuentos', 25, 2),
('JUGUETES', 'Juegos de mesa', 8, 3),
('JUGUETES', 'Peluches varios', 18, 2),

-- ÚTILES ESCOLARES
('UTILES_ESCOLARES', 'Cuadernos A4 rayados', 100, 2),
('UTILES_ESCOLARES', 'Lápices de grafito', 150, 3),
('UTILES_ESCOLARES', 'Lápices de colores (cajas x12)', 45, 2),
('UTILES_ESCOLARES', 'Biromes azules', 80, 3),
('UTILES_ESCOLARES', 'Gomas de borrar', 60, 2),
('UTILES_ESCOLARES', 'Reglas de 20cm', 40, 3),
('UTILES_ESCOLARES', 'Cartucheras de tela', 35, 2),
('UTILES_ESCOLARES', 'Mochilas escolares', 22, 3),
('UTILES_ESCOLARES', 'Marcadores fluorescentes', 50, 2);

INSERT INTO eventos (nombre, descripcion, fecha_evento, usuario_creacion) VALUES
('Visita Escuela N° 25', 'Entrega de útiles escolares y actividades recreativas con los niños de primaria', '2025-09-15 14:00:00', 4),
('Hogar de Ancianos San José', 'Actividades de entretenimiento y compañía para los residentes', '2025-09-20 10:00:00', 5),
('Centro Comunitario Barrio Sur', 'Distribución de alimentos y ropa para familias necesitadas', '2025-09-25 16:00:00', 4),
('Jardín de Infantes Arcoíris', 'Entrega de juguetes y libros de cuentos', '2025-10-02 15:30:00', 5),
('Hospital Pediátrico', 'Visita a niños internados con juguetes y actividades lúdicas', '2025-10-08 11:00:00', 4),
('Comedor Comunitario La Esperanza', 'Colaboración en la preparación y servicio de alimentos', '2025-10-15 12:00:00', 5),
('Escuela Técnica N° 12', 'Charla motivacional y entrega de útiles para estudiantes', '2025-10-22 09:00:00', 4),
('Hogar de Niños Santa María', 'Actividades recreativas y entrega de ropa', '2025-11-05 14:30:00', 5);

-- Evento 1: Visita Escuela N° 25
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(1, 4, 4), -- coordinador1 se asigna
(1, 6, 4), -- voluntario1 asignado por coordinador1
(1, 7, 4), -- voluntario2 asignado por coordinador1
(1, 8, 4); -- voluntario3 asignado por coordinador1

-- Evento 2: Hogar de Ancianos San José
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

-- Evento 4: Jardín de Infantes Arcoíris
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(4, 5, 5), -- coordinador2 se asigna
(4, 6, 6), -- voluntario1 se asigna a sí mismo
(4, 8, 8); -- voluntario3 se asigna a sí mismo

-- Evento 5: Hospital Pediátrico
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
(6, 7, 7), -- voluntario2 se asigna a sí mismo
(6, 8, 7); -- voluntario3 asignado por voluntario2 (esto no debería ser posible según las reglas, pero para pruebas)

-- Evento 7: Escuela Técnica N° 12
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(7, 4, 4), -- coordinador1 se asigna
(7, 6, 6), -- voluntario1 se asigna a sí mismo
(7, 8, 6); -- voluntario3 asignado por voluntario1 (esto no debería ser posible según las reglas)

-- Evento 8: Hogar de Niños Santa María
INSERT INTO evento_participaciones (evento_id, usuario_id, asignado_por) VALUES
(8, 5, 5), -- coordinador2 se asigna
(8, 1, 1), -- presidente se asigna
(8, 6, 5), -- voluntario1 asignado por coordinador2
(8, 7, 5), -- voluntario2 asignado por coordinador2
(8, 8, 8); -- voluntario3 se asigna a sí mismo

SELECT 'USUARIOS' as tabla, COUNT(*) as cantidad FROM usuarios
UNION ALL
SELECT 'INVENTARIO', COUNT(*) FROM inventario
UNION ALL
SELECT 'EVENTOS', COUNT(*) FROM eventos
UNION ALL
SELECT 'PARTICIPACIONES', COUNT(*) FROM evento_participaciones;