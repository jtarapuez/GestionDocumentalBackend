# Seguridad: remoto Git sin credenciales en la URL

## Problema

Si `git remote -v` muestra algo como:

`https://<TOKEN>@github.com/...`

ese token queda:

- En la configuración local del repositorio
- Potencialmente en logs, capturas o backups

Cualquier persona con acceso al token puede usar la API o el repositorio como tú.

## Qué hacer

1. **Revocar el token** en GitHub (ya no debe usarse).
2. Establecer un remoto **limpio**:

```bash
git remote set-url origin https://github.com/jtarapuez/GestionDocumentalBackend.git
```

o con SSH:

```bash
git remote set-url origin git@github.com:jtarapuez/GestionDocumentalBackend.git
```

3. Volver a autenticarse de forma segura:

- **HTTPS:** Credential Manager del SO, o `gh auth login`
- **SSH:** clave pública en GitHub

4. Comprobar:

```bash
git remote -v
git fetch origin
```

No debe aparecer el token en la salida.
