# 🔧 Solución: Error ORA-00001 - Unique Constraint Violated

## ❌ Error

```
ORA-00001: unique constraint (DOCUMENTAL_OWNER.GDOC_SECCIONES_PK) violated
```

## 🔍 Causa

Este error ocurre cuando intentas insertar datos que **ya existen** en la base de datos. Probablemente ya ejecutaste el script antes y los datos de prueba ya están insertados.

## ✅ Soluciones

### Solución 1: Limpiar datos antes de insertar (Recomendado)

**Paso 1:** Ejecuta el script de limpieza
```sql
-- Ejecutar: 00_limpiar_solo_secciones.sql
```

**Paso 2:** Luego ejecuta el script de inserción
```sql
-- Ejecutar: 01_insertar_secciones.sql
```

---

### Solución 2: Verificar qué datos existen

Antes de insertar, verifica si ya existen datos:

```sql
SELECT 
    ID_SECCION,
    NOM_SECCION,
    DESCR_SECCION,
    USU_CREACION,
    FEC_CREACION
FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%'
ORDER BY ID_SECCION;
```

Si hay resultados, significa que los datos ya están insertados. Puedes:
- **Opción A:** Limpiar y reinsertar (usar Solución 1)
- **Opción B:** Continuar con los otros scripts (series, subseries, inventarios)

---

### Solución 3: Usar MERGE en lugar de INSERT

Si quieres que el script sea "idempotente" (puede ejecutarse múltiples veces sin error), puedes usar `MERGE`:

```sql
MERGE INTO DOCUMENTAL_OWNER.GDOC_SECCIONES_TP T
USING (
    SELECT 
        DOCUMENTAL_OWNER.GDOC_SECCIONES_S.NEXTVAL AS ID_SECCION,
        'PRUEBA - Sección Pensiones' AS NOM_SECCION,
        'Sección documental de prueba para expedientes de pensiones. [PRUEBA]' AS DESCR_SECCION,
        'Creado' AS EST_REGISTRO,
        '1234567890' AS USU_CREACION,
        SYSDATE AS FEC_CREACION,
        SUBSTR(SYS_CONTEXT('USERENV', 'IP_ADDRESS'), 1, 50) AS IP_EQUIPO
    FROM DUAL
) S ON (T.NOM_SECCION = S.NOM_SECCION)
WHEN NOT MATCHED THEN
    INSERT (ID_SECCION, NOM_SECCION, DESCR_SECCION, EST_REGISTRO, USU_CREACION, FEC_CREACION, IP_EQUIPO)
    VALUES (S.ID_SECCION, S.NOM_SECCION, S.DESCR_SECCION, S.EST_REGISTRO, S.USU_CREACION, S.FEC_CREACION, S.IP_EQUIPO);
```

---

## 📋 Orden Recomendado de Ejecución

Si es la **primera vez** que ejecutas los scripts:

1. ✅ `00_limpiar_datos_prueba.sql` (opcional, para asegurar que está limpio)
2. ✅ `01_insertar_secciones.sql`
3. ✅ `02_insertar_series.sql`
4. ✅ `03_insertar_subseries.sql`
5. ✅ `04_insertar_inventarios.sql`

Si **ya ejecutaste** y quieres **reinsertar**:

1. ✅ `00_limpiar_solo_secciones.sql` (solo si quieres reinsertar secciones)
2. ✅ `01_insertar_secciones.sql`

---

## 🔍 Verificación Rápida

Para verificar si los datos ya existen:

```sql
-- Secciones
SELECT COUNT(*) FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%';

-- Series
SELECT COUNT(*) FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
WHERE NOM_SERIES LIKE '%PRUEBA%' OR NOM_SERIES LIKE '%TEST%';

-- Subseries
SELECT COUNT(*) FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T 
WHERE NOM_SUBSERIE LIKE '%PRUEBA%' OR NOM_SUBSERIE LIKE '%TEST%';

-- Inventarios
SELECT COUNT(*) FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T 
WHERE NUM_EXPEDIENTE LIKE 'EXP-TEST-%' OR NUM_EXPEDIENTE LIKE 'TEST-%';
```

**Resultados esperados si están vacíos:**
- Secciones: 0
- Series: 0
- Subseries: 0
- Inventarios: 0

---

## 💡 Recomendación

**Para desarrollo:** Siempre ejecuta el script de limpieza antes de insertar:
```sql
-- 1. Limpiar
@00_limpiar_solo_secciones.sql

-- 2. Insertar
@01_insertar_secciones.sql
```

Esto evita errores de constraint único.

---

**Última actualización:** 2026-01-06




