# 📋 Resumen de Todos los Endpoints

## 📍 Ubicación de los Endpoints

### 🔍 **Secciones Documentales**
**Controlador:** `CatalogoController`  
**Ruta base:** `/api/v1/catalogos/secciones`

**¿Por qué están en CatalogoController?**
- Las secciones se listan como parte de los catálogos
- No tienen su propio controlador dedicado
- Endpoint: `GET /api/v1/catalogos/secciones`

---

### 📚 **Series Documentales**
**Controlador:** `SerieDocumentalController`  
**Ruta base:** `/api/v1/series`

---

### 📑 **Subseries Documentales**
**Controlador:** `SubserieDocumentalController`  
**Ruta base:** `/api/v1/subseries`

---

## 📊 Endpoints Disponibles

### 🗂️ Secciones (en CatalogoController)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/catalogos/secciones` | Listar todas las secciones |

**Archivo JSON:** `docs/json/secciones/01_listar_secciones.json`

---

### 📚 Series Documentales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/series` | Listar todas las series |
| GET | `/api/v1/series?idSeccion=23` | Listar series filtradas por sección |
| GET | `/api/v1/series/{id}` | Obtener serie por ID |
| GET | `/api/v1/series/{idSerie}/subseries` | Listar subseries de una serie |
| POST | `/api/v1/series` | Crear nueva serie |
| PUT | `/api/v1/series/{id}` | Actualizar serie |

**Archivos JSON:**
- `docs/json/series/01_listar_series.json`
- `docs/json/series/02_obtener_serie_por_id.json`
- `docs/json/series/03_listar_subseries_de_serie.json`

---

### 📑 Subseries Documentales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/subseries` | Listar todas las subseries |
| GET | `/api/v1/subseries?idSerie=7` | Listar subseries filtradas por serie |
| GET | `/api/v1/subseries/{id}` | Obtener subserie por ID |
| GET | `/api/v1/subseries/serie/{idSerie}` | Listar subseries por serie (alternativo) |
| POST | `/api/v1/subseries` | Crear nueva subserie |
| PUT | `/api/v1/subseries/{id}` | Actualizar subserie |

**Archivos JSON:**
- `docs/json/subseries/01_listar_subseries.json`
- `docs/json/subseries/02_obtener_subserie_por_id.json`
- `docs/json/subseries/03_listar_subseries_por_serie.json`

---

## 🧪 Datos de Prueba Disponibles

### Secciones (3)
- ID: 23 - "PRUEBA - Sección Pensiones"
- ID: 24 - "PRUEBA - Sección Prestaciones"
- ID: 25 - "TEST - Sección Recursos Humanos"

### Series (3)
- ID: 7 - "PRUEBA - Serie Pensiones de Vejez" (Sección: 23)
- ID: 8 - "PRUEBA - Serie Pensiones de Invalidez" (Sección: 23)
- ID: 9 - "PRUEBA - Serie Prestaciones Económicas" (Sección: 24)

### Subseries (4)
- ID: 5 - "PRUEBA - Subserie Trámites Iniciales Vejez" (Serie: 7)
- ID: 6 - "PRUEBA - Subserie Renovaciones Vejez" (Serie: 7)
- ID: 7 - "PRUEBA - Subserie Evaluaciones Médicas Invalidez" (Serie: 8)
- ID: 8 - "PRUEBA - Subserie Subsidios Económicos" (Serie: 9)

---

## 🔗 Relación entre Entidades

```
SECCIÓN (ID: 23)
  └── SERIE (ID: 7)
      ├── SUBSERIE (ID: 5)
      └── SUBSERIE (ID: 6)
  └── SERIE (ID: 8)
      └── SUBSERIE (ID: 7)

SECCIÓN (ID: 24)
  └── SERIE (ID: 9)
      └── SUBSERIE (ID: 8)
```

---

## ✅ Endpoints para Probar

### 1. Secciones
```bash
GET http://localhost:8080/api/v1/catalogos/secciones
```

### 2. Series
```bash
# Listar todas
GET http://localhost:8080/api/v1/series

# Filtrar por sección
GET http://localhost:8080/api/v1/series?idSeccion=23

# Obtener por ID
GET http://localhost:8080/api/v1/series/7

# Subseries de una serie
GET http://localhost:8080/api/v1/series/7/subseries
```

### 3. Subseries
```bash
# Listar todas
GET http://localhost:8080/api/v1/subseries

# Filtrar por serie
GET http://localhost:8080/api/v1/subseries?idSerie=7

# Obtener por ID
GET http://localhost:8080/api/v1/subseries/5

# Alternativo: por serie
GET http://localhost:8080/api/v1/subseries/serie/7
```

---

## 📝 Notas Importantes

1. **Secciones están en CatalogoController:**
   - No tienen su propio controlador
   - Endpoint: `/api/v1/catalogos/secciones`
   - Esto es por diseño del sistema

2. **Rutas corregidas:**
   - Series: `/api/v1/series` (corregido de `/api/api/v1/series`)
   - Subseries: `/api/v1/subseries` (corregido de `/api/api/v1/subseries`)

3. **Filtros disponibles:**
   - Series: `?idSeccion=23` (filtrar por sección)
   - Subseries: `?idSerie=7` (filtrar por serie)

4. **Reiniciar servidor:**
   - Después de corregir las rutas, reinicia Quarkus para que los cambios surtan efecto

---

**Última actualización:** 2025-01-07  
**Mantenido por:** Equipo de Desarrollo Backend

