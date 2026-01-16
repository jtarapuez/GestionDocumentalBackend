-- =====================================================
-- SCRIPT DE CORRECCIÓN: ACTUALIZAR NOM_SERIES (VERSIÓN SQLPLUS)
-- =====================================================
-- Descripción: Corrige los valores de NOM_SERIES que tienen "3" o valores numéricos
-- Tabla: GDOC_SERIES_T
-- =====================================================

-- IMPORTANTE: Ejecutar este script conectado al schema DOCUMENTAL_OWNER
ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;
ALTER SESSION SET NLS_LANGUAGE = 'SPANISH';
ALTER SESSION SET NLS_TERRITORY = 'ECUADOR';

-- =====================================================
-- PASO 1: Verificar datos actuales (ANTES de actualizar)
-- =====================================================
SELECT ID_SERIE, NOM_SERIES AS NOMBRE_ACTUAL, DESCR_SERIE AS DESCRIPCION
FROM DOCUMENTAL_OWNER.GDOC_SERIES_T
WHERE NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$')
ORDER BY ID_SERIE;

-- =====================================================
-- PASO 2: Actualizar series específicas por ID
-- =====================================================
-- Serie ID 7: Pensiones de Vejez (basado en descripción)
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Pensiones de Vejez'
WHERE ID_SERIE = 7 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- Serie ID 8: Pensiones de Invalidez
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Pensiones de Invalidez'
WHERE ID_SERIE = 8 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- Serie ID 9: Prestaciones Económicas
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Prestaciones Económicas'
WHERE ID_SERIE = 9 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- Serie ID 10: Pensiones de Vejez (otra)
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Pensiones de Vejez'
WHERE ID_SERIE = 10 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- Serie ID 11: Basado en descripción "prueba perter"
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Prueba Perter'
WHERE ID_SERIE = 11 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- Serie ID 12: Basado en descripción "prueba perter"
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Prueba Perter 2'
WHERE ID_SERIE = 12 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- Serie ID 13: Basado en descripción "prueba de concepto"
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Prueba de Concepto'
WHERE ID_SERIE = 13 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- Serie ID 14: Basado en descripción "prueba subswerie docuental"
UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = 'PRUEBA - Serie Prueba Subserie Documental'
WHERE ID_SERIE = 14 AND (NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$'));

-- =====================================================
-- PASO 3: Verificar datos actualizados
-- =====================================================
SELECT ID_SERIE, NOM_SERIES AS NOMBRE_CORREGIDO, DESCR_SERIE AS DESCRIPCION
FROM DOCUMENTAL_OWNER.GDOC_SERIES_T
WHERE ID_SERIE IN (7, 8, 9, 10, 11, 12, 13, 14)
ORDER BY ID_SERIE;

COMMIT;

