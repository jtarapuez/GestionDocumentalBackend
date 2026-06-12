# Changelog – integración API (contrato frontend / errores)

Cambios relevantes para el **equipo frontend** y consumidores del API (`/api/v1/...`).

## 2026-06-11 – Integrado en `main` (optimización API)

- Merge de `feature/optimizacion-api-performance` a `main`. Documentación detallada: **`docs/CATALOGOS_BOOTSTRAP_Y_CACHE.md`**.

## 2026-06-04 – Optimización rendimiento (rama `feature/optimizacion-api-performance`)

- **Inventarios:** listados sin N+1 (`nombreSeccion`/`nombreSerie`/`nombreSubserie` solo en detalle y altas).
- **Paginación:** `GET /api/v1/inventarios?page=0&size=20` (base 0, máx. 100); respuesta con `meta.totalItems`, `meta.totalPages`, `meta.currentPage`, `meta.pageSize`. Sin `page`/`size` se devuelve la lista completa (retrocompat).
- **Catálogos bootstrap:** `GET /api/v1/catalogos/bootstrap` — una petición con `secciones` + `detallesPorCodigo` (`FORMATO`, `SEGURIDAD`, `ESTADO_INVENTARIO`, `ESTADO_SERIE`, `TIPO_CONTENEDOR`, `TIPO_ARCHIVO`). Swagger: tag Catálogos. Ejemplo JSON: `docs/json/catalogos/11_bootstrap_catalogos.json`.
- **Cache Quarkus:** `quarkus-cache` + Caffeine; caches `catalogo-bootstrap`, `catalogo-secciones`, `catalogo-detalles`; TTL 10 min (`expire-after-write`). Ver `docs/CATALOGOS_BOOTSTRAP_Y_CACHE.md` §3.
- **Infra:** compresión HTTP (`quarkus.http.enable-compression=true`); índices Oracle sugeridos en `docs/INDICES_ORACLE_SUGERIDOS.md` (DBA, pendiente).

## 2026-03-26 – Fase 4.5 coordinación contrato de errores

- **Documentación:** `docs/CODIGOS_ERROR_API.md` (catálogo `error.code` + acciones sugeridas).
- **Respuestas de error:** estructura `ApiResponse` con `error.message`, `error.code`, `meta.path`, `meta.requestId` (PAS-EST-043).
- **Integración monorepo:** `Documentacion/Integracion/NOTA_CONTRATO_ERRORES_API.md`.
- **Frontend:** `template-Kaycloack` — `parseGestionDocumentalApiError` / `getMensajeErrorGestionDocumental` alineados al cuerpo JSON anidado del backend (sin romper mensajes existentes).
