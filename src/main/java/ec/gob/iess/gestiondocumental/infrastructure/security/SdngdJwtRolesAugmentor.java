package ec.gob.iess.gestiondocumental.infrastructure.security;

import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Complementa roles de {@link RolesAllowed} con claims del access token IESS
 * y cabecera {@value SdngdJwtRoleExtractor#HEADER_ROLE_IDENTIFIER} del MFE.
 */
@ApplicationScoped
public class SdngdJwtRolesAugmentor implements SecurityIdentityAugmentor {

    private static final Logger LOG = Logger.getLogger(SdngdJwtRolesAugmentor.class);

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context,
            Map<String, Object> attributes) {
        return Uni.createFrom().item(() -> augmentBlocking(identity, attributes));
    }

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        return augment(identity, context, Map.of());
    }

    private SecurityIdentity augmentBlocking(SecurityIdentity identity, Map<String, Object> attributes) {
        if (identity.isAnonymous()) {
            return identity;
        }

        Set<String> extracted = new LinkedHashSet<>();

        if (identity.getPrincipal() instanceof OidcJwtCallerPrincipal oidcPrincipal) {
            extracted.addAll(SdngdJwtRoleExtractor.extract(oidcPrincipal.getClaims()));
        } else if (identity.getPrincipal() instanceof JsonWebToken jwt) {
            extracted.addAll(SdngdJwtRoleExtractor.extract(jwt));
        }

        RoutingContext routingContext = HttpSecurityUtils.getRoutingContextAttribute(attributes);
        if (routingContext == null) {
            routingContext = identity.getAttribute(RoutingContext.class.getName());
        }
        if (routingContext == null) {
            Object fromAttr = attributes.get(HttpSecurityUtils.ROUTING_CONTEXT_ATTRIBUTE);
            if (fromAttr instanceof RoutingContext rc) {
                routingContext = rc;
            }
        }
        if (routingContext != null) {
            String headerValue = routingContext.get(SdngdRoleHeaderHttpFilter.ROUTING_CONTEXT_ATTR);
            if (headerValue == null || headerValue.isBlank()) {
                headerValue = routingContext.request().getHeader(SdngdJwtRoleExtractor.HEADER_ROLE_IDENTIFIER);
            }
            extracted.addAll(SdngdJwtRoleExtractor.parseRoleHeader(headerValue));
        }

        if (extracted.isEmpty()) {
            LOG.debugf("Sin roles SDNGD en JWT ni header para principal=%s",
                    identity.getPrincipal() != null ? identity.getPrincipal().getName() : "?");
            return identity;
        }

        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
        for (String role : extracted) {
            if (!identity.hasRole(role)) {
                builder.addRole(role);
            }
        }
        LOG.debugf("Roles SDNGD augmentados para %s: %s",
                identity.getPrincipal() != null ? identity.getPrincipal().getName() : "?",
                extracted);
        return builder.build();
    }
}
