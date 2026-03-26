# Códigos de error de la API (PAS-EST-043)

Respuesta de error típica: `error.code`, `error.message`, y `meta` (`path`, `requestId`, `timestamp`).

| Código HTTP | `error.code` | Significado breve | Acción sugerida (cliente) |
|-------------|----------------|-------------------|---------------------------|
| 400 | `INV_PENDIENTES_VENCIDOS` | El operador tiene inventarios pendientes vencidos | Resolver pendientes antes de registrar otro inventario |
| 400 | `INV_OPERADOR_NO_AUTORIZADO` | Solo el operador creador puede actualizar | Enviar header `X-Operador-Id` del creador |
| 400 | `INV_ACTUALIZACION_*` | Estado/plazo no permite actualizar | Revisar estado y reglas de plazo |
| 400 | `INV_APROBACION_ESTADO_INVALIDO` / `INV_RECHAZO_*` | Aprobar/rechazar en estado incorrecto u observaciones faltantes | Ajustar flujo según estado |
| 400 | `SER_ID_SECCION_REQUERIDO` | Falta `idSeccion` al crear serie | Enviar `idSeccion` en el body |
| 400 | `SER_ID_SECCION_NO_EXISTE` | La sección no existe en BD | Listar secciones: `GET /api/v1/catalogos/secciones` |
| 400 | `SUB_ID_SERIE_REQUERIDO` | Falta `idSerie` al crear subserie | Enviar `idSerie` en el body |
| 400 | `SUB_ID_SERIE_NO_EXISTE` | La serie no existe | Listar series: `GET /api/v1/series` |
| 400 | `VALIDATION_ERROR` | Bean Validation (`@Valid`) | Corregir campos indicados en `details` |
| 404 | `CATALOGO_NOT_FOUND` | Catálogo por código inexistente | Ver códigos con `GET /api/v1/catalogos` |
| 404 | `INVENTARIO_NOT_FOUND` / `SERIE_NOT_FOUND` / `SUBSERIE_NOT_FOUND` | Recurso por id inexistente | Verificar id o listar antes |
| 500 | `INTERNAL_SERVER_ERROR` | Error no clasificado | Soporte: indicar `requestId` de `meta` |
| 501 | `PDF_EXPORT_NOT_IMPLEMENTED` / `EXCEL_EXPORT_NOT_IMPLEMENTED` | Reporte no implementado | No usar hasta versión que lo habilite |

**Mapeo técnico:** `NegocioApiException` y otras excepciones de aplicación se traducen en `interfaces/api/exception/GlobalExceptionMapper.java`. Errores construidos con `StandardResponses` ya incluyen `meta.path` y `meta.requestId`.

**Cambios de contrato:** códigos `INV_*` sustituyen a mensajes genéricos antiguos en inventario; coordinar despliegue con frontend si consumían solo texto libre.
