# Scripts de Datos de Prueba - Sistema de Gestión Documental

## 📋 Descripción

Este directorio contiene scripts SQL para insertar datos de prueba en la base de datos Oracle, siguiendo las **reglas de seguridad** establecidas para la base de datos de desarrollo.

## ⚠️ IMPORTANTE: Reglas de Seguridad

- ✅ **SOLO se insertan datos de prueba** identificados claramente
- ✅ Todos los datos de prueba tienen identificadores especiales:
  - Nombres contienen `PRUEBA` o `TEST`
  - Números de expediente: `EXP-TEST-XXX`
  - Usuario de prueba: `1234567890`
  - Observaciones contienen `[PRUEBA]` o `[TEST]`
- ✅ **NO se modifican** datos de producción
- ✅ **NO se altera** la estructura de tablas

## 📁 Archivos

### 1. `00_limpiar_datos_prueba.sql`
**Descripción:** Elimina todos los datos de prueba de la base de datos.

**Cuándo usar:**
- Antes de insertar nuevos datos de prueba
- Cuando se necesite limpiar datos de prueba antiguos

**⚠️ Advertencia:** Verificar que solo se eliminen datos de prueba.

---

### 2. `01_insertar_secciones.sql`
**Descripción:** Inserta 3 secciones documentales de prueba.

**Datos insertados:**
- PRUEBA - Sección Pensiones
- PRUEBA - Sección Prestaciones
- TEST - Sección Recursos Humanos

**Ejecutar primero:** Este es el primer script en el orden de ejecución.

---

### 3. `02_insertar_series.sql`
**Descripción:** Inserta 3 series documentales de prueba.

**Datos insertados:**
- PRUEBA - Serie Pensiones de Vejez
- PRUEBA - Serie Pensiones de Invalidez
- PRUEBA - Serie Prestaciones Económicas

**Dependencias:** Requiere que existan secciones (ejecutar `01_insertar_secciones.sql` primero).

---

### 4. `03_insertar_subseries.sql`
**Descripción:** Inserta 4 subseries documentales de prueba.

**Datos insertados:**
- PRUEBA - Subserie Trámites Iniciales Vejez
- PRUEBA - Subserie Renovaciones Vejez
- PRUEBA - Subserie Evaluaciones Médicas Invalidez
- PRUEBA - Subserie Subsidios Económicos

**Dependencias:** Requiere que existan series (ejecutar `02_insertar_series.sql` primero).

---

### 5. `04_insertar_inventarios.sql`
**Descripción:** Inserta 5 inventarios documentales de prueba con diferentes estados.

**Datos insertados:**
- `EXP-TEST-001`: Archivo activo, estado "Registrado"
- `EXP-TEST-002`: Archivo pasivo, estado "Registrado"
- `EXP-TEST-003`: Con RUC, estado "Registrado"
- `EXP-TEST-004`: Estado "Pendiente de Aprobación" (dentro de 5 días)
- `EXP-TEST-005`: Estado "Aprobado"

**Dependencias:** Requiere que existan subseries (ejecutar `03_insertar_subseries.sql` primero).

---

## 🚀 Cómo Ejecutar

### Opción 1: Ejecutar todos los scripts en orden

```bash
# Conectarse a la base de datos
sqlplus DOCUMENTAL_OWNER/DOC87desa@192.168.29.208:1539/PDBIESS_DESA

# Ejecutar scripts en orden
@01_insertar_secciones.sql
@02_insertar_series.sql
@03_insertar_subseries.sql
@04_insertar_inventarios.sql
```

### Opción 2: Ejecutar desde SQL Developer o DBeaver

1. Abrir cada archivo `.sql`
2. Ejecutar en orden: 01 → 02 → 03 → 04
3. Verificar que cada script muestre los datos insertados

### Opción 3: Ejecutar desde línea de comandos

```bash
# Ejecutar script individual
sqlplus DOCUMENTAL_OWNER/DOC87desa@192.168.29.208:1539/PDBIESS_DESA @01_insertar_secciones.sql
```

---

## ✅ Verificación

Después de ejecutar los scripts, verificar que los datos se insertaron correctamente:

```sql
-- Verificar secciones
SELECT COUNT(*) AS SECCIONES FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%';

-- Verificar series
SELECT COUNT(*) AS SERIES FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
WHERE NOM_SERIES LIKE '%PRUEBA%' OR NOM_SERIES LIKE '%TEST%';

-- Verificar subseries
SELECT COUNT(*) AS SUBSERIES FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T 
WHERE NOM_SUBSERIE LIKE '%PRUEBA%' OR NOM_SUBSERIE LIKE '%TEST%';

-- Verificar inventarios
SELECT COUNT(*) AS INVENTARIOS FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T 
WHERE NUM_EXPEDIENTE LIKE 'EXP-TEST-%' OR NUM_EXPEDIENTE LIKE 'TEST-%';
```

**Resultados esperados:**
- Secciones: 3
- Series: 3
- Subseries: 4
- Inventarios: 5

---

## 🔍 Consultas de Prueba

### Consultar inventarios de prueba

```sql
SELECT 
    I.ID_INVENTARIO,
    I.NUM_EXPEDIENTE,
    I.NOMBRES_APELLIDOS,
    I.RAZON_SOCIAL,
    I.ESTADO_INVENTARIO,
    SEC.NOM_SECCION AS SECCION,
    S.NOM_SERIES AS SERIE,
    SS.NOM_SUBSERIE AS SUBSERIE
FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T I
JOIN DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC ON I.ID_SECCION = SEC.ID_SECCION
JOIN DOCUMENTAL_OWNER.GDOC_SERIES_T S ON I.ID_SERIE = S.ID_SERIE
JOIN DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS ON I.ID_SUBSERIE = SS.ID_SUBSERIE
WHERE I.NUM_EXPEDIENTE LIKE 'EXP-TEST-%'
ORDER BY I.ID_INVENTARIO;
```

### Consultar series y subseries

```sql
SELECT 
    S.ID_SERIE,
    S.NOM_SERIES,
    SEC.NOM_SECCION AS SECCION,
    COUNT(SS.ID_SUBSERIE) AS CANT_SUBSERIES
FROM DOCUMENTAL_OWNER.GDOC_SERIES_T S
JOIN DOCUMENTAL_OWNER.GDOC_SECCIONES_TP SEC ON S.ID_SECCION = SEC.ID_SECCION
LEFT JOIN DOCUMENTAL_OWNER.GDOC_SUBSERIES_T SS ON S.ID_SERIE = SS.ID_SERIE
WHERE S.NOM_SERIES LIKE '%PRUEBA%'
GROUP BY S.ID_SERIE, S.NOM_SERIES, SEC.NOM_SECCION
ORDER BY S.ID_SERIE;
```

---

## 🧹 Limpieza

Para eliminar todos los datos de prueba:

```sql
@00_limpiar_datos_prueba.sql
```

O ejecutar manualmente:

```sql
DELETE FROM DOCUMENTAL_OWNER.GDOC_INVENTARIO_T 
WHERE NUM_EXPEDIENTE LIKE 'EXP-TEST-%' OR NUM_EXPEDIENTE LIKE 'TEST-%';

DELETE FROM DOCUMENTAL_OWNER.GDOC_SUBSERIES_T 
WHERE NOM_SUBSERIE LIKE '%PRUEBA%' OR NOM_SUBSERIE LIKE '%TEST%';

DELETE FROM DOCUMENTAL_OWNER.GDOC_SERIES_T 
WHERE NOM_SERIES LIKE '%PRUEBA%' OR NOM_SERIES LIKE '%TEST%';

DELETE FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%';

COMMIT;
```

---

## 📝 Notas

1. **Secuencias:** Los scripts usan `NEXTVAL` de las secuencias. Si las secuencias no existen, deben crearse primero.

2. **Foreign Keys:** Los scripts usan subconsultas para obtener los IDs de las tablas relacionadas, por lo que es importante ejecutarlos en orden.

3. **Fechas:** Se usan fechas de prueba (2020-2022). Ajustar según necesidades.

4. **Usuarios:**
   - Operador: `1234567890`
   - Supervisor: `0987654321`

5. **IP Equipo:** Se usa `192.168.1.100` como IP de prueba.

---

## 🔐 Seguridad

Estos scripts cumplen con las **reglas de seguridad** establecidas:
- ✅ Solo INSERT de datos de prueba
- ✅ Identificadores claros (`PRUEBA`, `TEST`, `EXP-TEST-`)
- ✅ Usuario de prueba identificable
- ✅ NO modifican estructura de tablas
- ✅ NO afectan datos de producción

---

**Última actualización:** 2026-01-06  
**Mantenido por:** Equipo de Desarrollo Backend

