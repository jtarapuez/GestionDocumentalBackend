package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.usecases.SerieDocumentalUseCase;
import ec.gob.iess.gestiondocumental.application.usecases.SubserieDocumentalUseCase;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalResponse;
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
 * Controlador REST para la gestión de series documentales
 * Expone endpoints para crear, actualizar y consultar series
 */
@Path("/v1/series")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Series Documentales", description = "API para gestión de series documentales")
public class SerieDocumentalController {

    @Inject
    SerieDocumentalUseCase serieUseCase;

    @Inject
    SubserieDocumentalUseCase subserieUseCase;

    /**
     * Crea una nueva serie documental
     * 
     * @param request Datos de la serie a crear
     * @return Respuesta con la serie creada
     */
    @POST
    @Operation(
        summary = "Crear serie documental",
        description = "Crea una nueva serie documental. Requiere rol ADMINISTRADOR_SDNGD"
    )
    @APIResponse(
        responseCode = "201",
        description = "Serie creada exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Datos inválidos",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response crearSerie(SerieDocumentalRequest request) {
        try {
            // TODO: Obtener usuario y IP del contexto de seguridad
            String usuarioCedula = "1234567890"; // Temporal hasta implementar Keycloak
            String ipEquipo = "127.0.0.1"; // Temporal
            
            SerieDocumentalResponse serie = serieUseCase.crearSerie(request, usuarioCedula, ipEquipo);
            ApiResponse<SerieDocumentalResponse> response = ApiResponse.success(serie);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al crear serie: " + e.getMessage(),
                "SERIE_CREATE_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Actualiza una serie documental existente
     * 
     * @param id ID de la serie a actualizar
     * @param request Datos actualizados
     * @return Respuesta con la serie actualizada
     */
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Actualizar serie documental",
        description = "Actualiza una serie documental existente. Requiere rol ADMINISTRADOR_SDNGD"
    )
    @APIResponse(
        responseCode = "200",
        description = "Serie actualizada exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Serie no encontrada",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response actualizarSerie(@PathParam("id") Long id, SerieDocumentalRequest request) {
        try {
            // TODO: Obtener usuario del contexto de seguridad
            String usuarioCedula = "1234567890"; // Temporal hasta implementar Keycloak
            
            return serieUseCase.actualizarSerie(id, request, usuarioCedula)
                    .map(serie -> {
                        ApiResponse<SerieDocumentalResponse> response = ApiResponse.success(serie);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Serie no encontrada con ID: " + id,
                            "SERIE_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al actualizar serie: " + e.getMessage(),
                "SERIE_UPDATE_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista todas las series documentales
     * 
     * @param idSeccion Filtro opcional por sección
     * @return Respuesta con lista de series
     */
    @GET
    @Operation(
        summary = "Listar series documentales",
        description = "Retorna una lista de series documentales. Opcionalmente filtradas por sección"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de series obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarSeries(@QueryParam("idSeccion") Long idSeccion) {
        try {
            List<SerieDocumentalResponse> series;
            if (idSeccion != null) {
                series = serieUseCase.listarPorSeccion(idSeccion);
            } else {
                series = serieUseCase.listarActivas();
            }
            ApiResponse<List<SerieDocumentalResponse>> response = ApiResponse.success(series);
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar series: " + e.getMessage(),
                "SERIES_LIST_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Obtiene una serie específica por su ID
     * 
     * @param id ID de la serie
     * @return Respuesta con la serie encontrada
     */
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Obtener serie por ID",
        description = "Retorna una serie documental específica identificada por su ID"
    )
    @APIResponse(
        responseCode = "200",
        description = "Serie obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Serie no encontrada",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response obtenerSeriePorId(@PathParam("id") Long id) {
        try {
            return serieUseCase.obtenerPorId(id)
                    .map(serie -> {
                        ApiResponse<SerieDocumentalResponse> response = ApiResponse.success(serie);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Serie no encontrada con ID: " + id,
                            "SERIE_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al obtener serie: " + e.getMessage(),
                "SERIE_GET_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista las subseries de una serie específica
     * 
     * @param idSerie ID de la serie
     * @return Respuesta con lista de subseries de la serie
     */
    @GET
    @Path("/{idSerie}/subseries")
    @Operation(
        summary = "Listar subseries de una serie",
        description = "Retorna todas las subseries de una serie documental específica"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de subseries obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarSubseriesPorSerie(@PathParam("idSerie") Long idSerie) {
        try {
            List<SubserieDocumentalResponse> subseries = subserieUseCase.listarPorSerie(idSerie);
            ApiResponse<List<SubserieDocumentalResponse>> response = ApiResponse.success(subseries);
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar subseries: " + e.getMessage(),
                "SUBSERIES_LIST_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }
}


