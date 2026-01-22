-- ============================================================
-- Script: Actualizar OPERADOR de cédulas a IDs numéricos
-- Fecha: 2026-01-22
-- Descripción: Actualiza el campo OPERADOR en GDOC_INVENTARIO_T
--              de cédulas (ej: "1234567890") a IDs numéricos (ej: "1")
-- ============================================================

-- ⚠️ IMPORTANTE: Ejecutar SOLO en base de datos de desarrollo
-- ⚠️ IMPORTANTE: Hacer backup antes de ejecutar

BEGIN
  -- Verificar estado actual
  DBMS_OUTPUT.PUT_LINE('=== ESTADO ANTES DE ACTUALIZACIÓN ===');
  FOR rec IN (
    SELECT OPERADOR, COUNT(*) as CANTIDAD
    FROM GDOC_INVENTARIO_T
    GROUP BY OPERADOR
    ORDER BY OPERADOR
  ) LOOP
    DBMS_OUTPUT.PUT_LINE('OPERADOR: ' || rec.OPERADOR || ' - Cantidad: ' || rec.CANTIDAD);
  END LOOP;
  
  -- Actualizar registros con cédula "1234567890" a ID "1"
  UPDATE GDOC_INVENTARIO_T 
  SET OPERADOR = '1'
  WHERE OPERADOR = '1234567890';
  
  DBMS_OUTPUT.PUT_LINE('Actualizados registros con cédula 1234567890 -> ID 1');
  
  -- Actualizar registros con cédula "1122334455" a ID "1"
  UPDATE GDOC_INVENTARIO_T 
  SET OPERADOR = '1'
  WHERE OPERADOR = '1122334455';
  
  DBMS_OUTPUT.PUT_LINE('Actualizados registros con cédula 1122334455 -> ID 1');
  
  -- Actualizar registros con cédula "2233445566" a ID "2" (operador2.sdngd)
  UPDATE GDOC_INVENTARIO_T 
  SET OPERADOR = '2'
  WHERE OPERADOR = '2233445566';
  
  DBMS_OUTPUT.PUT_LINE('Actualizados registros con cédula 2233445566 -> ID 2');
  
  -- Verificar estado después de actualización
  DBMS_OUTPUT.PUT_LINE('=== ESTADO DESPUÉS DE ACTUALIZACIÓN ===');
  FOR rec IN (
    SELECT OPERADOR, COUNT(*) as CANTIDAD
    FROM GDOC_INVENTARIO_T
    GROUP BY OPERADOR
    ORDER BY OPERADOR
  ) LOOP
    DBMS_OUTPUT.PUT_LINE('OPERADOR: ' || rec.OPERADOR || ' - Cantidad: ' || rec.CANTIDAD);
  END LOOP;
  
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('=== ACTUALIZACIÓN COMPLETADA ===');
END;
/

