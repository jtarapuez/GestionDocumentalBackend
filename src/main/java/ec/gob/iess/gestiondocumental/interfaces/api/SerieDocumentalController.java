package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.port.in.SerieDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.in.SubserieDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalResponse;
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
import org.jboss.logging.Logger;

import java.util.List;

/**
 * Adaptador REST: series y subseries por serie. {@link StandardResponses} + placeholder de seguridad.
 */
@Path("/v1/series")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Series Documentales", description = "API para gestión de series documentales")
public class SerieDocumentalController {

    private static final Logger LOG = Logger.getLogger(SerieDocumentalController.class);

    @Inject
    SerieDocumentalUseCasePort serieUseCase;

    @Inject
    SubserieDocumentalUseCasePort subserieUseCase;

    @Inject
    StandardResponses responses;

    @POST
    @Operation(
            summary = "Crear serie documental",
            description = "Crea una nueva serie documental. Requiere rol ADMINISTRADOR_SDNGD")
    @APIResponse(
            responseCode = "201",
            description = "Serie creada exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response crearSerie(SerieDocumentalRequest request) {
        try {
            String ipEquipo = "127.0.0.1";
            SerieDocumentalResponse serie = serieUseCase.crearSerie(
                    request, RestSecurityPlaceholder.ADMIN_CEDULA_TEMPORAL, ipEquipo);
            return responses.created(serie);
        } catch (NegocioApiException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("crearSerie falló", e);
            return responses.internalServerError(
                    "Error al crear serie: " + mensajeCausaRaiz(e), "SERIE_CREATE_ERROR");
        }
    }

    /** Expone el mensaje de la causa más interna (p. ej. ORA-02291 FK) para diagnóstico. */
    private static String mensajeCausaRaiz(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        String m = t.getMessage();
        return m != null ? m : String.valueOf(e);
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Actualizar serie documental",
            description = "Actualiza una serie documental existente. Requiere rol ADMINISTRADOR_SDNGD")
    @APIResponse(
            responseCode = "200",
            description = "Serie actualizada exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Serie no encontrada",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response actualizarSerie(@PathParam("id") Long id, SerieDocumentalRequest request) {
        try {
            return serieUseCase.actualizarSerie(id, request, RestSecurityPlaceholder.ADMIN_CEDULA_TEMPORAL)
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Serie no encontrada con ID: " + id, "SERIE_NOT_FOUND"));
        } catch (NegocioApiException e) {
            throw e;
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al actualizar serie: " + e.getMessage(), "SERIE_UPDATE_ERROR");
        }
    }

    @GET
    @Operation(
            summary = "Listar series documentales",
            description = "Retorna una lista de series documentales. Opcionalmente filtradas por sección")
    @APIResponse(
            responseCode = "200",
            description = "Lista de series obtenida exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarSeries(@QueryParam("idSeccion") Long idSeccion) {
        try {
            List<SerieDocumentalResponse> series = idSeccion != null
                    ? serieUseCase.listarPorSeccion(idSeccion)
                    : serieUseCase.listarActivas();
            return responses.ok(series);
        } catch (NegocioApiException e) {
            throw e;
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar series: " + e.getMessage(), "SERIES_LIST_ERROR");
        }
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Obtener serie por ID",
            description = "Retorna una serie documental específica identificada por su ID")
    @APIResponse(
            responseCode = "200",
            description = "Serie obtenida exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Serie no encontrada",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response obtenerSeriePorId(@PathParam("id") Long id) {
        try {
            return serieUseCase.obtenerPorId(id)
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Serie no encontrada con ID: " + id, "SERIE_NOT_FOUND"));
        } catch (NegocioApiException e) {
            throw e;
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al obtener serie: " + e.getMessage(), "SERIE_GET_ERROR");
        }
    }

    @GET
    @Path("/{idSerie}/subseries")
    @Operation(
            summary = "Listar subseries de una serie",
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
        } catch (NegocioApiException e) {
            throw e;
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar subseries: " + e.getMessage(), "SUBSERIES_LIST_ERROR");
        }
    }
}
