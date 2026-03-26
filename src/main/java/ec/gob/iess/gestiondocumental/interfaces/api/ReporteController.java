package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ConsultaRequest;
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

/**
 * Adaptador REST: exportación (PDF/Excel pendiente).
 */
@Path("/v1/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Reportes", description = "API para generación de reportes y exportación de inventarios")
public class ReporteController {

    @Inject
    StandardResponses responses;

    @POST
    @Path("/exportar-pdf")
    @Operation(
            summary = "Exportar inventarios a PDF",
            description = "TODO: Implementar generación de PDF")
    @APIResponse(
            responseCode = "200",
            description = "Reporte PDF generado exitosamente",
            content = @Content(mediaType = "application/pdf"))
    @APIResponse(
            responseCode = "501",
            description = "Funcionalidad no implementada aún",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response exportarPDF(ConsultaRequest request) {
        return responses.notImplemented(
                "Exportación a PDF no implementada aún. Esta funcionalidad será agregada en una versión futura.",
                "PDF_EXPORT_NOT_IMPLEMENTED");
    }

    @POST
    @Path("/exportar-excel")
    @Operation(
            summary = "Exportar inventarios a Excel",
            description = "TODO: Implementar generación de Excel")
    @APIResponse(
            responseCode = "200",
            description = "Reporte Excel generado exitosamente",
            content = @Content(
                    mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    @APIResponse(
            responseCode = "501",
            description = "Funcionalidad no implementada aún",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response exportarExcel(ConsultaRequest request) {
        return responses.notImplemented(
                "Exportación a Excel no implementada aún. Esta funcionalidad será agregada en una versión futura.",
                "EXCEL_EXPORT_NOT_IMPLEMENTED");
    }
}
