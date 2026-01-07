-- =====================================================
-- SCRIPT DE DATOS DE PRUEBA: SERIES DOCUMENTALES
-- =====================================================
-- Descripción: Inserta series documentales de prueba
-- Tabla: GDOC_SERIES_T
-- Convención: NOM_SERIES contiene 'PRUEBA' o 'TEST'
-- Usuario de prueba: 1234567890
-- NOTA: Ejecutar primero 01_insertar_secciones.sql
-- =====================================================

-- IMPORTANTE: Ejecutar este script conectado al schema DOCUMENTAL_OWNER
-- O usar: ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;

-- Configurar codificación UTF-8 para caracteres especiales
ALTER SESSION SET NLS_LANGUAGE = 'SPANISH';
ALTER SESSION SET NLS_TERRITORY = 'ECUADOR';

-- =====================================================
-- INSERT 1: Serie Pensiones de Vejez
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SERIES_T (
    ID_SERIE,
    NOM_SERIES,
    DESCR_SERIE,
    FORMATO_DOC,
    SEGURIDAD,
    NORMATIVA,
    RESPONSABLE,
    ESTADO,
    JUSTIFICACION,
    ID_SECCION,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SERIES_S.NEXTVAL,
    'PRUEBA - Serie Pensiones de Vejez',
    'Serie documental de prueba para expedientes de pensiones de vejez. [PRUEBA]',
    'Físico',
    'Confidencial',
    'Ley Orgánica de Seguridad Social - Art. 45',
    '1234567890',
    'Creado',
    'Creación inicial para pruebas',
    (SELECT MIN(ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
     WHERE NOM_SECCION LIKE '%PRUEBA%Pensiones%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 2: Serie Pensiones de Invalidez
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SERIES_T (
    ID_SERIE,
    NOM_SERIES,
    DESCR_SERIE,
    FORMATO_DOC,
    SEGURIDAD,
    NORMATIVA,
    RESPONSABLE,
    ESTADO,
    JUSTIFICACION,
    ID_SECCION,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SERIES_S.NEXTVAL,
    'PRUEBA - Serie Pensiones de Invalidez',
    'Serie documental de prueba para expedientes de pensiones de invalidez. [PRUEBA]',
    'Mixto',
    'Confidencial',
    'Ley Orgánica de Seguridad Social - Art. 50',
    '1234567890',
    'Creado',
    'Creación inicial para pruebas',
    (SELECT MIN(ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
     WHERE NOM_SECCION LIKE '%PRUEBA%Pensiones%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 3: Serie Prestaciones Económicas
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SERIES_T (
    ID_SERIE,
    NOM_SERIES,
    DESCR_SERIE,
    FORMATO_DOC,
    SEGURIDAD,
    NORMATIVA,
    RESPONSABLE,
    ESTADO,
    JUSTIFICACION,
    ID_SECCION,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SERIES_S.NEXTVAL,
    'PRUEBA - Serie Prestaciones Económicas',
    'Serie documental de prueba para prestaciones económicas. [PRUEBA]',
    'Digital',
    'Pública',
    'Reglamento de Prestaciones Económicas',
    '1234567890',
    'Creado',
    'Creación inicial para pruebas',
    (SELECT MIN(ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
     WHERE NOM_SECCION LIKE '%PRUEBA%Prestaciones%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- Verificar inserción
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
JOIN DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC ON S.ID_SECCION = SEC.ID_SECCION
WHERE S.NOM_SERIES LIKE '%PRUEBA%' OR S.NOM_SERIES LIKE '%TEST%'
ORDER BY S.ID_SERIE;

COMMIT;
