-- =====================================================
-- SCRIPT DE DATOS DE PRUEBA: SUBSERIES DOCUMENTALES
-- =====================================================
-- Descripción: Inserta subseries documentales de prueba
-- Tabla: GDOC_SUBSERIES_T
-- Convención: NOM_SUBSERIE contiene 'PRUEBA' o 'TEST'
-- Usuario de prueba: 1234567890
-- NOTA: Ejecutar primero 02_insertar_series.sql
-- =====================================================

-- IMPORTANTE: Ejecutar este script conectado al schema DOCUMENTAL_OWNER
-- O usar: ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;

-- Configurar codificación UTF-8 para caracteres especiales
ALTER SESSION SET NLS_LANGUAGE = 'SPANISH';
ALTER SESSION SET NLS_TERRITORY = 'ECUADOR';

-- =====================================================
-- INSERT 1: Subserie Trámites Iniciales Vejez
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SUBSERIES_T (
    ID_SUBSERIE,
    NOM_SUBSERIE,
    DESCR_SUBSERIE,
    FORMATO_DOC,
    SEGURIDAD,
    NORMATIVA,
    RESPONSABLE,
    ESTADO,
    JUSTIFICACION,
    ID_SERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SUBSERIES_S.NEXTVAL,
    'PRUEBA - Subserie Trámites Iniciales Vejez',
    'Subserie de prueba para trámites iniciales de pensiones de vejez. [PRUEBA]',
    'Físico',
    'Confidencial',
    'Ley Orgánica de Seguridad Social',
    '1234567890',
    'Creado',
    'Creación inicial para pruebas',
    (SELECT MIN(ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
     WHERE NOM_SERIES LIKE '%Pensiones de Vejez%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 2: Subserie Renovaciones Vejez
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SUBSERIES_T (
    ID_SUBSERIE,
    NOM_SUBSERIE,
    DESCR_SUBSERIE,
    FORMATO_DOC,
    SEGURIDAD,
    NORMATIVA,
    RESPONSABLE,
    ESTADO,
    JUSTIFICACION,
    ID_SERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SUBSERIES_S.NEXTVAL,
    'PRUEBA - Subserie Renovaciones Vejez',
    'Subserie de prueba para renovaciones de pensiones de vejez. [PRUEBA]',
    'Físico',
    'Confidencial',
    'Ley Orgánica de Seguridad Social',
    '1234567890',
    'Creado',
    'Creación inicial para pruebas',
    (SELECT MIN(ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
     WHERE NOM_SERIES LIKE '%Pensiones de Vejez%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 3: Subserie Evaluaciones Médicas Invalidez
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SUBSERIES_T (
    ID_SUBSERIE,
    NOM_SUBSERIE,
    DESCR_SUBSERIE,
    FORMATO_DOC,
    SEGURIDAD,
    NORMATIVA,
    RESPONSABLE,
    ESTADO,
    JUSTIFICACION,
    ID_SERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SUBSERIES_S.NEXTVAL,
    'PRUEBA - Subserie Evaluaciones Médicas Invalidez',
    'Subserie de prueba para evaluaciones médicas de invalidez. [PRUEBA]',
    'Mixto',
    'Confidencial',
    'Ley Orgánica de Seguridad Social',
    '1234567890',
    'Creado',
    'Creación inicial para pruebas',
    (SELECT MIN(ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
     WHERE NOM_SERIES LIKE '%Pensiones de Invalidez%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 4: Subserie Subsidios Económicos
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SUBSERIES_T (
    ID_SUBSERIE,
    NOM_SUBSERIE,
    DESCR_SUBSERIE,
    FORMATO_DOC,
    SEGURIDAD,
    NORMATIVA,
    RESPONSABLE,
    ESTADO,
    JUSTIFICACION,
    ID_SERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SUBSERIES_S.NEXTVAL,
    'PRUEBA - Subserie Subsidios Económicos',
    'Subserie de prueba para subsidios económicos. [PRUEBA]',
    'Digital',
    'Pública',
    'Reglamento de Prestaciones Económicas',
    '1234567890',
    'Creado',
    'Creación inicial para pruebas',
    (SELECT MIN(ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
     WHERE NOM_SERIES LIKE '%Prestaciones Económicas%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- Verificar inserción
-- =====================================================
SELECT 
    SS.ID_SUBSERIE,
    SS.NOM_SUBSERIE,
    SS.DESCR_SUBSERIE,
    SS.FORMATO_DOC,
    SS.SEGURIDAD,
    SS.ESTADO,
    S.NOM_SERIES AS SERIE,
    SEC.NOM_SECCION AS SECCION,
    SS.USU_CREACION,
    SS.FEC_CREACION
FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS
JOIN DOCUMENTAL_OWNER.GDOC_SERIES_T S ON SS.ID_SERIE = S.ID_SERIE
JOIN DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC ON S.ID_SECCION = SEC.ID_SECCION
WHERE SS.NOM_SUBSERIE LIKE '%PRUEBA%' OR SS.NOM_SUBSERIE LIKE '%TEST%'
ORDER BY SS.ID_SUBSERIE;

COMMIT;
