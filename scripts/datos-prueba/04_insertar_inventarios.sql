-- =====================================================
-- SCRIPT DE DATOS DE PRUEBA: INVENTARIOS DOCUMENTALES
-- =====================================================
-- Descripción: Inserta inventarios documentales de prueba
-- Tabla: GDOC_INVENTARIO_T
-- Convención: NUM_EXPEDIENTE contiene 'EXP-TEST-' o 'TEST-'
-- Usuario de prueba: 1234567890 (operador)
-- Supervisor: 0987654321
-- NOTA: Ejecutar primero 03_insertar_subseries.sql
-- =====================================================

-- IMPORTANTE: Ejecutar este script conectado al schema DOCUMENTAL_OWNER
-- O usar: ALTER SESSION SET CURRENT_SCHEMA = DOCUMENTAL_OWNER;

-- Configurar codificación UTF-8 para caracteres especiales
ALTER SESSION SET NLS_LANGUAGE = 'SPANISH';
ALTER SESSION SET NLS_TERRITORY = 'ECUADOR';

-- =====================================================
-- INSERT 1: Expediente de Prueba - Archivo Activo
-- =====================================================

INSERT INTO DOCUMENTAL_OWNER.GDOC_INVENTARIO_T (
    ID_INVENTARIO,
    NUM_EXPEDIENTE,
    NUM_CEDULA,
    NOMBRES_APELLIDOS,
    DESCR_SERIE,
    NUM_EXTDESDE,
    NUM_EXTHASTA,
    FEC_DESDE,
    FEC_HASTA,
    CANT_FOJAS,
    TIPO_CONTENEDOR,
    NUM_CONTENEDOR,
    SOPORTE,
    TIPO_ARCHIVO,
    OBSERVACIONES,
    OPERADOR,
    SUPERVISOR,
    ESTADO_INVENTARIO,
    FEC_CAMBIO_ESTADO,
    CED_USUARIO_CAMBIO,
    ID_SECCION,
    ID_SERIE,
    ID_SUBSERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_INVENTARIO_S.NEXTVAL,
    'EXP-TEST-001',
    '1234567890',
    'JUAN PÉREZ GONZÁLEZ',
    'Expediente de prueba para pensiones de vejez - Trámite inicial. [PRUEBA]',
    1,
    50,
    TO_DATE('2020-01-15', 'YYYY-MM-DD'),
    TO_DATE('2020-12-31', 'YYYY-MM-DD'),
    75,
    'Caja',
    1,
    'Físico',
    'Archivo activo',
    'Inventario de prueba para desarrollo. [PRUEBA]',
    '1234567890',
    '0987654321',
    'Registrado',
    SYSDATE,
    '1234567890',
    (SELECT MIN(SEC.ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC
     WHERE SEC.NOM_SECCION LIKE '%PRUEBA%Pensiones%'),
    (SELECT MIN(S.ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T S
     WHERE S.NOM_SERIES LIKE '%Pensiones de Vejez%'),
    (SELECT MIN(SS.ID_SUBSERIE) FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS
     WHERE SS.NOM_SUBSERIE LIKE '%Trámites Iniciales Vejez%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 2: Expediente de Prueba - Archivo Pasivo
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_INVENTARIO_T (
    ID_INVENTARIO,
    NUM_EXPEDIENTE,
    NUM_CEDULA,
    NOMBRES_APELLIDOS,
    DESCR_SERIE,
    NUM_EXTDESDE,
    NUM_EXTHASTA,
    FEC_DESDE,
    FEC_HASTA,
    CANT_FOJAS,
    TIPO_CONTENEDOR,
    NUM_CONTENEDOR,
    SOPORTE,
    TIPO_ARCHIVO,
    POSICION_PASIVO,
    NUM_RAC,
    NUM_FILA,
    NUM_COLUMNA,
    NUM_POSICION,
    BODEGA,
    OBSERVACIONES,
    OPERADOR,
    SUPERVISOR,
    ESTADO_INVENTARIO,
    FEC_CAMBIO_ESTADO,
    CED_USUARIO_CAMBIO,
    ID_SECCION,
    ID_SERIE,
    ID_SUBSERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_INVENTARIO_S.NEXTVAL,
    'EXP-TEST-002',
    '0987654321',
    'MARÍA LÓPEZ MARTÍNEZ',
    'Expediente de prueba para pensiones de vejez - Renovación. [PRUEBA]',
    51,
    100,
    TO_DATE('2019-01-01', 'YYYY-MM-DD'),
    TO_DATE('2019-12-31', 'YYYY-MM-DD'),
    120,
    'Caja',
    2,
    'Físico',
    'Archivo pasivo',
    '06.12.04.21.01',  -- Formato: RAC.FILA.COLUMNA.POSICION.BODEGA
    6,
    12,
    4,
    21,
    1,
    'Inventario de prueba - Archivo pasivo. [PRUEBA]',
    '1234567890',
    '0987654321',
    'Registrado',
    SYSDATE,
    '1234567890',
    (SELECT MIN(SEC.ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC
     WHERE SEC.NOM_SECCION LIKE '%PRUEBA%Pensiones%'),
    (SELECT MIN(S.ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T S
     WHERE S.NOM_SERIES LIKE '%Pensiones de Vejez%'),
    (SELECT MIN(SS.ID_SUBSERIE) FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS
     WHERE SS.NOM_SUBSERIE LIKE '%Renovaciones Vejez%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 3: Expediente de Prueba - Con RUC
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_INVENTARIO_T (
    ID_INVENTARIO,
    NUM_EXPEDIENTE,
    NUM_RUC,
    RAZON_SOCIAL,
    DESCR_SERIE,
    NUM_EXTDESDE,
    NUM_EXTHASTA,
    FEC_DESDE,
    FEC_HASTA,
    CANT_FOJAS,
    TIPO_CONTENEDOR,
    NUM_CONTENEDOR,
    SOPORTE,
    TIPO_ARCHIVO,
    OBSERVACIONES,
    OPERADOR,
    SUPERVISOR,
    ESTADO_INVENTARIO,
    FEC_CAMBIO_ESTADO,
    CED_USUARIO_CAMBIO,
    ID_SECCION,
    ID_SERIE,
    ID_SUBSERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_INVENTARIO_S.NEXTVAL,
    'EXP-TEST-003',
    '0991234567001',
    'EMPRESA DE PRUEBA S.A.',
    'Expediente de prueba para prestaciones económicas - Subsidios. [PRUEBA]',
    1,
    30,
    TO_DATE('2021-06-01', 'YYYY-MM-DD'),
    TO_DATE('2021-12-31', 'YYYY-MM-DD'),
    45,
    'Carpeta',
    1,
    'Digital',
    'Archivo activo',
    'Inventario de prueba con RUC. [PRUEBA]',
    '1234567890',
    '0987654321',
    'Registrado',
    SYSDATE,
    '1234567890',
    (SELECT MIN(SEC.ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC
     WHERE SEC.NOM_SECCION LIKE '%PRUEBA%Prestaciones%'),
    (SELECT MIN(S.ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T S
     WHERE S.NOM_SERIES LIKE '%Prestaciones Económicas%'),
    (SELECT MIN(SS.ID_SUBSERIE) FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS
     WHERE SS.NOM_SUBSERIE LIKE '%Subsidios Económicos%'),
    '1234567890',
    SYSDATE,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 4: Expediente de Prueba - Pendiente de Aprobación
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_INVENTARIO_T (
    ID_INVENTARIO,
    NUM_EXPEDIENTE,
    NUM_CEDULA,
    NOMBRES_APELLIDOS,
    DESCR_SERIE,
    NUM_EXTDESDE,
    NUM_EXTHASTA,
    FEC_DESDE,
    FEC_HASTA,
    CANT_FOJAS,
    TIPO_CONTENEDOR,
    NUM_CONTENEDOR,
    SOPORTE,
    TIPO_ARCHIVO,
    OBSERVACIONES,
    OPERADOR,
    SUPERVISOR,
    ESTADO_INVENTARIO,
    FEC_CAMBIO_ESTADO,
    CED_USUARIO_CAMBIO,
    ID_SECCION,
    ID_SERIE,
    ID_SUBSERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_INVENTARIO_S.NEXTVAL,
    'EXP-TEST-004',
    '1122334455',
    'CARLOS RODRÍGUEZ SÁNCHEZ',
    'Expediente de prueba para evaluaciones médicas de invalidez. [PRUEBA]',
    1,
    25,
    TO_DATE('2022-03-01', 'YYYY-MM-DD'),
    TO_DATE('2022-12-31', 'YYYY-MM-DD'),
    60,
    'Caja',
    3,
    'Mixto',
    'Archivo activo',
    'Inventario de prueba - Pendiente de aprobación. [PRUEBA]',
    '1234567890',
    '0987654321',
    'Pendiente de Aprobación',
    SYSDATE - 2,  -- Hace 2 días (dentro del límite de 5 días)
    '1234567890',
    (SELECT MIN(SEC.ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC
     WHERE SEC.NOM_SECCION LIKE '%PRUEBA%Pensiones%'),
    (SELECT MIN(S.ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T S
     WHERE S.NOM_SERIES LIKE '%Pensiones de Invalidez%'),
    (SELECT MIN(SS.ID_SUBSERIE) FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS
     WHERE SS.NOM_SUBSERIE LIKE '%Evaluaciones Médicas Invalidez%'),
    '1234567890',
    SYSDATE - 2,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- INSERT 5: Expediente de Prueba - Aprobado
-- =====================================================
INSERT INTO DOCUMENTAL_OWNER.GDOC_INVENTARIO_T (
    ID_INVENTARIO,
    NUM_EXPEDIENTE,
    NUM_CEDULA,
    NOMBRES_APELLIDOS,
    DESCR_SERIE,
    NUM_EXTDESDE,
    NUM_EXTHASTA,
    FEC_DESDE,
    FEC_HASTA,
    CANT_FOJAS,
    TIPO_CONTENEDOR,
    NUM_CONTENEDOR,
    SOPORTE,
    TIPO_ARCHIVO,
    OBSERVACIONES,
    OPERADOR,
    SUPERVISOR,
    ESTADO_INVENTARIO,
    FEC_CAMBIO_ESTADO,
    CED_USUARIO_CAMBIO,
    ID_SECCION,
    ID_SERIE,
    ID_SUBSERIE,
    USU_CREACION,
    FEC_CREACION,
    IP_EQUIPO
) VALUES (
    DOCUMENTAL_OWNER.GDOC_INVENTARIO_S.NEXTVAL,
    'EXP-TEST-005',
    '2233445566',
    'ANA GARCÍA FERNÁNDEZ',
    'Expediente de prueba aprobado. [PRUEBA]',
    1,
    40,
    TO_DATE('2021-01-01', 'YYYY-MM-DD'),
    TO_DATE('2021-12-31', 'YYYY-MM-DD'),
    90,
    'Caja',
    4,
    'Físico',
    'Archivo activo',
    'Inventario de prueba - Aprobado. [PRUEBA]',
    '1234567890',
    '0987654321',
    'Aprobado',
    SYSDATE - 10,  -- Aprobado hace 10 días
    '0987654321',
    (SELECT MIN(SEC.ID_SECCION) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC
     WHERE SEC.NOM_SECCION LIKE '%PRUEBA%Pensiones%'),
    (SELECT MIN(S.ID_SERIE) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T S
     WHERE S.NOM_SERIES LIKE '%Pensiones de Vejez%'),
    (SELECT MIN(SS.ID_SUBSERIE) FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS
     WHERE SS.NOM_SUBSERIE LIKE '%Trámites Iniciales Vejez%'),
    '1234567890',
    SYSDATE - 15,
    SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50)
);

-- =====================================================
-- Verificar inserción
-- =====================================================
SELECT 
    I.ID_INVENTARIO,
    I.NUM_EXPEDIENTE,
    I.NUM_CEDULA,
    I.NOMBRES_APELLIDOS,
    I.RAZON_SOCIAL,
    I.ESTADO_INVENTARIO,
    I.OPERADOR,
    I.SUPERVISOR,
    SEC.NOM_SECCION AS SECCION,
    S.NOM_SERIES AS SERIE,
    SS.NOM_SUBSERIE AS SUBSERIE,
    I.FEC_CREACION
FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T I
JOIN DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC ON I.ID_SECCION = SEC.ID_SECCION
JOIN DOCUMENTAL_OWNER.GDOC_SERIES_T S ON I.ID_SERIE = S.ID_SERIE
JOIN DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS ON I.ID_SUBSERIE = SS.ID_SUBSERIE
WHERE I.NUM_EXPEDIENTE LIKE 'EXP-TEST-%' OR I.NUM_EXPEDIENTE LIKE 'TEST-%'
ORDER BY I.ID_INVENTARIO;

COMMIT;
