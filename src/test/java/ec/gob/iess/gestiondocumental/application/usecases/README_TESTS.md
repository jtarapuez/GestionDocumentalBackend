# Tests unitarios – Casos de uso (hexagonal)

Los tests de esta carpeta validan la lógica de los use cases con **puertos mockeados** (`@InjectMock`), sin depender del comportamiento real de la base de datos.

## Cómo ejecutar

Desde la raíz del backend:

```bash
# Todos los tests del proyecto (incluye ApiMetaRequestIdTest, conexión Oracle, etc.)
mvn test

# Solo los tests de use cases + contrato API (meta/requestId)
mvn test -Dtest=InventarioDocumentalUseCaseTest,CatalogoUseCaseTest,SerieDocumentalUseCaseTest,SubserieDocumentalUseCaseTest,ApiMetaRequestIdTest

# Solo inventario
mvn test -Dtest=InventarioDocumentalUseCaseTest
```

**¿Docker?** Para estos tests (use cases + meta) **no hace falta**. Para `mvn test` completo, ver raíz del proyecto: `TESTS_Y_DOCKER.md`.

## Tests incluidos

| Clase | Qué prueba |
|-------|------------|
| `InventarioDocumentalUseCaseTest` | Registrar (bloqueo por pendientes vencidos, registro OK), obtenerPorId, listar pendientes, actualizar (operador distinto), aprobar, rechazar (observaciones vacías) |
| `CatalogoUseCaseTest` | listarCatalogos, obtenerPorCodigo, listarDetallesPorCatalogo, listarSecciones |
| `SerieDocumentalUseCaseTest` | crearSerie, obtenerPorId, listarPorSeccion, listarActivas |
| `SubserieDocumentalUseCaseTest` | crearSubserie, obtenerPorId, listarPorSerie, listarActivas |

Dependencias de test: JUnit 5, Mockito (Quarkus), AssertJ.
