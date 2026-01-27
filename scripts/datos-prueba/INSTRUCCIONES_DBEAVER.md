# 📋 Instrucciones para Ejecutar Scripts en DBeaver

## ⚠️ Problema Común: Error al Ejecutar Múltiples INSERTs

Cuando ejecutas múltiples `INSERT` statements juntos en DBeaver, puede aparecer el error:
```
ORA-00933: SQL command not properly ended
```

## ✅ Soluciones

### Opción 1: Ejecutar Statement por Statement (Recomendado)

1. **Selecciona SOLO el primer INSERT** (desde `INSERT INTO` hasta el `;`)
2. Presiona `Ctrl+Enter` (o `Cmd+Enter` en Mac) para ejecutar
3. Repite para cada INSERT individualmente

**Ventaja:** Funciona siempre, sin errores.

---

### Opción 2: Configurar DBeaver para Ejecutar Múltiples Statements

1. Ve a **Window → Preferences** (o **DBeaver → Preferences** en Mac)
2. Navega a **Connections → SQL Editor → SQL Processing**
3. Marca la opción **"Execute queries in separate statements"**
4. O marca **"Execute script"** en lugar de **"Execute SQL"**

**Ventaja:** Permite ejecutar múltiples statements juntos.

---

### Opción 3: Usar SQL*Plus o SQL Developer

Si DBeaver sigue dando problemas, puedes usar:

**SQL*Plus:**
```bash
sqlplus DOCUMENTAL_OWNER/DOC87desa@192.168.29.208:1539/PDBIESS_DESA @01_insertar_secciones.sql
```

**SQL Developer:**
- Abre el archivo `.sql`
- Ejecuta todo el script (F5)

---

## 🔍 Verificación

Después de ejecutar los scripts, verifica que los datos se insertaron:

```sql
SELECT COUNT(*) AS TOTAL_SECCIONES 
FROM DOCUMENTAL_OWNER.GDOC_SECCIONES_TP 
WHERE NOM_SECCION LIKE '%PRUEBA%' OR NOM_SECCION LIKE '%TEST%';
```

**Resultado esperado:** 3 secciones

---

## 💡 Recomendación

**Para desarrollo y pruebas:** Usa la **Opción 1** (ejecutar uno por uno). Es la más confiable y te permite ver el resultado de cada INSERT.

**Para producción o muchos datos:** Usa la **Opción 3** (SQL*Plus o SQL Developer).

---

**Última actualización:** 2026-01-06







