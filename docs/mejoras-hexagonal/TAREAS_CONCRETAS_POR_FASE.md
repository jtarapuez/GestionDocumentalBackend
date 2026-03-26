# Tareas concretas por fase – Backend (hexagonal)

> **Copia versionada en el repo del backend** (`docs/mejoras-hexagonal/`). Gobernanza: [CONTRIBUTING.md](../../CONTRIBUTING.md).

**Ubicación canónica (monorepo):** `Documentacion/backend/Mejoras Hexagonal/`  
**Alcance:** `gestion-documental-backend` (Quarkus).  
**Documento hermano:** [PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md](./PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md) (contexto, criterios de salida y normas).  
**Uso:** checklist operativa; marcar `[x]` al cerrar cada ítem. **No sustituye** el plan: amplía el detalle ejecutable.

**Convención:** cada fase tiene **orden sugerido**. Campos `Responsable` / `Fecha` son opcionales (rellenar en el gestor de tareas o aquí).

**Validación incremental (todas las fases):** no dejar **solo** una gran verificación al cierre. Después de **cada** ítem o de cada PR pequeño: comprobar lo mínimo (p. ej. `mvn test`, arranque local, grep) para detectar regresiones pronto. Al final de la fase se confirma el **criterio de salida** completo, no es la primera vez que se prueba.

---

## Índice de fases

| Fase | Enfoque | Depende de |
|------|---------|------------|
| [0](#fase-0--baseline-y-gobernanza) | Baseline y gobernanza | — |
| [1](#fase-1--observabilidad-y-ruido) | Logs, sin `println`, limpieza `main` | 0 |
| [2](#fase-2--inventario-mapeo-y-reglas) | SRP inventario documental | 1 (recomendado) |
| [3](#fase-3--controladores-delgados) | REST como adaptador | 2 (parcial o total) |
| [4](#fase-4--errores-y-contratos) | Códigos y mapeo global | 3 (recomendado) |
| [5](#fase-5--consultas-y-repositorio) | Filtros / `listarConFiltros` | — (puede paralelizarse con 4) |
| [6](#fase-6--revisión-y-deuda) | Deuda, dependencias, plan vivo | continuación |

---

## Fase 0 – Baseline y gobernanza

**Objetivo:** mismas reglas para todo el equipo antes de tocar código.

**Cómo ir probando de a poco (Fase 0):** esta fase es sobre todo **acuerdos y Git**; igual conviene **validar en cada paso** para no acumular sorpresas al final.

| Paso | Tarea concreta | Hecho | Responsable | Fecha |
|------|----------------|-------|-------------|-------|
| 0.1 | Equipo leyó [PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md](./PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md) y este checklist | [ ] | | |
| ↳ | **Tras 0.1:** anotar dudas/bloqueos en reunión o issue; no hace falta `mvn test` (solo lectura). | [ ] | | |
| 0.2 | Plan versionado en Git (commit con mensaje tipo `docs: plan mejoras hexagonal`) | [x] | | Repo: `gestion-documental-backend` commit fase 0 |
| ↳ | **Tras 0.2:** en `gestion-documental-backend/`, ejecutar `mvn test` una vez para **línea base verde** (documentar si ya fallaba algo antes). | [x] | | 2026-03-23 |
| 0.3 | Acordar rama: `main` directo **o** `feature/mejoras-mantenibilidad` + PR | [x] | | Ver `CONTRIBUTING.md` en repo backend |
| ↳ | **Tras 0.3:** si abren rama nueva, hacer **push** y (opcional) PR solo de docs o vacío para ver que el flujo/CI (si existe) corre; corregir antes de seguir. | [ ] | | |
| 0.4 | Definir **DoD por PR**: `mvn test` OK; sin nuevos `System.out` en `src/main`; sin secretos | [x] | | `CONTRIBUTING.md` |
| ↳ | **Tras 0.4:** correr otra vez `mvn test` y dejar constancia (“baseline DoD = verde en commit ___”). | [x] | | 2026-03-23 |
| 0.5 | (Opcional) Plantilla de PR con checklist DoD | [x] | | `.github/pull_request_template.md` |
| ↳ | **Tras 0.5:** probar la plantilla en un PR real o de prueba (marca ítems, revisión rápida). | [ ] | | |
| 0.6 | `git remote -v` sin usuario/contraseña/token en URL | [x] | | URL HTTPS sin token |
| ↳ | **Tras 0.6:** si hubo que cambiar remoto, `git fetch` y un pull/push de prueba a rama de trabajo. | [x] | | `git fetch origin` OK |

**Criterio de salida fase:** 0.2–0.4 y 0.6 cumplidos (0.5 opcional); **además** al menos una corrida de `mvn test` **después** del commit de documentación (0.2) para tener baseline explícito.

---

## Fase 1 – Observabilidad y ruido

**Objetivo:** logs profesionales; cero ruido de consola en producción.

| # | Tarea concreta | Hecho | Responsable | Fecha |
|---|----------------|-------|-------------|-------|
| 1.1 | Buscar `System.out` en `gestion-documental-backend/src/main/java`: `grep -r "System.out" src/main/java` | [x] | | 2026-03-23 |
| 1.2 | Sustituir por `Logger` (`org.jboss.logging.Logger` o SLF4J) en `InventarioDocumentalController.java` | [x] | | 2026-03-23 |
| 1.3 | Idem en `InventarioDocumentalUseCase.java` (y cualquier otra clase `main` afectada) | [x] | | incl. `TestConexionBD` |
| 1.4 | Revisar niveles: `debug`/`trace` para diagnóstico; **no** loguear PII completa | [x] | | DEBUG sin cédulas completas |
| 1.5 | Ajustar `application.properties` / `application-dev.properties` / prod: paquete `ec.gob.iess.gestiondocumental` → dev `DEBUG`, prod `INFO` por defecto | [x] | | prod: categoría `WARN` |
| 1.6 | Decidir destino de `infrastructure/persistence/TestConexionBD.java`: mover a `src/test`, eliminar del JAR, o documentar exclusión | [x] | | Permanece en `main`; Javadoc |
| 1.7 | Si queda en `main`: sin credenciales/URLs sensibles sin perfil; alinear con tests existentes `TestConexionOracle*.java` | [x] | | Sin URL/usuario en stdout |
| 1.8 | Ejecutar `mvn test` en raíz del backend y corregir regresiones | [x] | | 2026-03-23 |

**Criterio de salida fase:** sin `System.out.println` en `src/main/java` (salvo excepción documentada en README o plan); niveles configurables.

---

## Fase 2 – Inventario: mapeo y reglas

**Objetivo:** `InventarioDocumentalUseCase` como orquestador; reglas y mapeo testeables.

| # | Tarea concreta | Hecho | Responsable | Fecha |
|---|----------------|-------|-------------|-------|
| 2.1 | Localizar mapeo `InventarioDocumentalRequest` → dominio en `InventarioDocumentalUseCase.java` | [x] | | 2026-03-23 |
| 2.2 | Crear componente dedicado en `application/` (ej. `*Mapper` / `*Factory`) **sin** JPA | [x] | | `application/inventario/InventarioDocumentalRegistroMapper` |
| 2.3 | Tests unitarios del mapper: casos normales + bordes (archivo pasivo, posición, campos nulos si aplica) | [x] | | `InventarioDocumentalRegistroMapperTest` |
| 2.4 | Extraer regla **pendientes vencidos** a clase/método reutilizable (dominio o application) | [x] | | `InventarioPendientesRegla` |
| 2.5 | Test unitario de la regla con repositorio mock | [x] | | `InventarioPendientesReglaTest` |
| 2.6 | Unificar lógica **posición archivo pasivo** (`PASIVO`, `Archivo pasivo`, formato `RAC...`) en un solo lugar | [x] | | `ArchivoPasivoPosicion` |
| 2.7 | Tests de la construcción de posición | [x] | | `ArchivoPasivoPosicionTest` |
| 2.8 | Extraer regla **mismo operador** en actualización + mensaje de negocio estable | [x] | | `InventarioOperadorRegla` + `InventarioNegocioMessages` |
| 2.9 | Tests de actualización / operador | [x] | | `InventarioOperadorReglaTest` + use case |
| 2.10 | Revisión de par: métodos públicos legibles; `InventarioDocumentalUseCaseTest` sigue verde; ampliar tests si hace falta | [x] | | `mvn test` OK; caso PASIVO en use case |

**Criterio de salida fase:** use case más corto y delegado; reglas y mapeo con tests; sin romper puertos hexagonales.

---

## Fase 3 – Controladores delgados

**Objetivo:** HTTP ↔ puerto de entrada; sin lógica de negocio en el controller.

| # | Tarea concreta | Hecho | Responsable | Fecha |
|---|----------------|-------|-------------|-------|
| 3.1 | Auditar `InventarioDocumentalController.java`: métodos > ~40 líneas | [x] | | 2026-03-25 |
| 3.2 | Mover lógica a caso de uso o componente de aplicación; dejar parseo HTTP + llamada al puerto | [x] | | `StandardResponses`, `HttpOperadorExtractor`, helpers `ejecutar` / `ejecutarOptional` |
| 3.3 | Revisar otros `interfaces/api/*Controller.java` con el mismo criterio (priorizar los más usados) | [x] | | `Catalogo`, `Serie`, `Subserie`, `Consulta`, `Reporte` + `StandardResponses` |
| 3.4 | Homogeneizar `ApiResponse` + `RequestContext` según `docs/VALIDACION_PAS_EST_043.md` | [x] | | Misma meta path/requestId vía `StandardResponses` en todos los controllers anteriores |
| 3.5 | Actualizar anotaciones OpenAPI si cambian DTOs o códigos HTTP | [ ] | | Sin cambio de contrato; revisar Swagger si se desea más detalle |
| 3.6 | Prueba manual o integración de endpoints tocados (Postman/Swagger) | [ ] | | Inventarios + catálogos + series + consultas + reportes (501) |

**Criterio de salida fase:** controladores como adaptadores delgados; respuestas alineadas al estándar.

---

## Fase 4 – Errores y contratos

**Objetivo:** códigos estables para cliente y soporte.

| # | Tarea concreta | Hecho | Responsable | Fecha |
|---|----------------|-------|-------------|-------|
| 4.1 | Inventariar excepciones lanzadas en `application/` e `interfaces/` (`IllegalStateException`, `IllegalArgumentException`, etc.) | [x] | | 2026-03-26 |
| 4.2 | Definir catálogo de códigos (ej. `INV_OPERADOR_NO_AUTORIZADO`, `INV_PENDIENTES_VENCIDOS`) en doc o enum | [x] | | `InventarioCodigosError`, `SerieCodigosError`, `SubserieCodigosError` + `docs/CODIGOS_ERROR_API.md` |
| 4.3 | Implementar o extender `GlobalExceptionMapper` (o equivalente): código + mensaje + HTTP + `ApiResponse` | [x] | | `NegocioApiException` en mapper; controllers re-lanzan |
| 4.4 | Añadir documento corto para frontend: tabla código → significado → acción (en `gestion-documental-backend/docs/` o `Documentacion/`) | [x] | | `docs/CODIGOS_ERROR_API.md` |
| 4.5 | Coordinar con frontend si cambia forma de error (changelog o versión API) | [ ] | | |

**Criterio de salida fase:** errores predecibles y documentados; menos dependencia del texto libre.

---

## Fase 5 – Consultas y repositorio

**Objetivo:** SQL dinámico mantenible y contrato claro de consultas.

| # | Tarea concreta | Hecho | Responsable | Fecha |
|---|----------------|-------|-------------|-------|
| 5.1 | Listar filtros de `InventarioDocumentalRepository.java` (u homólogo de consultas dinámicas) | [ ] | | |
| 5.2 | Documentar cada filtro: parámetro API → columna → operador (`=`, `LIKE`, rango fechas) en `gestion-documental-backend/docs/` | [ ] | | |
| 5.3 | Documentar semántica de `operador` en BD (id vs cédula) y alinear con frontend | [ ] | | |
| 5.4 | Decidir estrategia de tests: Testcontainers / H2 / mocks (registrar decisión en el mismo doc) | [ ] | | |
| 5.5 | Refactor incremental: extraer predicados a métodos privados o builder **sin** cambiar comportamiento (PR pequeño) | [ ] | | |
| 5.6 | Repetir 5.5 hasta que el repositorio sea legible; `mvn test` tras cada PR | [ ] | | |

**Criterio de salida fase:** documentación de filtros vigente; código más modular sin regresiones.

---

## Fase 6 – Revisión y deuda

**Objetivo:** sostenibilidad en el tiempo.

| # | Tarea concreta | Hecho | Responsable | Fecha |
|---|----------------|-------|-------------|-------|
| 6.1 | Revisión trimestral `pom.xml`: actualizaciones patch/minor con CI verde | [ ] | | |
| 6.2 | Revisar cobertura / tests en casos de uso críticos (inventario, aprobación, consulta) | [ ] | | |
| 6.3 | Actualizar [PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md](./PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md) sección *Historial de cambios* | [ ] | | |
| 6.4 | Actualizar este checklist si cambian fases o rutas de archivos | [ ] | | |

---

## Plantilla rápida por semana (copiar al tablero)

```
Semana __: Fase __
- [ ] Ítems X.Y–X.Z de TAREAS_CONCRETAS_POR_FASE.md
- [ ] PR(s) mergeados + mvn test
- [ ] Notas / bloqueos: ...
```

---

## Historial de cambios

| Versión | Fecha | Descripción |
|---------|-------|-------------|
| 1.0 | 2026-01-26 | Emisión: checklist ejecutable por fases 0–6, rutas concretas al backend. |
| 1.1 | 2026-01-26 | Principio validación incremental en todas las fases; Fase 0 con micro-pasos y `mvn test` tras commits/DoD. |
| 1.2 | 2026-03-23 | Fase 2: paquete `application/inventario` (mapper, reglas pendientes/operador, posición pasivo) + tests unitarios. |
| 1.3 | 2026-03-25 | Fase 3 (parcial): `InventarioDocumentalController` delgado + `interfaces/api/support/*` (`StandardResponses`, extractores HTTP). |
| 1.4 | 2026-03-26 | Fase 3: resto de controllers con `StandardResponses`; `notImplemented` (501); `ADMIN_CEDULA_TEMPORAL` en `RestSecurityPlaceholder`. |
| 1.5 | 2026-03-26 | Fase 4 (parcial): `NegocioApiException` + códigos INV/SER/SUB; validación FK serie-subserie; `CODIGOS_ERROR_API.md`; re-lanzar en catálogo/consulta/series. |

---

*Fin del documento.*
