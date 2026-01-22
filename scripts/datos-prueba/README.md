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

### 0. `00_configurar_schema.sql`
**Descripción:** Configura el schema actual de Oracle.

**Cuándo usar:**
- Antes de ejecutar cualquier script si no estás conectado directamente al schema DOCUMENTAL_OWNER

---

### 1. `00_limpiar_datos_prueba.sql`
**Descripción:** Elimina todos los datos de prueba de la base de datos.

**Cuándo usar:**
- Antes de insertar nuevos datos de prueba
- Cuando se necesite limpiar datos de prueba antiguos

**⚠️ Advertencia:** Verificar que solo se eliminen datos de prueba.

---

### 2. `05_insertar_catalogos_secciones.sql`
**Descripción:** Inserta el catálogo de secciones documentales (Áreas IESS) basado en el ANEXO 1 del requerimiento funcional GTI-P02-F02.

**Datos insertados:**
- Catálogo maestro: `SECCIONES_DOC` (código acortado para cumplir límite de 20 caracteres)
- 110 áreas IESS (Direcciones Nacionales, Subdirecciones Nacionales, Coordinaciones, Comités, etc.)

**Ejecutar primero:** Este script debe ejecutarse ANTES de los demás, ya que las secciones documentales pueden referenciar este catálogo.

**Nota:** Este script inserta el catálogo completo de áreas IESS. Las secciones de prueba en `01_insertar_secciones.sql` son independientes.

**Script de verificación:** Ver `05_verificar_catalogos_secciones.sql` para consultar los datos insertados.

---

### 2.1. `05_verificar_catalogos_secciones.sql`
**Descripción:** Script de verificación para consultar el catálogo de secciones documentales insertado.

**Uso:** Ejecutar en Toad, SQL Developer o cualquier cliente SQL para verificar que los datos se insertaron correctamente.

**Contenido:**
- Resumen del catálogo (total de áreas)
- Listado completo de todas las áreas
- Verificación de estados
- Consultas de ejemplo

**Ejecutar:** Puede ejecutarse independientemente después de `05_insertar_catalogos_secciones.sql`.

---

### 3. `01_insertar_secciones.sql`
**Descripción:** Inserta 3 secciones documentales de prueba.

**Datos insertados:**
- PRUEBA - Sección Pensiones
- PRUEBA - Sección Prestaciones
- TEST - Sección Recursos Humanos

**Ejecutar después de:** `05_insertar_catalogos_secciones.sql` (opcional, pero recomendado)

---

### 4. `02_insertar_series.sql`
**Descripción:** Inserta 3 series documentales de prueba.

**Datos insertados:**
- PRUEBA - Serie Pensiones de Vejez
- PRUEBA - Serie Pensiones de Invalidez
- PRUEBA - Serie Prestaciones Económicas

**Dependencias:** Requiere que existan secciones (ejecutar `01_insertar_secciones.sql` primero).

---

### 4.1. `02_corregir_nom_series.sql` ⚠️ **NUEVO**
**Descripción:** Script de corrección para actualizar `NOM_SERIES` cuando tiene valores incorrectos (como "3" o números).

**Cuándo usar:**
- Cuando `NOM_SERIES` tiene valores numéricos en lugar de nombres descriptivos
- Cuando los datos se insertaron incorrectamente y necesitan corrección
- Después de detectar que el frontend muestra IDs en lugar de nombres

**Qué hace:**
- Identifica series con `NOM_SERIES = '3'` o valores numéricos
- Actualiza `NOM_SERIES` basándose en `DESCR_SERIE` para generar nombres apropiados
- Usa patrones inteligentes para reconocer tipos de series (Pensiones de Vejez, Invalidez, Prestaciones, etc.)
- Genera nombres descriptivos cuando no hay coincidencia exacta

**Ejecutar:**
```sql
@02_corregir_nom_series.sql
```

**⚠️ Importante:** 
- Este script solo actualiza series que tienen valores numéricos en `NOM_SERIES`
- Verifica los resultados antes de hacer COMMIT
- Puedes hacer ROLLBACK si algo sale mal

---

### 5. `03_insertar_subseries.sql`
**Descripción:** Inserta 4 subseries documentales de prueba.

**Datos insertados:**
- PRUEBA - Subserie Trámites Iniciales Vejez
- PRUEBA - Subserie Renovaciones Vejez
- PRUEBA - Subserie Evaluaciones Médicas Invalidez
- PRUEBA - Subserie Subsidios Económicos

**Dependencias:** Requiere que existan series (ejecutar `02_insertar_series.sql` primero).

---

### 6. `04_insertar_inventarios.sql`
**Descripción:** Inserta 5 inventarios documentales de prueba con diferentes estados.

**Datos insertados:**
- `EXP-TEST-001`: Archivo activo, estado "Registrado"
- `EXP-TEST-002`: Archivo pasivo, estado "Registrado"
- `EXP-TEST-003`: Con RUC, estado "Registrado"
- `EXP-TEST-004`: Estado "Pendiente de Aprobación" (dentro de 5 días)
- `EXP-TEST-005`: Estado "Aprobado"

**Dependencias:** Requiere que existan subseries (ejecutar `03_insertar_subseries.sql` primero).

---

### 6.1. `04_limpiar_pendientes_vencidos.sql` ⚠️ **NUEVO**
**Descripción:** Limpia inventarios de prueba en estado "Pendiente de Aprobación" vencidos (más de 5 días).

**Cuándo usar:**
- Cuando no se pueden registrar nuevos inventarios porque hay pendientes vencidos bloqueando
- Cuando el sistema muestra error: "No se puede registrar nuevo inventario. Tiene registros pendientes de aprobación vencidos"
- Después de que los inventarios de prueba hayan pasado más de 5 días en estado "Pendiente de Aprobación"

**Qué hace:**
- Identifica inventarios de prueba en estado "Pendiente de Aprobación" con más de 5 días
- **OPCIÓN 1 (Recomendada):** Actualiza el estado a "Registrado" para que el operador pueda actualizarlos
- **OPCIÓN 2:** Elimina los inventarios pendientes vencidos (comentada por defecto)

**Ejecutar:**
```sql
@04_limpiar_pendientes_vencidos.sql
```

**⚠️ Importante:** 
- Este script solo afecta datos de prueba (identificados por `EXP-TEST-`, `[PRUEBA]`, usuario `1234567890`)
- Verifica los resultados antes de hacer COMMIT
- Puedes hacer ROLLBACK si algo sale mal

**Nota:** Según el requerimiento funcional, si un operador tiene inventarios pendientes vencidos (más de 5 días), no puede registrar nuevos inventarios hasta que actualice los pendientes. Este script ayuda a limpiar datos de prueba para permitir el desarrollo.

---

### 6.2. `05_actualizar_operadores.sql` ⚠️ **NUEVO**
**Descripción:** Actualiza el campo `OPERADOR` en `GDOC_INVENTARIO_T` de cédulas (ej: "1234567890", "1122334455") a IDs numéricos (ej: "1").

**Cuándo usar:**
- Cuando se implementa el sistema de operadores con IDs numéricos (similar a supervisores)
- Para migrar datos de prueba de cédulas a IDs numéricos
- Después de configurar Keycloak con el atributo `operadorId`

**Qué hace:**
- Muestra el estado actual de los valores de `OPERADOR` antes de actualizar
- Actualiza registros con cédula `"1234567890"` → ID `"1"`
- Actualiza registros con cédula `"1122334455"` → ID `"1"`
- Muestra el estado después de la actualización
- Hace COMMIT automático

**Ejecutar:**
```sql
-- En DBeaver, SQL Developer o cualquier cliente SQL
@05_actualizar_operadores.sql

-- O copiar y pegar el contenido del archivo directamente
```

**⚠️ Importante:** 
- Este script **SOLO actualiza datos de prueba** (cédulas conocidas de prueba)
- Verifica los resultados mostrados en `DBMS_OUTPUT` antes de confirmar
- Si algo sale mal, puedes hacer `ROLLBACK;` antes de que se ejecute el `COMMIT`
- Ejecutar **SOLO en base de datos de desarrollo**

**Mapeo aplicado:**
- `"1234567890"` → `"1"` (legacy)
- `"1122334455"` → `"1"` (operador.sdngd - Carlos Operador)
- `"2233445566"` → `"2"` (operador2.sdngd - Ana Operadora)

**Nota:** Este script es parte de la implementación del sistema de operadores con IDs numéricos, siguiendo el mismo patrón implementado para supervisores.

---

## 🚀 Cómo Ejecutar

### Opción 1: Ejecutar todos los scripts en orden

```bash
# Conectarse a la base de datos
sqlplus DOCUMENTAL_OWNER/DOC87desa@192.168.29.208:1539/PDBIESS_DESA

# Ejecutar scripts en orden
@05_insertar_catalogos_secciones.sql
@01_insertar_secciones.sql
@02_insertar_series.sql
@03_insertar_subseries.sql
@04_insertar_inventarios.sql
```

### Opción 2: Ejecutar desde SQL Developer o DBeaver

1. Abrir cada archivo `.sql`
2. Ejecutar en orden: 05 → 01 → 02 → 03 → 04
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
- Catálogo de Secciones: 1 catálogo con 60+ áreas IESS
- Secciones de prueba: 3
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


