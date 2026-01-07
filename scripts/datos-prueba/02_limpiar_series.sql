-- =====================================================
-- SCRIPT PARA LIMPIAR SERIES DE PRUEBA
-- =====================================================
-- Descripción: Elimina todas las series de prueba
-- Tabla: GDOC_SERIES_T
-- NOTA: Primero eliminar subseries e inventarios que dependen de ellas
-- =====================================================

-- IMPORTANTE: Ejecutar este script conectado al schema DOCUMENTAL_OWNER
-- O usar: ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;

-- Configurar codificación UTF-8 para caracteres especiales
ALTER SESSION SET NLS_LANGUAGE = 'SPANISH';
ALTER SESSION SET NLS_TERRITORY = 'ECUADOR';

-- Eliminar inventarios primero (tienen foreign key a subseries)
DELETE FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T 
WHERE ID_SUBSERIE IN (
    SELECT SS.ID_SUBSERIE 
    FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS
    JOIN DOCUMENTAL_OWNER.GDOC_SERIES_T S ON SS.ID_SERIE = S.ID_SERIE
    WHERE S.NOM_SERIES LIKE '%PRUEBA%' OR S.NOM_SERIES LIKE '%TEST%'
);

-- Eliminar subseries (tienen foreign key a series)
DELETE FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T 
WHERE ID_SERIE IN (
    SELECT ID_SERIE 
    FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
    WHERE NOM_SERIES LIKE '%PRUEBA%' OR NOM_SERIES LIKE '%TEST%'
);

-- Eliminar series de prueba
DELETE FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
WHERE NOM_SERIES LIKE '%PRUEBA%' OR NOM_SERIES LIKE '%TEST%';

COMMIT;
