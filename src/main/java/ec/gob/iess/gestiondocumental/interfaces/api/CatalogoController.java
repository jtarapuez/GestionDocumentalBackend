package ec.gob.iess.gestiondocumental.interfaces.api;

import ec.gob.iess.gestiondocumental.application.usecases.CatalogoUseCase;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoDetalleResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SeccionDocumentalResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.context.RequestContext;
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
 * Controlador REST para la gestión de catálogos
 * Expone endpoints para consultar catálogos y sus detalles
 */
@Path("/v1/catalogos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Catálogos", description = "API para gestión de catálogos del sistema documental")
public class CatalogoController {

    @Inject
    CatalogoUseCase catalogoUseCase;

    @Inject
    RequestContext requestContext;

    /**
     * Lista todos los catálogos activos
     * 
     * @return Respuesta con lista de catálogos
     */
    @GET
    @Operation(
        summary = "Listar todos los catálogos",
        description = "Retorna una lista de todos los catálogos activos del sistema"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de catálogos obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarCatalogos() {
        try {
            List<CatalogoResponse> catalogos = catalogoUseCase.listarCatalogos();
            ApiResponse<List<CatalogoResponse>> response = ApiResponse.success(catalogos,
                    requestContext.getPath(), requestContext.getRequestId());
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar catálogos: " + e.getMessage(),
                "CATALOGOS_LIST_ERROR",
                requestContext.getPath(), requestContext.getRequestId()
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Obtiene un catálogo específico por su código
     * 
     * @param codigo Código del catálogo
     * @return Respuesta con el catálogo encontrado
     */
    @GET
    @Path("/{codigo}")
    @Operation(
        summary = "Obtener catálogo por código",
        description = "Retorna un catálogo específico identificado por su código"
    )
    @APIResponse(
        responseCode = "200",
        description = "Catálogo obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Catálogo no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response obtenerCatalogoPorCodigo(@PathParam("codigo") String codigo) {
        try {
            return catalogoUseCase.obtenerCatalogoPorCodigo(codigo)
                    .map(catalogo -> {
                        ApiResponse<CatalogoResponse> response = ApiResponse.success(catalogo,
                                requestContext.getPath(), requestContext.getRequestId());
                        return Response.ok(response).build();
                    })
                    .orElseGet(() -> {
                        ApiResponse<Object> errorResponse = ApiResponse.error(
                            "Catálogo no encontrado con código: " + codigo,
                            "CATALOGO_NOT_FOUND",
                            requestContext.getPath(), requestContext.getRequestId()
                        );
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(errorResponse)
                                .build();
                    });
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al obtener catálogo: " + e.getMessage(),
                "CATALOGO_GET_ERROR",
                requestContext.getPath(), requestContext.getRequestId()
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista los detalles de un catálogo específico por su código
     * 
     * @param codigo Código del catálogo
     * @return Respuesta con lista de detalles del catálogo
     */
    @GET
    @Path("/{codigo}/detalles")
    @Operation(
        summary = "Listar detalles de un catálogo",
        description = "Retorna todos los detalles/valores de un catálogo específico identificado por su código"
    )
    @APIResponse(
        responseCode = "200",
        description = "Detalles del catálogo obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Catálogo no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarDetallesPorCatalogo(@PathParam("codigo") String codigo) {
        try {
            // Verificar que el catálogo existe
            if (catalogoUseCase.obtenerCatalogoPorCodigo(codigo).isEmpty()) {
                ApiResponse<Object> errorResponse = ApiResponse.error(
                    "Catálogo no encontrado con código: " + codigo,
                    "CATALOGO_NOT_FOUND",
                    requestContext.getPath(), requestContext.getRequestId()
                );
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(errorResponse)
                        .build();
            }

            List<CatalogoDetalleResponse> detalles = catalogoUseCase.listarDetallesPorCatalogo(codigo);
            ApiResponse<List<CatalogoDetalleResponse>> response = ApiResponse.success(detalles,
                    requestContext.getPath(), requestContext.getRequestId());
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar detalles del catálogo: " + e.getMessage(),
                "CATALOGO_DETALLES_ERROR",
                requestContext.getPath(), requestContext.getRequestId()
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * Lista los detalles del catálogo FORMATO
     * 
     * @return Respuesta con lista de formatos (Físico, Digital, Mixto)
     */
    @GET
    @Path("/formatos")
    @Operation(
        summary = "Listar formatos de documentos",
        description = "Retorna los valores del catálogo FORMATO (Físico, Digital, Mixto)"
    )
    @APIResponse(
        responseCode = "200",
        description = "Formatos obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarFormatos() {
        return listarDetallesPorCatalogo("FORMATO");
    }

    /**
     * Lista los detalles del catálogo SEGURIDAD
     * 
     * @return Respuesta con lista de niveles de seguridad (Pública, Confidencial, Reservada)
     */
    @GET
    @Path("/seguridad")
    @Operation(
        summary = "Listar niveles de seguridad",
        description = "Retorna los valores del catálogo SEGURIDAD (Pública, Confidencial, Reservada)"
    )
    @APIResponse(
        responseCode = "200",
        description = "Niveles de seguridad obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarSeguridad() {
        return listarDetallesPorCatalogo("SEGURIDAD");
    }

    /**
     * Lista los detalles del catálogo ESTADO_SERIE
     * 
     * @return Respuesta con lista de estados de serie (Creado, Actualizado)
     */
    @GET
    @Path("/estados-serie")
    @Operation(
        summary = "Listar estados de serie documental",
        description = "Retorna los valores del catálogo ESTADO_SERIE (Creado, Actualizado)"
    )
    @APIResponse(
        responseCode = "200",
        description = "Estados de serie obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarEstadosSerie() {
        return listarDetallesPorCatalogo("ESTADO_SERIE");
    }

    /**
     * Lista los detalles del catálogo ESTADO_INVENTARIO
     * 
     * @return Respuesta con lista de estados de inventario
     */
    @GET
    @Path("/estados-inventario")
    @Operation(
        summary = "Listar estados de inventario",
        description = "Retorna los valores del catálogo ESTADO_INVENTARIO "
                + "(Registrado, Pendiente, Actualizado, Aprobado)"
    )
    @APIResponse(
        responseCode = "200",
        description = "Estados de inventario obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarEstadosInventario() {
        return listarDetallesPorCatalogo("ESTADO_INVENTARIO");
    }

    /**
     * Lista los detalles del catálogo TIPO_CONTENEDOR
     * 
     * @return Respuesta con lista de tipos de contenedor (Caja, Carpeta, Legajo, Tomo)
     */
    @GET
    @Path("/tipos-contenedor")
    @Operation(
        summary = "Listar tipos de contenedor",
        description = "Retorna los valores del catálogo TIPO_CONTENEDOR (Caja, Carpeta, Legajo, Tomo)"
    )
    @APIResponse(
        responseCode = "200",
        description = "Tipos de contenedor obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarTiposContenedor() {
        return listarDetallesPorCatalogo("TIPO_CONTENEDOR");
    }

    /**
     * Lista los detalles del catálogo TIPO_ARCHIVO
     * 
     * @return Respuesta con lista de tipos de archivo (Activo, Pasivo)
     */
    @GET
    @Path("/tipos-archivo")
    @Operation(
        summary = "Listar tipos de archivo",
        description = "Retorna los valores del catálogo TIPO_ARCHIVO (Activo, Pasivo)"
    )
    @APIResponse(
        responseCode = "200",
        description = "Tipos de archivo obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarTiposArchivo() {
        return listarDetallesPorCatalogo("TIPO_ARCHIVO");
    }

    /**
     * Lista todas las secciones documentales activas
     * 
     * @return Respuesta con lista de secciones documentales
     */
    @GET
    @Path("/secciones")
    @Operation(
        summary = "Listar secciones documentales",
        description = "Retorna una lista de todas las secciones documentales activas del sistema"
    )
    @APIResponse(
        responseCode = "200",
        description = "Secciones documentales obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public Response listarSecciones() {
        try {
            List<SeccionDocumentalResponse> secciones = catalogoUseCase.listarSecciones();
            ApiResponse<List<SeccionDocumentalResponse>> response = ApiResponse.success(secciones,
                    requestContext.getPath(), requestContext.getRequestId());
            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error al listar secciones: " + e.getMessage(),
                "SECCIONES_LIST_ERROR",
                requestContext.getPath(), requestContext.getRequestId()
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }
}

