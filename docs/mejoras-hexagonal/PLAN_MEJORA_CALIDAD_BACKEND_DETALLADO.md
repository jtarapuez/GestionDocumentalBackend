# Plan detallado de mejora de calidad y mantenibilidad – Backend

> **Copia versionada en el repo del backend** (`docs/mejoras-hexagonal/`). Los enlaces del cuadro de referencias que apuntan a `../../…` corresponden al monorepo IESS completo; en un clon solo del backend pueden no existir esas rutas. Ver también [README.md](./README.md) de esta carpeta.

**Ubicación canónica (monorepo):** `Documentacion/backend/Mejoras Hexagonal/`  
**Alcance:** únicamente `gestion-documental-backend` (Quarkus).  
**Propósito:** describir **qué** hay que hacer, **dónde** y **cómo validar**, para luego ejecutar el trabajo **por fases** con trazabilidad.  
**Versión documento:** 1.4  

---

## 1. Documentos y normas de referencia (¿en qué nos basamos?)

Rutas relativas **desde esta carpeta** (`Mejoras Hexagonal/`):

| Referencia | Ubicación | Uso |
|------------|-----------|-----|
| **PAS-GUI-047 – Arquitectura hexagonal** | [../../PAS-GUI-047 Guía de Implementación Arquitectura Hexagonal.md](../../PAS-GUI-047%20Guía%20de%20Implementación%20Arquitectura%20Hexagonal.md) | **Guía rectora IESS:** dominio sin JPA; puertos de entrada/salida; adaptadores *driving* (REST) y *driven* (persistencia); wiring CDI. Cualquier refactor debe **mantener o acercarse** a este modelo. |
| **PAS-EST-043 – Estándar Quarkus** | [../../PAS-EST-043 Estandar Quarkus-signed.md](../../PAS-EST-043%20Estandar%20Quarkus-signed.md) y `gestion-documental-backend/docs/` | APIs REST, `ApiResponse`, `meta.path`, `meta.requestId`, manejo de errores, convenciones de capas respecto a presentación. |
| **Análisis del backend** | [../../ANALISIS_BACKEND_COMPLETO.md](../../ANALISIS_BACKEND_COMPLETO.md) | Estado del proyecto, cumplimiento PAS-EST-043/PAS-GUI-047, enlaces a migración y tests. |
| **Migración hexagonal (fases ya definidas)** | [../../Migracion_Backend/](../../Migracion_Backend/) (`01_…`, `02_…`, `00_DEPENDENCIAS…`, `03_…`) | Contexto de lo ya migrado; este plan es **posterior**: pulir mantenibilidad sin deshacer hexagonal. |
| **Validación PAS-EST-043** | `gestion-documental-backend/docs/VALIDACION_PAS_EST_043.md` | Checklist de estándar API y trazabilidad. |
| **Principios SOLID** | Buenas prácticas generales | **S:** responsabilidad única por clase. **O/L/I/D:** interfaces estables, sustitución, segregación, inversión de dependencias (puertos). |
| **Git (convenciones al subir)** | [../../git/COMANDOS_GIT.md](../../git/COMANDOS_GIT.md) | Mensajes `feat` / `fix` / `refactor` / `test` / `docs`; flujo habitual `main` en el repo del backend. |

### 1.1 Reglas derivadas de PAS-GUI-047 (no negociables en refactor)

1. **Dominio** (`domain/model/`): POJOs de negocio **sin** `@Entity` ni dependencias JPA.  
2. **Aplicación:** casos de uso implementan **puertos de entrada**; dependen solo de **puertos de salida** (interfaces), no de repositorios Panache concretos.  
3. **Infraestructura:** entidades JPA en `infrastructure/persistence/entity/`, mappers dominio ↔ JPA, adaptadores que implementan `*RepositoryPort`.  
4. **Interfaces:** controladores REST actúan como *driving adapters*: reciben HTTP, llaman al **puerto de entrada**, devuelven DTOs/`ApiResponse`.  
5. No “colar” lógica de persistencia en el dominio ni en DTOs como sustituto del modelo de dominio.

---

## 2. Línea base actual (resumen técnico)

- **Estructura hexagonal** ya presente: `application/port/in|out`, `usecases`, `infrastructure/persistence/adapter`, `entity`, `mapper`.  
- **Puntos de fricción** para mantenimiento a largo plazo:  
  - Clases muy extensas: `InventarioDocumentalUseCase`, `InventarioDocumentalController`.  
  - Depuración con `System.out.println` en código de producción.  
  - Reglas de negocio y mapeo manual request→dominio **mezclados** en métodos largos.  
  - Consulta dinámica en `InventarioDocumentalRepository` (muchas ramas; crece con cada filtro).  
  - Posible código de prueba de conexión en `src/main` (evaluar exclusión o traslado a `src/test`).

Este plan **no** sustituye la migración documentada en `Migracion_Backend/`; la **complementa** con trabajo incremental de calidad.

---

## 3. Visión por fases (orden recomendado)

Las fases están numeradas para **dependencias**: la Fase N+1 asume criterios de salida de N cumplidos.

| Fase | Nombre corto | Objetivo |
|------|----------------|----------|
| **0** | Baseline y gobernanza | Acuerdos, ramas, definición de hecho común. |
| **1** | Observabilidad y ruido | Loggers, retirar `println`, limpieza de utilidades en `main`. |
| **2** | Extracción de mapeo y reglas (inventario) | SRP en el caso de uso crítico. |
| **3** | Controladores delgados | Alinear REST con PAS-GUI-047 / PAS-EST-043. |
| **4** | Errores y contratos | Excepciones/códigos estables para cliente y soporte. |
| **5** | Consultas y repositorio | Documentar y, si aplica, refactorizar filtros. |
| **6** | Revisión y deuda | Tests, cobertura crítica, dependencias. |

---

## 4. Detalle operativo por fase

### Fase 0 – Baseline y gobernanza

**Objetivo:** que todo el equipo ejecute las mismas reglas antes de tocar código.

| # | Tarea | Detalle | Evidencia / criterio de salida |
|---|--------|---------|--------------------------------|
| 0.1 | Aprobar este plan | Versión en `Documentacion/backend/Mejoras Hexagonal/`; changelog al final del archivo si se actualiza. Checklist ejecutable: [TAREAS_CONCRETAS_POR_FASE.md](./TAREAS_CONCRETAS_POR_FASE.md). | Plan versionado en Git. |
| 0.2 | Rama de trabajo | Acordar si mejoras van en `main` o en rama `feature/mejoras-mantenibilidad` con PR. | Documentado en README del backend o wiki interna. |
| 0.3 | Definición de hecho (DoD) por PR | Al menos: `mvn test` sin fallos; sin nuevos `System.out` en `src/main`; sin secretos en commits. | Checklist en plantilla de PR (opcional). |
| 0.4 | Revisar remoto Git | No almacenar tokens en URL de `origin`; usar SSH o credential helper. | `git remote -v` sin credenciales en texto. |

**Dependencias:** ninguna.  
**Riesgo:** bajo.

**Validación incremental:** no posponer todas las comprobaciones al cierre de la fase. Tras versionar el plan en Git (0.1/0.2), ejecutar al menos una vez `mvn test` en `gestion-documental-backend` para fijar **línea base verde** (o documentar fallos preexistentes). Tras acordar rama/PR y DoD, repetir `mvn test` antes de dar por cerrada la Fase 0. Detalle en [TAREAS_CONCRETAS_POR_FASE.md](./TAREAS_CONCRETAS_POR_FASE.md).

---

### Fase 1 – Observabilidad y reducción de ruido

**Objetivo:** diagnóstico profesional y código listo para producción según PAS-EST-043 (trazabilidad vía logs estructurados donde aplique).

| # | Tarea | Archivos / ámbito | Detalle |
|---|--------|-------------------|---------|
| 1.1 | Reemplazar `System.out.println` por logger | `interfaces/api/InventarioDocumentalController.java`, `application/usecases/InventarioDocumentalUseCase.java` | Usar `org.jboss.logging.Logger` o SLF4J. Niveles: `debug`/`trace` para diagnóstico operador/filtros; no loguear datos sensibles completos (PII). |
| 1.2 | Configurar niveles por perfil | `application.properties`, `application-dev.properties`, etc. | En dev: `DEBUG` para paquetes `ec.gob.iess.gestiondocumental`; en prod: `INFO` por defecto. |
| 1.3 | Auditar `TestConexionBD` (u homólogos) | `infrastructure/persistence/TestConexionBD.java` | Si es solo prueba manual: mover a `src/test` o documentar como no empaquetado en artefacto final; evitar URLs/usuarios hardcodeados en `main` sin perfil. |
| 1.4 | Verificar build y tests | Raíz backend | `mvn -q test` (o `mvn verify` si usan integración). |

**Criterio de salida:** cero `System.out.println` en `src/main/java` del paquete productivo (salvo excepción documentada); logs con nivel configurable.  
**Dependencias:** Fase 0.  
**Alineación PAS-GUI-047:** sin cambiar capas; solo infraestructura de observabilidad en adaptadores y aplicación.

---

### Fase 2 – Extracción de mapeo y reglas de negocio (Inventario)

**Objetivo:** cumplir **SRP** y acercar el caso de uso al rol de **orquestador** descrito en PAS-GUI-047 (Application Service).

| # | Tarea | Detalle | Criterio de salida |
|---|--------|---------|---------------------|
| 2.1 | Extraer mapeo `InventarioDocumentalRequest` → `InventarioDocumental` | Nueva clase en `application` (ej. `InventarioDocumentalFactory`, `InventarioRegistroMapper` o similar **sin** depender de JPA). | `registrarInventario` (y similares) delegan en un solo lugar; tests unitarios del mapper/factory con casos borde (archivo pasivo, posición, etc.). |
| 2.2 | Extraer regla **pendientes vencidos** | Clase o método estático en dominio/servicio de dominio: entrada operador/id, salida boolean / resultado. | Use case solo invoca la regla; test unitario de la regla con repositorio mock. |
| 2.3 | Extraer construcción **posición archivo pasivo** | Evitar duplicar lógica `PASIVO` / `Archivo pasivo` y formato `RAC.fila...` dispersos. | Un solo componente testeado; use case delgado. |
| 2.4 | Extraer validación **mismo operador** en actualización | Regla explícita + mensaje de negocio estable. | Tests de autorización operador en actualización. |
| 2.5 | Reducir tamaño de `InventarioDocumentalUseCase` | Objetivo orientativo: métodos < ~50–80 líneas donde sea razonable; clase con responsabilidad clara por método público. | Revisión de par; sin regresión en tests existentes `InventarioDocumentalUseCaseTest`. |

**Dependencias:** Fase 1 recomendada (para depurar con logger durante refactor).  
**Alineación PAS-GUI-047:** reglas preferibles en **dominio** o **application** sin JPA; persistencia sigue en adaptadores.

---

### Fase 3 – Controladores REST delgados (*driving adapters*)

**Objetivo:** que `interfaces/api/*Controller` cumplan el rol de adaptador: HTTP ↔ puerto de entrada.

| # | Tarea | Detalle | Criterio de salida |
|---|--------|---------|---------------------|
| 3.1 | Inventario: revisar métodos > ~40 líneas | Mover lógica a use case o a componentes de aplicación. | Controller solo: parseo de headers/query, llamada a puerto, mapeo a `Response`. |
| 3.2 | Homogeneizar uso de `ApiResponse` | Según `VALIDACION_PAS_EST_043.md` y `RequestContext`. | Respuestas error/success consistentes. |
| 3.3 | OpenAPI | Anotaciones actualizadas si cambian DTOs o códigos. | Documentación Swagger alineada (si el proyecto la publica). |

**Dependencias:** Fase 2 parcial o total (inventario primero; otros controladores después).  
**Riesgo:** medio (contratos API); hacer con tests y pruebas manuales de integración.

---

### Fase 4 – Errores y contratos estables

**Objetivo:** soporte y cliente puedan actuar ante fallos sin depender del texto libre cambiante.

| # | Tarea | Detalle |
|---|--------|---------|
| 4.1 | Inventario de excepciones actuales | `IllegalStateException`, `IllegalArgumentException`, etc. |
| 4.2 | Definir catálogo de códigos | Ej. `INV_OPERADOR_NO_AUTORIZADO`, `INV_PENDIENTES_VENCIDOS`. |
| 4.3 | `GlobalExceptionMapper` o equivalente | Mapear código + mensaje + HTTP status en `ApiResponse`. |
| 4.4 | Documento corto para frontend | Tabla código → significado → acción sugerida (en `docs/` del backend o `Documentacion/`). |

**Dependencias:** Fase 3 recomendada para no duplicar manejo en controlador.  
**Alineación PAS-EST-043:** respuestas de error trazables y estructuradas.

---

### Fase 5 – Consultas (`listarConFiltros`) y repositorio

**Objetivo:** mantenibilidad del SQL dinámico y contrato claro con `POST /v1/consultas`.

| # | Tarea | Detalle | Criterio de salida |
|---|--------|---------|---------------------|
| 5.1 | Documentar cada filtro | Tabla: parámetro API → columna/tabla → comportamiento (LIKE, =, rango fechas). | Archivo en `gestion-documental-backend/docs/` o `Documentacion/`. |
| 5.2 | Tests de integración (opcional) | Con Testcontainers o BD de prueba si el equipo lo usa; si no, tests de repositorio con Panache mock/h2 según política IESS. | Decisión explícita en el documento de la 5.1. |
| 5.3 | Refactor incremental | Extraer construcción de predicados a métodos privados o builder **sin** cambiar comportamiento en una sola PR grande. | PRs pequeños + `mvn test`. |

**Dependencias:** ninguna estricta; puede paralelizarse con Fase 4 si equipos distintos.  
**Nota:** El campo `operador` en BD debe documentarse (id vs cédula) para alinear con frontend y consultas.

---

### Fase 6 – Revisión periódica y deuda técnica

| # | Tarea | Frecuencia sugerida |
|---|--------|---------------------|
| 6.1 | Actualizar dependencias Maven (patch/minor) | Trimestral, con CI. |
| 6.2 | Revisar cobertura en casos de uso críticos | Inventario, aprobación, consulta. |
| 6.3 | Actualizar este plan | Sección “Historial de cambios” abajo. |

---

## 5. Matriz de trazabilidad (fase → artefactos típicos)

| Fase | Paquetes / rutas típicas |
|------|---------------------------|
| 1 | `interfaces/api/*.java`, `application/usecases/*.java`, `src/main/resources/application*.properties` |
| 2 | `application/usecases/InventarioDocumentalUseCase.java`, nuevo código bajo `application/` o `domain/` |
| 3 | `interfaces/api/InventarioDocumentalController.java` (y otros controladores) |
| 4 | `interfaces/api/exception/` o mapper global existente, `docs/` |
| 5 | `infrastructure/persistence/InventarioDocumentalRepository.java`, `ConsultaController.java`, DTO `ConsultaRequest` |
| 6 | `pom.xml`, CI, este documento |

---

## 6. Riesgos y mitigación

| Riesgo | Mitigación |
|--------|------------|
| Regresión en inventarios | Mantener y ampliar `InventarioDocumentalUseCaseTest`; pruebas manuales en dev contra Oracle. |
| Cambio de contrato API | Fase 4 coordinada con frontend; versionado o changelog. |
| PRs demasiado grandes | Fases 2 y 5 en sub-PRs (una regla o un mapper por PR cuando sea posible). |
| Desalineación con PAS-GUI-047 | Revisión de checklist sección 1.1 antes de cada merge grande. |

---

## 7. Historial de cambios del documento

| Versión | Fecha | Autor | Descripción |
|---------|-------|--------|-------------|
| 1.0 | 2026-01-26 | Equipo | Emisión inicial: plan detallado backend, basado en PAS-GUI-047, PAS-EST-043 y análisis existente. |
| 1.1 | 2026-01-26 | Equipo | Traslado a `Documentacion/backend/Mejoras Hexagonal/`; enlaces relativos; índice en README. |
| 1.2 | 2026-01-26 | Equipo | Checklist [TAREAS_CONCRETAS_POR_FASE.md](./TAREAS_CONCRETAS_POR_FASE.md); rutas de archivos en Fase 1; enlace desde sección 8. |
| 1.3 | 2026-01-26 | Equipo | Fase 0: validación incremental (`mvn test` por pasos); alineado con checklist. |
| 1.4 | 2026-03-23 | Equipo | Copia versionada en `gestion-documental-backend/docs/mejoras-hexagonal/`; nota sobre enlaces al monorepo. |

---

## 8. Próximo paso operativo

1. Revisar y aprobar este documento en el equipo.  
2. Usar **[TAREAS_CONCRETAS_POR_FASE.md](./TAREAS_CONCRETAS_POR_FASE.md)** como checklist semanal (o crear **issues** copiando bloques por fase).  
3. Asignar responsables y fechas objetivo por fase.  
4. Ir marcando criterios de salida en cada fase antes de pasar a la siguiente.

---

*Fin del documento.*
