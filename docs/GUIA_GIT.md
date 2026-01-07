# 📚 Guía de Git - Sistema de Gestión Documental Backend

## 🔐 Información del Repositorio

**Repositorio:** https://github.com/jtarapuez/GestionDocumentalBackend.git  
**Usuario:** jtarapuez  
**Email:** tarapuez9@gmail.com  
**Token de Acceso:** [Ver sección de configuración de autenticación]  
**Rama Principal:** `main`

---

## 🚀 Configuración Inicial (Primera Vez)

### 1. Clonar el Repositorio

**Opción A: Con Token (Recomendado)**
```bash
# Reemplazar YOUR_TOKEN con el token de acceso personal
git clone https://YOUR_TOKEN@github.com/jtarapuez/GestionDocumentalBackend.git
cd GestionDocumentalBackend
```

**Opción B: Con SSH (Si está configurado)**
```bash
git clone git@github.com:jtarapuez/GestionDocumentalBackend.git
cd GestionDocumentalBackend
```

**Opción C: Clonar y autenticar después**
```bash
git clone https://github.com/jtarapuez/GestionDocumentalBackend.git
cd GestionDocumentalBackend
# Git pedirá credenciales al hacer push/pull
```

### 2. Configurar Usuario y Email

```bash
git config user.name "jtarapuez"
git config user.email "tarapuez9@gmail.com"
```

### 3. Verificar Configuración

```bash
git config --list
git remote -v
```

---

## 📋 Comandos Básicos de Git

### Ver Estado del Repositorio

```bash
git status
```

### Ver Cambios Específicos

```bash
# Ver cambios en archivos modificados
git diff

# Ver cambios en un archivo específico
git diff ruta/al/archivo.java

# Ver historial de commits
git log --oneline
```

### Agregar Archivos al Staging

```bash
# Agregar un archivo específico
git add ruta/al/archivo.java

# Agregar todos los archivos modificados
git add .

# Agregar todos los archivos (incluyendo nuevos)
git add -A
```

### Crear Commit

```bash
# Commit con mensaje
git commit -m "Descripción clara del cambio realizado"

# Ejemplos de mensajes:
git commit -m "feat: Agregar endpoint para consultar inventarios"
git commit -m "fix: Corregir validación de tildes en scripts SQL"
git commit -m "docs: Actualizar documentación de endpoints"
```

### Sincronizar con el Repositorio Remoto

```bash
# Obtener cambios del remoto
git pull origin main

# Enviar cambios al remoto
git push origin main

# Configurar tracking (solo primera vez)
git push -u origin main
```

---

## 🔄 Flujo de Trabajo Recomendado

### 1. Antes de Empezar a Trabajar

```bash
# Actualizar código local
git pull origin main

# Verificar estado
git status
```

### 2. Durante el Desarrollo

```bash
# Ver cambios realizados
git status
git diff

# Agregar archivos modificados
git add .

# Crear commit
git commit -m "Descripción del cambio"
```

### 3. Al Finalizar el Trabajo

```bash
# Verificar que todo esté commiteado
git status

# Enviar cambios al remoto
git push origin main
```

---

## 📝 Convenciones de Mensajes de Commit

Usar prefijos descriptivos:

- **`feat:`** Nueva funcionalidad
- **`fix:`** Corrección de bug
- **`docs:`** Cambios en documentación
- **`style:`** Cambios de formato (sin afectar código)
- **`refactor:`** Refactorización de código
- **`test:`** Agregar o modificar tests
- **`chore:`** Tareas de mantenimiento

### Ejemplos:

```bash
git commit -m "feat: Implementar endpoint POST /api/v1/inventarios"
git commit -m "fix: Corregir error de validación en SerieDocumental"
git commit -m "docs: Actualizar README con instrucciones de instalación"
git commit -m "refactor: Reorganizar estructura de DTOs"
```

---

## 🔀 Manejo de Ramas (Opcional)

### Crear Nueva Rama

```bash
# Crear y cambiar a nueva rama
git checkout -b nombre-rama

# Ejemplo: rama para nueva funcionalidad
git checkout -b feature/agregar-catalogos
```

### Cambiar de Rama

```bash
# Cambiar a rama existente
git checkout main

# Cambiar a rama y actualizar
git checkout main && git pull origin main
```

### Fusionar Rama

```bash
# Cambiar a rama principal
git checkout main

# Fusionar rama
git merge nombre-rama

# Eliminar rama local (opcional)
git branch -d nombre-rama
```

---

## 🛠️ Solución de Problemas Comunes

### 1. Conflictos al Hacer Pull

```bash
# Si hay conflictos, resolver manualmente y luego:
git add .
git commit -m "Resolve merge conflicts"
git push origin main
```

### 2. Deshacer Cambios Locales (No Commiteados)

```bash
# Descartar cambios en un archivo
git checkout -- ruta/al/archivo.java

# Descartar todos los cambios
git checkout -- .

# Descartar archivos agregados al staging
git reset HEAD ruta/al/archivo.java
```

### 3. Deshacer Último Commit (Sin Push)

```bash
# Mantener cambios pero deshacer commit
git reset --soft HEAD~1

# Descartar cambios y commit
git reset --hard HEAD~1
```

### 4. Actualizar Remoto Después de Cambios Locales

```bash
# Si el remoto tiene cambios nuevos
git pull origin main --rebase

# O hacer merge
git pull origin main --no-rebase
```

### 5. Verificar Estado del Repositorio

```bash
# Ver estado detallado
git status

# Ver diferencias con remoto
git fetch origin
git diff main origin/main

# Ver historial
git log --oneline --graph --all
```

---

## 📦 Archivos Importantes

### `.gitignore`

El proyecto incluye un `.gitignore` que excluye:
- `target/` - Archivos compilados de Maven
- `.DS_Store` - Archivos del sistema macOS
- `*.log` - Archivos de log
- `*.class` - Archivos compilados Java
- Archivos de backup

**No modificar** el `.gitignore` sin consultar primero.

---

## 🔐 Seguridad y Autenticación

### ⚠️ IMPORTANTE: Token de Acceso Personal

**NUNCA** incluir tokens de acceso en archivos que se suben al repositorio.

### Configurar Autenticación

**Opción 1: Token en URL del Remoto (Solo para desarrollo local)**
```bash
# Reemplazar YOUR_TOKEN con el token de acceso personal
git remote set-url origin https://YOUR_TOKEN@github.com/jtarapuez/GestionDocumentalBackend.git
```

**Opción 2: Usar Credential Helper (Recomendado)**
```bash
# Configurar para guardar credenciales
git config --global credential.helper osxkeychain  # macOS
# o
git config --global credential.helper store         # Linux/Windows

# Al hacer push/pull, Git pedirá el token y lo guardará
```

**Opción 3: SSH Keys (Más seguro)**
```bash
# Generar SSH key (si no existe)
ssh-keygen -t ed25519 -C "tarapuez9@gmail.com"

# Agregar clave SSH a GitHub (copiar contenido de ~/.ssh/id_ed25519.pub)
# Luego cambiar remoto a SSH
git remote set-url origin git@github.com:jtarapuez/GestionDocumentalBackend.git
```

### Si el Token se Compromete

1. Ir a GitHub → Settings → Developer settings → Personal access tokens
2. Revocar el token comprometido
3. Generar nuevo token
4. Actualizar la configuración local:
   ```bash
   git remote set-url origin https://NUEVO_TOKEN@github.com/jtarapuez/GestionDocumentalBackend.git
   ```

---

## 📋 Checklist Antes de Hacer Push

Antes de enviar cambios al repositorio, verificar:

- [ ] `git status` muestra solo los archivos que quieres commitear
- [ ] `git diff` muestra los cambios esperados
- [ ] El mensaje del commit es claro y descriptivo
- [ ] No hay archivos sensibles (passwords, tokens) en el código
- [ ] Los archivos compilados (`target/`) no están incluidos
- [ ] Se ejecutó `git pull` para obtener cambios recientes

---

## 🎯 Comandos Rápidos de Referencia

```bash
# Estado y cambios
git status
git diff
git log --oneline

# Agregar y commitear
git add .
git commit -m "mensaje"

# Sincronizar
git pull origin main
git push origin main

# Ver configuración
git config --list
git remote -v
```

---

## 📞 Información de Contacto

**Repositorio:** https://github.com/jtarapuez/GestionDocumentalBackend  
**Usuario GitHub:** jtarapuez  
**Email:** tarapuez9@gmail.com

---

## 🔄 Actualización de esta Guía

Esta guía debe actualizarse cuando:
- Cambie la estructura del repositorio
- Se agreguen nuevas convenciones
- Se modifiquen los procesos de trabajo
- Se actualicen las credenciales

---

**Última actualización:** 2026-01-07  
**Versión:** 1.0
