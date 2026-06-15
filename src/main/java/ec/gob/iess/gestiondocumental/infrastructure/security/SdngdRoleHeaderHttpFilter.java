package ec.gob.iess.gestiondocumental.infrastructure.security;

import io.quarkus.vertx.http.runtime.filters.Filter;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Captura {@value SdngdJwtRoleExtractor#HEADER_ROLE_IDENTIFIER} al inicio de la cadena HTTP
 * para que {@link SdngdJwtRolesAugmentor} pueda leerlo con auth proactiva.
 */
@ApplicationScoped
public class SdngdRoleHeaderHttpFilter implements Filter {

    public static final String ROUTING_CONTEXT_ATTR = "sdngd.ec-iess-role-identifier";

    @Override
    public Handler<RoutingContext> getHandler() {
        return routingContext -> {
            String header = routingContext.request().getHeader(SdngdJwtRoleExtractor.HEADER_ROLE_IDENTIFIER);
            if (header != null && !header.isBlank()) {
                routingContext.put(ROUTING_CONTEXT_ATTR, header);
            }
            routingContext.next();
        };
    }

    @Override
    public int getPriority() {
        return 90;
    }
}
