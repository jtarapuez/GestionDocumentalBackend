package ec.gob.iess.gestiondocumental.interfaces.api.support;

import ec.gob.iess.gestiondocumental.interfaces.api.context.RequestContext;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * Construye {@link Response} con {@link ApiResponse} y meta (path, requestId) según PAS-EST-043.
 * Centraliza el envoltorio JSON para mantener controladores como adaptadores delgados.
 */
@RequestScoped
public class StandardResponses {

    @Inject
    RequestContext requestContext;

    public <T> Response ok(T data) {
        return Response.ok(wrapSuccess(data)).build();
    }

    public <T> Response created(T data) {
        return Response.status(Response.Status.CREATED).entity(wrapSuccess(data)).build();
    }

    public Response notFound(String message, String code) {
        return Response.status(Response.Status.NOT_FOUND).entity(wrapError(message, code)).build();
    }

    public Response badRequest(String message, String code) {
        return Response.status(Response.Status.BAD_REQUEST).entity(wrapError(message, code)).build();
    }

    public Response internalServerError(String message, String code) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(wrapError(message, code)).build();
    }

    /**
     * HTTP 501 (funcionalidad no implementada; p. ej. exportación PDF/Excel pendiente).
     */
    public Response notImplemented(String message, String code) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(wrapError(message, code)).build();
    }

    private <T> ApiResponse<T> wrapSuccess(T data) {
        return ApiResponse.success(data, requestContext.getPath(), requestContext.getRequestId());
    }

    private ApiResponse<Object> wrapError(String message, String code) {
        return ApiResponse.error(message, code, requestContext.getPath(), requestContext.getRequestId());
    }
}
