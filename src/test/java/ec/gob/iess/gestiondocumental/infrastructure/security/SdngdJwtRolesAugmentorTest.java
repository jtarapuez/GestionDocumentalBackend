package ec.gob.iess.gestiondocumental.infrastructure.security;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SdngdJwtRolesAugmentorTest {

    @Test
    @DisplayName("fusiona rol desde cabecera ec-iess-role-identifier capturada en RoutingContext")
    void augmentaRolesDesdeHeaderInstitucional() {
        SdngdJwtRolesAugmentor augmentor = new SdngdJwtRolesAugmentor();

        SecurityIdentity base = QuarkusSecurityIdentity.builder()
                .setPrincipal((Principal) () -> "adminHeader")
                .setAnonymous(false)
                .build();

        RoutingContext routingContext = mock(RoutingContext.class);
        HttpServerRequest request = mock(HttpServerRequest.class);
        when(routingContext.request()).thenReturn(request);
        when(routingContext.get(SdngdRoleHeaderHttpFilter.ROUTING_CONTEXT_ATTR))
                .thenReturn(SdngdRoles.ADMINISTRADOR_SDNGD);

        AuthenticationRequestContext authContext = mock(AuthenticationRequestContext.class);
        SecurityIdentity augmented = augmentor
                .augment(base, authContext, Map.of(HttpSecurityUtils.ROUTING_CONTEXT_ATTRIBUTE, routingContext))
                .await()
                .indefinitely();

        assertTrue(augmented.hasRole(SdngdRoles.ADMINISTRADOR_SDNGD));
        assertFalse(augmented.isAnonymous());
    }
}
