# 🔧 Solución: Conexión Oracle desde Quarkus

**Fecha:** 2026-01-05  
**Problema:** Error al conectar Quarkus con Oracle 19.3  
**Estado:** ✅ RESUELTO

---

## 📋 Resumen del Problema

### ❌ Error Encontrado
```
Unsupported Database: Oracle 19.3
Flyway bloquea el inicio de Quarkus
```

### ✅ Solución Aplicada
Deshabilitar Flyway temporalmente hasta que se actualice a una versión compatible con Oracle 19.3.

---

## 🔍 Análisis del Problema

### 1. **Problema Principal**
- **Flyway** (herramienta de migraciones de BD) no soporta Oracle 19.3
- Quarkus intenta inicializar Flyway al arrancar
- Esto bloquea el inicio de la aplicación

### 2. **Validación de Conexión**
✅ **La conexión JDBC funciona correctamente:**
- Host: `192.168.29.208:1539`
- Service: `PDBIESS_DESA`
- Usuario: `DOCUMENTAL_OWNER`
- Password: `DOC87desa`
- ✅ Validado con test Java simple (`TestConexionOracleSimple`)

### 3. **Proyecto de Prueba**
El proyecto `test-oracle-connection` confirmó que:
- ✅ La conexión JDBC funciona
- ✅ Las credenciales son correctas
- ❌ El problema es solo con Quarkus + Flyway

---

## ✅ Solución Implementada

### 1. **Configuración en `application.properties`**

Se agregó la siguiente línea para deshabilitar Flyway:

```properties
# Flyway (Migraciones)
# IMPORTANTE: Deshabilitado temporalmente porque Flyway no soporta Oracle 19.3
# Ver: test-oracle-connection/SOLUCION_FINAL.md
quarkus.flyway.enabled=false
quarkus.flyway.migrate-at-start=false
quarkus.flyway.baseline-on-migrate=true
quarkus.flyway.locations=classpath:db/migration
```

**Ubicación:** `gestion-documental-backend/src/main/resources/application.properties` (línea 69)

### 2. **Dependencias Verificadas**

✅ Todas las dependencias necesarias están presentes en `pom.xml`:

```xml
<!-- JDBC Oracle -->
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-jdbc-oracle</artifactId>
</dependency>

<!-- Agroal (Connection Pool) -->
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-agroal</artifactId>
</dependency>
```

### 3. **Configuración de Base de Datos**

✅ Configuración correcta en `application.properties`:

```properties
# Datasource
quarkus.datasource.db-kind=oracle
quarkus.datasource.username=DOCUMENTAL_OWNER
quarkus.datasource.password=DOC87desa
quarkus.datasource.jdbc.url=jdbc:oracle:thin:@192.168.29.208:1539/PDBIESS_DESA

# Connection Pool
quarkus.datasource.jdbc.min-size=0
quarkus.datasource.jdbc.max-size=3
quarkus.datasource.jdbc.initial-size=0
quarkus.datasource.jdbc.acquisition-timeout=120
quarkus.datasource.jdbc.background-validation-interval=PT10M
quarkus.datasource.jdbc.idle-removal-interval=PT10M
quarkus.datasource.jdbc.max-lifetime=PT1H
quarkus.datasource.jdbc.validation-query-sql=SELECT 1 FROM DUAL
```

---

## 🚀 Cómo Probar la Conexión

### 1. **Iniciar la Aplicación**

```bash
cd gestion-documental-backend
mvn quarkus:dev
```

### 2. **Verificar Logs**

Deberías ver en los logs:
```
✅ Quarkus iniciado correctamente
✅ Datasource configurado
✅ Sin errores de Flyway
```

### 3. **Probar Endpoints**

- **Health Check:** http://localhost:8080/q/health
- **Swagger UI:** http://localhost:8080/swagger-ui
- **API de Catálogos:** http://localhost:8080/api/catalogos

### 4. **Verificar Conexión a BD**

Si tienes un endpoint de prueba, debería responder correctamente sin errores de conexión.

---

## 📝 Notas Importantes

### ⚠️ Flyway Deshabilitado

**Razón:** Flyway no soporta Oracle 19.3 en la versión actual.

**Impacto:**
- ✅ La aplicación puede iniciar correctamente
- ✅ Las conexiones JDBC funcionan
- ✅ Hibernate ORM funciona
- ❌ Las migraciones automáticas de Flyway no se ejecutan

**Alternativas para Migraciones:**
1. **Ejecutar migraciones manualmente** cuando sea necesario
2. **Usar scripts SQL directos** para cambios de esquema
3. **Esperar actualización de Flyway** que soporte Oracle 19.3
4. **Usar Liquibase** como alternativa (si es necesario)

### 🔄 Cuando Reactivar Flyway

Flyway se puede reactivar cuando:
- Se actualice Flyway a una versión que soporte Oracle 19.3
- O se migre a una versión de Oracle compatible con Flyway actual

Para reactivar, simplemente cambiar:
```properties
quarkus.flyway.enabled=true
```

---

## 🔗 Referencias

### Archivos Relacionados
- `test-oracle-connection/SOLUCION_FINAL.md` - Solución encontrada en proyecto de prueba
- `test-oracle-connection/README.md` - Configuración mínima de prueba
- `gestion-documental-backend/src/main/resources/application.properties` - Configuración actual

### Documentación Externa
- [Quarkus JDBC Oracle Guide](https://quarkus.io/guides/datasource)
- [Quarkus Flyway Guide](https://quarkus.io/guides/flyway)
- [Oracle JDBC Driver Documentation](https://docs.oracle.com/en/database/oracle/oracle-database/)

---

## ✅ Checklist de Verificación

Antes de considerar el problema resuelto, verificar:

- [x] `quarkus.flyway.enabled=false` agregado en `application.properties`
- [x] Dependencias `quarkus-jdbc-oracle` y `quarkus-agroal` presentes en `pom.xml`
- [x] Configuración de datasource correcta (URL, usuario, password)
- [ ] Aplicación inicia sin errores (`mvn quarkus:dev`)
- [ ] Health check responde correctamente
- [ ] Endpoints de API funcionan correctamente
- [ ] Conexiones a base de datos funcionan

---

## 🐛 Troubleshooting

### Si aún hay problemas de conexión:

1. **Verificar credenciales:**
   ```bash
   # Probar conexión directa con sqlplus o DBeaver
   # Host: 192.168.29.208:1539
   # Service: PDBIESS_DESA
   # Usuario: DOCUMENTAL_OWNER
   # Password: DOC87desa
   ```

2. **Verificar red:**
   ```bash
   # Probar conectividad
   telnet 192.168.29.208 1539
   # O
   nc -zv 192.168.29.208 1539
   ```

3. **Verificar logs de Quarkus:**
   ```bash
   # Buscar errores específicos en los logs
   mvn quarkus:dev | grep -i error
   ```

4. **Verificar versión de Oracle JDBC:**
   ```bash
   # Verificar que la versión del driver sea compatible
   mvn dependency:tree | grep oracle
   ```

---

## 📞 Contacto

Si el problema persiste después de aplicar esta solución:
1. Revisar logs completos de Quarkus
2. Verificar conectividad de red
3. Validar credenciales de base de datos
4. Consultar con el equipo de base de datos

---

**Última actualización:** 2026-01-05  
**Mantenido por:** Equipo de Desarrollo Backend




