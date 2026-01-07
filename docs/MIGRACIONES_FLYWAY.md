# 📁 Ubicación de Migraciones de Flyway

## 🗂️ Estructura de Directorios

Las migraciones de Flyway están ubicadas en:

```
gestion-documental-backend/
└── src/
    └── main/
        └── resources/
            └── db/
                └── migration/
                    └── V1__Create_catalogos.sql  ← Aquí está la migración
```

## 📍 Ruta Completa

**Ruta absoluta:**
```
/Users/desarrollo/Documents/IESS/CURSOR/ProyectoGestionDocumental/gestion-documental-backend/src/main/resources/db/migration/V1__Create_catalogos.sql
```

**Ruta relativa desde el proyecto:**
```
src/main/resources/db/migration/V1__Create_catalogos.sql
```

## 🔍 ¿Por qué está en esa ubicación?

Flyway busca automáticamente las migraciones en:
- `src/main/resources/db/migration/` (para proyectos Maven/Gradle)
- Esta es la ubicación estándar que Flyway reconoce

## 📝 Convención de Nombres

Los archivos de migración deben seguir este formato:

```
V{version}__{descripcion}.sql
```

**Ejemplo:**
- `V1__Create_catalogos.sql`
  - `V1` = Versión 1 (primera migración)
  - `__` = Separador obligatorio (doble guión bajo)
  - `Create_catalogos` = Descripción de lo que hace
  - `.sql` = Extensión SQL

## 📋 Migraciones Actuales

### V1__Create_catalogos.sql
- **Ubicación:** `src/main/resources/db/migration/V1__Create_catalogos.sql`
- **Qué hace:** Crea los 6 catálogos base del sistema
  - FORMATO
  - SEGURIDAD
  - ESTADO_SERIE
  - ESTADO_INVENTARIO
  - TIPO_CONTENEDOR
  - TIPO_ARCHIVO
- **Estado:** ✅ Ejecutada

## 🆕 Cómo Agregar Nuevas Migraciones

Si necesitas crear una nueva migración:

1. **Crea un nuevo archivo** en `src/main/resources/db/migration/`
2. **Nómbralo** siguiendo el patrón: `V2__{descripcion}.sql`
3. **Escribe el SQL** necesario
4. **Flyway lo ejecutará automáticamente** al iniciar la aplicación

**Ejemplo:**
```
V2__Create_indices.sql
V3__Add_nuevas_columnas.sql
V4__Insert_datos_iniciales.sql
```

## ⚙️ Configuración de Flyway

La configuración de Flyway está en:

**Archivo:** `src/main/resources/application.properties`

Busca las propiedades que empiezan con `quarkus.flyway.*`:

```properties
# Configuración de Flyway
quarkus.flyway.migrate-at-start=true
quarkus.flyway.locations=db/migration
quarkus.flyway.baseline-on-migrate=true
```

## 🔄 ¿Cuándo se Ejecutan las Migraciones?

Las migraciones se ejecutan automáticamente cuando:

1. **Inicias la aplicación Quarkus** (si `migrate-at-start=true`)
2. **Flyway detecta migraciones nuevas** que no se han ejecutado
3. **Las ejecuta en orden** (V1, V2, V3, etc.)

## 📊 Tabla de Control de Flyway

Flyway crea una tabla en la base de datos para controlar qué migraciones se han ejecutado:

**Tabla:** `flyway_schema_history`

Esta tabla contiene:
- Versión de la migración
- Descripción
- Fecha de ejecución
- Checksum del archivo
- Estado (SUCCESS, FAILED, etc.)

## 🔍 Cómo Verificar Migraciones Ejecutadas

Puedes consultar la tabla de control:

```sql
SELECT * FROM DOCUMENTAL_OWNER.flyway_schema_history
ORDER BY installed_rank;
```

## ⚠️ Importante

1. **No modifiques migraciones ya ejecutadas** - Si cambias V1 después de ejecutarla, Flyway detectará el cambio y puede fallar
2. **Siempre crea nuevas migraciones** - Si necesitas cambios, crea V2, V3, etc.
3. **Orden es importante** - Las migraciones se ejecutan en orden numérico (V1, V2, V3...)
4. **Nombres únicos** - No puede haber dos migraciones con la misma versión

## 📚 Recursos

- **Documentación Flyway:** https://flywaydb.org/documentation/
- **Flyway con Quarkus:** https://quarkus.io/guides/flyway

---

**Última actualización:** 2025-01-07  
**Mantenido por:** Equipo de Desarrollo Backend
