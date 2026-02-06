# Validación punto por punto – PAS-EST-043 (backend sin Keycloak)

Esta guía permite validar cada punto del plan de cumplimiento PAS-EST-043 mediante **pruebas automatizadas** (donde aplica) y **revisión manual** (checklist).

---

## 1. Meta con path y requestId (trazabilidad)

### 1.0 ¿Qué se está validando en el Paso 1? (detalle)

El estándar **PAS-EST-043** exige que cada respuesta de la API incluya un **requestId** (o traceId) y datos de trazabilidad para observabilidad (logs, soporte, correlación entre petición y respuesta).

#### Qué debe tener cada respuesta JSON

Toda respuesta (éxito o error) debe incluir un objeto **`meta`** con al menos:

| Campo        | Significado |
|-------------|-------------|
| **`meta.path`**   | Ruta del recurso llamado (ej. `api/v1/reportes/exportar-pdf`). Sirve para saber qué endpoint generó la respuesta. |
| **`meta.requestId`** | Identificador único de la petición. Si el cliente envía el header `X-Request-Id`, ese valor debe aparecer aquí; si no, el servidor genera uno (p. ej. un UUID). Permite correlacionar logs del cliente y del servidor. |

Ejemplo de respuesta de **error** que cumple el estándar:

```json
{
  "meta": {
    "path": "api/v1/reportes/exportar-pdf",
    "requestId": "a1b2c3d4-5678-90ab-cdef-1234567890ab",
    "timestamp": "2026-02-06T10:15:00"
  },
  "error": {
    "message": "Exportación a PDF no implementada aún...",
    "code": "PDF_EXPORT_NOT_IMPLEMENTED"
  }
}
```

#### Cómo lo implementa el backend (archivos donde está el código)

1. **RequestIdFilter** (filtro JAX-RS): en cada petición lee el header `X-Request-Id`; si no viene, genera un UUID. Además obtiene el path de la petición (`UriInfo.getPath()`). Guarda ambos en **RequestContext** (bean por petición).  
   - **Archivo:** `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/filter/RequestIdFilter.java`

2. **RequestContext** (bean por petición que guarda path y requestId).  
   - **Archivo:** `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/context/RequestContext.java`

3. **ApiResponse**: métodos estáticos `success(data, path, requestId)` y `error(..., path, requestId)` que crean la `Meta` con path y requestId.  
   - **Archivo:** `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/dto/ApiResponse.java`

4. **Controladores**: inyectan `RequestContext` y al devolver respuestas usan los métodos de `ApiResponse` con `requestContext.getPath()` y `requestContext.getRequestId()`.  
   - **Archivos:** `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/` → `SerieDocumentalController.java`, `SubserieDocumentalController.java`, `InventarioDocumentalController.java`, `CatalogoController.java`, `ConsultaController.java`, `ReporteController.java`

5. **GlobalExceptionMapper**: inyecta `RequestContext` y al mapear excepciones a respuesta usa `ApiResponse.error(..., path, requestId)` con el contexto.  
   - **Archivo:** `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/exception/GlobalExceptionMapper.java`

#### Qué hacen exactamente los tests automatizados

Los tests levantan la aplicación en modo test (Quarkus) y envían peticiones HTTP reales a la API. Luego comprueban el **código de estado** y el **JSON de la respuesta**:

| Test | Qué hace la petición | Qué se valida en la respuesta |
|------|------------------------|-------------------------------|
| **metaEnRespuestaErrorIncluyePathYRequestId** | `POST /api/v1/reportes/exportar-pdf` con body `{}` | Código **501** (No implementado). Que existan `meta.path` y `meta.requestId` (no nulos). Que `error.code` sea `PDF_EXPORT_NOT_IMPLEMENTED`. |
| **requestIdPropagadoDesdeHeaderXRequestId** | La misma petición pero con header **`X-Request-Id: test-request-id-12345`** | Código **501**. Que **`meta.requestId` sea exactamente** `test-request-id-12345` (el valor enviado). Que `meta.path` exista. |
| **metaEnRespuestaErrorExcelIncluyePathYRequestId** | `POST /api/v1/reportes/exportar-excel` con body `{}` | Código **501**. Que existan `meta.path` y `meta.requestId`. Que `error.code` sea `EXCEL_EXPORT_NOT_IMPLEMENTED`. |

Se usan los endpoints de **reportes** (exportar PDF/Excel) porque devuelven **501** de forma predecible, sin depender de datos en base de datos; así los tests son rápidos y no necesitan datos de prueba.

**En resumen:** el Paso 1 valida que el filtro y los controladores estén conectados correctamente y que **todas** las respuestas (al menos las de error que probamos) incluyan **path** y **requestId** en **meta**, y que el **X-Request-Id** del cliente se devuelva en **meta.requestId**.

---

### 1.1 Respuestas exitosas y de error incluyen `meta.path` y `meta.requestId`

**Automático (tests):**

```bash
cd gestion-documental-backend
mvn test -Dtest=PasEst043MetaTest
```

Los tests verifican:

- Que las respuestas JSON incluyan `meta.path` y `meta.requestId` (no nulos).
- Que, si se envía el header `X-Request-Id`, el mismo valor aparezca en `meta.requestId`.

**Manual (Postman/curl):**

En este proyecto la API tiene prefijo `@ApplicationPath("/api")`, por tanto las rutas son **`/api/v1/...`**.

1. **Éxito con path y requestId**
   - `GET /api/v1/catalogos` (o cualquier endpoint que devuelva 200).
   - Comprobar en el JSON: `meta.path` (ej. `api/v1/catalogos` o similar) y `meta.requestId` (UUID o el que envíes).

2. **Error con path y requestId**
   - `POST /api/v1/reportes/exportar-pdf` con body `{}`.
   - Respuesta 501: comprobar que existan `meta.path`, `meta.requestId` y `error.message`, `error.code`.

3. **Propagación de X-Request-Id**
   - Misma petición con header: `X-Request-Id: mi-id-123`.
   - Comprobar que `meta.requestId` sea `mi-id-123`.

**Checklist**

| Acción | Resultado |
|--------|-----------|
| Test `metaEnRespuestaErrorIncluyePathYRequestId` pasa | ☐ |
| Test `requestIdPropagadoDesdeHeaderXRequestId` pasa | ☐ |
| Manual: 200 con meta.path y meta.requestId | ☐ |
| Manual: 4xx/5xx con meta.path y meta.requestId | ☐ |

---

## 2. Uso de path/requestId en controladores y GlobalExceptionMapper

**Automático:** cubierto por los mismos tests de meta (las respuestas que se validan salen de los controladores y del mapper).

**Manual (revisión de código):**

- Todos los controladores inyectan `RequestContext` y usan `ApiResponse.success(..., requestContext.getPath(), requestContext.getRequestId())` o el overload de `error` con path/requestId.
- `GlobalExceptionMapper` construye `ApiResponse.error(..., path, requestId)` usando el contexto.

### 2.0 Paso 2 con más detalle: qué revisar en cada archivo

Objetivo: comprobar que **todas** las respuestas (éxito y error) pasan `path` y `requestId` a `ApiResponse`, para que en el JSON siempre aparezca `meta.path` y `meta.requestId`.

#### A) Controladores

En cada controlador (ruta: `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/`):

1. **Que esté inyectado el contexto**
   - Buscar en el archivo: `RequestContext` y algo como `@Inject` + `RequestContext requestContext` (o el nombre del campo que uses).
   - Si no está, el controlador no puede enviar path/requestId.

2. **Que no se use el ApiResponse sin path/requestId**
   - Buscar: `ApiResponse.success(` sin tres argumentos.
   - Debe usarse: `ApiResponse.success(dato, requestContext.getPath(), requestContext.getRequestId())` (o con `meta` ya construida incluyendo path y requestId).
   - Buscar: `ApiResponse.error(` con solo dos argumentos (message, code).
   - Debe usarse: `ApiResponse.error(message, code, requestContext.getPath(), requestContext.getRequestId())` (o el overload con `details` más path y requestId).

3. **Dónde mirar en cada controlador**
   - Al inicio de la clase: inyección de `RequestContext`.
   - En cada método que devuelve `Response`: en los `return` donde se construye `ApiResponse.success(...)` o `ApiResponse.error(...)`.

| Archivo | Qué comprobar |
|---------|----------------|
| `SerieDocumentalController.java` | Campo `RequestContext`; todos los `ApiResponse.success` y `ApiResponse.error` con path y requestId. |
| `SubserieDocumentalController.java` | Igual. |
| `InventarioDocumentalController.java` | Igual (hay muchos returns; revisar todos). |
| `CatalogoController.java` | Igual. |
| `ConsultaController.java` | Igual. |
| `ReporteController.java` | Igual (solo respuestas de error 501). |

#### B) GlobalExceptionMapper

Archivo: `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/exception/GlobalExceptionMapper.java`

1. **Inyección de RequestContext**
   - Debe tener un campo (o parámetro) con `RequestContext` para poder leer `getPath()` y `getRequestId()`.

2. **Uso en cada método toResponse(...)**
   - Donde se construye la respuesta de error no debe usarse `ApiResponse.error(message, code)` a secas.
   - Debe usarse el overload con path y requestId, por ejemplo:  
     `ApiResponse.error(message, code, requestContext.getPath(), requestContext.getRequestId())`  
   - Si el contexto puede ser null (p. ej. si el filtro no ha corrido), el código puede generar un requestId de respaldo; lo importante es que la respuesta lleve siempre `meta` con path y requestId.

#### C) Búsqueda rápida en el proyecto

Para no abrir archivo por archivo:

- Buscar **`ApiResponse.success(`** y comprobar que en controladores siempre haya **tres** argumentos (data, path, requestId) o que usen una `Meta` que ya tenga path y requestId.
- Buscar **`ApiResponse.error(`** y comprobar que en controladores y en `GlobalExceptionMapper` siempre se pasen **path y requestId** (4 argumentos o 5 si hay details).

Cuando en todos los controladores y en el mapper se cumpla eso, el Paso 2 está validado.

**Checklist**

| Controlador / componente | Usa path y requestId en respuestas |
|--------------------------|------------------------------------|
| SerieDocumentalController | ☐ |
| SubserieDocumentalController | ☐ |
| InventarioDocumentalController | ☐ |
| CatalogoController | ☐ |
| ConsultaController | ☐ |
| ReporteController | ☐ |
| GlobalExceptionMapper | ☐ |

---

## 3. Javadoc en DTOs

**Manual (revisión):**

- Cada DTO en `interfaces.api.dto` tiene Javadoc de **clase** (propósito y operación/endpoint donde se usa).
- Los **constructores públicos** tienen Javadoc (y `@param` cuando aplica).

### 3.0 Paso 3 con más detalle: qué revisar en cada DTO

Objetivo: que todas las clases DTO y sus constructores públicos tengan documentación clara (PAS-EST-043 / estándar de documentación).

**Ubicación:** `src/main/java/ec/gob/iess/gestiondocumental/interfaces/api/dto/`

#### Qué debe tener cada DTO

1. **Javadoc de clase (arriba de la clase)**
   - Una o dos frases que digan **para qué sirve** el DTO (entrada o respuesta, qué datos representa).
   - Opcional pero recomendado: **en qué operación se usa**, por ejemplo:
     - `Se utiliza en PUT /v1/inventarios/{id}/aprobar`
     - `Se usa en GET /v1/catalogos y GET /v1/catalogos/{codigo}`
   - Se puede usar `{@code GET /v1/catalogos}` para que se vea como código en el Javadoc generado.

2. **Javadoc de constructores públicos**
   - **Constructor sin argumentos:** por ejemplo `/** Constructor por defecto. */`
   - **Constructor con argumentos:** descripción breve y **@param** por cada parámetro, por ejemplo:
     - `@param id identificador del catálogo`
     - `@param codigo código del catálogo`

3. **Getters/setters**
   - El plan pide Javadoc en métodos públicos “si no son triviales”. Para getters/setters que solo devuelven o asignan un campo, en este proyecto se consideran triviales; no es obligatorio documentarlos. Opcional: una línea con `@return` o `@param` si se quiere.

#### Lista de archivos a revisar

| Archivo | Tipo | Revisar |
|---------|------|--------|
| `AprobacionRequest.java` | Request | Clase + constructor por defecto (+ getter/setter si se documentaron). |
| `CatalogoDetalleResponse.java` | Response | Clase + constructor por defecto + constructor con parámetros (@param). |
| `CatalogoResponse.java` | Response | Clase + ambos constructores. |
| `ConsultaRequest.java` | Request | Clase + constructor por defecto. |
| `InventarioDocumentalRequest.java` | Request | Clase + constructor por defecto. |
| `InventarioDocumentalResponse.java` | Response | Clase + constructor por defecto. |
| `RechazoRequest.java` | Request | Clase + constructor por defecto. |
| `SeccionDocumentalResponse.java` | Response | Clase + ambos constructores. |
| `SerieDocumentalRequest.java` | Request | Clase + constructor por defecto. |
| `SerieDocumentalResponse.java` | Response | Clase + constructor por defecto. |
| `SubserieDocumentalRequest.java` | Request | Clase + constructor por defecto. |
| `SubserieDocumentalResponse.java` | Response | Clase + constructor por defecto. |

**Búsqueda rápida:** en la carpeta `dto`, buscar clases que **no** tengan un bloque `/** ... */` encima de la declaración de la clase, o constructores con parámetros sin `@param`. Esas serían las que faltaría completar.

**Checklist**

| DTO | Javadoc clase | Javadoc constructores |
|-----|----------------|------------------------|
| AprobacionRequest | ☐ | ☐ |
| CatalogoDetalleResponse | ☐ | ☐ |
| CatalogoResponse | ☐ | ☐ |
| ConsultaRequest | ☐ | ☐ |
| InventarioDocumentalRequest | ☐ | ☐ |
| InventarioDocumentalResponse | ☐ | ☐ |
| RechazoRequest | ☐ | ☐ |
| SeccionDocumentalResponse | ☐ | ☐ |
| SerieDocumentalRequest | ☐ | ☐ |
| SerieDocumentalResponse | ☐ | ☐ |
| SubserieDocumentalRequest | ☐ | ☐ |
| SubserieDocumentalResponse | ☐ | ☐ |

---

## 4. Javadoc en repositorios y servicios de persistencia

**Manual (revisión):**

- Cada repositorio en `infrastructure.persistence` tiene Javadoc de **clase** (qué persiste, tabla, criterios).
- Los **métodos públicos** tienen Javadoc con criterios/parámetros.

### 4.0 Paso 4 con más detalle: qué revisar

Objetivo: que las clases de persistencia tengan Javadoc de clase (qué persisten, qué criterios usan) y que los métodos públicos estén documentados.

**Ubicación:** `src/main/java/ec/gob/iess/gestiondocumental/infrastructure/persistence/`

#### Repositorios (Panache)

Para cada repositorio:

1. **Javadoc de clase**
   - Qué entidad persiste (y opcionalmente la tabla, ej. GDOC_CATALOGOS_T).
   - Qué criterios de búsqueda ofrece (por ID, por código, activos, etc.).

2. **Javadoc de métodos públicos**
   - Cada método (findByCodigo, findActivos, findByCatalogoId, etc.) debe tener:
     - Una línea que diga qué hace.
     - `@param` por cada parámetro.
     - `@return` cuando sea relevante.

| Archivo | Revisar |
|---------|--------|
| `CatalogoDetalleRepository.java` | Clase + findByCatalogoId, findActivosByCatalogoId, findByCodigoCatalogo, findActivosByCodigoCatalogo. |
| `CatalogoRepository.java` | Clase + findByCodigo, findActivos, existsByCodigo. |
| `InventarioDocumentalRepository.java` | Clase + findByIdOptional, findByOperador, findPendientesByOperador, findPendientesAprobacion, findByEstado, tienePendientesVencidos, buscarConFiltros. |
| `SeccionDocumentalRepository.java` | Clase + findActivas. |
| `SubserieDocumentalRepository.java` | Clase + findByIdOptional, findBySerie, findActivas, findBySerieAndEstado. |
| `SerieDocumentalRepository.java` | Clase + findByIdOptional, findBySeccion, findActivas, findBySeccionAndEstado. |

#### Servicios de persistencia (opcionales en el plan)

| Archivo | Revisar |
|---------|--------|
| `ConnectionWarmupService.java` | Clase (warm-up del pool) + método que observa StartupEvent (ej. onStart) con @param si aplica. |
| `TestConexionBD.java` | Clase + método testConexion() con @throws si lanza excepción. |

**Búsqueda rápida:** en la carpeta `persistence`, abrir cada clase y comprobar que tenga un bloque `/** ... */` encima de la clase y que cada método público tenga al menos una línea de descripción (y @param / @return donde corresponda).

**Checklist**

| Clase | Javadoc clase | Javadoc métodos públicos |
|-------|----------------|---------------------------|
| CatalogoDetalleRepository | ☐ | ☐ |
| CatalogoRepository | ☐ | ☐ |
| InventarioDocumentalRepository | ☐ | ☐ |
| SeccionDocumentalRepository | ☐ | ☐ |
| SubserieDocumentalRepository | ☐ | ☐ |
| SerieDocumentalRepository | ☐ | ☐ |
| ConnectionWarmupService | ☐ | ☐ |
| TestConexionBD | ☐ | ☐ |

---

## 5. Checkstyle (tras aplicar cambios)

Ejecutar Checkstyle sobre el módulo del backend y corregir solo las reglas que no obliguen a quitar funcionalidad (estilo, nombrado, Javadoc).

**En consola (listado de violaciones):**
```bash
cd gestion-documental-backend
mvn checkstyle:check
```

**Ver informe en el navegador (más amigable):**
```bash
cd gestion-documental-backend
mvn checkstyle:checkstyle
```
Luego abre en el navegador el archivo: **`target/site/checkstyle.html`**  
(En macOS: `open target/site/checkstyle.html` desde la carpeta del backend.)

---

## Resumen de comandos

| Objetivo | Comando |
|----------|---------|
| Tests de meta (path/requestId) | `mvn test -Dtest=PasEst043MetaTest` |
| Todos los tests del backend | `mvn test` |
| Checkstyle | `mvn checkstyle:check` |
