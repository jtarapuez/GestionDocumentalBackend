# 📋 Plan de Desarrollo - Endpoints Faltantes

**Fecha:** 2026-01-06  
**Proyecto:** gestion-documental-backend  
**Objetivo:** Implementar todos los endpoints faltantes según requerimiento GTI-P02-F02

---

## 🎯 Estado Actual

### ✅ Implementado
- Módulo de Catálogos (100%)
- Test de conexión a base de datos
- Estructura DDD correcta
- Configuración Quarkus completa

### ❌ Pendiente de Implementar
- EF-1: Series/Subseries Documentales
- EF-2: Inventarios Documentales
- EF-3: Aprobación de Registros
- EF-4: Consultas y Reportes
- Seguridad con Keycloak
- Validaciones Bean

---

## 📝 Plan de Implementación por Fases

### FASE 1: Validación de Conexión y Base ✅

**Estado:** ✅ COMPLETADO

- [x] Test de conexión automático al iniciar
- [x] Verificar configuración de datasource
- [x] Validar que las tablas existan

---

### FASE 2: Módulo Series/Subseries Documentales (EF-1)

**Prioridad:** ALTA  
**Tiempo estimado:** 2-3 días

#### 2.1 Entidades Domain

**Archivos a crear:**
- `domain/model/SerieDocumental.java`
- `domain/model/SubserieDocumental.java`

**Campos principales:**
- SerieDocumental: id, seccionId, nombreSerie, descripcion, formatoId, seguridadId, normativa, responsableId, estadoId, justificacion
- SubserieDocumental: id, serieId, nombreSubserie, descripcion, formatoId, seguridadId, normativa, responsableId, estadoId, justificacion

#### 2.2 Repositorios Infrastructure

**Archivos a crear:**
- `infrastructure/persistence/SerieDocumentalRepository.java`
- `infrastructure/persistence/SubserieDocumentalRepository.java`

#### 2.3 Casos de Uso Application

**Archivos a crear:**
- `application/usecases/SerieDocumentalUseCase.java`
- `application/usecases/SubserieDocumentalUseCase.java`

**Funcionalidades:**
- Crear serie/subserie
- Actualizar serie/subserie
- Listar series por sección
- Listar subseries por serie
- Generar código alfanumérico automático

#### 2.4 DTOs y Controladores

**Archivos a crear:**
- `interfaces/api/dto/SerieDocumentalRequest.java`
- `interfaces/api/dto/SerieDocumentalResponse.java`
- `interfaces/api/dto/SubserieDocumentalRequest.java`
- `interfaces/api/dto/SubserieDocumentalResponse.java`
- `interfaces/api/SerieDocumentalController.java`
- `interfaces/api/SubserieDocumentalController.java`

#### 2.5 Endpoints REST

```
POST   /api/v1/series                    - Crear serie
PUT    /api/v1/series/{id}               - Actualizar serie
GET    /api/v1/series                    - Listar series (con filtros)
GET    /api/v1/series/{id}               - Obtener serie por ID
GET    /api/v1/series/{serieId}/subseries - Listar subseries de una serie

POST   /api/v1/subseries                 - Crear subserie
PUT    /api/v1/subseries/{id}            - Actualizar subserie
GET    /api/v1/subseries                 - Listar subseries (con filtros)
GET    /api/v1/subseries/{id}            - Obtener subserie por ID
```

#### 2.6 Validaciones

- Campos obligatorios: sección, serie, descripción, responsable, normativa, estado
- Validación de formato: solo letras y tildes (no números ni caracteres especiales)
- Tamaños: serie/subserie 120 caracteres, descripción 250 caracteres
- Relaciones: subserie debe tener serie, serie debe tener sección

---

### FASE 3: Módulo Inventarios Documentales (EF-2)

**Prioridad:** ALTA  
**Tiempo estimado:** 3-4 días

#### 3.1 Entidades Domain

**Archivos a crear:**
- `domain/model/InventarioDocumental.java`
- `domain/model/HistorialEstadoInventario.java` (auditoría)

**Campos principales InventarioDocumental:**
- id, seccionId, serieId, subserieId, numeroExpediente
- numeroCedula, numeroRuc, nombresApellidos, razonSocial
- extremosDesde, extremosHasta, fechaExtremaDesde, fechaExtremaHasta
- cantidadFojas, tipoContenedorId, numeroContenedor
- soporteDocumentoId, tipoArchivoId, posicionArchivoPasivo
- observaciones, supervisorId, operadorId, estadoId
- auditoría: usuarioCreacion, fechaCreacion, usuarioModificacion, fechaModificacion

#### 3.2 Repositorios

**Archivos a crear:**
- `infrastructure/persistence/InventarioDocumentalRepository.java`
- `infrastructure/persistence/HistorialEstadoInventarioRepository.java`

#### 3.3 Casos de Uso

**Archivos a crear:**
- `application/usecases/InventarioDocumentalUseCase.java`

**Funcionalidades:**
- Registrar inventario (OPERADOR)
- Actualizar inventario (solo PENDIENTE_APROBACION)
- Validar control de 5 días calendario
- Bloquear nuevos registros si hay pendientes vencidos
- Auto-completar nombres por cédula
- Auto-completar razón social por RUC

#### 3.4 Servicios de Dominio

**Archivos a crear:**
- `domain/services/ValidacionInventarioService.java` - Validaciones de negocio
- `domain/services/ControlDiasService.java` - Control de 5 días calendario

#### 3.5 Adapters (Integraciones Externas)

**Archivos a crear:**
- `infrastructure/adapters/SRIAdapter.java` - Consulta RUC
- `infrastructure/adapters/UsuarioAdapter.java` - Consulta cédulas

#### 3.6 DTOs y Controladores

**Archivos a crear:**
- `interfaces/api/dto/InventarioDocumentalRequest.java`
- `interfaces/api/dto/InventarioDocumentalResponse.java`
- `interfaces/api/dto/InventarioDocumentalUpdateRequest.java`
- `interfaces/api/InventarioDocumentalController.java`

#### 3.7 Endpoints REST

```
POST   /api/v1/inventarios               - Registrar inventario
PUT    /api/v1/inventarios/{id}           - Actualizar inventario
GET    /api/v1/inventarios                - Consultar inventarios (filtros)
GET    /api/v1/inventarios/{id}           - Obtener inventario por ID
GET    /api/v1/inventarios/pendientes    - Listar pendientes del operador
```

#### 3.8 Validaciones

- Campos obligatorios según requerimiento
- Validación cédula: 10 dígitos + algoritmo
- Validación RUC: 13 dígitos + algoritmo
- Validación posición archivo: RAC.FILA.COLUMNA.POSICION.BODEGA
- Control de 5 días calendario
- Bloqueo si hay pendientes vencidos

---

### FASE 4: Módulo Aprobación de Registros (EF-3)

**Prioridad:** ALTA  
**Tiempo estimado:** 2 días

#### 4.1 Casos de Uso

**Archivos a crear:**
- `application/usecases/AprobacionInventarioUseCase.java`

**Funcionalidades:**
- Aprobar inventario (SUPERVISOR)
- Rechazar inventario (PENDIENTE_APROBACION)
- Cambiar estado
- Registrar auditoría

#### 4.2 DTOs

**Archivos a crear:**
- `interfaces/api/dto/AprobacionRequest.java`
- `interfaces/api/dto/RechazoRequest.java`

#### 4.3 Endpoints REST

```
PUT    /api/v1/inventarios/{id}/aprobar   - Aprobar inventario
PUT    /api/v1/inventarios/{id}/rechazar  - Rechazar inventario
GET    /api/v1/inventarios/pendientes-aprobacion - Listar pendientes de aprobación
```

#### 4.4 Reglas de Negocio

- Solo SUPERVISOR puede aprobar/rechazar
- Estados: REGISTRADO → APROBADO o PENDIENTE_APROBACION
- PENDIENTE_APROBACION → ACTUALIZADO (por operador)
- ACTUALIZADO → APROBADO_CON_MODIFICACIONES
- Auditoría: usuario + fecha en cada cambio

---

### FASE 5: Módulo Consultas y Reportes (EF-4)

**Prioridad:** MEDIA  
**Tiempo estimado:** 3-4 días

#### 5.1 Casos de Uso

**Archivos a crear:**
- `application/usecases/ConsultaInventarioUseCase.java`
- `application/usecases/ReporteInventarioUseCase.java`

#### 5.2 DTOs

**Archivos a crear:**
- `interfaces/api/dto/ConsultaRequest.java`
- `interfaces/api/dto/ConsultaResponse.java`

#### 5.3 Endpoints REST

```
POST   /api/v1/consultas                 - Consulta avanzada con filtros
POST   /api/v1/reportes/exportar-pdf     - Exportar a PDF
POST   /api/v1/reportes/exportar-excel   - Exportar a Excel
```

#### 5.4 Filtros Soportados

- Período (fecha desde/hasta)
- Número de expediente
- Tipo/Número de contenedor
- Sección/Serie/Subserie documental
- Tipo de archivo (Activo/Pasivo)
- Usuario Operador
- Cédula/RUC
- Nombres/Razón social
- Descripción de serie
- Estado

---

### FASE 6: Seguridad con Keycloak (OIDC)

**Prioridad:** ALTA  
**Tiempo estimado:** 2 días

#### 6.1 Dependencias

**Agregar a pom.xml:**
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-oidc</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-security</artifactId>
</dependency>
```

#### 6.2 Configuración

**Agregar a application.properties:**
```properties
quarkus.oidc.auth-server-url=${KEYCLOAK_AUTH_SERVER_URL}
quarkus.oidc.client-id=gestion-documental-api
quarkus.oidc.credentials.secret=${KEYCLOAK_CLIENT_SECRET}
quarkus.oidc.application-type=service
quarkus.oidc.roles.source=realm
quarkus.http.auth.proactive=true
```

#### 6.3 Protección de Endpoints

**Agregar anotaciones:**
- `@RolesAllowed("ADMINISTRADOR_SDNGD")` - Series/Subseries
- `@RolesAllowed("OPERADOR_SDNGD")` - Inventarios (crear/actualizar)
- `@RolesAllowed("SUPERVISOR_SDNGD")` - Aprobación

#### 6.4 Extracción de Usuario

**Crear utilidad:**
- `infrastructure/security/SecurityUtils.java` - Extraer usuario del JWT

---

### FASE 7: Validaciones Bean

**Prioridad:** MEDIA  
**Tiempo estimado:** 1 día

#### 7.1 Validaciones en DTOs

**Agregar a todos los Request DTOs:**
- `@NotNull`, `@NotBlank`, `@Size`, `@Pattern`
- Validaciones personalizadas (cédula, RUC, posición archivo)

#### 7.2 Validaciones de Negocio

**Crear validadores:**
- `domain/services/validators/CedulaValidator.java`
- `domain/services/validators/RUCValidator.java`
- `domain/services/validators/PosicionArchivoValidator.java`

---

## 📅 Cronograma Sugerido

| Fase | Descripción | Días | Prioridad |
|------|------------|------|-----------|
| 1 | Validación Conexión | 0.5 | ✅ COMPLETADO |
| 2 | Series/Subseries | 2-3 | ALTA |
| 3 | Inventarios | 3-4 | ALTA |
| 4 | Aprobación | 2 | ALTA |
| 6 | Seguridad Keycloak | 2 | ALTA |
| 5 | Consultas/Reportes | 3-4 | MEDIA |
| 7 | Validaciones Bean | 1 | MEDIA |

**Total estimado:** 13-16 días

---

## 🎯 Orden de Implementación Recomendado

1. **FASE 1** ✅ - Validación de conexión (COMPLETADO)
2. **FASE 6** - Seguridad Keycloak (implementar primero para proteger endpoints)
3. **FASE 2** - Series/Subseries (base para inventarios)
4. **FASE 3** - Inventarios (módulo principal)
5. **FASE 4** - Aprobación (completa flujo de inventarios)
6. **FASE 7** - Validaciones Bean (mejora calidad)
7. **FASE 5** - Consultas/Reportes (funcionalidad adicional)

---

## 📝 Notas Importantes

### Reglas de Base de Datos
- ⚠️ NO alterar estructura de tablas
- ⚠️ NO modificar privilegios
- ✅ Solo INSERT/UPDATE/DELETE de datos de prueba
- ✅ Siempre usar WHERE específico

### Convenciones de Código
- Seguir estructura DDD establecida
- Usar `ApiResponse<T>` para todas las respuestas
- Manejo de excepciones con `GlobalExceptionMapper`
- Documentación OpenAPI en todos los endpoints

### Testing
- Crear tests unitarios para casos de uso
- Crear tests de integración para endpoints
- Validar reglas de negocio complejas

---

**Última actualización:** 2026-01-06  
**Mantenido por:** Equipo de Desarrollo Backend


