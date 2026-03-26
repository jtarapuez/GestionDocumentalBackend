package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.port.in.InventarioDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ConsultaRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
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
 * Adaptador REST: consulta avanzada de inventarios.
 */
@Path("/v1/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Consultas", description = "API para consultas avanzadas de inventarios documentales")
public class ConsultaController {

    @Inject
    InventarioDocumentalUseCasePort inventarioUseCase;

    @Inject
    StandardResponses responses;

    @POST
    @Operation(
            summary = "Consulta avanzada de inventarios",
            description = "Consulta con múltiples filtros opcionales (sección, serie, expediente, fechas, etc.)")
    @APIResponse(
            responseCode = "200",
            description = "Consulta realizada exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
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
                    null);

            return responses.ok(inventarios);
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al realizar consulta: " + e.getMessage(), "CONSULTA_ERROR");
        }
    }
}
