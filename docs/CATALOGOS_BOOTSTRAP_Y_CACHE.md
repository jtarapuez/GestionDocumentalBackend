# Catálogos bootstrap y cache Quarkus

**Proyecto:** gestion-documental-backend  
**Fecha:** 2026-06-11  
**Estado:** Integrado en `main` (optimización API — punto 2.1 hoja de ruta)

---

## 1. Resumen

El endpoint **`GET /api/v1/catalogos/bootstrap`** agrupa en **una sola petición** los catálogos que el MFE necesita al abrir pantallas (combos, filtros, formularios). Sustituye varias llamadas separadas (`/secciones`, `/formatos`, `/seguridad`, etc.).

El resultado se **cachea en memoria** con **Quarkus Cache + Caffeine** (TTL 10 minutos), reduciendo consultas repetidas a Oracle.

---

## 2. Endpoint bootstrap

| Campo | Valor |
|-------|-------|
| **Método** | `GET` |
| **Ruta** | `/api/v1/catalogos/bootstrap` |
| **Auth** | Según perfil OIDC del ambiente (dev puede estar permisivo) |
| **Swagger UI** | `http://localhost:8080/swagger-ui` → tag **Catálogos** |
| **Controlador** | `CatalogoController.obtenerBootstrap()` |
| **Caso de uso** | `CatalogoUseCase.obtenerBootstrap()` |

### 2.1 Qué devuelve (`data`)

```json
{
  "data": {
    "secciones": [
      {
        "id": 23,
        "nombre": "PRUEBA - Sección Pensiones",
        "descripcion": "...",
        "estadoRegistro": "Creado"
      }
    ],
    "detallesPorCodigo": {
      "FORMATO": [ { "id": 111, "codigo": "FISICO", "descripcion": "...", "estado": "A", "idCatalogo": 3 } ],
      "SEGURIDAD": [ ... ],
      "ESTADO_INVENTARIO": [ ... ],
      "ESTADO_SERIE": [ ... ],
      "TIPO_CONTENEDOR": [ ... ],
      "TIPO_ARCHIVO": [ ... ]
    }
  },
  "meta": {
    "timestamp": "2026-06-11T16:24:08",
    "path": "/v1/catalogos/bootstrap",
    "requestId": "..."
  },
  "error": null
}
```

### 2.2 Catálogos incluidos en `detallesPorCodigo`

Definidos en `CatalogoUseCase.BOOTSTRAP_CATALOGOS`:

| Código | Uso típico en MFE |
|--------|-------------------|
| `FORMATO` | Físico / Digital / Mixto |
| `SEGURIDAD` | Pública / Confidencial / Reservada |
| `ESTADO_INVENTARIO` | Registrado, Pendiente, Aprobado, etc. |
| `ESTADO_SERIE` | Creado, Actualizado |
| `TIPO_CONTENEDOR` | Caja, Carpeta, Legajo, Tomo |
| `TIPO_ARCHIVO` | Activo, Pasivo |

Las **secciones documentales** van en el array `secciones` (tabla `GDOC_SECCIONES_TP`), no dentro de `detallesPorCodigo`.

### 2.3 Antes vs después

| Antes (MFE) | Después (MFE) |
|-------------|---------------|
| 4–7 requests al cargar una pantalla | **1 request** `bootstrap` |
| Latencia acumulada | Una sola ida y vuelta |
| Más carga al backend y Oracle | Cache servidor reutiliza el JSON |

### 2.4 Consumo en el MFE

- Hook: `useCatalogosInventarioBootstrap` / `useCatalogosBootstrap`
- RTK Query: `obtenerCatalogosBootstrap` en `gestionDocumentalApi.ts`
- Pantallas: inventarios, aprobar, consultas, reportes, series-subseries (listado, crear, actualizar)

Los endpoints de conveniencia (`/formatos`, `/seguridad`, etc.) **siguen existiendo** por retrocompatibilidad; el MFE en `main` prioriza bootstrap.

### 2.5 Errores

| HTTP | `error.code` | Cuándo |
|------|--------------|--------|
| 500 | `CATALOGOS_BOOTSTRAP_ERROR` | Fallo inesperado al armar el bootstrap |

---

## 3. Cache Quarkus (Caffeine)

### 3.1 Dependencia

```xml
<artifactId>quarkus-cache</artifactId>
```

(`pom.xml` — extensión oficial Quarkus.)

### 3.2 Caches definidos

| Nombre cache | Método anotado | Qué guarda |
|--------------|----------------|------------|
| `catalogo-bootstrap` | `CatalogoUseCase.obtenerBootstrap()` | Respuesta completa bootstrap |
| `catalogo-secciones` | `CatalogoUseCase.listarSecciones()` | Lista de secciones activas |
| `catalogo-detalles` | `CatalogoUseCase.listarDetallesPorCatalogo(codigo)` | Detalles por código de catálogo |

Anotación en código:

```java
@CacheResult(cacheName = "catalogo-bootstrap")
public CatalogoBootstrapResponse obtenerBootstrap() { ... }
```

### 3.3 Configuración TTL

En `src/main/resources/application.properties`:

```properties
quarkus.cache.caffeine."catalogo-secciones".expire-after-write=10M
quarkus.cache.caffeine."catalogo-detalles".expire-after-write=10M
quarkus.cache.caffeine."catalogo-bootstrap".expire-after-write=10M
```

- **Motor:** Caffeine (en memoria, por instancia JVM).
- **TTL:** 10 minutos tras escribir en cache (`expire-after-write`).
- Tras expirar, la siguiente petición vuelve a Oracle y repuebla el cache.

### 3.4 Comportamiento práctico

1. Primera llamada a `/bootstrap` → consulta BD → guarda en `catalogo-bootstrap`.
2. Llamadas siguientes (≤ 10 min) → respuesta desde memoria (más rápida).
3. `obtenerBootstrap()` internamente usa `listarSecciones()` y `listarDetallesPorCatalogo()`; esos métodos tienen **su propio** cache, así que piezas sueltas también se benefician si otro endpoint las invoca.

### 3.5 Invalidación

Hoy **no hay** `@CacheInvalidate` al crear/actualizar catálogos en BD. Los cambios en catálogos se reflejan como máximo tras **10 minutos** o al **reiniciar** Quarkus.

Si en el futuro se editan catálogos con frecuencia, valorar invalidación explícita en altas/actualizaciones de catálogo.

### 3.6 Compresión HTTP (relacionado)

En el mismo bloque de optimización:

```properties
quarkus.http.enable-compression=true
```

Reduce el tamaño del JSON bootstrap (~3–4 KB sin comprimir) en tránsito hacia el MFE.

---

## 4. Pruebas rápidas

### Swagger UI

1. `http://localhost:8080/swagger-ui`
2. **Catálogos** → `GET /api/v1/catalogos/bootstrap` → **Try it out** → **Execute**
3. Esperado: **200** y `data.secciones` + `data.detallesPorCodigo.FORMATO`

### curl

```bash
curl -s "http://localhost:8080/api/v1/catalogos/bootstrap" | head -c 500
```

### Verificar cache (opcional)

- Dos llamadas seguidas: la segunda suele ser más rápida (misma JVM).
- Tras `mvn quarkus:dev` restart, la primera llamada siempre va a BD.

---

## 5. Referencias en código

| Archivo | Rol |
|---------|-----|
| `CatalogoController.java` | REST `/bootstrap` |
| `CatalogoUseCase.java` | Lógica + `@CacheResult` |
| `CatalogoBootstrapResponse.java` | DTO respuesta |
| `application.properties` | TTL Caffeine + compresión |
| `CHANGELOG_API_INTEGRACION.md` | Contrato para frontend |

---

**Mantenido por:** Equipo de Desarrollo Backend — Gestión Documental IESS
