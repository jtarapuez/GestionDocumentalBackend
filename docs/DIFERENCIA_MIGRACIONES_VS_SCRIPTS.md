# 🔄 Migraciones vs Scripts de Inserción: ¿Cuándo usar cada uno?

## 📊 Comparación Rápida

| Aspecto | Migraciones (Flyway) | Scripts de Inserción |
|---------|---------------------|---------------------|
| **Ubicación** | `src/main/resources/db/migration/` | `scripts/datos-prueba/` |
| **Cuándo se ejecuta** | Automáticamente al iniciar la app | Manualmente cuando lo necesites |
| **Propósito** | Estructura y datos esenciales del sistema | Datos de prueba/desarrollo |
| **Control de versión** | Sí, Flyway controla qué se ejecutó | No, tú controlas manualmente |
| **Reversión** | Difícil (requiere nueva migración) | Fácil (DELETE manual) |
| **Ambiente** | Desarrollo, QA, Producción | Solo Desarrollo/Pruebas |

---

## 🎯 ¿Qué son las Migraciones (Flyway)?

### Características:
- ✅ Se ejecutan **automáticamente** cuando inicias la aplicación
- ✅ Flyway **controla** qué migraciones ya se ejecutaron
- ✅ Se ejecutan **una sola vez** (no se repiten)
- ✅ Son **parte del código** de la aplicación
- ✅ Se ejecutan en **todos los ambientes** (desarrollo, QA, producción)

### Ejemplo:
```sql
-- V1__Create_catalogos.sql
-- Este script crea los catálogos BASE del sistema
-- Se ejecuta automáticamente al iniciar la app
INSERT INTO GDOC_CATALOGOS_T (COD_CATALOGO, ...) VALUES ('FORMATO', ...);
```

### ¿Cuándo usar Migraciones?
✅ **Usa migraciones para:**
- Crear catálogos **esenciales** del sistema (FORMATO, SEGURIDAD, etc.)
- Datos que **siempre deben existir** para que el sistema funcione
- Estructura de base de datos (tablas, índices, constraints)
- Datos maestros que son **parte del sistema**

---

## 📝 ¿Qué son los Scripts de Inserción?

### Características:
- ⚙️ Se ejecutan **manualmente** cuando los necesitas
- 👤 **Tú controlas** cuándo y cómo ejecutarlos
- 🔄 Puedes ejecutarlos **múltiples veces** (con cuidado)
- 📁 Están en una carpeta separada (`scripts/datos-prueba/`)
- 🧪 Solo para **desarrollo y pruebas**

### Ejemplo:
```sql
-- 05_insertar_catalogos_secciones.sql
-- Este script inserta 110 áreas IESS para pruebas
-- Lo ejecutas manualmente cuando necesites datos de prueba
INSERT INTO GDOC_CATALOGOSDET_T (COD_CATALOGOSDET, ...) VALUES ('AI', ...);
```

### ¿Cuándo usar Scripts de Inserción?
✅ **Usa scripts de inserción para:**
- Datos de **prueba** (secciones, series, inventarios de prueba)
- Datos que **puedes eliminar** y volver a insertar
- Datos que **no son esenciales** para el funcionamiento del sistema
- Datos que solo necesitas en **desarrollo**

---

## 🤔 ¿Por qué se Usa Migración para los Catálogos Base?

### Razón 1: Son Datos Esenciales
Los catálogos FORMATO, SEGURIDAD, ESTADO_SERIE, etc. son **necesarios** para que el sistema funcione. Sin ellos, la aplicación no puede operar correctamente.

### Razón 2: Deben Existir en Todos los Ambientes
Estos catálogos deben estar en:
- ✅ Desarrollo
- ✅ QA (Calidad)
- ✅ Producción

Con Flyway, **automáticamente** se crean en todos los ambientes.

### Razón 3: Control de Versión
Flyway garantiza que:
- ✅ Solo se ejecutan **una vez**
- ✅ No se duplican datos
- ✅ Se puede rastrear qué se ejecutó y cuándo

### Razón 4: Consistencia
Todos los ambientes tendrán **exactamente los mismos** catálogos base, garantizando consistencia.

---

## 🆚 Comparación Práctica

### Escenario 1: Catálogos Base (FORMATO, SEGURIDAD)

**❌ Si usáramos script de inserción:**
```bash
# Tendrías que ejecutar manualmente en cada ambiente
sqlplus ... @scripts/datos-prueba/insertar_catalogos_base.sql
```
- ❌ Olvidas ejecutarlo en QA → Sistema no funciona
- ❌ Olvidas ejecutarlo en Producción → Sistema no funciona
- ❌ Ejecutas dos veces → Datos duplicados

**✅ Con migración (Flyway):**
```bash
# Se ejecuta automáticamente al iniciar la app
./mvnw quarkus:dev
```
- ✅ Se ejecuta automáticamente en todos los ambientes
- ✅ Flyway controla que no se dupliquen
- ✅ Garantiza que siempre existan

### Escenario 2: Datos de Prueba (110 áreas IESS)

**❌ Si usáramos migración:**
```sql
-- V2__Insert_110_areas_iess.sql
-- Se ejecutaría automáticamente en PRODUCCIÓN también
```
- ❌ Se insertarían datos de prueba en producción
- ❌ No puedes eliminarlos fácilmente
- ❌ No es lo que quieres en producción

**✅ Con script de inserción:**
```bash
# Solo lo ejecutas cuando necesitas datos de prueba
sqlplus ... @scripts/datos-prueba/05_insertar_catalogos_secciones.sql
```
- ✅ Solo en desarrollo
- ✅ Puedes eliminarlos cuando quieras
- ✅ No afecta producción

---

## 📋 Resumen: ¿Cuándo usar cada uno?

### 🟢 Usa Migraciones (Flyway) para:

1. **Catálogos esenciales del sistema**
   - FORMATO, SEGURIDAD, ESTADO_SERIE, etc.
   - Datos que **siempre deben existir**

2. **Estructura de base de datos**
   - Crear tablas, índices, constraints
   - Modificar estructura

3. **Datos maestros críticos**
   - Configuraciones del sistema
   - Valores por defecto necesarios

### 🟡 Usa Scripts de Inserción para:

1. **Datos de prueba**
   - Secciones, series, inventarios de prueba
   - Datos que puedes eliminar

2. **Datos de desarrollo**
   - 110 áreas IESS (catálogo SECCIONES_DOC)
   - Datos que solo necesitas en desarrollo

3. **Datos temporales**
   - Datos que insertas, pruebas y eliminas

---

## 🎯 Caso Especial: SECCIONES_DOC

### ¿Por qué SECCIONES_DOC está en script y no en migración?

**Razón:** Las 110 áreas IESS son:
- ✅ Datos de **referencia** (no esenciales para funcionamiento básico)
- ✅ Pueden **cambiar** con el tiempo
- ✅ Son **muchos datos** (110 registros)
- ✅ Se pueden **agregar más** áreas después

**Si fuera migración:**
- ❌ Se insertarían en producción (no queremos 110 áreas de prueba en prod)
- ❌ Sería difícil actualizar (requeriría nueva migración)
- ❌ No es esencial para el funcionamiento básico

**Con script:**
- ✅ Solo en desarrollo cuando lo necesites
- ✅ Fácil de actualizar (modificas el script)
- ✅ Puedes eliminar y volver a insertar

---

## 🔄 Flujo de Trabajo Recomendado

### Al iniciar un nuevo ambiente:

1. **Flyway ejecuta migraciones automáticamente**
   - Crea catálogos base (FORMATO, SEGURIDAD, etc.)
   - Crea estructura de tablas

2. **Tú ejecutas scripts de datos de prueba (opcional)**
   - Solo si necesitas datos para desarrollo
   - `05_insertar_catalogos_secciones.sql`
   - `01_insertar_secciones.sql`
   - etc.

### En producción:

1. **Flyway ejecuta migraciones automáticamente**
   - Crea catálogos base
   - Crea estructura

2. **NO ejecutas scripts de datos de prueba**
   - Producción no necesita datos de prueba

---

## ✅ Conclusión

| Tipo | Propósito | Ejecución | Ambiente |
|------|-----------|-----------|----------|
| **Migraciones** | Datos esenciales del sistema | Automática | Todos |
| **Scripts** | Datos de prueba/desarrollo | Manual | Solo desarrollo |

**Regla de oro:**
- Si el sistema **no funciona sin esos datos** → Migración
- Si son datos **solo para probar** → Script de inserción

---

**Última actualización:** 2025-01-07  
**Mantenido por:** Equipo de Desarrollo Backend


