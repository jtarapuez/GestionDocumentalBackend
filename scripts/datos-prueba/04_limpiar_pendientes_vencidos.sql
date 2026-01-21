-- =====================================================
-- SCRIPT DE LIMPIEZA: INVENTARIOS PENDIENTES VENCIDOS
-- =====================================================
-- Descripción: Actualiza o elimina inventarios de prueba 
--              en estado "Pendiente de Aprobación" vencidos (más de 5 días)
-- IMPORTANTE: Ejecutar SOLO cuando se necesite limpiar datos de prueba
-- =====================================================

-- ⚠️ ADVERTENCIA: Este script actualizará/eliminará inventarios de prueba pendientes vencidos
-- ⚠️ Verificar antes de ejecutar que solo se afecten datos de prueba

-- Verificar inventarios pendientes vencidos de prueba
SELECT 
    I.ID_INVENTARIO,
    I.NUM_EXPEDIENTE,
    I.ESTADO_INVENTARIO,
    I.OPERADOR,
    I.FEC_CAMBIO_ESTADO,
    ROUND(SYSDATE - I.FEC_CAMBIO_ESTADO, 0) AS DIAS_VENCIDOS,
    I.OBSERVACIONES
FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T I
WHERE I.ESTADO_INVENTARIO = 'Pendiente de Aprobación'
  AND I.FEC_CAMBIO_ESTADO < SYSDATE - 5  -- Más de 5 días
  AND (
      I.NUM_EXPEDIENTE LIKE 'EXP-TEST-%' 
      OR I.NUM_EXPEDIENTE LIKE 'TEST-%'
      OR I.OBSERVACIONES LIKE '%[PRUEBA]%'
      OR I.OBSERVACIONES LIKE '%[TEST]%'
      OR I.OPERADOR = '1234567890'
      OR I.USU_CREACION = '1234567890'
  )
ORDER BY I.FEC_CAMBIO_ESTADO;

-- OPCIÓN 1: Actualizar inventarios pendientes vencidos a "Registrado" (recomendado para desarrollo)
-- Esto permite que el operador pueda actualizarlos nuevamente
UPDATE DOCUMENTAL_OWNER.GDOC_INVENTARIO_T 
SET ESTADO_INVENTARIO = 'Registrado',
    FEC_CAMBIO_ESTADO = SYSDATE,
    CED_USUARIO_CAMBIO = '1234567890'
WHERE ESTADO_INVENTARIO = 'Pendiente de Aprobación'
  AND FEC_CAMBIO_ESTADO < SYSDATE - 5  -- Más de 5 días
  AND (
      NUM_EXPEDIENTE LIKE 'EXP-TEST-%' 
      OR NUM_EXPEDIENTE LIKE 'TEST-%'
      OR OBSERVACIONES LIKE '%[PRUEBA]%'
      OR OBSERVACIONES LIKE '%[TEST]%'
      OR OPERADOR = '1234567890'
      OR USU_CREACION = '1234567890'
  );

-- OPCIÓN 2: Eliminar inventarios pendientes vencidos de prueba (descomentar si se prefiere eliminar)
-- DELETE FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T 
-- WHERE ESTADO_INVENTARIO = 'Pendiente de Aprobación'
--   AND FEC_CAMBIO_ESTADO < SYSDATE - 5  -- Más de 5 días
--   AND (
--       NUM_EXPEDIENTE LIKE 'EXP-TEST-%' 
--       OR NUM_EXPEDIENTE LIKE 'TEST-%'
--       OR OBSERVACIONES LIKE '%[PRUEBA]%'
--       OR OBSERVACIONES LIKE '%[TEST]%'
--       OR OPERADOR = '1234567890'
--       OR USU_CREACION = '1234567890'
--   );

-- Verificar que ya no hay pendientes vencidos
SELECT 
    COUNT(*) AS PENDIENTES_VENCIDOS_RESTANTES
FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T 
WHERE ESTADO_INVENTARIO = 'Pendiente de Aprobación'
  AND FEC_CAMBIO_ESTADO < SYSDATE - 5
  AND (
      NUM_EXPEDIENTE LIKE 'EXP-TEST-%' 
      OR NUM_EXPEDIENTE LIKE 'TEST-%'
      OR OBSERVACIONES LIKE '%[PRUEBA]%'
      OR OBSERVACIONES LIKE '%[TEST]%'
      OR OPERADOR = '1234567890'
      OR USU_CREACION = '1234567890'
  );

COMMIT;



