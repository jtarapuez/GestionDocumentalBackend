-- ============================================================
-- Script: Actualizar 3 registros para tener operador "2"
-- Fecha: 2026-01-22
-- Descripción: Actualiza 3 registros específicos para que tengan operador "2"
--              para pruebas (el resto se queda en "1")
-- ============================================================

-- ⚠️ IMPORTANTE: Ejecutar SOLO en base de datos de desarrollo

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
  
  DBMS_OUTPUT.PUT_LINE('');
  DBMS_OUTPUT.PUT_LINE('=== ACTUALIZANDO 3 REGISTROS PARA OPERADOR 2 ===');
  
  -- Actualizar los registros 4, 5 y 6 (después de los primeros 3) para operador "2"
  -- Si hay menos de 6 registros, actualizará los que existan
  UPDATE GDOC_INVENTARIO_T 
  SET OPERADOR = '2'
  WHERE ID_INVENTARIO IN (
    SELECT ID_INVENTARIO 
    FROM (
      SELECT ID_INVENTARIO 
      FROM GDOC_INVENTARIO_T 
      ORDER BY ID_INVENTARIO 
      OFFSET 3 ROWS FETCH NEXT 3 ROWS ONLY
    )
  );
  
  DBMS_OUTPUT.PUT_LINE('Actualizados 3 registros -> Operador 2');
  
  -- Verificar estado después de actualización
  DBMS_OUTPUT.PUT_LINE('');
  DBMS_OUTPUT.PUT_LINE('=== ESTADO DESPUÉS DE ACTUALIZACIÓN ===');
  FOR rec IN (
    SELECT OPERADOR, COUNT(*) as CANTIDAD
    FROM GDOC_INVENTARIO_T
    GROUP BY OPERADOR
    ORDER BY OPERADOR
  ) LOOP
    DBMS_OUTPUT.PUT_LINE('OPERADOR: ' || rec.OPERADOR || ' - Cantidad: ' || rec.CANTIDAD);
  END LOOP;
  
  -- Mostrar detalle de los primeros 6 registros
  DBMS_OUTPUT.PUT_LINE('');
  DBMS_OUTPUT.PUT_LINE('=== DETALLE DE PRIMEROS 6 REGISTROS ===');
  FOR rec IN (
    SELECT ID_INVENTARIO, NUM_EXPEDIENTE, OPERADOR
    FROM GDOC_INVENTARIO_T
    ORDER BY ID_INVENTARIO
    FETCH FIRST 6 ROWS ONLY
  ) LOOP
    DBMS_OUTPUT.PUT_LINE('ID: ' || rec.ID_INVENTARIO || ' | Expediente: ' || NVL(rec.NUM_EXPEDIENTE, 'N/A') || ' | Operador: ' || rec.OPERADOR);
  END LOOP;
  
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('');
  DBMS_OUTPUT.PUT_LINE('=== ACTUALIZACIÓN COMPLETADA ===');
  DBMS_OUTPUT.PUT_LINE('Resultado: Los primeros 3 registros tienen operador "1", los siguientes 3 tienen operador "2"');
END;
/

