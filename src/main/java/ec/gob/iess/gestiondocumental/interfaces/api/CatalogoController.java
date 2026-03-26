package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.port.in.CatalogoUseCasePort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoDetalleResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SeccionDocumentalResponse;
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
 * Adaptador REST: catálogos y secciones. Respuestas vía {@link StandardResponses} (PAS-EST-043).
 */
@Path("/v1/catalogos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Catálogos", description = "API para gestión de catálogos del sistema documental")
public class CatalogoController {

    @Inject
    CatalogoUseCasePort catalogoUseCase;

    @Inject
    StandardResponses responses;

    @GET
    @Operation(
            summary = "Listar todos los catálogos",
            description = "Retorna una lista de todos los catálogos activos del sistema")
    @APIResponse(
            responseCode = "200",
            description = "Lista de catálogos obtenida exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarCatalogos() {
        try {
            return responses.ok(catalogoUseCase.listarCatalogos());
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar catálogos: " + e.getMessage(), "CATALOGOS_LIST_ERROR");
        }
    }

    @GET
    @Path("/{codigo}")
    @Operation(
            summary = "Obtener catálogo por código",
            description = "Retorna un catálogo específico identificado por su código")
    @APIResponse(
            responseCode = "200",
            description = "Catálogo obtenido exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Catálogo no encontrado",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response obtenerCatalogoPorCodigo(@PathParam("codigo") String codigo) {
        try {
            return catalogoUseCase.obtenerCatalogoPorCodigo(codigo)
                    .map(responses::ok)
                    .orElseGet(() -> responses.notFound(
                            "Catálogo no encontrado con código: " + codigo, "CATALOGO_NOT_FOUND"));
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al obtener catálogo: " + e.getMessage(), "CATALOGO_GET_ERROR");
        }
    }

    @GET
    @Path("/{codigo}/detalles")
    @Operation(
            summary = "Listar detalles de un catálogo",
            description = "Retorna todos los detalles/valores de un catálogo identificado por su código")
    @APIResponse(
            responseCode = "200",
            description = "Detalles del catálogo obtenidos exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "Catálogo no encontrado",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarDetallesPorCatalogo(@PathParam("codigo") String codigo) {
        try {
            if (catalogoUseCase.obtenerCatalogoPorCodigo(codigo).isEmpty()) {
                return responses.notFound(
                        "Catálogo no encontrado con código: " + codigo, "CATALOGO_NOT_FOUND");
            }
            List<CatalogoDetalleResponse> detalles = catalogoUseCase.listarDetallesPorCatalogo(codigo);
            return responses.ok(detalles);
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar detalles del catálogo: " + e.getMessage(), "CATALOGO_DETALLES_ERROR");
        }
    }

    @GET
    @Path("/formatos")
    @Operation(
            summary = "Listar formatos de documentos",
            description = "Retorna los valores del catálogo FORMATO (Físico, Digital, Mixto)")
    @APIResponse(
            responseCode = "200",
            description = "Formatos obtenidos exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarFormatos() {
        return listarDetallesPorCatalogo("FORMATO");
    }

    @GET
    @Path("/seguridad")
    @Operation(
            summary = "Listar niveles de seguridad",
            description = "Retorna los valores del catálogo SEGURIDAD (Pública, Confidencial, Reservada)")
    @APIResponse(
            responseCode = "200",
            description = "Niveles de seguridad obtenidos exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarSeguridad() {
        return listarDetallesPorCatalogo("SEGURIDAD");
    }

    @GET
    @Path("/estados-serie")
    @Operation(
            summary = "Listar estados de serie documental",
            description = "Retorna los valores del catálogo ESTADO_SERIE")
    @APIResponse(
            responseCode = "200",
            description = "Estados de serie obtenidos exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarEstadosSerie() {
        return listarDetallesPorCatalogo("ESTADO_SERIE");
    }

    @GET
    @Path("/estados-inventario")
    @Operation(
            summary = "Listar estados de inventario",
            description = "Retorna los valores del catálogo ESTADO_INVENTARIO "
                    + "(Registrado, Pendiente, Actualizado, Aprobado)")
    @APIResponse(
            responseCode = "200",
            description = "Estados de inventario obtenidos exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarEstadosInventario() {
        return listarDetallesPorCatalogo("ESTADO_INVENTARIO");
    }

    @GET
    @Path("/tipos-contenedor")
    @Operation(
            summary = "Listar tipos de contenedor",
            description = "Retorna los valores del catálogo TIPO_CONTENEDOR (Caja, Carpeta, Legajo, Tomo)")
    @APIResponse(
            responseCode = "200",
            description = "Tipos de contenedor obtenidos exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarTiposContenedor() {
        return listarDetallesPorCatalogo("TIPO_CONTENEDOR");
    }

    @GET
    @Path("/tipos-archivo")
    @Operation(
            summary = "Listar tipos de archivo",
            description = "Retorna los valores del catálogo TIPO_ARCHIVO (Activo, Pasivo)")
    @APIResponse(
            responseCode = "200",
            description = "Tipos de archivo obtenidos exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarTiposArchivo() {
        return listarDetallesPorCatalogo("TIPO_ARCHIVO");
    }

    @GET
    @Path("/secciones")
    @Operation(
            summary = "Listar secciones documentales",
            description = "Retorna una lista de todas las secciones documentales activas del sistema")
    @APIResponse(
            responseCode = "200",
            description = "Secciones documentales obtenidas exitosamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ApiResponse.class)))
    public Response listarSecciones() {
        try {
            List<SeccionDocumentalResponse> secciones = catalogoUseCase.listarSecciones();
            return responses.ok(secciones);
        } catch (Exception e) {
            return responses.internalServerError(
                    "Error al listar secciones: " + e.getMessage(), "SECCIONES_LIST_ERROR");
        }
    }
}
