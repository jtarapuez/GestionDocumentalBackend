# Índices Oracle sugeridos — Gestión Documental

Documento de referencia para el **DBA**. El equipo de desarrollo **no** ejecuta `ALTER TABLE` ni crea índices en la BD de desarrollo compartida.

## Tabla `GDOC_INVENTARIO_T` (o equivalente en schema `DOCUMENTAL_OWNER`)

Consultas frecuentes del API:

- Filtro por `OPERADOR` (`listarPendientesPorOperador`)
- Filtro por `SUPERVISOR` (supervisor en listados y aprobación)
- Filtro por `ESTADO_INVENTARIO` (listados y pendientes)
- Filtro por `ID_SECCION` (y cadena sección → serie → subserie)

### Índices recomendados (revisar nombres de columnas en `USER_TAB_COLUMNS`)

| Columna(s) | Motivo |
|------------|--------|
| `OPERADOR` | Listado pendientes operador |
| `SUPERVISOR` | Listado por supervisor asignado |
| `ESTADO_INVENTARIO` | Filtros por estado en GET `/v1/inventarios` |
| `ID_SECCION` | Filtro por sección documental |
| `NUM_EXPEDIENTE` | Búsqueda por expediente (consultas) |

Índice compuesto opcional si el plan de ejecución lo justifica:

```sql
-- Ejemplo: solo referencia para DBA — NO ejecutar desde desarrollo sin autorización
-- CREATE INDEX IDX_GDOC_INV_SUP_EST ON GDOC_INVENTARIO_T (SUPERVISOR, ESTADO_INVENTARIO);
```

## Validación

Tras crear índices en un ambiente de prueba, comparar planes de ejecución (`EXPLAIN PLAN`) para:

- `GET /api/v1/inventarios?supervisor=...&estado=...&page=0&size=20`
- `GET /api/v1/inventarios/pendientes`

## Relacionado

- Optimización API por fases (N+1, paginación, cache catálogos)
- `docs/CONSULTA_FILTROS_INVENTARIO.md`
