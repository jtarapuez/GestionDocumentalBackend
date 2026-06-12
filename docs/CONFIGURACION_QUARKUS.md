# ⚙️ Configuración de Quarkus - Sistema de Gestión Documental

**Proyecto:** Backend del Sistema de Gestión Documental  
**Framework:** Quarkus 3.9.5  
**Estándar:** PAS-EST-043  
**Fecha:** 2026-01-05

---

## 📋 Ubicación del Archivo

```
gestion-documental-backend/
└── src/
    └── main/
        └── resources/
            └── application.properties  ← Archivo de configuración principal
```

**Copia de referencia:** `docs/application.properties`

---

## 🔧 Configuraciones Implementadas

### 1. Información de la Aplicación

```properties
quarkus.application.name=gestion-documental-backend
quarkus.application.version=1.0.0-SNAPSHOT
```

**Descripción:**
- Nombre de la aplicación usado en logs y métricas
- Versión actual del proyecto

**Para Producción:**
- Actualizar la versión según el release
- Ejemplo: `1.0.0`, `1.1.0`, etc.

---

### 2. Configuración del Servidor HTTP

```properties
quarkus.http.port=8080
quarkus.http.host=0.0.0.0
```

**Descripción:**
- **Puerto:** 8080 (puerto estándar para desarrollo)
- **Host:** 0.0.0.0 (acepta conexiones desde cualquier interfaz de red)

**Para Producción:**
- Considerar usar un puerto específico según el entorno
- Host puede ser `0.0.0.0` o una IP específica según necesidades

---

### 3. Configuración de CORS ⚠️

```properties
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:8080,http://127.0.0.1:8080
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
```

**Descripción:**
- Habilita CORS para permitir peticiones desde el navegador
- Orígenes permitidos: localhost y 127.0.0.1 (desarrollo)
- Headers permitidos: necesarios para Swagger UI y peticiones AJAX
- Métodos HTTP: todos los métodos REST estándar

**⚠️ IMPORTANTE - Para Producción:**
```properties
# Configuración de CORS (para producción)
quarkus.http.cors=true
quarkus.http.cors.origins=https://tudominio.com,https://www.tudominio.com
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
quarkus.http.cors.credentials=true  # Solo si necesitas cookies/credenciales
```

**Ver documentación completa:** `docs/SOLUCION_ERROR_CORS.md`

---

### 4. OpenAPI / Swagger

```properties
quarkus.smallrye-openapi.info-title=Sistema de Gestión Documental API
quarkus.smallrye-openapi.info-version=1.0.0
quarkus.smallrye-openapi.info-description=API del Sistema de Gestión Documental del IESS - Backend Quarkus siguiendo el estándar PAS-EST-043
```

**Descripción:**
- Título de la API en la documentación OpenAPI
- Versión de la API
- Descripción detallada de la API

**URLs Disponibles:**
- **Swagger UI:** http://localhost:8080/swagger-ui
- **OpenAPI JSON:** http://localhost:8080/q/openapi
- **OpenAPI YAML:** http://localhost:8080/q/openapi?format=yaml

**Para Producción:**
- Considerar deshabilitar Swagger UI en producción
- O protegerlo con autenticación

---

### 5. Swagger UI

```properties
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/swagger-ui
```

**Descripción:**
- `always-include=true`: Swagger UI siempre disponible (desarrollo)
- `path=/swagger-ui`: Ruta donde se accede a Swagger UI

**Para Producción:**
```properties
# Deshabilitar Swagger UI en producción
quarkus.swagger-ui.always-include=false

# O proteger con autenticación
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/swagger-ui
# + Configuración de seguridad
```

---

### 6. Health Checks

```properties
quarkus.smallrye-health.ui.enable=true
quarkus.smallrye-health.ui.path=/health-ui
```

**Descripción:**
- Habilita la interfaz web para health checks
- Ruta: `/health-ui`

**URLs Disponibles:**
- **Health Check JSON:** http://localhost:8080/q/health
- **Health UI:** http://localhost:8080/health-ui
- **Health Live:** http://localhost:8080/q/health/live
- **Health Ready:** http://localhost:8080/q/health/ready

**Para Producción:**
- Health checks son esenciales para monitoreo
- Considerar proteger `/health-ui` pero mantener `/q/health` público para orquestadores

---

### 7. Logging

```properties
quarkus.log.level=INFO
quarkus.log.category."ec.gob.iess".level=DEBUG
```

**Descripción:**
- Nivel global de log: `INFO`
- Nivel específico para el paquete `ec.gob.iess`: `DEBUG`

**Niveles de Log:**
- `TRACE`: Muy detallado (solo desarrollo)
- `DEBUG`: Información de depuración
- `INFO`: Información general (producción)
- `WARN`: Advertencias
- `ERROR`: Errores

**Para Producción:**
```properties
quarkus.log.level=INFO
quarkus.log.category."ec.gob.iess".level=INFO
# O usar variables de entorno para diferentes entornos
```

---

### 8. Dev Mode

```properties
quarkus.dev.instrumentation.enabled=true
```

**Descripción:**
- Habilita instrumentación para hot reload en modo desarrollo
- Solo aplica cuando se ejecuta con `mvn quarkus:dev`

**Para Producción:**
- Esta propiedad no se usa en producción (solo en dev mode)

---

## 📝 Arreglos y Mejoras Implementadas

### 1. ✅ Error de Compilación - Clase Duplicada

**Problema:**
- Existían dos archivos: `Application.java` y `EjemploApplication.java`
- Ambos contenían la misma clase `EjemploApplication`
- Error: "duplicate class: ec.gob.iess.ejemplo.EjemploApplication"

**Solución:**
- Eliminado `Application.java`
- Mantenido solo `EjemploApplication.java`
- Archivo renombrado correctamente

**Archivos afectados:**
- `src/main/java/ec/gob/iess/ejemplo/Application.java` (eliminado)
- `src/main/java/ec/gob/iess/ejemplo/EjemploApplication.java` (mantenido)

---

### 2. ✅ Error de CORS - Swagger UI

**Problema:**
- Error "Failed to fetch" en Swagger UI
- Configuración CORS demasiado permisiva (`origins=*`)

**Solución:**
- Orígenes específicos: `http://localhost:8080,http://127.0.0.1:8080`
- Headers permitidos explícitos
- Métodos HTTP permitidos explícitos

**Archivos afectados:**
- `src/main/resources/application.properties` (líneas 14-18)

**Ver documentación completa:** `docs/SOLUCION_ERROR_CORS.md`

---

### 3. ✅ Actualización de Dependencias Quarkus 3.9.5

**Problema:**
- Dependencias obsoletas: `quarkus-resteasy-reactive` y `quarkus-resteasy-reactive-jackson`
- Warnings de Maven sobre artefactos relocalizados

**Solución:**
- Actualizado a `quarkus-rest` y `quarkus-rest-jackson`
- Compatible con Quarkus 3.9.5

**Archivos afectados:**
- `pom.xml` (dependencias actualizadas)

---

### 4. ✅ Renombrado del Proyecto

**Cambio:**
- Nombre anterior: `quarkus-ejemplo-simple`
- Nombre nuevo: `gestion-documental-backend`

**Archivos actualizados:**
- `pom.xml` (artifactId, name, description)
- `src/main/resources/application.properties` (quarkus.application.name)
- `README.md` (referencias al nombre del proyecto)

---

## 🚀 Configuración para Diferentes Entornos

### Desarrollo (Actual)

```properties
quarkus.application.name=gestion-documental-backend
quarkus.http.port=8080
quarkus.http.cors.origins=http://localhost:8080,http://127.0.0.1:8080
quarkus.swagger-ui.always-include=true
quarkus.log.level=INFO
quarkus.log.category."ec.gob.iess".level=DEBUG
```

### Calidad / Testing

```properties
quarkus.application.name=gestion-documental-backend
quarkus.http.port=8080
quarkus.http.cors.origins=https://calidad.tudominio.com
quarkus.swagger-ui.always-include=true
quarkus.log.level=INFO
quarkus.log.category."ec.gob.iess".level=INFO
```

### Producción

```properties
quarkus.application.name=gestion-documental-backend
quarkus.application.version=1.0.0
quarkus.http.port=8080
quarkus.http.cors.origins=https://tudominio.com,https://www.tudominio.com
quarkus.swagger-ui.always-include=false
quarkus.log.level=INFO
quarkus.log.category."ec.gob.iess".level=INFO
```

**Recomendación:** Usar perfiles de Maven o variables de entorno para diferentes configuraciones.

---

### 12. Cache de catálogos (Quarkus Cache + Caffeine) — 2026-06

```properties
quarkus.cache.caffeine."catalogo-secciones".expire-after-write=10M
quarkus.cache.caffeine."catalogo-detalles".expire-after-write=10M
quarkus.cache.caffeine."catalogo-bootstrap".expire-after-write=10M
```

**Descripción:**
- Extensión `quarkus-cache` con backend **Caffeine** (memoria local por instancia JVM).
- TTL **10 minutos** tras la última escritura en cada entrada (`expire-after-write`).
- Usado en `CatalogoUseCase` con `@CacheResult` para:
  - `catalogo-bootstrap` → respuesta de `GET /api/v1/catalogos/bootstrap`
  - `catalogo-secciones` → listado de secciones
  - `catalogo-detalles` → detalles por código de catálogo

**Documentación ampliada:** [CATALOGOS_BOOTSTRAP_Y_CACHE.md](./CATALOGOS_BOOTSTRAP_Y_CACHE.md)

**Para producción:**
- Catálogos cambian poco; 10 min suele ser adecuado.
- Si hay varias réplicas, cada nodo tiene su propia cache (no es Redis compartido).
- Tras cambios urgentes en BD, reiniciar instancia o esperar TTL.

---

### 13. Compresión HTTP — 2026-06

```properties
quarkus.http.enable-compression=true
```

**Descripción:** Comprime respuestas JSON (incluido bootstrap) cuando el cliente envía `Accept-Encoding: gzip`. Reduce ancho de banda hacia el MFE.

---

## 📚 Referencias

- **Documentación Quarkus:** https://quarkus.io/guides/
- **Quarkus Configuration Guide:** https://quarkus.io/guides/config
- **Quarkus CORS:** https://quarkus.io/guides/http-reference#cors-filter
- **Quarkus Cache:** https://quarkus.io/guides/cache
- **Bootstrap catálogos:** [CATALOGOS_BOOTSTRAP_Y_CACHE.md](./CATALOGOS_BOOTSTRAP_Y_CACHE.md)
- **Estándar PAS-EST-043:** Documentación interna IESS

---

## ✅ Checklist de Configuración

### Desarrollo
- [x] Puerto HTTP configurado (8080)
- [x] CORS configurado para localhost
- [x] Swagger UI habilitado
- [x] Health checks habilitados
- [x] Logging configurado (DEBUG para desarrollo)
- [x] OpenAPI documentado

### Calidad
- [ ] CORS configurado para dominio de calidad
- [ ] Logging ajustado a INFO
- [ ] Swagger UI habilitado (o protegido)
- [ ] Health checks verificados

### Producción
- [ ] CORS configurado para dominios de producción
- [ ] Swagger UI deshabilitado o protegido
- [ ] Logging ajustado a INFO/WARN
- [ ] Health checks configurados para monitoreo
- [ ] Variables de entorno configuradas
- [ ] Seguridad implementada (Keycloak/OIDC)

---

**Última actualización:** 2026-06-11  
**Mantenido por:** Sistema de Gestión Documental - Backend Team
