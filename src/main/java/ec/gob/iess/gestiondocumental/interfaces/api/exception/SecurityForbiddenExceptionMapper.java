package ec.gob.iess.gestiondocumental.interfaces.api.exception;

import ec.gob.iess.gestiondocumental.infrastructure.security.SecurityCodigosError;
import ec.gob.iess.gestiondocumental.interfaces.api.context.RequestContext;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import io.quarkus.security.ForbiddenException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Mapea denegaciones {@link RolesAllowed} a contrato PAS-EST-043 (403 + código estable).
 */
@Provider
public class SecurityForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Inject
    RequestContext requestContext;

    @Override
    public Response toResponse(ForbiddenException exception) {
        String path = requestContext != null ? requestContext.getPath() : null;
        String requestId = requestContext != null ? requestContext.getRequestId() : null;
        ApiResponse<Object> errorResponse = ApiResponse.error(
                "No tiene permisos para realizar esta operación",
                SecurityCodigosError.AUTH_FORBIDDEN,
                path, requestId);
        return Response.status(Response.Status.FORBIDDEN).entity(errorResponse).build();
    }
}
