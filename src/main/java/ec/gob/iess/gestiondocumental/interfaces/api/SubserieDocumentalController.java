package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.port.in.SubserieDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.support.RestSecurityPlaceholder;
import ec.gob.iess.gestiondocumental.interfaces.api.support.StandardResponses;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * Adaptador REST: subseries. {@link StandardResponses} + placeholder de seguridad.
 */
@Path("/v1/subseries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Subseries Documentales", description = "API para gestión de subseries documentales")
public class SubserieDocumentalController {

    @Inject
    SubserieDocumentalUseCasePort subserieUseCase;

    @Inject
    StandardResponses responses;

    @POST
    @Operation(
            summary = "Crear subserie documental",
            description = "Crea una nueva subserie documental. Requiere rol ADMINISTRADOR_SDNGD")
    @APIResponse(
            responseCode = "201",
            description = "Subserie creada exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response crearSubserie(SubserieDocumentalRequest request) {
        try {
            String ipEquipo = "127.0.0.1";
            SubserieDocumentalResponse subserie = subserieUseCase.crearSubserie(
                    request, RestSecurityPlaceholder.ADMIN_CEDULA_TEMPORAL, ipEquipo);
            return responses.created(subserie);
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al crear subserie: " + e.getMessage(), "SUBSERIE_CREATE_ERROR");
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Actualizar subserie documental",
            description = "Actualiza una subserie documental existente. Requiere rol ADMINISTRADOR_SDNGD")
    @APIResponse(
            responseCode = "200",
            description = "Subserie actualizada exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Subserie no encontrada",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response actualizarSubserie(@PathParam("id") Long id, SubserieDocumentalRequest request) {
        try {
            return subserieUseCase.actualizarSubserie(id, request, RestSecurityPlaceholder.ADMIN_CEDULA_TEMPORAL)
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Subserie no encontrada con ID: " + id, "SUBSERIE_NOT_FOUND"));
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al actualizar subserie: " + e.getMessage(), "SUBSERIE_UPDATE_ERROR");
        }
    }

    @GET
    @Operation(
            summary = "Listar subseries documentales",
            description = "Retorna una lista de subseries documentales. Opcionalmente filtradas por serie")
    @APIResponse(
            responseCode = "200",
            description = "Lista de subseries obtenida exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarSubseries(@QueryParam("idSerie") Long idSerie) {
        try {
            List<SubserieDocumentalResponse> subseries = idSerie != null
                    ? subserieUseCase.listarPorSerie(idSerie)
                    : subserieUseCase.listarActivas();
            return responses.ok(subseries);
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar subseries: " + e.getMessage(), "SUBSERIES_LIST_ERROR");
        }
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Obtener subserie por ID",
            description = "Retorna una subserie documental específica identificada por su ID")
    @APIResponse(
            responseCode = "200",
            description = "Subserie obtenida exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Subserie no encontrada",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response obtenerSubseriePorId(@PathParam("id") Long id) {
        try {
            return subserieUseCase.obtenerPorId(id)
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Subserie no encontrada con ID: " + id, "SUBSERIE_NOT_FOUND"));
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al obtener subserie: " + e.getMessage(), "SUBSERIE_GET_ERROR");
        }
    }

    @GET
    @Path("/serie/{idSerie}")
    @Operation(
            summary = "Listar subseries por serie",
            description = "Retorna todas las subseries de una serie documental específica")
    @APIResponse(
            responseCode = "200",
            description = "Lista de subseries obtenida exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarSubseriesPorSerie(@PathParam("idSerie") Long idSerie) {
        try {
            List<SubserieDocumentalResponse> subseries = subserieUseCase.listarPorSerie(idSerie);
            return responses.ok(subseries);
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar subseries: " + e.getMessage(), "SUBSERIES_LIST_ERROR");
        }
    }
}
