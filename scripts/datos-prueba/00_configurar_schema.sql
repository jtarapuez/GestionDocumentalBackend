-- =====================================================
-- SCRIPT DE CONFIGURACIÓN: SET SCHEMA
-- =====================================================
-- Descripción: Configura el schema actual para facilitar la ejecución
-- =====================================================

-- Establecer el schema actual como DOCUMENTAL_OWNER
-- Esto permite usar las secuencias sin el prefijo DOCUMENTAL_OWNER
ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;

-- Verificar que el schema se estableció correctamente
SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') AS SCHEMA_ACTUAL FROM DUAL;

-- Verificar que las secuencias existen
SELECT SEQUENCE_NAME 
FROM USER_SEQUENCES 
WHERE SEQUENCE_NAME IN (
    'GDOC_SECCIONES_S',
    'GDOC_SERIES_S',
    'GDOC_SUBSERIES_S',
    'GDOC_INVENTARIO_S'
)
ORDER BY SEQUENCE_NAME;


