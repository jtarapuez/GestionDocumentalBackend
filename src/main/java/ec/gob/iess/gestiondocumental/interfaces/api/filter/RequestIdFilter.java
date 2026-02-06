package ec.gob.iess.gestiondocumental.interfaces.api.filter;

import ec.gob.iess.gestiondocumental.interfaces.api.context.RequestContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

import java.util.UUID;

/**
 * Filtro que asigna requestId y path al contexto de la petición (PAS-EST-043).
 * Lee X-Request-Id si viene en el header; si no, genera un UUID.
 */
@Provider
public class RequestIdFilter implements ContainerRequestFilter {

    private static final String HEADER_REQUEST_ID = "X-Request-Id";

    @Inject
    RequestContext requestContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String requestId = requestContext.getHeaderString(HEADER_REQUEST_ID);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        this.requestContext.setRequestId(requestId);

        UriInfo uriInfo = requestContext.getUriInfo();
        if (uriInfo != null && uriInfo.getPath() != null) {
            this.requestContext.setPath(uriInfo.getPath());
        } else {
            this.requestContext.setPath("");
        }
    }
}
