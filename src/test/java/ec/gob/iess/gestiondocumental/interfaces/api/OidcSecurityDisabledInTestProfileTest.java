package ec.gob.iess.gestiondocumental.interfaces.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * OIDC desactivado en perfil test ({@code %test.quarkus.oidc.enabled=false}).
 * Valida que la API no exige Bearer en tests automatizados.
 */
@QuarkusTest
class OidcSecurityDisabledInTestProfileTest {

    @Test
    @DisplayName("perfil test: POST reportes sin Bearer no devuelve 401 (OIDC off)")
    void apiSinBearerEnPerfilTestNoRequiereJwt() {
        RestAssured.given()
                .contentType(JSON)
                .accept(JSON)
                .body("{}")
                .when()
                .post("/api/v1/reportes/exportar-pdf")
                .then()
                .statusCode(501)
                .body("meta.requestId", notNullValue())
                .body("error.code", equalTo("PDF_EXPORT_NOT_IMPLEMENTED"));
    }
}
