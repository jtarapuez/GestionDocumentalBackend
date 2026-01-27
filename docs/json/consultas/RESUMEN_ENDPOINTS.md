# 📋 Resumen de Endpoints - Consultas y Reportes

## 🎯 Controladores

### ConsultaController
**Path Base:** `/api/v1/consultas` (corregido de `/api/v1/consultas` a `/v1/consultas`)

### ReporteController
**Path Base:** `/api/v1/reportes` (corregido de `/api/v1/reportes` a `/v1/reportes`)

---

## 📚 Endpoints Disponibles

### 1. **Consulta Avanzada** (POST)
```
POST /api/v1/consultas
```
- **Descripción:** Realiza una consulta avanzada de inventarios con múltiples filtros opcionales
- **Body:** `ConsultaRequest` (todos los campos opcionales)
- **Estado:** ✅ **Implementado y funcionando**
- **Archivos:** Separados por tipo de consulta (10 archivos)

### 2. **Exportar a PDF** (POST)
```
POST /api/v1/reportes/exportar-pdf
```
- **Descripción:** Exporta inventarios a PDF según los filtros proporcionados
- **Body:** `ConsultaRequest` (mismo formato que consulta avanzada)
- **Estado:** ⚠️ **NO IMPLEMENTADO** (retorna 501)
- **Archivo:** `01_exportar_pdf.json`

### 3. **Exportar a Excel** (POST)
```
POST /api/v1/reportes/exportar-excel
```
- **Descripción:** Exporta inventarios a Excel según los filtros proporcionados
- **Body:** `ConsultaRequest` (mismo formato que consulta avanzada)
- **Estado:** ⚠️ **NO IMPLEMENTADO** (retorna 501)
- **Archivo:** `02_exportar_excel.json`

---

## 🔍 Filtros Disponibles (ConsultaRequest)

Todos los filtros son **opcionales** y se pueden combinar:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idSeccion` | Long | Filtro por sección |
| `idSerie` | Long | Filtro por serie |
| `idSubserie` | Long | Filtro por subserie |
| `numeroExpediente` | String | Filtro por número de expediente |
| `tipoContenedor` | String | Caja, Carpeta, Legajo, Tomo |
| `numeroContenedor` | Integer | Número de contenedor |
| `tipoArchivo` | String | Archivo activo, Archivo pasivo |
| `operador` | String | Cédula del operador |
| `numeroCedula` | String | Cédula del beneficiario |
| `numeroRuc` | String | RUC de empresa |
| `nombresApellidos` | String | Búsqueda parcial por nombres |
| `razonSocial` | String | Búsqueda parcial por razón social |
| `descripcionSerie` | String | Búsqueda parcial por descripción |
| `estado` | String | Registrado, Pendiente, Actualizado, Aprobado |
| `fechaDesde` | LocalDate | Filtro por fecha desde (YYYY-MM-DD) |
| `fechaHasta` | LocalDate | Filtro por fecha hasta (YYYY-MM-DD) |

---

## 📝 Ejemplos de Uso

### Consulta Avanzada (Funcionando)

**Ejemplo 1: Por sección y serie**
```json
{
  "idSeccion": 23,
  "idSerie": 7
}
```

**Ejemplo 2: Por estado y operador**
```json
{
  "estado": "Registrado",
  "operador": "1234567890"
}
```

**Ejemplo 3: Por rango de fechas**
```json
{
  "fechaDesde": "2020-01-01",
  "fechaHasta": "2020-12-31"
}
```

**Ejemplo 4: Consulta completa**
```json
{
  "idSeccion": 23,
  "idSerie": 7,
  "estado": "Registrado",
  "operador": "1234567890",
  "fechaDesde": "2020-01-01",
  "fechaHasta": "2020-12-31"
}
```

---

## ⚠️ Estado de Implementación

### ✅ Funcionando
- **Consulta Avanzada:** Completamente implementada y funcionando
- Filtros básicos: idSeccion, idSerie, idSubserie, numeroExpediente, estado

### ⚠️ Parcialmente Implementado
- Algunos filtros avanzados están marcados como TODO en el código:
  - `tipoContenedor`
  - `operador`
  - `numeroCedula` / `numeroRuc`
  - `nombresApellidos` / `razonSocial`
  - `descripcionSerie`
  - `fechaDesde` / `fechaHasta`

### ❌ No Implementado
- **Exportar a PDF:** Retorna 501 (Not Implemented)
- **Exportar a Excel:** Retorna 501 (Not Implemented)

---

## 🔧 Para Implementar Exportaciones

### PDF
- **Librería requerida:** iText, Apache PDFBox, o similar
- **Formato:** application/pdf
- **Headers:** Content-Disposition con nombre de archivo

### Excel
- **Librería requerida:** Apache POI
- **Formato:** application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
- **Extensión:** .xlsx (Excel 2007+)
- **Headers:** Content-Disposition con nombre de archivo

---

## 📁 Archivos JSON Disponibles

### Consultas (Separadas por tipo)
- `01_consulta_por_seccion_serie.json` - Por sección, serie y/o subserie
- `02_consulta_por_expediente.json` - Por número de expediente
- `03_consulta_por_estado.json` - Por estado (Registrado, Pendiente, etc.)
- `04_consulta_por_operador.json` - Por operador
- `05_consulta_por_cedula_ruc.json` - Por cédula o RUC
- `06_consulta_por_nombres.json` - Por nombres o razón social
- `07_consulta_por_fechas.json` - Por rango de fechas
- `08_consulta_por_contenedor.json` - Por tipo y número de contenedor
- `09_consulta_por_tipo_archivo.json` - Por tipo de archivo (activo/pasivo)
- `10_consulta_completa.json` - Consulta con múltiples filtros combinados

### Reportes
- `01_exportar_pdf.json`
- `02_exportar_excel.json`

---

## 🔧 Correcciones Aplicadas

- ✅ Path de `ConsultaController` corregido: `/api/v1/consultas` → `/v1/consultas`
- ✅ Path de `ReporteController` corregido: `/api/v1/reportes` → `/v1/reportes`
- ✅ Documentación JSON creada para todos los endpoints

---

**Última actualización:** 2026-01-07






