-- =====================================================
-- SCRIPT DE LIMPIEZA: SOLO SECCIONES DE PRUEBA
-- =====================================================
-- Descripción: Elimina solo las secciones de prueba
-- Útil cuando solo necesitas limpiar secciones antes de reinsertar
-- =====================================================

-- Verificar qué secciones de prueba existen
SELECT 
    ID_SECCION,
    NOM_SECCION,
    DESCR_SECCION,
    USU_CREACION,
    FEC_CREACION
FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%'
ORDER BY ID_SECCION;

-- Eliminar secciones de prueba
DELETE FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
WHERE NOM_SECCION LIKE '%PRUEBA%'
   OR NOM_SECCION LIKE '%TEST%'
   OR USU_CREACION = '1234567890';

-- Verificar eliminación
SELECT COUNT(*) AS SECCIONES_RESTANTES
FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%';

COMMIT;
