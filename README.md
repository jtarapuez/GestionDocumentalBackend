# 🚀 Sistema de Gestión Documental - Backend

**Proyecto:** Backend del Sistema de Gestión Documental del IESS  
**Framework:** Quarkus 3.9.5  
**Estándar:** PAS-EST-043  
**Versión:** 1.0.0-SNAPSHOT

---

## 📋 Descripción

Backend del Sistema de Gestión Documental del IESS desarrollado con Quarkus siguiendo el estándar **PAS-EST-043**.

**Incluye:**
- ✅ Estructura DDD básica
- ✅ Endpoint REST simple (Hola Mundo)
- ✅ OpenAPI/Swagger UI
- ✅ Health Checks
- ✅ Configuración según estándar

---

## 🛠️ Requisitos

- **Java:** 17 o superior (verificado: Java 21)
- **Maven:** 3.8+ (o usar Maven Wrapper)
- **Quarkus:** 3.9.5

---

## 🚀 Inicio Rápido

### 1. Compilar el proyecto

```bash
cd gestion-documental-backend
mvn clean compile
```

### 2. Ejecutar en modo desarrollo

```bash
mvn quarkus:dev
```

El servidor se iniciará en: **http://localhost:8080**

### 3. Probar los endpoints

**Hola Mundo Simple:**
```bash
curl http://localhost:8080/api/hola-mundo
```

**Respuesta:**
```json
{
  "mensaje": "¡Hola Mundo desde Quarkus!",
  "version": "1.0.0",
  "framework": "Quarkus 3.9.5"
}
```

**Hola Mundo Personalizado:**
```bash
curl http://localhost:8080/api/hola-mundo/Juan
```

**Respuesta:**
```json
{
  "mensaje": "¡Hola Juan! Bienvenido a Quarkus",
  "version": "1.0.0",
  "framework": "Quarkus 3.9.5"
}
```

---

## 📚 Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/hola-mundo` | Saludo simple |
| GET | `/api/hola-mundo/{nombre}` | Saludo personalizado |

---

## 🔧 Herramientas de Desarrollo

### Swagger UI
- **URL:** http://localhost:8080/swagger-ui
- **Descripción:** Interfaz web para probar los endpoints

### OpenAPI
- **URL:** http://localhost:8080/q/openapi
- **Descripción:** Especificación OpenAPI en formato JSON/YAML

### Health Checks
- **URL:** http://localhost:8080/q/health
- **Descripción:** Estado de salud de la aplicación

### Health UI
- **URL:** http://localhost:8080/health-ui
- **Descripción:** Interfaz web para health checks

---

## 📁 Estructura del Proyecto

```
quarkus-ejemplo-simple/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ec/gob/iess/ejemplo/
│   │   │       ├── EjemploApplication.java       # Configuración JAX-RS
│   │   │       └── interfaces/
│   │   │           └── api/
│   │   │               └── HolaMundoController.java  # Controlador REST
│   │   └── resources/
│   │       └── application.properties            # Configuración
│   └── test/
│       └── java/                                 # Pruebas (futuro)
├── pom.xml                                       # Configuración Maven
└── README.md                                     # Este archivo
```

---

## 🏗️ Arquitectura (DDD)

Según **PAS-EST-043**, la estructura sigue Domain-Driven Design:

```
ec.gob.iess.ejemplo/
├── domain/           # (Futuro: Entidades, Value Objects)
├── application/      # (Futuro: Casos de uso)
├── infrastructure/   # (Futuro: Repositorios, adaptadores)
└── interfaces/       # ✅ Controladores REST, DTOs
    └── api/
        └── HolaMundoController.java
```

---

## 📦 Extensiones Quarkus Incluidas

Según **PAS-EST-043**, extensiones obligatorias:

- ✅ `quarkus-rest` - REST API
- ✅ `quarkus-rest-jackson` - JSON
- ✅ `quarkus-smallrye-openapi` - OpenAPI
- ✅ `quarkus-swagger-ui` - Swagger UI
- ✅ `quarkus-arc` - CDI (inyección de dependencias)
- ✅ `quarkus-smallrye-health` - Health Checks
- ✅ `quarkus-smallrye-metrics` - Métricas

---

## 🧪 Pruebas

### Ejecutar pruebas

```bash
mvn test
```

### Ejecutar en modo desarrollo con pruebas

```bash
mvn quarkus:dev
```

---

## 📝 Comandos Útiles

```bash
# Desarrollo (con hot reload)
mvn quarkus:dev

# Compilar
mvn clean compile

# Build
mvn clean package

# Ejecutar JAR
java -jar target/quarkus-app/quarkus-run.jar

# Verificar dependencias
mvn dependency:tree

# Limpiar proyecto
mvn clean
```

---

## 🔍 Verificar que Funciona

1. **Iniciar el servidor:**
   ```bash
   mvn quarkus:dev
   ```

2. **Abrir en el navegador:**
   - Swagger UI: http://localhost:8080/swagger-ui
   - Health: http://localhost:8080/q/health

3. **Probar con curl:**
   ```bash
   curl http://localhost:8080/api/hola-mundo
   ```

---

## 📚 Próximos Pasos

Este ejemplo simple puede extenderse para:

1. **Agregar Base de Datos:**
   - Agregar `quarkus-hibernate-orm-panache`
   - Agregar `quarkus-jdbc-oracle`
   - Crear entidades JPA

2. **Agregar Seguridad:**
   - Agregar `quarkus-oidc` (Keycloak)
   - Proteger endpoints con `@RolesAllowed`

3. **Agregar Validaciones:**
   - Agregar `quarkus-hibernate-validator`
   - Validar DTOs con Bean Validation

4. **Agregar Tests:**
   - Pruebas unitarias con JUnit 5
   - Pruebas de integración

---

## 📖 Referencias

- **Estándar PAS-EST-043:** `Documentacion/PAS-EST-043 Estandar Quarkus-signed.md`
- **Plan de Desarrollo Backend:** `Documentacion/backend/PLAN_DESARROLLO_BACKEND.md`
- **Documentación Quarkus:** https://quarkus.io

---

## ✅ Checklist de Verificación

- [x] Proyecto Maven creado
- [x] Estructura DDD básica
- [x] Endpoint REST funcionando
- [x] OpenAPI/Swagger configurado
- [x] Health Checks configurados
- [x] Configuración según PAS-EST-043
- [x] README con instrucciones

---

**¡Listo para probar!** 🎉

Ejecuta `mvn quarkus:dev` y visita http://localhost:8080/swagger-ui
