# 🔧 Configurar Postman para Probar Endpoints

## ✅ Si funciona en Swagger pero NO en Postman

Esto generalmente es un problema de configuración en Postman. Sigue estos pasos:

---

## 📋 Configuración Básica de Postman

### 1. **URL Correcta**

```
GET http://localhost:8080/api/v1/catalogos
```

**NOTA:** Si el servidor tiene `@ApplicationPath("/api")`, la URL final será `/api/v1/catalogos`. Si no funciona, prueba con `/api/api/v1/catalogos` temporalmente.

**Importante:**
- ✅ Usa `http://` (no `https://`)
- ✅ Puerto: `8080`
- ✅ Ruta completa: `/api/v1/catalogos`

### 2. **Headers Necesarios**

En la pestaña **Headers** de Postman, agrega:

| Key | Value |
|-----|-------|
| `Content-Type` | `application/json` |
| `Accept` | `application/json` |

**Cómo agregar:**
1. Abre la pestaña **Headers** en Postman
2. Haz clic en **Bulk Edit** (o agrega manualmente)
3. Pega esto:
```
Content-Type: application/json
Accept: application/json
```

### 3. **Método HTTP**

- Selecciona: **GET**
- No necesitas Body para GET

---

## 🔍 Verificación Paso a Paso

### Paso 1: Crear Nueva Request

1. En Postman, haz clic en **New** → **HTTP Request**
2. Nombra la request: `Listar Catálogos`

### Paso 2: Configurar URL

1. Método: **GET**
2. URL: `http://localhost:8080/api/v1/catalogos`
3. Verifica que no haya espacios extra

### Paso 3: Configurar Headers

1. Ve a la pestaña **Headers**
2. Agrega:
   - `Content-Type`: `application/json`
   - `Accept`: `application/json`

### Paso 4: Enviar Request

1. Haz clic en **Send**
2. Deberías ver la respuesta con los 7 catálogos

---

## 🐛 Solución de Problemas Comunes

### Error 1: "404 Not Found" o "500 Internal Server Error"

**Causa:** URL incorrecta o headers faltantes

**Solución:**
1. Verifica la URL: `http://localhost:8080/api/v1/catalogos`
2. Agrega los headers mencionados arriba
3. Asegúrate de que el servidor esté corriendo

### Error 2: "Connection refused"

**Causa:** El servidor Quarkus no está corriendo

**Solución:**
```bash
cd gestion-documental-backend
./mvnw quarkus:dev
```

### Error 3: "CORS error"

**Causa:** Problema de CORS (aunque debería estar configurado)

**Solución:**
1. Verifica que `quarkus.http.cors=true` en `application.properties`
2. Si persiste, agrega este header en Postman:
   - `Origin`: `http://localhost:8080`

### Error 4: Respuesta vacía o diferente

**Causa:** Headers incorrectos

**Solución:**
- Asegúrate de tener `Accept: application/json`
- Verifica que `Content-Type: application/json`

---

## 📝 Ejemplo Completo de Request en Postman

### Request Configuration:

```
Method: GET
URL: http://localhost:8080/api/v1/catalogos
```

### Headers:

```
Content-Type: application/json
Accept: application/json
```

### Expected Response (200 OK):

```json
{
  "data": [
    {
      "id": 2,
      "codigo": "SECCIONES_DOC",
      "descripcion": "Catálogo de secciones documentales - Áreas IESS",
      "estado": "A"
    },
    {
      "id": 3,
      "codigo": "FORMATO",
      "descripcion": "Catálogo de formatos de documentos (Físico, Digital, Mixto)",
      "estado": "A"
    }
    // ... más catálogos
  ],
  "meta": {
    "timestamp": "2026-01-07T..."
  }
}
```

---

## 🎯 Comparar con Swagger

Si funciona en Swagger, compara:

1. **URL en Swagger:** ¿Cuál es la URL exacta que muestra?
2. **Headers en Swagger:** Abre las herramientas de desarrollador (F12) y ve a la pestaña Network para ver qué headers envía Swagger
3. **Replica en Postman:** Copia exactamente la misma configuración

---

## ✅ Checklist de Verificación

Antes de enviar la request en Postman, verifica:

- [ ] Servidor Quarkus está corriendo (`./mvnw quarkus:dev`)
- [ ] URL correcta: `http://localhost:8080/api/v1/catalogos`
- [ ] Método: **GET**
- [ ] Header `Content-Type: application/json`
- [ ] Header `Accept: application/json`
- [ ] No hay Body (para GET requests)
- [ ] No hay parámetros en la URL (para este endpoint)

---

## 🔄 Probar Otros Endpoints

Una vez que funcione el primero, puedes probar los demás:

### 2. Obtener Catálogo por Código
```
GET http://localhost:8080/api/v1/catalogos/SECCIONES_DOC
```

### 3. Listar Detalles
```
GET http://localhost:8080/api/v1/catalogos/SECCIONES_DOC/detalles
```

### 4. Formatos (Endpoint de Conveniencia)
```
GET http://localhost:8080/api/v1/catalogos/formatos
```

**Mismos headers para todos:**
- `Content-Type: application/json`
- `Accept: application/json`

---

## 📸 Captura de Pantalla de Referencia

Si aún no funciona, verifica que tu configuración en Postman se vea así:

**Request Tab:**
- Method: `GET`
- URL: `http://localhost:8080/api/v1/catalogos`

**Headers Tab:**
- `Content-Type` → `application/json`
- `Accept` → `application/json`

**Body Tab:**
- Seleccionado: `none` (para GET requests)

---

## 🆘 Si Aún No Funciona

1. **Reinicia Postman**
2. **Verifica que el servidor esté corriendo:**
   ```bash
   curl http://localhost:8080/api/v1/catalogos
   ```
3. **Revisa los logs del servidor Quarkus** para ver errores
4. **Compara con Swagger:** Abre las herramientas de desarrollador (F12) en el navegador cuando uses Swagger y copia exactamente la misma request

---

**Última actualización:** 2025-01-07  
**Mantenido por:** Equipo de Desarrollo Backend




