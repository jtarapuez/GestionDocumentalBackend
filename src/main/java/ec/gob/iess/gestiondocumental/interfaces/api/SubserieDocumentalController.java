package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.usecases.SubserieDocumentalUseCase;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalRequest;
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
 * Controlador REST para la gestión de subseries documentales
 * Expone endpoints para crear, actualizar y consultar subseries
 */
@Path("/v1/subseries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Subseries Documentales", description = "API para gestión de subseries documentales")
public class SubserieDocumentalController {

    @Inject
    SubserieDocumentalUseCase subserieUseCase;

    /**
     * Crea una nueva subserie documental
     * 
     * @param request Datos de la subserie a crear
     * @return Respuesta con la subserie creada
     */
    @POST
    @Operation(
        summary = "Crear subserie documental",
        description = "Crea una nueva subserie documental. Requiere rol ADMINISTRADOR_SDNGD"
    )
    @APIResponse(
        responseCode = "201",
        description = "Subserie creada exitosamente",
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
    public Response crearSubserie(SubserieDocumentalRequest request) {
        try {
            // TODO: Obtener usuario y IP del contexto de seguridad
            String usuarioCedula = "1234567890"; // Temporal hasta implementar Keycloak
            String ipEquipo = "127.0.0.1"; // Temporal
            
            SubserieDocumentalResponse subserie = subserieUseCase.crearSubserie(request, usuarioCedula, ipEquipo);
            ApiResponse<SubserieDocumentalResponse> response = ApiResponse.success(subserie);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al crear subserie: " + e.getMessage(),
                "SUBSERIE_CREATE_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Actualiza una subserie documental existente
     * 
     * @param id ID de la subserie a actualizar
     * @param request Datos actualizados
     * @return Respuesta con la subserie actualizada
     */
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Actualizar subserie documental",
        description = "Actualiza una subserie documental existente. Requiere rol ADMINISTRADOR_SDNGD"
    )
    @APIResponse(
        responseCode = "200",
        description = "Subserie actualizada exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Subserie no encontrada",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response actualizarSubserie(@PathParam("id") Long id, SubserieDocumentalRequest request) {
        try {
            // TODO: Obtener usuario del contexto de seguridad
            String usuarioCedula = "1234567890"; // Temporal hasta implementar Keycloak
            
            return subserieUseCase.actualizarSubserie(id, request, usuarioCedula)
                    .map(subserie -> {
                        ApiResponse<SubserieDocumentalResponse> response = ApiResponse.success(subserie);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Subserie no encontrada con ID: " + id,
                            "SUBSERIE_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al actualizar subserie: " + e.getMessage(),
                "SUBSERIE_UPDATE_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista todas las subseries documentales
     * 
     * @param idSerie Filtro opcional por serie
     * @return Respuesta con lista de subseries
     */
    @GET
    @Operation(
        summary = "Listar subseries documentales",
        description = "Retorna una lista de subseries documentales. Opcionalmente filtradas por serie"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de subseries obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarSubseries(@QueryParam("idSerie") Long idSerie) {
        try {
            List<SubserieDocumentalResponse> subseries;
            if (idSerie != null) {
                subseries = subserieUseCase.listarPorSerie(idSerie);
            } else {
                subseries = subserieUseCase.listarActivas();
            }
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

    /**
     * Obtiene una subserie específica por su ID
     * 
     * @param id ID de la subserie
     * @return Respuesta con la subserie encontrada
     */
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Obtener subserie por ID",
        description = "Retorna una subserie documental específica identificada por su ID"
    )
    @APIResponse(
        responseCode = "200",
        description = "Subserie obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Subserie no encontrada",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response obtenerSubseriePorId(@PathParam("id") Long id) {
        try {
            return subserieUseCase.obtenerPorId(id)
                    .map(subserie -> {
                        ApiResponse<SubserieDocumentalResponse> response = ApiResponse.success(subserie);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Subserie no encontrada con ID: " + id,
                            "SUBSERIE_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al obtener subserie: " + e.getMessage(),
                "SUBSERIE_GET_ERROR"
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
    @Path("/serie/{idSerie}")
    @Operation(
        summary = "Listar subseries por serie",
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

