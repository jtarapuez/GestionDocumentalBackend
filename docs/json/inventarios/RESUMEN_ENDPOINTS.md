# 📋 Resumen de Endpoints - Inventarios Documentales

## 🎯 Controlador
**Clase:** `InventarioDocumentalController`  
**Path Base:** `/api/v1/inventarios` (corregido de `/api/v1/inventarios` a `/v1/inventarios`)

---

## 📚 Endpoints Disponibles

### 1. **Listar Inventarios** (GET)
```
GET /api/v1/inventarios
```
- **Descripción:** Lista inventarios con filtros opcionales
- **Filtros disponibles:**
  - `idSeccion` (Long)
  - `idSerie` (Long)
  - `idSubserie` (Long)
  - `numeroExpediente` (String)
  - `estado` (String: Registrado, Pendiente, Actualizado, Aprobado)
- **Archivo:** `01_listar_inventarios.json`

### 2. **Obtener Inventario por ID** (GET)
```
GET /api/v1/inventarios/{id}
```
- **Descripción:** Obtiene un inventario específico por su ID
- **Archivo:** `02_obtener_inventario_por_id.json`

### 3. **Listar Pendientes de Aprobación** (GET) - Supervisor
```
GET /api/v1/inventarios/pendientes-aprobacion
```
- **Descripción:** Lista inventarios pendientes de aprobación (para supervisores)
- **Rol requerido:** `SUPERVISOR_SDNGD`
- **Archivo:** `03_listar_pendientes_aprobacion.json`

### 4. **Listar Pendientes del Operador** (GET) - Operador
```
GET /api/v1/inventarios/pendientes
```
- **Descripción:** Lista inventarios pendientes del operador actual
- **Rol requerido:** `OPERADOR_SDNGD`
- **Archivo:** `04_listar_pendientes_operador.json`

### 5. **Crear Inventario** (POST)
```
POST /api/v1/inventarios
```
- **Descripción:** Registra un nuevo inventario documental
- **Rol requerido:** `OPERADOR_SDNGD`
- **Campos requeridos:**
  - `idSeccion`
  - `idSerie`
  - `idSubserie`
  - `numeroExpediente`
- **Validaciones:**
  - No se puede registrar si hay pendientes vencidos (más de 5 días)
- **Archivo:** `05_crear_inventario.json`

### 6. **Actualizar Inventario** (PUT)
```
PUT /api/v1/inventarios/{id}
```
- **Descripción:** Actualiza un inventario existente
- **Rol requerido:** `OPERADOR_SDNGD`
- **Restricciones:**
  - Solo se pueden actualizar inventarios en estado `Registrado`
  - Debe estar dentro de los 5 días calendario desde su creación
- **Archivo:** `06_actualizar_inventario.json`

### 7. **Aprobar Inventario** (PUT) - Supervisor
```
PUT /api/v1/inventarios/{id}/aprobar
```
- **Descripción:** Aprueba un inventario documental
- **Rol requerido:** `SUPERVISOR_SDNGD`
- **Restricciones:**
  - Solo se pueden aprobar inventarios en estado `Registrado` o `Actualizado`
- **Body:** `{ "observaciones": "..." }` (opcional)
- **Archivo:** `07_aprobar_inventario.json`

### 8. **Rechazar Inventario** (PUT) - Supervisor
```
PUT /api/v1/inventarios/{id}/rechazar
```
- **Descripción:** Rechaza un inventario documental
- **Rol requerido:** `SUPERVISOR_SDNGD`
- **Restricciones:**
  - Solo se pueden rechazar inventarios en estado `Registrado` o `Actualizado`
  - Las observaciones son **OBLIGATORIAS**
- **Body:** `{ "observaciones": "..." }` (obligatorio)
- **Archivo:** `08_rechazar_inventario.json`

---

## 🔄 Flujo de Estados

```
Registrado → [Aprobación] → Aprobado
     ↓
Actualizado → [Aprobación] → Aprobado
     ↓
Registrado → [Rechazo] → Pendiente
     ↓
Actualizado → [Rechazo] → Pendiente
```

### Estados Válidos:
- **Registrado:** Inventario recién creado
- **Actualizado:** Inventario modificado por el operador
- **Pendiente:** Inventario rechazado por el supervisor
- **Aprobado:** Inventario aprobado por el supervisor

---

## 📝 Campos del Inventario

### Campos Requeridos (POST):
- `idSeccion` (Long)
- `idSerie` (Long)
- `idSubserie` (Long)
- `numeroExpediente` (String)

### Campos Opcionales:
- `numeroCedula` (String) - Para personas naturales
- `numeroRuc` (String) - Para empresas
- `nombresApellidos` (String) - Para personas naturales
- `razonSocial` (String) - Para empresas
- `descripcionSerie` (String)
- `numeroExtremoDesde` (Integer)
- `numeroExtremoHasta` (Integer)
- `fechaDesde` (LocalDate) - Formato: "YYYY-MM-DD"
- `fechaHasta` (LocalDate) - Formato: "YYYY-MM-DD"
- `cantidadFojas` (Integer)
- `tipoContenedor` (String) - Valores: "Caja", "Carpeta", "Legajo", "Tomo"
- `numeroContenedor` (Integer)
- `soporte` (String) - Valores: "Físico", "Digital", "Mixto"
- `tipoArchivo` (String) - Valores: "Archivo activo", "Archivo pasivo"
- `posicionPasivo` (String) - Formato: "RAC.FILA.COLUMNA.POSICION.BODEGA"
- `numeroRac` (Integer) - Para archivo pasivo
- `numeroFila` (Integer) - Para archivo pasivo
- `numeroColumna` (Integer) - Para archivo pasivo
- `numeroPosicion` (Integer) - Para archivo pasivo
- `bodega` (Integer) - Para archivo pasivo
- `observaciones` (String)
- `supervisor` (String) - Cédula del supervisor

---

## 🔐 Roles y Usuarios Temporales

**Nota:** Hasta implementar Keycloak, se usan usuarios temporales:

- **Operador:** `1234567890` (rol: `OPERADOR_SDNGD`)
- **Supervisor:** `0987654321` (rol: `SUPERVISOR_SDNGD`)

---

## 📊 Datos de Prueba

Los inventarios de prueba tienen:
- **Número de expediente:** `EXP-TEST-XXX` o `TEST-XXX`
- **Operador:** `1234567890`
- **Supervisor:** `0987654321`
- **Observaciones:** Contienen `[PRUEBA]`

---

## ⚠️ Validaciones Importantes

1. **Crear Inventario:**
   - No se puede crear si hay pendientes vencidos (más de 5 días)

2. **Actualizar Inventario:**
   - Solo estado `Registrado`
   - Dentro de 5 días calendario desde creación

3. **Aprobar Inventario:**
   - Solo estados `Registrado` o `Actualizado`
   - Requiere rol `SUPERVISOR_SDNGD`

4. **Rechazar Inventario:**
   - Solo estados `Registrado` o `Actualizado`
   - Observaciones **OBLIGATORIAS**
   - Requiere rol `SUPERVISOR_SDNGD`

---

## 🧪 Orden Sugerido para Probar

1. **GET** `/api/v1/inventarios` - Listar todos
2. **GET** `/api/v1/inventarios/1` - Obtener uno específico
3. **GET** `/api/v1/inventarios?estado=Registrado` - Filtrar por estado
4. **POST** `/api/v1/inventarios` - Crear nuevo inventario
5. **PUT** `/api/v1/inventarios/1` - Actualizar inventario
6. **GET** `/api/v1/inventarios/pendientes-aprobacion` - Ver pendientes (supervisor)
7. **PUT** `/api/v1/inventarios/1/aprobar` - Aprobar inventario (supervisor)
8. **PUT** `/api/v1/inventarios/1/rechazar` - Rechazar inventario (supervisor)

---

## 📁 Archivos JSON Disponibles

Todos los archivos están en: `docs/json/inventarios/`

1. `01_listar_inventarios.json`
2. `02_obtener_inventario_por_id.json`
3. `03_listar_pendientes_aprobacion.json`
4. `04_listar_pendientes_operador.json`
5. `05_crear_inventario.json`
6. `06_actualizar_inventario.json`
7. `07_aprobar_inventario.json`
8. `08_rechazar_inventario.json`

---

## 🔧 Correcciones Aplicadas

- ✅ Path del controlador corregido: `/api/v1/inventarios` → `/v1/inventarios`
- ✅ Todos los endpoints documentados con ejemplos
- ✅ Validaciones y restricciones documentadas
- ✅ Flujo de estados explicado

---

**Última actualización:** 2026-01-07



