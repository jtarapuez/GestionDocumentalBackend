-- ============================================================
-- Script: Actualizar OPERADOR para pruebas - Distribuir entre operador 1 y 2
-- Fecha: 2026-01-22
-- Descripción: Actualiza algunos registros para tener operadores diferentes
--              para pruebas (algunos con operador "1", otros con "2")
-- ============================================================

-- ⚠️ IMPORTANTE: Ejecutar SOLO en base de datos de desarrollo
-- ⚠️ IMPORTANTE: Este script actualiza registros específicos para pruebas

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
  DBMS_OUTPUT.PUT_LINE('=== ACTUALIZANDO REGISTROS PARA PRUEBAS ===');
  
  -- Actualizar los primeros 3 registros para que tengan operador "1"
  -- (ya deberían estar en "1", pero por si acaso)
  UPDATE GDOC_INVENTARIO_T 
  SET OPERADOR = '1'
  WHERE ID_INVENTARIO IN (
    SELECT ID_INVENTARIO 
    FROM (
      SELECT ID_INVENTARIO 
      FROM GDOC_INVENTARIO_T 
      ORDER BY ID_INVENTARIO 
      FETCH FIRST 3 ROWS ONLY
    )
  );
  
  DBMS_OUTPUT.PUT_LINE('Actualizados primeros 3 registros -> Operador 1');
  
  -- Actualizar los siguientes 3 registros para que tengan operador "2"
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
  
  DBMS_OUTPUT.PUT_LINE('Actualizados siguientes 3 registros -> Operador 2');
  
  -- Si hay más registros, dejarlos en "1" (operador principal)
  -- (no es necesario actualizarlos, ya están en "1")
  
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
    DBMS_OUTPUT.PUT_LINE('ID: ' || rec.ID_INVENTARIO || ' | Expediente: ' || rec.NUM_EXPEDIENTE || ' | Operador: ' || rec.OPERADOR);
  END LOOP;
  
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('');
  DBMS_OUTPUT.PUT_LINE('=== ACTUALIZACIÓN COMPLETADA ===');
END;
/

