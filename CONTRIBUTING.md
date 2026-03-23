# Contribución y gobernanza – Backend Gestión Documental

Este documento cierra la **Fase 0** (baseline): reglas comunes antes de refactor y nuevas features.

## Mejoras por fases (hexagonal / calidad)

- **Plan detallado:** [docs/mejoras-hexagonal/PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md](docs/mejoras-hexagonal/PLAN_MEJORA_CALIDAD_BACKEND_DETALLADO.md)
- **Checklist operativo:** [docs/mejoras-hexagonal/TAREAS_CONCRETAS_POR_FASE.md](docs/mejoras-hexagonal/TAREAS_CONCRETAS_POR_FASE.md)

Si trabajas también con el monorepo IESS que incluye `Documentacion/`, mantén alineadas las copias al hacer cambios normativos.

## Estrategia de ramas (Fase 0.3)

- **Por defecto:** cambios de mejoras y fixes en ramas `feature/<nombre-corto>` (ej. `feature/fase-1-logs`) con **pull request** hacia `main`.
- **Directo a `main`:** solo si el equipo lo acuerda explícitamente para hotfixes puntuales y siempre con revisión.

## Definición de hecho (DoD) por PR (Fase 0.4)

Antes de pedir merge:

1. `mvn test` en la raíz de `gestion-documental-backend` **sin fallos**.
2. No añadir `System.out` / `System.err` nuevos en `src/main/java` (usar logger; excepción solo si está documentada en el PR).
3. No commitear secretos: contraseñas, tokens API, URLs con credenciales, claves privadas.
4. Referenciar en el PR la **fase** del plan si aplica (ej. “Fase 1 – observabilidad”).

## Git remoto sin credenciales (Fase 0.6)

El `origin` **no** debe incluir usuario, contraseña ni token en la URL.

```bash
git remote -v
```

Si aparece un token en la URL:

1. **Revocar** ese token en GitHub (Settings → Developer settings → Personal access tokens) — quedó expuesto en historial local o en herramientas.
2. Configurar de nuevo el remoto **sin** token, por ejemplo:

```bash
git remote set-url origin https://github.com/jtarapuez/GestionDocumentalBackend.git
```

o SSH:

```bash
git remote set-url origin git@github.com:jtarapuez/GestionDocumentalBackend.git
```

3. Autenticarse con **credential helper**, **gh CLI** o **SSH key**.

Más detalle: [docs/SEGURIDAD_GIT_REMOTO.md](docs/SEGURIDAD_GIT_REMOTO.md).

## Línea base de tests (Fase 0)

Última corrida registrada: **2026-03-23** — `mvn test` en la raíz del backend **exit 0** (incluye Testcontainers/Oracle en entorno local con Docker). Repetir antes de cerrar cada fase del plan de mejoras.

## Mensajes de commit sugeridos

- `feat:` nueva funcionalidad
- `fix:` corrección
- `refactor:` cambio interno sin cambiar contrato
- `test:` solo pruebas
- `docs:` documentación (incluye plan de mejoras)
