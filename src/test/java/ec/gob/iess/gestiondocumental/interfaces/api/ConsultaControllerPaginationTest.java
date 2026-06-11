package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.common.PaginatedResult;
import ec.gob.iess.gestiondocumental.application.port.in.InventarioDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Contrato POST /v1/consultas: paginación opcional (page/size) sin romper consultas sin paginar.
 */
@QuarkusTest
class ConsultaControllerPaginationTest {

    @InjectMock
    InventarioDocumentalUseCasePort inventarioUseCase;

    @Test
    @DisplayName("con page/size devuelve meta de paginación")
    void consultaPaginadaIncluyeMeta() {
        InventarioDocumentalResponse inv = new InventarioDocumentalResponse();
        inv.setId(1L);
        inv.setNumeroExpediente("EXP-001");
        when(inventarioUseCase.listarConFiltrosPaginado(
                any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(),
                eq(0), eq(10)))
                .thenReturn(new PaginatedResult<>(List.of(inv), 25, 0, 10));

        given()
                .contentType(JSON)
                .accept(JSON)
                .body("{\"page\":0,\"size\":10}")
                .when()
                .post("/api/v1/consultas")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(1))
                .body("meta.totalItems", equalTo(25))
                .body("meta.totalPages", equalTo(3))
                .body("meta.currentPage", equalTo(0))
                .body("meta.pageSize", equalTo(10))
                .body("meta.path", notNullValue())
                .body("meta.requestId", notNullValue());

        verify(inventarioUseCase).listarConFiltrosPaginado(
                any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(),
                eq(0), eq(10));
    }

    @Test
    @DisplayName("sin page/size mantiene listado completo sin meta de paginación")
    void consultaSinPaginacionNoIncluyeTotalPages() {
        when(inventarioUseCase.listarConFiltros(
                any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        given()
                .contentType(JSON)
                .accept(JSON)
                .body("{}")
                .when()
                .post("/api/v1/consultas")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(0))
                .body("meta.totalPages", nullValue())
                .body("meta.path", notNullValue())
                .body("meta.requestId", notNullValue());

        verify(inventarioUseCase).listarConFiltros(
                any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}
