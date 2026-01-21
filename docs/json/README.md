# 📋 Archivos JSON para Pruebas de Endpoints

Esta carpeta contiene archivos JSON con ejemplos de respuestas esperadas para cada endpoint del sistema de gestión documental.

## 📁 Estructura

- `catalogos/` - Archivos para endpoints de catálogos
- `secciones/` - Archivos para endpoints de secciones (próximamente)
- `series/` - Archivos para endpoints de series (próximamente)
- `subseries/` - Archivos para endpoints de subseries (próximamente)
- `inventarios/` - Archivos para endpoints de inventarios (próximamente)

## 🚀 Cómo Usar

### Opción 1: Usar con cURL

```bash
# Ejemplo: Listar catálogos
curl -X GET http://localhost:8080/api/v1/catalogos \
  -H "Content-Type: application/json"
```

### Opción 2: Usar con Postman

1. Importar los archivos JSON como ejemplos de respuesta
2. Crear una colección de Postman con los endpoints
3. Comparar las respuestas reales con los ejemplos esperados

### Opción 3: Usar con HTTPie

```bash
# Ejemplo: Obtener catálogo por código
http GET http://localhost:8080/api/v1/catalogos/SECCIONES_DOC
```

## 📝 Formato de Respuesta

Todos los endpoints siguen el formato estándar `ApiResponse`:

```json
{
  "data": { ... },
  "meta": {
    "timestamp": "2025-01-XX...",
    "totalItems": 10
  },
  "error": null
}
```

En caso de error:

```json
{
  "data": null,
  "meta": null,
  "error": {
    "message": "Mensaje de error",
    "code": "ERROR_CODE"
  }
}
```

## ✅ Verificación

Cada archivo JSON incluye:
- **Endpoint**: URL completa del endpoint
- **Método**: GET, POST, PUT, DELETE
- **Parámetros**: Si requiere parámetros de ruta o query
- **Respuesta esperada**: Estructura JSON esperada
- **Código HTTP**: 200, 404, 500, etc.
- **Notas**: Información adicional sobre el endpoint

## 🔍 Pruebas

Para probar los endpoints, asegúrate de que:
1. El servidor Quarkus esté corriendo
2. La base de datos esté configurada y accesible
3. Los datos de prueba estén insertados (scripts en `scripts/datos-prueba/`)

---

**Última actualización:** 2025-01-XX  
**Mantenido por:** Equipo de Desarrollo Backend




