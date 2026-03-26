package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.port.in.InventarioDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.AprobacionRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.RechazoRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.support.HttpOperadorExtractor;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Adaptador REST (driving): HTTP ↔ {@link InventarioDocumentalUseCasePort}.
 * Respuestas homogéneas vía {@link StandardResponses} y PAS-EST-043.
 */
@Path("/v1/inventarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Inventarios Documentales", description = "API para gestión de inventarios documentales")
public class InventarioDocumentalController {

    private static final Logger LOG = Logger.getLogger(InventarioDocumentalController.class);

    @Inject
    InventarioDocumentalUseCasePort inventarioUseCase;

    @Inject
    StandardResponses responses;

    @POST
    @Operation(
            summary = "Registrar inventario documental",
            description = "Registra un nuevo inventario documental. Requiere rol OPERADOR_SDNGD. "
                    + "No se puede registrar si hay pendientes vencidos (más de 5 días)")
    @APIResponse(responseCode = "201", description = "Inventario registrado exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "400", description = "Datos inválidos o pendientes vencidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response registrarInventario(InventarioDocumentalRequest request,
                                        @HeaderParam("X-Operador-Id") String operadorIdHeader) {
        return ejecutar(
                () -> {
                    String operadorId = HttpOperadorExtractor.fromHeaderOrFallback(operadorIdHeader);
                    LOG.debugf("registrarInventario: longitud operadorId=%d", operadorId.length());
                    return inventarioUseCase.registrarInventario(request, operadorId, "127.0.0.1");
                },
                responses::created,
                "INVENTARIO_VALIDATION_ERROR",
                "Error al registrar inventario: ",
                "INVENTARIO_CREATE_ERROR");
    }

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Actualizar inventario documental",
            description = "Actualiza un inventario documental existente. Solo se pueden actualizar "
                    + "inventarios en estado 'Pendiente de Aprobación' y dentro de los 5 días calendario. "
                    + "Requiere rol OPERADOR_SDNGD")
    @APIResponse(responseCode = "200", description = "Inventario actualizado exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "400", description = "No se puede actualizar (estado incorrecto o vencido)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "404", description = "Inventario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response actualizarInventario(@PathParam("id") Long id,
                                         InventarioDocumentalRequest request,
                                         @HeaderParam("X-Operador-Id") String operadorIdHeader) {
        return ejecutarOptional(
                () -> {
                    String operadorId = HttpOperadorExtractor.fromHeaderOrFallback(operadorIdHeader);
                    LOG.debugf("actualizarInventario: longitud operadorId=%d", operadorId.length());
                    return inventarioUseCase.actualizarInventario(id, request, operadorId);
                },
                id,
                "INVENTARIO_UPDATE_VALIDATION_ERROR",
                "Error al actualizar inventario: ",
                "INVENTARIO_UPDATE_ERROR");
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener inventario por ID",
            description = "Retorna un inventario documental específico identificado por su ID")
    @APIResponse(responseCode = "200", description = "Inventario obtenido exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "404", description = "Inventario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response obtenerInventarioPorId(@PathParam("id") Long id) {
        try {
            return inventarioUseCase.obtenerPorId(id)
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Inventario no encontrado con ID: " + id, "INVENTARIO_NOT_FOUND"));
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al obtener inventario: " + e.getMessage(), "INVENTARIO_GET_ERROR");
        }
    }

    @GET
    @Operation(summary = "Listar inventarios documentales",
            description = "Retorna una lista de inventarios documentales con filtros opcionales")
    @APIResponse(responseCode = "200", description = "Lista de inventarios obtenida exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response listarInventarios(
            @QueryParam("idSeccion") Long idSeccion,
            @QueryParam("idSerie") Long idSerie,
            @QueryParam("idSubserie") Long idSubserie,
            @QueryParam("numeroExpediente") String numeroExpediente,
            @QueryParam("estado") String estado,
            @QueryParam("supervisor") String supervisor) {
        try {
            LOG.debug(String.format(
                    "listarInventarios: idSeccion=%s, idSerie=%s, idSubserie=%s, estado=%s, "
                            + "filtro expediente presente=%s, supervisor presente=%s",
                    idSeccion, idSerie, idSubserie, estado,
                    numeroExpediente != null && !numeroExpediente.isBlank(),
                    supervisor != null && !supervisor.isBlank()));
            List<InventarioDocumentalResponse> inventarios = inventarioUseCase.listarConFiltros(
                    idSeccion, idSerie, idSubserie, numeroExpediente, estado,
                    null, null, null, null, null, null, null, null, null, null, null,
                    supervisor);
            return responses.ok(inventarios);
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar inventarios: " + e.getMessage(), "INVENTARIOS_LIST_ERROR");
        }
    }

    @GET
    @Path("/pendientes-aprobacion")
    @Operation(summary = "Listar inventarios pendientes de aprobación",
            description = "Retorna una lista de inventarios pendientes de aprobación. Requiere rol SUPERVISOR_SDNGD")
    @APIResponse(responseCode = "200", description = "Lista de inventarios pendientes obtenida exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response listarPendientesAprobacion() {
        try {
            return responses.ok(inventarioUseCase.listarPendientesAprobacion());
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar inventarios pendientes: " + e.getMessage(), "INVENTARIOS_PENDIENTES_ERROR");
        }
    }

    @GET
    @Path("/pendientes")
    @Operation(summary = "Listar inventarios pendientes del operador",
            description = "Retorna inventarios pendientes del operador. Requiere rol OPERADOR_SDNGD")
    @APIResponse(responseCode = "200", description = "Lista obtenida",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response listarPendientes(@HeaderParam("X-Operador-Id") String operadorIdHeader) {
        try {
            String operadorId = HttpOperadorExtractor.fromHeaderOrFallback(operadorIdHeader);
            LOG.debugf("listarPendientes: longitud operadorId=%d", operadorId.length());
            return responses.ok(inventarioUseCase.listarPendientesPorOperador(operadorId));
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar inventarios pendientes: " + e.getMessage(), "INVENTARIOS_PENDIENTES_ERROR");
        }
    }

    @PUT
    @Path("/{id}/aprobar")
    @Operation(summary = "Aprobar inventario",
            description = "Aprueba un inventario. Estados permitidos: 'Registrado' o 'Actualizado'. Requiere SUPERVISOR_SDNGD")
    @APIResponse(responseCode = "200", description = "Inventario aprobado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "400", description = "Estado incorrecto",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "404", description = "Inventario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response aprobarInventario(@PathParam("id") Long id, AprobacionRequest request) {
        return ejecutarOptional(
                () -> inventarioUseCase.aprobarInventario(
                        id, RestSecurityPlaceholder.SUPERVISOR_CEDULA_TEMPORAL,
                        request != null ? request.getObservaciones() : null),
                id,
                "INVENTARIO_APROBACION_VALIDATION_ERROR",
                "Error al aprobar inventario: ",
                "INVENTARIO_APROBACION_ERROR");
    }

    @PUT
    @Path("/{id}/rechazar")
    @Operation(summary = "Rechazar inventario",
            description = "Rechazo con observaciones obligatorias. Requiere SUPERVISOR_SDNGD")
    @APIResponse(responseCode = "200", description = "Inventario rechazado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "400", description = "Validación",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(responseCode = "404", description = "Inventario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ApiResponse.class)))
    public Response rechazarInventario(@PathParam("id") Long id, RechazoRequest request) {
        if (request == null || request.getObservaciones() == null || request.getObservaciones().trim().isEmpty()) {
            return responses.badRequest(
                    "Las observaciones del rechazo son obligatorias", "RECHAZO_OBSERVACIONES_REQUIRED");
        }
        try {
            return inventarioUseCase.rechazarInventario(
                            id, RestSecurityPlaceholder.SUPERVISOR_CEDULA_TEMPORAL, request.getObservaciones())
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Inventario no encontrado con ID: " + id, "INVENTARIO_NOT_FOUND"));
        } catch (IllegalArgumentException e) {
            return responses.badRequest(e.getMessage(), "RECHAZO_VALIDATION_ERROR");
        } catch (IllegalStateException e) {
            return responses.badRequest(e.getMessage(), "INVENTARIO_RECHAZO_VALIDATION_ERROR");
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al rechazar inventario: " + e.getMessage(), "INVENTARIO_RECHAZO_ERROR");
        }
    }

    /**
     * Caso de uso que devuelve entidad: 201/200 vía {@code toResponse}, errores mapeados.
     */
    private <T> Response ejecutar(
            Supplier<T> accion,
            Function<T, Response> toResponse,
            String illegalStateCode,
            String errorPrefijo,
            String errorCode) {
        try {
            return toResponse.apply(accion.get());
        } catch (IllegalStateException e) {
            return responses.badRequest(e.getMessage(), illegalStateCode);
        } catch (Exception e) {
            return responses.internalServerError(errorPrefijo + e.getMessage(), errorCode);
        }
    }

    /**
     * Caso de uso {@link Optional}: presente OK, vacío 404, {@link IllegalStateException} 400.
     */
    private <T> Response ejecutarOptional(
            Supplier<Optional<T>> accion,
            Long id,
            String illegalStateCode,
            String errorPrefijo,
            String errorCode) {
        try {
            return accion.get()
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound("Inventario no encontrado con ID: " + id, "INVENTARIO_NOT_FOUND"));
        } catch (IllegalStateException e) {
            return responses.badRequest(e.getMessage(), illegalStateCode);
        } catch (Exception e) {
            return responses.internalServerError(errorPrefijo + e.getMessage(), errorCode);
        }
    }
}
