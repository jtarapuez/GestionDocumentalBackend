# 🔧 Solución: Error de CORS en Swagger UI

**Fecha:** 2026-01-05  
**Proyecto:** Sistema de Gestión Documental - Backend  
**Framework:** Quarkus 3.9.5

---

## 📋 Problema Identificado

Al intentar usar Swagger UI para probar los endpoints de la API, se presentaba el error:

```
Failed to fetch
```

**Posibles causas:**
- CORS (Cross-Origin Resource Sharing)
- Network Failure
- URL scheme must be "http" or "https" for CORS request

---

## 🔍 Análisis del Problema

### Configuración Original (Problemática)

**Archivo:** `src/main/resources/application.properties`

```properties
# Configuración de CORS (para desarrollo)
quarkus.http.cors=true
quarkus.http.cors.origins=*
```

### Problemas con esta Configuración

1. **`origins=*` es demasiado permisivo:**
   - Algunos navegadores modernos rechazan `*` cuando hay credenciales
   - Swagger UI puede tener problemas con esta configuración
   - No es seguro para producción

2. **Faltan headers permitidos:**
   - No se especifican qué headers puede enviar el cliente
   - Swagger UI necesita headers específicos como `accept`, `content-type`

3. **Faltan métodos HTTP permitidos:**
   - No se especifican qué métodos HTTP están permitidos
   - Swagger UI necesita hacer peticiones `OPTIONS` (preflight)

---

## ✅ Solución Implementada

### Configuración Corregida

**Archivo:** `src/main/resources/application.properties`

```properties
# Configuración de CORS (para desarrollo)
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:8080,http://127.0.0.1:8080
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
```

### Explicación de cada Parámetro

#### 1. `quarkus.http.cors=true`
- Habilita el soporte de CORS en Quarkus

#### 2. `quarkus.http.cors.origins`
- **Valor:** `http://localhost:8080,http://127.0.0.1:8080`
- **Descripción:** Orígenes permitidos explícitamente
- **Por qué:** 
  - Es más seguro que `*`
  - Los navegadores lo aceptan sin problemas
  - Swagger UI funciona correctamente desde estos orígenes

#### 3. `quarkus.http.cors.headers`
- **Valor:** `accept,authorization,content-type,x-requested-with`
- **Descripción:** Headers HTTP permitidos en las peticiones CORS
- **Headers incluidos:**
  - `accept`: Para negociación de contenido (JSON, XML, etc.)
  - `authorization`: Para tokens de autenticación (Bearer tokens)
  - `content-type`: Para especificar el tipo de contenido (application/json)
  - `x-requested-with`: Para peticiones AJAX

#### 4. `quarkus.http.cors.methods`
- **Valor:** `GET,POST,PUT,DELETE,OPTIONS,PATCH`
- **Descripción:** Métodos HTTP permitidos
- **Métodos incluidos:**
  - `GET`: Consultar recursos
  - `POST`: Crear recursos
  - `PUT`: Actualizar recursos completos
  - `DELETE`: Eliminar recursos
  - `OPTIONS`: Peticiones preflight (requeridas por CORS)
  - `PATCH`: Actualizar recursos parciales

---

## 🎯 Resultado

Después de aplicar esta configuración:

✅ **Swagger UI funciona correctamente**  
✅ **Las peticiones desde el navegador se ejecutan sin errores**  
✅ **Los endpoints responden correctamente**  
✅ **No hay errores de CORS en la consola del navegador**

---

## 📝 Configuración para Producción

Para producción, ajusta los orígenes a los dominios reales:

```properties
# Configuración de CORS (para producción)
quarkus.http.cors=true
quarkus.http.cors.origins=https://tudominio.com,https://www.tudominio.com
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
quarkus.http.cors.credentials=true  # Si necesitas enviar cookies/credenciales
```

### Consideraciones de Seguridad

1. **Nunca uses `origins=*` en producción**
2. **Lista explícita de orígenes permitidos**
3. **Solo incluye los headers necesarios**
4. **Solo incluye los métodos HTTP que realmente uses**

---

## 🔗 Referencias

- **Documentación Quarkus CORS:** https://quarkus.io/guides/http-reference#cors-filter
- **MDN Web Docs - CORS:** https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
- **Estándar PAS-EST-043:** Documentación interna IESS

---

## 📌 Ubicación del Archivo de Configuración

```
gestion-documental-backend/
└── src/
    └── main/
        └── resources/
            └── application.properties  ← Aquí está la configuración
```

---

## ✅ Checklist de Verificación

- [x] CORS habilitado en `application.properties`
- [x] Orígenes específicos configurados (no `*`)
- [x] Headers permitidos especificados
- [x] Métodos HTTP permitidos especificados
- [x] Swagger UI funciona correctamente
- [x] Endpoints responden sin errores de CORS
- [x] Documentación creada

---

**Última actualización:** 2026-01-05  
**Autor:** Sistema de Gestión Documental - Backend Team
