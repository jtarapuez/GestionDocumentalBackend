# 🧪 Guía para Probar Endpoints de Catálogos

## 📋 Prerequisitos

1. **Servidor Quarkus corriendo:**
   ```bash
   cd gestion-documental-backend
   ./mvnw quarkus:dev
   ```

2. **Base de datos configurada:**
   - Datos de prueba insertados (scripts en `scripts/datos-prueba/`)
   - Catálogo `SECCIONES_DOC` con 110 áreas insertadas

3. **URL base del servidor:**
   - Desarrollo: `http://localhost:8080`
   - Producción: (configurar según ambiente)

## 🚀 Probar Endpoints

### 1. Listar Todos los Catálogos

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos' \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Lista de 7 catálogos (FORMATO, SEGURIDAD, ESTADO_SERIE, ESTADO_INVENTARIO, TIPO_CONTENEDOR, TIPO_ARCHIVO, SECCIONES_DOC)
- Cada catálogo con: `id`, `codigo`, `descripcion`, `estado`, `observacion`

---

### 2. Obtener Catálogo por Código

```bash
# Catálogo existente
curl -X GET 'http://localhost:8080/api/v1/catalogos/SECCIONES_DOC' \
  -H 'Content-Type: application/json' | jq

# Catálogo inexistente (debe retornar 404)
curl -X GET 'http://localhost:8080/api/v1/catalogos/CATALOGO_INEXISTENTE' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK` (si existe) o `404 Not Found` (si no existe)
- Si existe: objeto con `id`, `codigo`, `descripcion`, `estado`, `observacion`
- Si no existe: error con código `CATALOGO_NOT_FOUND`

---

### 3. Listar Detalles de un Catálogo

```bash
# Detalles de SECCIONES_DOC (110 áreas)
curl -X GET 'http://localhost:8080/api/v1/catalogos/SECCIONES_DOC/detalles' \
  -H 'Content-Type: application/json' | jq

# Detalles de FORMATO (3 valores)
curl -X GET 'http://localhost:8080/api/v1/catalogos/FORMATO/detalles' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con todos los detalles del catálogo
- Para SECCIONES_DOC: 110 elementos (áreas IESS)
- Para FORMATO: 3 elementos (Físico, Digital, Mixto)

---

### 4. Listar Formatos (Endpoint de Conveniencia)

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos/formatos' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con 3 elementos: Físico, Digital, Mixto

---

### 5. Listar Niveles de Seguridad

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos/seguridad' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con 3 elementos: Pública, Confidencial, Reservada

---

### 6. Listar Estados de Serie

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos/estados-serie' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con 2 elementos: Creado, Actualizado

---

### 7. Listar Estados de Inventario

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos/estados-inventario' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con 4 elementos: Registrado, Pendiente, Actualizado, Aprobado

---

### 8. Listar Tipos de Contenedor

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos/tipos-contenedor' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con 4 elementos: Caja, Carpeta, Legajo, Tomo

---

### 9. Listar Tipos de Archivo

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos/tipos-archivo' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con 2 elementos: Activo, Pasivo

---

### 10. Listar Secciones Documentales

```bash
curl -X GET 'http://localhost:8080/api/v1/catalogos/secciones' \
  -H 'Content-Type: application/json' | jq
```

**Resultado esperado:**
- Status: `200 OK`
- Array con secciones documentales activas
- Si hay datos de prueba: 3 secciones (PRUEBA - Sección Pensiones, PRUEBA - Sección Prestaciones, TEST - Sección Recursos Humanos)

---

## ✅ Verificación de Resultados

### Checklist de Pruebas

- [ ] **GET /api/v1/catalogos** - Retorna lista de catálogos
- [ ] **GET /api/v1/catalogos/{codigo}** - Retorna catálogo específico
- [ ] **GET /api/v1/catalogos/{codigo}** - Retorna 404 para catálogo inexistente
- [ ] **GET /api/v1/catalogos/SECCIONES_DOC/detalles** - Retorna 110 áreas
- [ ] **GET /api/v1/catalogos/FORMATO/detalles** - Retorna 3 formatos
- [ ] **GET /api/v1/catalogos/formatos** - Retorna formatos
- [ ] **GET /api/v1/catalogos/seguridad** - Retorna niveles de seguridad
- [ ] **GET /api/v1/catalogos/estados-serie** - Retorna estados de serie
- [ ] **GET /api/v1/catalogos/estados-inventario** - Retorna estados de inventario
- [ ] **GET /api/v1/catalogos/tipos-contenedor** - Retorna tipos de contenedor
- [ ] **GET /api/v1/catalogos/tipos-archivo** - Retorna tipos de archivo
- [ ] **GET /api/v1/catalogos/secciones** - Retorna secciones documentales

### Validaciones

1. **Estructura de respuesta:**
   - Todas las respuestas exitosas tienen estructura `{ data, meta, error }`
   - `meta.timestamp` debe estar presente
   - `meta.totalItems` debe coincidir con el número de elementos en `data`

2. **Códigos de estado HTTP:**
   - `200 OK` para respuestas exitosas
   - `404 Not Found` para recursos no encontrados
   - `500 Internal Server Error` para errores del servidor

3. **Formato JSON:**
   - Todas las respuestas deben ser JSON válido
   - Los campos deben coincidir con los DTOs definidos

---

## 🐛 Troubleshooting

### Error: Connection refused
- **Causa:** El servidor Quarkus no está corriendo
- **Solución:** Ejecutar `./mvnw quarkus:dev`

### Error: 404 Not Found
- **Causa:** El endpoint no existe o la URL es incorrecta
- **Solución:** Verificar la URL y que el servidor esté corriendo

### Error: 500 Internal Server Error
- **Causa:** Error en el servidor o base de datos
- **Solución:** Revisar logs del servidor y verificar conexión a BD

### No hay datos en las respuestas
- **Causa:** Los datos de prueba no están insertados
- **Solución:** Ejecutar scripts en `scripts/datos-prueba/`

---

**Última actualización:** 2025-01-XX  
**Mantenido por:** Equipo de Desarrollo Backend







