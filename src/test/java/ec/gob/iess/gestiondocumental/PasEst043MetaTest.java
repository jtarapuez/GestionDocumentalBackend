package ec.gob.iess.gestiondocumental;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Pruebas de cumplimiento PAS-EST-043: meta con path y requestId en respuestas.
 * Valida que las respuestas de error incluyan meta.path y meta.requestId,
 * y que el header X-Request-Id se propague a meta.requestId.
 * Quarkus configura Rest Assured con el puerto de test automáticamente.
 */
@QuarkusTest
class PasEst043MetaTest {

    @Test
    void metaEnRespuestaErrorIncluyePathYRequestId() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body("{}")
                .when()
                .post("/api/v1/reportes/exportar-pdf")
                .then()
                .statusCode(501)
                .body("meta.path", notNullValue())
                .body("meta.requestId", notNullValue())
                .body("error.code", equalTo("PDF_EXPORT_NOT_IMPLEMENTED"));
    }

    @Test
    void requestIdPropagadoDesdeHeaderXRequestId() {
        String requestId = "test-request-id-12345";
        given()
                .contentType(JSON)
                .accept(JSON)
                .header("X-Request-Id", requestId)
                .body("{}")
                .when()
                .post("/api/v1/reportes/exportar-pdf")
                .then()
                .statusCode(501)
                .body("meta.requestId", equalTo(requestId))
                .body("meta.path", notNullValue());
    }

    @Test
    void metaEnRespuestaErrorExcelIncluyePathYRequestId() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body("{}")
                .when()
                .post("/api/v1/reportes/exportar-excel")
                .then()
                .statusCode(501)
                .body("meta.path", notNullValue())
                .body("meta.requestId", notNullValue())
                .body("error.code", equalTo("EXCEL_EXPORT_NOT_IMPLEMENTED"));
    }
}
