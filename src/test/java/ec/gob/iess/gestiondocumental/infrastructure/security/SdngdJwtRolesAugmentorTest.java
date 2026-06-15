package ec.gob.iess.gestiondocumental.infrastructure.security;

import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import org.jose4j.jwt.JwtClaims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SdngdJwtRolesAugmentorTest {

    @Test
    @DisplayName("augmenta roles SDNGD desde claims user.rolesDisponibles del JWT")
    void augmentaRolesDesdeJwtKeycloak() {
        SdngdJwtRolesAugmentor augmentor = new SdngdJwtRolesAugmentor();

        JwtClaims claims = new JwtClaims();
        claims.setClaim("user", Map.of(
                "rolesDisponibles", List.of("ADMINISTRADOR_SDNGD"),
                "rol", "ADMINISTRADOR_SDNGD"));

        OidcJwtCallerPrincipal principal = mock(OidcJwtCallerPrincipal.class);
        when(principal.getName()).thenReturn("juan.pascal");
        when(principal.getClaims()).thenReturn(claims);

        SecurityIdentity base = QuarkusSecurityIdentity.builder()
                .setPrincipal((Principal) principal)
                .setAnonymous(false)
                .build();

        AuthenticationRequestContext authContext = mock(AuthenticationRequestContext.class);
        SecurityIdentity augmented = augmentor
                .augment(base, authContext)
                .await()
                .indefinitely();

        assertTrue(augmented.hasRole(SdngdRoles.ADMINISTRADOR_SDNGD));
        assertFalse(augmented.isAnonymous());
    }
}
