-- =====================================================
-- SCRIPT DE CORRECCIÓN: ACTUALIZAR NOM_SERIES
-- =====================================================
-- Descripción: Corrige los valores de NOM_SERIES que tienen "3" o valores numéricos
-- Tabla: GDOC_SERIES_T
-- Fecha: 2026-01-XX
-- =====================================================
-- IMPORTANTE: Este script corrige los datos que tienen "3" en NOM_SERIES
-- basándose en la descripción (DESCR_SERIE) para generar nombres apropiados
-- =====================================================

-- IMPORTANTE: Ejecutar este script conectado al schema DOCUMENTAL_OWNER
-- O usar: ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;

-- Configurar codificación UTF-8 para caracteres especiales
ALTER SESSION SET NLS_LANGUAGE = 'SPANISH';
ALTER SESSION SET NLS_TERRITORY = 'ECUADOR';

-- =====================================================
-- PASO 1: Verificar datos actuales (ANTES de actualizar)
-- =====================================================
SELECT 
    ID_SERIE,
    NOM_SERIES AS NOMBRE_ACTUAL,
    DESCR_SERIE AS DESCRIPCION,
    ID_SECCION,
    ESTADO
FROM DOCUMENTAL_OWNER.GDOC_SERIES_T
WHERE NOM_SERIES = '3' 
   OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$')  -- Solo números
ORDER BY ID_SERIE;

-- =====================================================
-- PASO 2: Actualizar NOM_SERIES basándose en DESCR_SERIE
-- =====================================================
-- Estrategia: Extraer un nombre descriptivo de DESCR_SERIE
-- Si DESCR_SERIE contiene "pensiones de vejez" -> "PRUEBA - Serie Pensiones de Vejez"
-- Si DESCR_SERIE contiene "pensiones de invalidez" -> "PRUEBA - Serie Pensiones de Invalidez"
-- Si DESCR_SERIE contiene "prestaciones económicas" -> "PRUEBA - Serie Prestaciones Económicas"
-- Si no coincide, usar un nombre genérico basado en la descripción

UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
SET NOM_SERIES = CASE
    -- Serie Pensiones de Vejez
    WHEN UPPER(DESCR_SERIE) LIKE '%PENSIONES DE VEJEZ%' 
         OR UPPER(DESCR_SERIE) LIKE '%VEJEZ%'
    THEN 'PRUEBA - Serie Pensiones de Vejez'
    
    -- Serie Pensiones de Invalidez
    WHEN UPPER(DESCR_SERIE) LIKE '%PENSIONES DE INVALIDEZ%'
         OR UPPER(DESCR_SERIE) LIKE '%INVALIDEZ%'
    THEN 'PRUEBA - Serie Pensiones de Invalidez'
    
    -- Serie Prestaciones Económicas
    WHEN UPPER(DESCR_SERIE) LIKE '%PRESTACIONES ECONÓMICAS%'
         OR UPPER(DESCR_SERIE) LIKE '%PRESTACIONES%'
    THEN 'PRUEBA - Serie Prestaciones Económicas'
    
    -- Otros casos: Generar nombre desde la descripción
    WHEN DESCR_SERIE IS NOT NULL AND LENGTH(TRIM(DESCR_SERIE)) > 0
    THEN 'PRUEBA - Serie ' || SUBSTR(
        REGEXP_REPLACE(
            INITCAP(SUBSTR(DESCR_SERIE, 1, 50)),  -- Primeros 50 caracteres, capitalizados
            '[^A-Za-z0-9 ]', ''  -- Remover caracteres especiales
        ),
        1, 80  -- Limitar a 80 caracteres
    )
    
    -- Fallback: Nombre genérico con ID
    ELSE 'PRUEBA - Serie ' || TO_CHAR(ID_SERIE)
END
WHERE NOM_SERIES = '3' 
   OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$');  -- Solo números

-- =====================================================
-- PASO 3: Verificar datos actualizados (DESPUÉS de actualizar)
-- =====================================================
SELECT 
    ID_SERIE,
    NOM_SERIES AS NOMBRE_CORREGIDO,
    DESCR_SERIE AS DESCRIPCION,
    ID_SECCION,
    ESTADO
FROM DOCUMENTAL_OWNER.GDOC_SERIES_T
WHERE ID_SERIE IN (
    SELECT ID_SERIE 
    FROM DOCUMENTAL_OWNER.GDOC_SERIES_T
    WHERE NOM_SERIES = '3' OR REGEXP_LIKE(NOM_SERIES, '^[0-9]+$')
)
ORDER BY ID_SERIE;

-- =====================================================
-- PASO 4: Verificación final - Todas las series
-- =====================================================
SELECT 
    S.ID_SERIE,
    S.NOM_SERIES,
    S.DESCR_SERIE,
    S.FORMATO_DOC,
    S.SEGURIDAD,
    S.ESTADO,
    SEC.NOM_SECCION AS SECCION,
    S.USU_CREACION,
    S.FEC_CREACION
FROM DOCUMENTAL_OWNER.GDOC_SERIES_T S
LEFT JOIN DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC ON S.ID_SECCION = SEC.ID_SECCION
ORDER BY S.ID_SERIE;

COMMIT;

-- =====================================================
-- NOTAS:
-- =====================================================
-- 1. Este script actualiza solo las series que tienen "3" o valores numéricos en NOM_SERIES
-- 2. Si necesitas actualizar series específicas por ID, usa:
--    UPDATE DOCUMENTAL_OWNER.GDOC_SERIES_T
--    SET NOM_SERIES = 'PRUEBA - Serie Pensiones de Vejez'
--    WHERE ID_SERIE = 7;
-- 3. Verifica los resultados antes de hacer COMMIT
-- 4. Si algo sale mal, puedes hacer ROLLBACK
-- 5. Después de ejecutar este script, el frontend debería mostrar los nombres correctos

