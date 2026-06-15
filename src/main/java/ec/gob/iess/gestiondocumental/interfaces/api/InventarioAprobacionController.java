package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.port.in.InventarioDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.AprobacionRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.RechazoRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.support.HttpOperadorExtractor;
import ec.gob.iess.gestiondocumental.interfaces.api.support.StandardResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Aprobación y rechazo de inventarios (EF-3). Separado del CRUD para claridad JAX-RS/OpenAPI.
 */
@Path("/v1/inventarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Inventarios Documentales", description = "API para gestión de inventarios documentales")
public class InventarioAprobacionController {

    @Inject
    InventarioDocumentalUseCasePort inventarioUseCase;

    @Inject
    StandardResponses responses;

    @RolesAllowed({"SUPERVISOR_SDNGD", "SUPERVISOR"})
    @PUT
    @Path("/{id}/aprobar")
    @Operation(
            summary = "Aprobar inventario",
            description = "Aprueba un inventario. Estados permitidos: 'Registrado' o 'Actualizado'. Requiere SUPERVISOR_SDNGD")
    @APIResponse(responseCode = "200", description = "Inventario aprobado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "400", description = "Estado incorrecto",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "403", description = "Sin rol supervisor",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "404", description = "Inventario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response aprobarInventario(
            @PathParam("id") Long id,
            AprobacionRequest request,
            @HeaderParam(HttpOperadorExtractor.HEADER_OPERADOR_ID) String supervisorIdHeader) {
        return ejecutarOptional(
                () -> inventarioUseCase.aprobarInventario(
                        id,
                        HttpOperadorExtractor.fromHeaderRequired(supervisorIdHeader),
                        request != null ? request.getObservaciones() : null),
                id,
                "Error al aprobar inventario: ",
                "INVENTARIO_APROBACION_ERROR");
    }

    @RolesAllowed({"SUPERVISOR_SDNGD", "SUPERVISOR"})
    @PUT
    @Path("/{id}/rechazar")
    @Operation(
            summary = "Rechazar inventario",
            description = "Rechazo con observaciones obligatorias. Requiere SUPERVISOR_SDNGD")
    @APIResponse(responseCode = "200", description = "Inventario rechazado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "400", description = "Validación",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "403", description = "Sin rol supervisor",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "404", description = "Inventario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response rechazarInventario(
            @PathParam("id") Long id,
            RechazoRequest request,
            @HeaderParam(HttpOperadorExtractor.HEADER_OPERADOR_ID) String supervisorIdHeader) {
        return ejecutarOptional(
                () -> inventarioUseCase.rechazarInventario(
                        id,
                        HttpOperadorExtractor.fromHeaderRequired(supervisorIdHeader),
                        request != null ? request.getObservaciones() : null),
                id,
                "Error al rechazar inventario: ",
                "INVENTARIO_RECHAZO_ERROR");
    }

    private <T> Response ejecutarOptional(
            Supplier<Optional<T>> accion,
            Long id,
            String errorPrefijo,
            String errorCode) {
        try {
            return accion.get()
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Inventario no encontrado con ID: " + id, "INVENTARIO_NOT_FOUND"));
        } catch (NegocioApiException e) {
            throw e;
        } catch (Exception e) {
            return responses.internalServerError(errorPrefijo + e.getMessage(), errorCode);
        }
    }
}
