package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.domain.model.Catalogo;
import ec.gob.iess.gestiondocumental.domain.model.CatalogoDetalle;
import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.CatalogoDetalleRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.CatalogoRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.SeccionDocumentalRepository;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoDetalleResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SeccionDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para la gestión de catálogos
 * Contiene la lógica de negocio para operaciones con catálogos
 */
@ApplicationScoped
public class CatalogoUseCase {

    @Inject
    CatalogoRepository catalogoRepository;

    @Inject
    CatalogoDetalleRepository catalogoDetalleRepository;

    @Inject
    SeccionDocumentalRepository seccionDocumentalRepository;

    /**
     * Lista todos los catálogos activos
     * @return Lista de catálogos activos
     */
    public List<CatalogoResponse> listarCatalogos() {
        List<Catalogo> catalogos = catalogoRepository.findActivos();
        return catalogos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un catálogo por su código
     * @param codigo Código del catálogo
     * @return Optional con el catálogo encontrado
     */
    public Optional<CatalogoResponse> obtenerCatalogoPorCodigo(String codigo) {
        return catalogoRepository.findByCodigo(codigo)
                .map(this::toResponse);
    }

    /**
     * Lista los detalles de un catálogo por su código
     * @param codigoCatalogo Código del catálogo (ej: "FORMATO", "SEGURIDAD")
     * @return Lista de detalles del catálogo
     */
    public List<CatalogoDetalleResponse> listarDetallesPorCatalogo(String codigoCatalogo) {
        List<CatalogoDetalle> detalles = catalogoDetalleRepository.findActivosByCodigoCatalogo(codigoCatalogo);
        return detalles.stream()
                .map(this::toDetalleResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un catálogo completo con sus detalles
     * @param codigo Código del catálogo
     * @return Optional con el catálogo y sus detalles
     */
    public Optional<Catalogo> obtenerCatalogoCompleto(String codigo) {
        return catalogoRepository.findByCodigo(codigo);
    }

    /**
     * Convierte una entidad Catalogo a DTO CatalogoResponse
     * @param catalogo Entidad Catalogo
     * @return DTO CatalogoResponse
     */
    public CatalogoResponse toResponse(Catalogo catalogo) {
        if (catalogo == null) {
            return null;
        }
        CatalogoResponse response = new CatalogoResponse();
        response.setId(catalogo.getId());
        response.setCodigo(catalogo.getCodigo());
        response.setDescripcion(catalogo.getDescripcion());
        response.setEstado(catalogo.getEstado());
        response.setObservacion(catalogo.getObservacion());
        return response;
    }

    /**
     * Convierte una entidad CatalogoDetalle a DTO CatalogoDetalleResponse
     * @param detalle Entidad CatalogoDetalle
     * @return DTO CatalogoDetalleResponse
     */
    public CatalogoDetalleResponse toDetalleResponse(CatalogoDetalle detalle) {
        if (detalle == null) {
            return null;
        }
        CatalogoDetalleResponse response = new CatalogoDetalleResponse();
        response.setId(detalle.getId());
        response.setCodigo(detalle.getCodigo());
        response.setDescripcion(detalle.getDescripcion());
        response.setEstado(detalle.getEstado());
        response.setObservacion(detalle.getObservacion());
        response.setIdCatalogo(detalle.getIdCatalogo());
        return response;
    }

    /**
     * Lista todas las secciones documentales activas
     * @return Lista de secciones documentales activas
     */
    public List<SeccionDocumentalResponse> listarSecciones() {
        List<SeccionDocumental> secciones = seccionDocumentalRepository.findActivas();
        return secciones.stream()
                .map(this::toSeccionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad SeccionDocumental a DTO SeccionDocumentalResponse
     * @param seccion Entidad SeccionDocumental
     * @return DTO SeccionDocumentalResponse
     */
    public SeccionDocumentalResponse toSeccionResponse(SeccionDocumental seccion) {
        if (seccion == null) {
            return null;
        }
        SeccionDocumentalResponse response = new SeccionDocumentalResponse();
        response.setId(seccion.getId());
        response.setNombre(seccion.getNombre());
        response.setDescripcion(seccion.getDescripcion());
        response.setEstadoRegistro(seccion.getEstadoRegistro());
        return response;
    }
}

