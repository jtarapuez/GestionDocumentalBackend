# 📚 Estructura de Catálogos: Catálogo Maestro vs Detalles

## 🎯 Concepto Principal

Los **catálogos** tienen una estructura jerárquica de dos niveles:

1. **Catálogo Maestro** = El tipo/clasificación (ej: FORMATO, SECCIONES_DOC)
2. **Detalles del Catálogo** = Los valores específicos dentro de ese tipo (ej: Físico, Digital, Mixto)

---

## 📊 Ejemplo Visual

```
┌─────────────────────────────────────────────────────────┐
│  CATÁLOGO MAESTRO: FORMATO                               │
│  (El tipo/clasificación)                                 │
├─────────────────────────────────────────────────────────┤
│  Detalles (valores dentro de este catálogo):            │
│  ├── Físico                                              │
│  ├── Digital                                             │
│  └── Mixto                                               │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│  CATÁLOGO MAESTRO: SECCIONES_DOC                        │
│  (El tipo/clasificación)                                 │
├─────────────────────────────────────────────────────────┤
│  Detalles (valores dentro de este catálogo):            │
│  ├── AI - AUDITORÍA INTERNA                             │
│  ├── CNA - COMISIÓN NACIONAL DE APELACIONES             │
│  ├── SDNGD - SUBDIRECCIÓN NACIONAL DE GESTIÓN DOCUMENTAL│
│  ├── ...                                                 │
│  └── (110 áreas IESS en total)                          │
└─────────────────────────────────────────────────────────┘
```

---

## 🔍 Diferencia Clave

| Nivel | Qué es | Ejemplo | Cuántos hay |
|-------|--------|---------|-------------|
| **Catálogo Maestro** | El tipo/clasificación | FORMATO, SECCIONES_DOC | 7 catálogos |
| **Detalles** | Valores dentro del catálogo | Físico, Digital, Mixto | Variable (3, 110, etc.) |

---

## 📋 Ejemplos Prácticos

### Ejemplo 1: Catálogo FORMATO

**Catálogo Maestro:**
- Código: `FORMATO`
- Descripción: "Catálogo de formatos de documentos"

**Detalles del Catálogo (3 valores):**
1. `FISICO` - "Físico"
2. `DIGITAL` - "Digital"
3. `MIXTO` - "Mixto"

### Ejemplo 2: Catálogo SECCIONES_DOC

**Catálogo Maestro:**
- Código: `SECCIONES_DOC`
- Descripción: "Catálogo de secciones documentales - Áreas IESS"

**Detalles del Catálogo (110 valores):**
1. `AI` - "AUDITORÍA INTERNA"
2. `CNA` - "COMISIÓN NACIONAL DE APELACIONES"
3. `SDNGD` - "SUBDIRECCIÓN NACIONAL DE GESTIÓN DOCUMENTAL"
4. ... (107 más)
5. `UPPP` - "UNIDAD PROVINCIAL DE PLANIFICACIÓN PICHINCHA"

---

## 🗄️ Estructura en Base de Datos

### Tabla: `GDOC_CATALOGOS_T` (Catálogos Maestros)

| ID | CODIGO | DESCRIPCION | ESTADO |
|----|--------|-------------|--------|
| 3 | FORMATO | Catálogo de formatos... | A |
| 2 | SECCIONES_DOC | Catálogo de secciones... | A |

**Total: 7 catálogos maestros**

### Tabla: `GDOC_CATALOGOSDET_T` (Detalles de Catálogos)

| ID | CODIGO | DESCRIPCION | ID_CATALOGO |
|----|--------|-------------|-------------|
| 1 | FISICO | Físico | 3 (FORMATO) |
| 2 | DIGITAL | Digital | 3 (FORMATO) |
| 3 | MIXTO | Mixto | 3 (FORMATO) |
| 4 | AI | AUDITORÍA INTERNA | 2 (SECCIONES_DOC) |
| 5 | CNA | COMISIÓN NACIONAL... | 2 (SECCIONES_DOC) |
| ... | ... | ... | ... |

**Total: Variable (3 para FORMATO, 110 para SECCIONES_DOC, etc.)**

---

## 🔗 Relación entre Tablas

```
GDOC_CATALOGOS_T (Catálogo Maestro)
    │
    ├── ID_CATALOGO = 3 (FORMATO)
    │   └── GDOC_CATALOGOSDET_T
    │       ├── FISICO
    │       ├── DIGITAL
    │       └── MIXTO
    │
    └── ID_CATALOGO = 2 (SECCIONES_DOC)
        └── GDOC_CATALOGOSDET_T
            ├── AI
            ├── CNA
            ├── SDNGD
            └── ... (110 en total)
```

---

## 🌐 Endpoints y Qué Retornan

### 1. `GET /api/v1/catalogos`
**Retorna:** Lista de **catálogos maestros** (7 catálogos)

```json
{
  "data": [
    { "codigo": "FORMATO", ... },
    { "codigo": "SECCIONES_DOC", ... },
    { "codigo": "SEGURIDAD", ... }
    // ... 7 en total
  ]
}
```

### 2. `GET /api/v1/catalogos/{codigo}`
**Retorna:** Un **catálogo maestro** específico

```json
{
  "data": {
    "codigo": "SECCIONES_DOC",
    "descripcion": "Catálogo de secciones documentales..."
  }
}
```

### 3. `GET /api/v1/catalogos/{codigo}/detalles`
**Retorna:** Los **detalles** (valores) de un catálogo específico

**Para FORMATO:**
```json
{
  "data": [
    { "codigo": "FISICO", "descripcion": "Físico" },
    { "codigo": "DIGITAL", "descripcion": "Digital" },
    { "codigo": "MIXTO", "descripcion": "Mixto" }
  ]
}
```

**Para SECCIONES_DOC:**
```json
{
  "data": [
    { "codigo": "AI", "descripcion": "AUDITORÍA INTERNA" },
    { "codigo": "CNA", "descripcion": "COMISIÓN NACIONAL..." },
    // ... 110 en total
  ]
}
```

---

## 💡 Analogía Simple

Piensa en los catálogos como **categorías** y los detalles como **opciones dentro de esa categoría**:

### Ejemplo: Menú de Restaurante

**Catálogo: "Bebidas"**
- Detalles: Agua, Refresco, Jugo, Café

**Catálogo: "Postres"**
- Detalles: Helado, Pastel, Flan, Fruta

### En tu Sistema:

**Catálogo: "FORMATO"**
- Detalles: Físico, Digital, Mixto

**Catálogo: "SECCIONES_DOC"**
- Detalles: AI, CNA, SDNGD, ... (110 áreas IESS)

---

## 📊 Resumen de tus Catálogos

| Catálogo Maestro | Cuántos Detalles | Ejemplos de Detalles |
|------------------|------------------|---------------------|
| FORMATO | 3 | Físico, Digital, Mixto |
| SEGURIDAD | 3 | Pública, Confidencial, Reservada |
| ESTADO_SERIE | 2 | Creado, Actualizado |
| ESTADO_INVENTARIO | 4 | Registrado, Pendiente, Actualizado, Aprobado |
| TIPO_CONTENEDOR | 4 | Caja, Carpeta, Legajo, Tomo |
| TIPO_ARCHIVO | 2 | Activo, Pasivo |
| SECCIONES_DOC | **110** | AI, CNA, SDNGD, ... (áreas IESS) |

---

## 🎯 Respuesta a tu Pregunta

**Pregunta:** "Los 110 catálogos o cómo se llaman los que pertenecen a un catálogo"

**Respuesta:**
- Los **7 catálogos maestros** son los tipos (FORMATO, SECCIONES_DOC, etc.)
- Los **110 valores** son los **detalles** del catálogo `SECCIONES_DOC`
- Se llaman **"Detalles del Catálogo"** o **"Valores del Catálogo"**

**Para obtener los 110:**
```
GET /api/v1/catalogos/SECCIONES_DOC/detalles
```

Esto retorna los 110 detalles (áreas IESS) que pertenecen al catálogo SECCIONES_DOC.

---

## 🔍 Consulta SQL para Ver la Relación

```sql
-- Ver catálogo maestro y sus detalles
SELECT 
    C.COD_CATALOGO AS CATALOGO,
    C.DESCRIPCION AS DESCRIPCION_CATALOGO,
    CD.COD_CATALOGOSDET AS CODIGO_DETALLE,
    CD.DESCRIPCION AS DESCRIPCION_DETALLE
FROM DOCUMENTAL_OWNER.GDOC_CATALOGOS_T C
LEFT JOIN DOCUMENTAL_OWNER.GDOC_CATALOGOSDET_T CD 
    ON C.ID_CATALOGO = CD.ID_CATALOGO
WHERE C.COD_CATALOGO = 'SECCIONES_DOC'
ORDER BY CD.COD_CATALOGOSDET;
```

Esto mostrará:
- 1 fila del catálogo maestro SECCIONES_DOC
- 110 filas de sus detalles (áreas IESS)

---

**Última actualización:** 2025-01-07  
**Mantenido por:** Equipo de Desarrollo Backend





