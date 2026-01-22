package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.usecases.InventarioDocumentalUseCase;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ConsultaRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
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
 * Controlador REST para consultas avanzadas de inventarios
 * Expone endpoints para consultas con múltiples filtros
 */
@Path("/v1/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Consultas", description = "API para consultas avanzadas de inventarios documentales")
public class ConsultaController {

    @Inject
    InventarioDocumentalUseCase inventarioUseCase;

    /**
     * Realiza una consulta avanzada de inventarios con múltiples filtros
     * 
     * @param request Filtros de la consulta
     * @return Respuesta con lista de inventarios que cumplen los filtros
     */
    @POST
    @Operation(
        summary = "Consulta avanzada de inventarios",
        description = "Realiza una consulta avanzada de inventarios con múltiples filtros opcionales. " +
                      "Soporta filtros por: sección, serie, subserie, expediente, contenedor, tipo archivo, " +
                      "operador, cédula/RUC, nombres, descripción, estado, y período de fechas"
    )
    @APIResponse(
        responseCode = "200",
        description = "Consulta realizada exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response consultarInventarios(ConsultaRequest request) {
        try {
            if (request == null) {
                request = new ConsultaRequest();
            }

            List<InventarioDocumentalResponse> inventarios = inventarioUseCase.listarConFiltros(
                request.getIdSeccion(),
                request.getIdSerie(),
                request.getIdSubserie(),
                request.getNumeroExpediente(),
                request.getEstado(),
                request.getNumeroCedula(),
                request.getNumeroRuc(),
                request.getOperador(),
                request.getNombresApellidos(),
                request.getRazonSocial(),
                request.getDescripcionSerie(),
                request.getTipoContenedor(),
                request.getNumeroContenedor(),
                request.getTipoArchivo(),
                request.getFechaDesde(),
                request.getFechaHasta(),
                null // supervisor - no se filtra por supervisor en consultas avanzadas
            );

            ApiResponse<List<InventarioDocumentalResponse>> response = ApiResponse.success(inventarios);
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al realizar consulta: " + e.getMessage(),
                "CONSULTA_ERROR"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }
}




