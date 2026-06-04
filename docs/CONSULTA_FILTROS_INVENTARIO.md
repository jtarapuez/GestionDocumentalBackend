# Consultas de inventario: filtros y mapeo a BD

**Alcance:** `InventarioDocumentalRepository.buscarConFiltros` + adaptador `InventarioDocumentalPersistenceAdapter.buscarConFiltros`.  
**Fase 5:** contrato API ↔ JPQL (Panache) ↔ columnas `GDOC_INVENTARIO_T`.

## Entradas HTTP

| Origen | Uso |
|--------|-----|
| `POST /api/v1/consultas` | Body `ConsultaRequest` (todos los campos opcionales). |
| `GET /api/v1/inventarios` | Query: `idSeccion`, `idSerie`, `idSubserie`, `numeroExpediente`, `estado`, `supervisor`. El resto de filtros va en null en el caso de uso. |

## Tabla de filtros (parámetro → entidad JPA → columna Oracle → operador)

Los nombres de propiedad son los de `InventarioDocumentalEntity` (JPQL).

| Parámetro Java / API | Propiedad JPQL | Columna (`GDOC_INVENTARIO_T`) | Comportamiento |
|----------------------|----------------|--------------------------------|----------------|
| `idSeccion` | `idSeccion` | `ID_SECCION` | `=` |
| `idSerie` | Ver nota **Serie y subseries** | `ID_SERIE` / `ID_SUBSERIE` | `=` y/o `IN` |
| `idSubserie` | `idSubserie` | `ID_SUBSERIE` | `=` |
| `numeroExpediente` | `numeroExpediente` | `NUM_EXPEDIENTE` | `UPPER(...) LIKE UPPER('%' \|\| ? \|\| '%')` |
| `estado` | `estadoInventario` | `ESTADO_INVENTARIO` | `=` |
| `numeroCedula` | `numeroCedula` | `NUM_CEDULA` | `=` |
| `numeroRuc` | `numeroRuc` | `NUM_RUC` | `=` |
| `operador` | `operador` | `OPERADOR` | `=` |
| `nombresApellidos` | `nombresApellidos` | `NOMBRES_APELLIDOS` | `LIKE` case-insensitive (ver abajo) |
| `razonSocial` | `razonSocial` | `RAZON_SOCIAL` | `LIKE` case-insensitive |
| `descripcionSerie` | `descripcionSerie` | `DESCR_SERIE` | `LIKE` case-insensitive |
| `tipoContenedor` | `tipoContenedor` | `TIPO_CONTENEDOR` | `=` |
| `numeroContenedor` | `numeroContenedor` | `NUM_CONTENEDOR` | `=` (solo si no null) |
| `tipoArchivo` | `tipoArchivo` | `TIPO_ARCHIVO` | `=` |
| `fechaDesde` | `fechaDesde` | `FEC_DESDE` | `fechaDesde >= ?` (fecha del **contenido** del expediente) |
| `fechaHasta` | `fechaHasta` | `FEC_HASTA` | `fechaHasta <= ?` |
| `supervisor` | `supervisor` | `SUPERVISOR` | `=` (se aplica `trim()` al valor) |

### Nota: Serie y subseries (`idSerie`)

Si solo viene `idSerie`:

1. El **adaptador** carga los IDs de subseries de esa serie (`SubserieDocumentalRepositoryPort.findBySerie`).
2. El **repositorio** arma un predicado del tipo:  
   `(idSerie = ? OR idSubserie IN (?, ?, ...))`  
   para incluir inventarios ligados por serie o por cualquiera de sus subseries.

Si la serie no tiene subseries en catálogo, la lista de IDs puede quedar vacía y el repositorio filtra solo por `idSerie = ?`.

Si además viene `idSubserie`, se aplica **además** el predicado `idSubserie = ?` (intersección lógica con AND).

## Campo `operador` (semántica 5.3)

- En **API** (`ConsultaRequest.operador` y header `X-Operador-Id` en otros endpoints): se documenta como **cédula** del operador.
- En **BD** (`OPERADOR`): almacena **cédula** (hasta 10 caracteres según entidad), no un ID numérico de usuario.
- El filtro en consulta usa **igualdad exacta** (`operador = ?`). El cliente debe enviar el mismo formato que se guardó (normalmente cédula sin espacios).

## Sin filtros

Si ningún predicado aplica, se ejecuta **`listAll()`** sobre la entidad (todos los inventarios visibles al ORM según configuración).

## Estrategia de tests (5.4)

| Nivel | Qué usa el proyecto | Notas |
|-------|-------------------|--------|
| Integración / regresión | `mvn test` con Quarkus, Testcontainers Oracle (perfil `test`) | Ya en uso; valida arranque y persistencia real. |
| Unitarios de caso de uso | Mocks de `InventarioDocumentalRepositoryPort` | Cubren orquestación sin JPQL. |
| Unitarios del repositorio Panache | Opcional | Requeriría `@QuarkusTest` + BD o stub del `EntityManager`; no obligatorio para Fase 5 si la documentación y el refactor incremental están cubiertos por tests existentes. |

**Decisión:** mantener la batería actual; cualquier test dedicado a JPQL dinámico sería **opcional** y se añadiría si el equipo exige cobertura explícita de `buscarConFiltros`.

## Referencias

- Entidad: `infrastructure/persistence/entity/InventarioDocumentalEntity.java`
- Repositorio: `infrastructure/persistence/InventarioDocumentalRepository.java`
- Adaptador: `infrastructure/persistence/adapter/InventarioDocumentalPersistenceAdapter.java`
