# Ambientes: desarrollo, calidad y producción

Guía para levantar el backend en cada ambiente (desarrollo, calidad y producción).

---

## 1. Perfiles disponibles

| Perfil   | Archivo de configuración      | Uso principal                    |
|----------|--------------------------------|----------------------------------|
| **dev**  | `application-dev.properties`   | Desarrollo local                 |
| **calidad** | `application-calidad.properties` | Servidor de pruebas / QA      |
| **prod** | `application-prod.properties` | Producción                      |

La configuración base está en `application.properties`; cada perfil sobrescribe lo necesario.

---

## 2. Construir la aplicación

Desde la raíz del proyecto **gestion-documental-backend**:

```bash
# Build JAR (recomendado para calidad y producción)
./mvnw clean package -DskipTests

# El JAR ejecutable queda en:
# target/quarkus-app/quarkus-run.jar
```

Para ejecutar el JAR necesitas también la carpeta `target/quarkus-app/` completa (no solo el JAR), ya que Quarkus usa librerías externas en esa misma carpeta.

---

## 3. Levantar en CALIDAD

### 3.1 Activar el perfil

Puedes usar **variable de entorno** o **parámetro de JVM**:

```bash
# Opción A: variable de entorno
export QUARKUS_PROFILE=calidad

# Opción B: parámetro al ejecutar
java -Dquarkus.profile=calidad -jar target/quarkus-app/quarkus-run.jar
```

### 3.2 Variables de entorno recomendadas

En el servidor de calidad conviene definir:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `QUARKUS_PROFILE` | Perfil activo | `calidad` |
| `QUARKUS_DATASOURCE_USERNAME` | Usuario Oracle | `DOCUMENTAL_OWNER` |
| `QUARKUS_DATASOURCE_PASSWORD` | Contraseña Oracle | *(valor seguro)* |
| `QUARKUS_DATASOURCE_JDBC_URL` | URL JDBC Oracle | `jdbc:oracle:thin:@host:1521/SERVICIO?oracle.net.CONNECT_TIMEOUT=15000&oracle.jdbc.ReadTimeout=30000` |
| `CORS_ORIGINS` | Orígenes permitidos (CORS) | `https://calidad.gestiondocumental.iess.gob.ec` |
| `QUARKUS_HTTP_PORT` | Puerto (opcional) | `8080` |

### 3.3 Ejemplo completo (calidad)

```bash
cd gestion-documental-backend

# Construir
./mvnw clean package -DskipTests

# Exportar variables (ajustar valores reales)
export QUARKUS_PROFILE=calidad
export QUARKUS_DATASOURCE_USERNAME=DOCUMENTAL_OWNER
export QUARKUS_DATASOURCE_PASSWORD=MiPasswordSeguro
export QUARKUS_DATASOURCE_JDBC_URL="jdbc:oracle:thin:@192.168.29.208:1539/PDBIESS_CALI?oracle.net.CONNECT_TIMEOUT=15000&oracle.jdbc.ReadTimeout=30000"
export CORS_ORIGINS=https://calidad.gestiondocumental.iess.gob.ec

# Ejecutar (desde el directorio donde está target/)
java -jar target/quarkus-app/quarkus-run.jar
```

La API quedará en `http://<servidor>:8080/api` (por ejemplo `http://localhost:8080/api`). Swagger en calidad suele estar habilitado: `/swagger-ui`.

---

## 4. Levantar en PRODUCCIÓN

### 4.1 Activar el perfil

```bash
export QUARKUS_PROFILE=prod
# o
java -Dquarkus.profile=prod -jar target/quarkus-app/quarkus-run.jar
```

### 4.2 Variables de entorno obligatorias

En producción **no** hay valores por defecto para credenciales. Debes definir siempre:

| Variable | Descripción |
|----------|-------------|
| `QUARKUS_PROFILE` | `prod` |
| `QUARKUS_DATASOURCE_USERNAME` | Usuario Oracle |
| `QUARKUS_DATASOURCE_PASSWORD` | Contraseña Oracle |
| `QUARKUS_DATASOURCE_JDBC_URL` | URL JDBC Oracle |

Opcionales pero recomendadas:

| Variable | Descripción |
|----------|-------------|
| `CORS_ORIGINS` | Orígenes permitidos (ej. `https://gestiondocumental.iess.gob.ec`) |
| `QUARKUS_HTTP_PORT` | Puerto (por defecto 8080) |
| `QUARKUS_HTTP_HOST` | Host (por defecto 0.0.0.0) |

### 4.3 Ejemplo completo (producción)

```bash
cd gestion-documental-backend

./mvnw clean package -DskipTests

# Todas obligatorias en prod
export QUARKUS_PROFILE=prod
export QUARKUS_DATASOURCE_USERNAME=DOCUMENTAL_OWNER
export QUARKUS_DATASOURCE_PASSWORD=PasswordProduccionSeguro
export QUARKUS_DATASOURCE_JDBC_URL="jdbc:oracle:thin:@bd-prod:1521/PDBIESS?oracle.net.CONNECT_TIMEOUT=15000&oracle.jdbc.ReadTimeout=30000"
export CORS_ORIGINS=https://gestiondocumental.iess.gob.ec

java -jar target/quarkus-app/quarkus-run.jar
```

En producción, Swagger UI está **deshabilitado** por seguridad.

### 4.4 Buenas prácticas en producción

- No dejar credenciales en archivos; usar solo variables de entorno o un gestor de secretos.
- Definir las variables en el servidor, en el contenedor (Docker) o en el orquestador (Kubernetes/OpenShift), no en scripts en el repositorio.
- Usar un proceso manager o systemd para mantener la aplicación corriendo y reiniciarla si cae.

---

## 5. Resumen rápido

| Ambiente   | Perfil    | Cómo activar              | BD / CORS                          |
|-----------|-----------|----------------------------|------------------------------------|
| Desarrollo| `dev`     | Por defecto al ejecutar    | En `application-dev.properties`    |
| Calidad   | `calidad` | `QUARKUS_PROFILE=calidad`  | Variables de entorno recomendadas  |
| Producción| `prod`    | `QUARKUS_PROFILE=prod`     | Variables de entorno obligatorias  |

---

## 6. Verificar que el perfil está activo

Al arrancar, Quarkus muestra en consola el perfil activo, por ejemplo:

```
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
...
Profile prod activated.
```

También puedes llamar al health (si está habilitado) y revisar que la API responde en la URL esperada.
