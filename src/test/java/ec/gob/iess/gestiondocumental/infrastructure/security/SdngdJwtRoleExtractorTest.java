package ec.gob.iess.gestiondocumental.infrastructure.security;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SdngdJwtRoleExtractorTest {

    @Test
    @DisplayName("extrae ADMINISTRADOR_SDNGD desde user.rol en access token")
    void extraeRolInstitucionalDesdeUserRol() {
        JsonWebToken jwt = mock(JsonWebToken.class);
        when(jwt.getClaim("user")).thenReturn(Map.of("rol", "ADMINISTRADOR_SDNGD"));

        Set<String> roles = SdngdJwtRoleExtractor.extract(jwt);
        assertTrue(roles.contains("ADMINISTRADOR_SDNGD"));
    }

    @Test
    @DisplayName("fusiona rolesDisponibles, realm y resource_access")
    void fusionaMultiplesFuentes() {
        Map<String, Object> user = new HashMap<>();
        user.put("rolesDisponibles", List.of("OPERADOR_SDNGD"));
        user.put("rol", "ADMINISTRADOR_SDNGD");

        JsonWebToken jwt = mock(JsonWebToken.class);
        when(jwt.getClaim("user")).thenReturn(user);
        when(jwt.getClaim("realm_access")).thenReturn(Map.of("roles", List.of("default-roles-plantilla-front")));
        when(jwt.getClaim("resource_access")).thenReturn(Map.of(
                "plantilla-front-cliente", Map.of("roles", List.of("SUPERVISOR"))));

        Set<String> roles = SdngdJwtRoleExtractor.extract(jwt);
        assertTrue(roles.contains("OPERADOR_SDNGD"));
        assertTrue(roles.contains("ADMINISTRADOR_SDNGD"));
        assertTrue(roles.contains("SUPERVISOR"));
        assertFalse(roles.contains("default-roles-plantilla-front"));
    }

    @Test
    @DisplayName("excluye roles de ruido Keycloak del JWT")
    void excluyeRuidoKeycloak() {
        JsonWebToken jwt = mock(JsonWebToken.class);
        when(jwt.getClaim("realm_access")).thenReturn(Map.of("roles",
                List.of("default-roles-plantilla-front", "offline_access", "OPERADOR_SDNGD")));

        Set<String> roles = SdngdJwtRoleExtractor.extract(jwt);
        assertTrue(roles.contains("OPERADOR_SDNGD"));
        assertFalse(roles.contains("default-roles-plantilla-front"));
        assertFalse(roles.contains("offline_access"));
    }

    @Test
    @DisplayName("parsea ec-iess-role-identifier con varios roles")
    void parseaHeaderRoles() {
        Set<String> roles = SdngdJwtRoleExtractor.parseRoleHeader("ADMINISTRADOR_SDNGD,OPERADOR");
        assertTrue(roles.contains("ADMINISTRADOR_SDNGD"));
        assertTrue(roles.contains("OPERADOR"));
    }
}
