-- Datos de prueba para respuestas a ofertas (recuperatorio)
-- Este script inserta datos de ejemplo para probar la visualización

USE empuje_comunitario;

-- Limpiar datos de prueba anteriores si existen
DELETE FROM respuestas_ofertas WHERE id_oferta IN (100, 101, 102);

-- Oferta 100: Oferta de Alimentos con 3 organizaciones solicitantes
INSERT INTO respuestas_ofertas 
(id_oferta, id_organizacion_solicitante, categoria, descripcion, cantidad, fecha_solicitud, procesada)
VALUES 
-- Organizacion 1 solicita de la oferta 100
(100, 'ORG-001', 'Alimentos', 'Arroz', 50, NOW() - INTERVAL 2 DAY, false),
(100, 'ORG-001', 'Alimentos', 'Fideos', 30, NOW() - INTERVAL 2 DAY, false),
(100, 'ORG-001', 'Alimentos', 'Aceite', 20, NOW() - INTERVAL 2 DAY, false),

-- Organizacion 2 solicita de la oferta 100
(100, 'ORG-002', 'Alimentos', 'Leche en polvo', 40, NOW() - INTERVAL 1 DAY, false),
(100, 'ORG-002', 'Alimentos', 'Harina', 25, NOW() - INTERVAL 1 DAY, false),

-- Organizacion 3 solicita de la oferta 100
(100, 'ORG-003', 'Alimentos', 'Arroz', 30, NOW() - INTERVAL 3 HOUR, true);

-- Oferta 101: Oferta de Ropa con 2 organizaciones solicitantes
INSERT INTO respuestas_ofertas 
(id_oferta, id_organizacion_solicitante, categoria, descripcion, cantidad, fecha_solicitud, procesada)
VALUES 
-- Organizacion 4 solicita de la oferta 101
(101, 'ORG-004', 'Ropa', 'Camperas de invierno', 15, NOW() - INTERVAL 5 DAY, true),
(101, 'ORG-004', 'Ropa', 'Bufandas', 25, NOW() - INTERVAL 5 DAY, true),
(101, 'ORG-004', 'Ropa', 'Guantes', 20, NOW() - INTERVAL 5 DAY, true),

-- Organizacion 5 solicita de la oferta 101
(101, 'ORG-005', 'Ropa', 'Zapatillas deportivas', 10, NOW() - INTERVAL 4 DAY, false),
(101, 'ORG-005', 'Ropa', 'Medias', 50, NOW() - INTERVAL 4 DAY, false);

-- Oferta 102: Oferta de Utiles Escolares con 1 organizacion solicitante
INSERT INTO respuestas_ofertas 
(id_oferta, id_organizacion_solicitante, categoria, descripcion, cantidad, fecha_solicitud, procesada)
VALUES 
-- Organizacion 6 solicita de la oferta 102
(102, 'ORG-006', 'Utiles escolares', 'Cuadernos', 100, NOW() - INTERVAL 6 HOUR, false),
(102, 'ORG-006', 'Utiles escolares', 'Lapices', 200, NOW() - INTERVAL 6 HOUR, false),
(102, 'ORG-006', 'Utiles escolares', 'Mochilas', 30, NOW() - INTERVAL 6 HOUR, false),
(102, 'ORG-006', 'Utiles escolares', 'Cartucheras', 30, NOW() - INTERVAL 6 HOUR, false);

-- Verificar datos insertados
SELECT 
    id_oferta as 'ID Oferta',
    COUNT(DISTINCT id_organizacion_solicitante) as 'Organizaciones Solicitantes',
    COUNT(*) as 'Total Donaciones Solicitadas',
    SUM(CASE WHEN procesada = true THEN 1 ELSE 0 END) as 'Procesadas',
    SUM(CASE WHEN procesada = false THEN 1 ELSE 0 END) as 'Pendientes'
FROM respuestas_ofertas
WHERE id_oferta IN (100, 101, 102)
GROUP BY id_oferta
ORDER BY id_oferta;

SELECT '✓ Datos de prueba insertados correctamente' as Resultado;
