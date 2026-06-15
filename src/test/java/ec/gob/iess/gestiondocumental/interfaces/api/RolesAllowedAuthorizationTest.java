package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.port.in.InventarioDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.infrastructure.security.SdngdRoles;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Fase S4: {@code @RolesAllowed} por módulo — operador no puede aprobar; supervisor sí (con auth de test).
 */
@QuarkusTest
class RolesAllowedAuthorizationTest {

    @InjectMock
    InventarioDocumentalUseCasePort inventarioUseCase;

    @Test
    @DisplayName("operador con JWT de test no puede PUT aprobar → 403 AUTH_FORBIDDEN")
    @TestSecurity(user = "operador", roles = {SdngdRoles.OPERADOR_SDNGD})
    void operadorNoPuedeAprobarInventario() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .header("X-Operador-Id", "1712345678")
                .body("{\"observaciones\":\"test\"}")
                .when()
                .put("/api/v1/inventarios/1/aprobar")
                .then()
                .statusCode(403)
                .body("error.code", equalTo("AUTH_FORBIDDEN"));
    }

    @Test
    @DisplayName("supervisor con JWT de test puede invocar PUT aprobar (404 si no existe inventario)")
    @TestSecurity(user = "supervisor", roles = {SdngdRoles.SUPERVISOR_SDNGD})
    void supervisorPuedeInvocarAprobarInventario() {
        when(inventarioUseCase.aprobarInventario(eq(999999L), eq("1798765432"), any()))
                .thenReturn(Optional.empty());

        given()
                .contentType(JSON)
                .accept(JSON)
                .header("X-Operador-Id", "1798765432")
                .body("{\"observaciones\":\"test\"}")
                .when()
                .put("/api/v1/inventarios/999999/aprobar")
                .then()
                .statusCode(404)
                .body("error.code", equalTo("INVENTARIO_NOT_FOUND"));
    }

    @Test
    @DisplayName("operador con OPERADOR_SDNGD puede registrar inventario → no 403")
    @TestSecurity(user = "operador", roles = {SdngdRoles.OPERADOR_SDNGD})
    void operadorPuedeRegistrarInventario() {
        when(inventarioUseCase.registrarInventario(any(), eq("1712345678"), any()))
                .thenReturn(new InventarioDocumentalResponse());

        given()
                .contentType(JSON)
                .accept(JSON)
                .header("X-Operador-Id", "1712345678")
                .body("{\"idSeccion\":1,\"idSerie\":1,\"numeroExpediente\":\"EXP-TEST-001\"}")
                .when()
                .post("/api/v1/inventarios")
                .then()
                .statusCode(org.hamcrest.Matchers.not(403));
    }

    @Test
    @DisplayName("operador no puede crear serie → 403")
    @TestSecurity(user = "operador", roles = {SdngdRoles.OPERADOR_SDNGD})
    void operadorNoPuedeCrearSerie() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .header("X-Operador-Id", "1712345678")
                .body("{\"idSeccion\":1,\"nombreSerie\":\"Test\"}")
                .when()
                .post("/api/v1/series")
                .then()
                .statusCode(403)
                .body("error.code", equalTo("AUTH_FORBIDDEN"));
    }

    @Test
    @DisplayName("administrador puede invocar crear serie (400/500 si payload incompleto, no 403)")
    @TestSecurity(user = "admin", roles = {SdngdRoles.ADMINISTRADOR_SDNGD})
    void administradorPuedeInvocarCrearSerie() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .header("X-Operador-Id", "1712345678")
                .body("{\"idSeccion\":1,\"nombreSerie\":\"Test\"}")
                .when()
                .post("/api/v1/series")
                .then()
                .statusCode(org.hamcrest.Matchers.not(403));
    }
}
