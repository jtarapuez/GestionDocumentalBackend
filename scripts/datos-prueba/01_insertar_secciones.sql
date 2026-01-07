-- =====================================================
-- SCRIPT DE DATOS DE PRUEBA: SECCIONES DOCUMENTALES
-- =====================================================
-- Descripción: Inserta secciones documentales de prueba
-- Tabla: GDOC_SECCIONES_TP
-- Convención: NOM_SECCION contiene 'PRUEBA' o 'TEST'
-- Usuario de prueba: 1234567890
-- =====================================================

-- IMPORTANTE: Ejecutar este script conectado al schema DOCUMENTAL_OWNER
-- O usar: ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;

-- Configurar codificación UTF-8 para caracteres especiales
ALTER SESSION SET NLS_LANGUAGE = 'SPANISH';
ALTER SESSION SET NLS_TERRITORY = 'ECUADOR';

-- ⚠️ IMPORTANTE: Si obtienes error ORA-00001 (unique constraint),
-- ejecuta primero: 00_limpiar_solo_secciones.sql

-- =====================================================
-- INSERT 1: Sección Pensiones
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SECCIONES_TP (
    ID_SECCION,
    NOM_SECCION,
    DESCR_SECCION,
    EST_REGISTRO,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SECCIONES_S.NEXTVAL,
    'PRUEBA - Sección Pensiones',
    'Sección documental de prueba para expedientes de pensiones. [PRUEBA]',
    'Creado',
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 2: Sección Prestaciones
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SECCIONES_TP (
    ID_SECCION,
    NOM_SECCION,
    DESCR_SECCION,
    EST_REGISTRO,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SECCIONES_S.NEXTVAL,
    'PRUEBA - Sección Prestaciones',
    'Sección documental de prueba para prestaciones sociales. [PRUEBA]',
    'Creado',
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 3: Sección Recursos Humanos
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_SECCIONES_TP (
    ID_SECCION,
    NOM_SECCION,
    DESCR_SECCION,
    EST_REGISTRO,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_SECCIONES_S.NEXTVAL,
    'TEST - Sección Recursos Humanos',
    'Sección documental de prueba para recursos humanos. [TEST]',
    'Creado',
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- Verificar inserción
-- =====================================================
SELECT 
    ID_SECCION,
    NOM_SECCION,
    DESCR_SECCION,
    EST_REGISTRO,
    USU_CREACION,
    FEC_CREACION
FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%'
ORDER BY ID_SECCION;

COMMIT;
