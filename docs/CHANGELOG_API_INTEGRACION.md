# Changelog – integración API (contrato frontend / errores)

Cambios relevantes para el **equipo frontend** y consumidores del API (`/api/v1/...`).

## 2026-03-26 – Fase 4.5 coordinación contrato de errores

- **Documentación:** `docs/CODIGOS_ERROR_API.md` (catálogo `error.code` + acciones sugeridas).
- **Respuestas de error:** estructura `ApiResponse` con `error.message`, `error.code`, `meta.path`, `meta.requestId` (PAS-EST-043).
- **Integración monorepo:** `Documentacion/Integracion/NOTA_CONTRATO_ERRORES_API.md`.
- **Frontend:** `template-Kaycloack` — `parseGestionDocumentalApiError` / `getMensajeErrorGestionDocumental` alineados al cuerpo JSON anidado del backend (sin romper mensajes existentes).
