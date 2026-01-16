package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.usecases.InventarioDocumentalUseCase;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.AprobacionRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.RechazoRequest;
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
 * Controlador REST para la gestión de inventarios documentales
 * Expone endpoints para crear, actualizar y consultar inventarios
 */
@Path("/v1/inventarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Inventarios Documentales", description = "API para gestión de inventarios documentales")
public class InventarioDocumentalController {

    @Inject
    InventarioDocumentalUseCase inventarioUseCase;

    /**
     * Registra un nuevo inventario documental
     * 
     * @param request Datos del inventario a crear
     * @return Respuesta con el inventario creado
     */
    @POST
    @Operation(
        summary = "Registrar inventario documental",
        description = "Registra un nuevo inventario documental. Requiere rol OPERADOR_SDNGD. " +
                      "No se puede registrar si hay pendientes vencidos (más de 5 días)"
    )
    @APIResponse(
        responseCode = "201",
        description = "Inventario registrado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Datos inválidos o pendientes vencidos",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response registrarInventario(InventarioDocumentalRequest request) {
        try {
            // TODO: Obtener usuario y IP del contexto de seguridad
            String usuarioCedula = "1234567890"; // Temporal hasta implementar Keycloak
            String ipEquipo = "127.0.0.1"; // Temporal
            
            InventarioDocumentalResponse inventario = inventarioUseCase.registrarInventario(request, usuarioCedula, ipEquipo);
            ApiResponse<InventarioDocumentalResponse> response = ApiResponse.success(inventario);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (IllegalStateException e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                e.getMessage(),
                "INVENTARIO_VALIDATION_ERROR"
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al registrar inventario: " + e.getMessage(),
                "INVENTARIO_CREATE_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Actualiza un inventario existente (solo Pendiente de Aprobación)
     * 
     * @param id ID del inventario a actualizar
     * @param request Datos actualizados
     * @return Respuesta con el inventario actualizado
     */
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Actualizar inventario documental",
        description = "Actualiza un inventario documental existente. Solo se pueden actualizar " +
                      "inventarios en estado 'Pendiente de Aprobación' y dentro de los 5 días calendario. " +
                      "Requiere rol OPERADOR_SDNGD"
    )
    @APIResponse(
        responseCode = "200",
        description = "Inventario actualizado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "No se puede actualizar (estado incorrecto o vencido)",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Inventario no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response actualizarInventario(@PathParam("id") Long id, InventarioDocumentalRequest request) {
        try {
            // TODO: Obtener usuario del contexto de seguridad
            String usuarioCedula = "1234567890"; // Temporal hasta implementar Keycloak
            
            return inventarioUseCase.actualizarInventario(id, request, usuarioCedula)
                    .map(inventario -> {
                        ApiResponse<InventarioDocumentalResponse> response = ApiResponse.success(inventario);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Inventario no encontrado con ID: " + id,
                            "INVENTARIO_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (IllegalStateException e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                e.getMessage(),
                "INVENTARIO_UPDATE_VALIDATION_ERROR"
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al actualizar inventario: " + e.getMessage(),
                "INVENTARIO_UPDATE_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Obtiene un inventario específico por su ID
     * 
     * @param id ID del inventario
     * @return Respuesta con el inventario encontrado
     */
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Obtener inventario por ID",
        description = "Retorna un inventario documental específico identificado por su ID"
    )
    @APIResponse(
        responseCode = "200",
        description = "Inventario obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Inventario no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response obtenerInventarioPorId(@PathParam("id") Long id) {
        try {
            return inventarioUseCase.obtenerPorId(id)
                    .map(inventario -> {
                        ApiResponse<InventarioDocumentalResponse> response = ApiResponse.success(inventario);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Inventario no encontrado con ID: " + id,
                            "INVENTARIO_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al obtener inventario: " + e.getMessage(),
                "INVENTARIO_GET_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista inventarios con filtros opcionales
     * 
     * @param idSeccion Filtro por sección
     * @param idSerie Filtro por serie
     * @param idSubserie Filtro por subserie
     * @param numeroExpediente Filtro por número de expediente
     * @param estado Filtro por estado
     * @return Respuesta con lista de inventarios
     */
    @GET
    @Operation(
        summary = "Listar inventarios documentales",
        description = "Retorna una lista de inventarios documentales con filtros opcionales"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de inventarios obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarInventarios(
            @QueryParam("idSeccion") Long idSeccion,
            @QueryParam("idSerie") Long idSerie,
            @QueryParam("idSubserie") Long idSubserie,
            @QueryParam("numeroExpediente") String numeroExpediente,
            @QueryParam("estado") String estado) {
        try {
            List<InventarioDocumentalResponse> inventarios = inventarioUseCase.listarConFiltros(
                idSeccion, idSerie, idSubserie, numeroExpediente, estado, 
                null, null, null, null, null, null, null, null, null, null, null
            );
            ApiResponse<List<InventarioDocumentalResponse>> response = ApiResponse.success(inventarios);
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar inventarios: " + e.getMessage(),
                "INVENTARIOS_LIST_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista inventarios pendientes de aprobación (para supervisores)
     * 
     * @return Respuesta con lista de inventarios pendientes
     */
    @GET
    @Path("/pendientes-aprobacion")
    @Operation(
        summary = "Listar inventarios pendientes de aprobación",
        description = "Retorna una lista de inventarios pendientes de aprobación. Requiere rol SUPERVISOR_SDNGD"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de inventarios pendientes obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarPendientesAprobacion() {
        try {
            List<InventarioDocumentalResponse> inventarios = inventarioUseCase.listarPendientesAprobacion();
            ApiResponse<List<InventarioDocumentalResponse>> response = ApiResponse.success(inventarios);
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar inventarios pendientes: " + e.getMessage(),
                "INVENTARIOS_PENDIENTES_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista inventarios pendientes del operador actual
     * 
     * @return Respuesta con lista de inventarios pendientes del operador
     */
    @GET
    @Path("/pendientes")
    @Operation(
        summary = "Listar inventarios pendientes del operador",
        description = "Retorna una lista de inventarios pendientes de aprobación del operador actual. " +
                      "Requiere rol OPERADOR_SDNGD"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de inventarios pendientes obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarPendientes() {
        try {
            // TODO: Obtener usuario del contexto de seguridad
            String usuarioCedula = "1234567890"; // Temporal hasta implementar Keycloak
            
            List<InventarioDocumentalResponse> inventarios = inventarioUseCase.listarPendientesPorOperador(usuarioCedula);
            ApiResponse<List<InventarioDocumentalResponse>> response = ApiResponse.success(inventarios);
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar inventarios pendientes: " + e.getMessage(),
                "INVENTARIOS_PENDIENTES_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Aprueba un inventario (solo supervisores)
     * 
     * @param id ID del inventario a aprobar
     * @param request Datos de la aprobación
     * @return Respuesta con el inventario aprobado
     */
    @PUT
    @Path("/{id}/aprobar")
    @Operation(
        summary = "Aprobar inventario",
        description = "Aprueba un inventario documental. Solo se pueden aprobar inventarios en estado " +
                      "'Registrado' o 'Actualizado'. Requiere rol SUPERVISOR_SDNGD"
    )
    @APIResponse(
        responseCode = "200",
        description = "Inventario aprobado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "No se puede aprobar (estado incorrecto)",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Inventario no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response aprobarInventario(@PathParam("id") Long id, AprobacionRequest request) {
        try {
            // TODO: Obtener usuario del contexto de seguridad
            String usuarioCedula = "0987654321"; // Temporal hasta implementar Keycloak
            
            return inventarioUseCase.aprobarInventario(id, usuarioCedula, 
                    request != null ? request.getObservaciones() : null)
                    .map(inventario -> {
                        ApiResponse<InventarioDocumentalResponse> response = ApiResponse.success(inventario);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Inventario no encontrado con ID: " + id,
                            "INVENTARIO_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (IllegalStateException e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                e.getMessage(),
                "INVENTARIO_APROBACION_VALIDATION_ERROR"
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al aprobar inventario: " + e.getMessage(),
                "INVENTARIO_APROBACION_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Rechaza un inventario (Pendiente de Aprobación)
     * 
     * @param id ID del inventario a rechazar
     * @param request Datos del rechazo (observaciones obligatorias)
     * @return Respuesta con el inventario rechazado
     */
    @PUT
    @Path("/{id}/rechazar")
    @Operation(
        summary = "Rechazar inventario",
        description = "Rechaza un inventario documental, cambiando su estado a 'Pendiente de Aprobación'. " +
                      "Solo se pueden rechazar inventarios en estado 'Registrado' o 'Actualizado'. " +
                      "Requiere rol SUPERVISOR_SDNGD. Las observaciones son obligatorias."
    )
    @APIResponse(
        responseCode = "200",
        description = "Inventario rechazado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "No se puede rechazar (estado incorrecto o observaciones faltantes)",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Inventario no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response rechazarInventario(@PathParam("id") Long id, RechazoRequest request) {
        try {
            if (request == null || request.getObservaciones() == null || request.getObservaciones().trim().isEmpty()) {
                ApiResponse<Object> errorResponse = ApiResponse.error(
                    "Las observaciones del rechazo son obligatorias",
                    "RECHAZO_OBSERVACIONES_REQUIRED"
                );
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .build();
            }

            // TODO: Obtener usuario del contexto de seguridad
            String usuarioCedula = "0987654321"; // Temporal hasta implementar Keycloak
            
            return inventarioUseCase.rechazarInventario(id, usuarioCedula, request.getObservaciones())
                    .map(inventario -> {
                        ApiResponse<InventarioDocumentalResponse> response = ApiResponse.success(inventario);
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Inventario no encontrado con ID: " + id,
                            "INVENTARIO_NOT_FOUND"
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (IllegalArgumentException e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                e.getMessage(),
                "RECHAZO_VALIDATION_ERROR"
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        } catch (IllegalStateException e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                e.getMessage(),
                "INVENTARIO_RECHAZO_VALIDATION_ERROR"
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al rechazar inventario: " + e.getMessage(),
                "INVENTARIO_RECHAZO_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }
}


