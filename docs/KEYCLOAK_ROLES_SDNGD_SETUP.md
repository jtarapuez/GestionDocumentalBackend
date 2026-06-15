# Roles SDNGD en Keycloak — backend

Guía operativa para que Quarkus autorice con `@RolesAllowed` leyendo roles del JWT.

**Guía completa (Keycloak + MFE + validación):** ver el mismo documento en el repo MFE  
`docs/KEYCLOAK_ROLES_SDNGD_SETUP.md`.

---

## Roles que espera el backend

Definidos en `SdngdRoles.java`:

| Rol | Uso |
|-----|-----|
| `ADMINISTRADOR_SDNGD` | Series, subseries, reportes |
| `OPERADOR_SDNGD` | Inventarios operador |
| `SUPERVISOR_SDNGD` | Aprobar/rechazar inventarios |

Alias legacy: `ADMINISTRADOR`, `OPERADOR`, `SUPERVISOR`.

---

## Claims JWT (Keycloak)

Quarkus lee (`application.properties`):

- `user.rolesDisponibles` (array)
- `user.rol` (string)
- `realm_access.roles` (fallback)

El augmentor `SdngdJwtRolesAugmentor` lee **solo el JWT** Keycloak (`user.rolesDisponibles`, `user.rol`, `realm_access`). El puente por header `ec-iess-role-identifier` fue retirado en la rama `feature/keycloak-roles-sdngd`.

---

## Variables de entorno (dev)

```bash
KEYCLOAK_AUTH_SERVER_URL=https://keycloack-dev.iess.gob.ec/realms/plantilla-front
KEYCLOAK_CLIENT_ID=gestion-documental-api
KEYCLOAK_CLIENT_SECRET=<no commitear>
KEYCLOAK_OIDC_ENABLED=true
```

Cliente API: ver `KEYCLOAK_REGISTRO_CLIENTE_API_DEV.md` en documentación Seguridades.

---

## Prueba rápida

```http
POST http://localhost:8080/api/v1/series
Authorization: Bearer <access_token_con_ADMINISTRADOR_SDNGD>
X-Operador-Id: <cedula>
Content-Type: application/json
```

- Token válido + rol → **201** / **200**
- Token válido sin rol → **403** `AUTH_FORBIDDEN`
- Sin token (OIDC on) → **401**

Log DEBUG: `Roles SDNGD augmentados para <usuario>: [...]`
