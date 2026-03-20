package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.port.in.CatalogoUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.CatalogoDetalleRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.CatalogoRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SeccionDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.Catalogo;
import ec.gob.iess.gestiondocumental.domain.model.CatalogoDetalle;
import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoDetalleResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SeccionDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para la gestión de catálogos.
 * Depende solo de puertos de salida (PAS-GUI-047); Catálogo/CatalogoDetalle ya migrados a hexagonal.
 */
@ApplicationScoped
public class CatalogoUseCase implements CatalogoUseCasePort {

    @Inject
    CatalogoRepositoryPort catalogoRepositoryPort;

    @Inject
    CatalogoDetalleRepositoryPort catalogoDetalleRepositoryPort;

    @Inject
    SeccionDocumentalRepositoryPort seccionDocumentalRepositoryPort;

    public List<CatalogoResponse> listarCatalogos() {
        List<Catalogo> catalogos = catalogoRepositoryPort.findActivos();
        return catalogos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<CatalogoResponse> obtenerCatalogoPorCodigo(String codigo) {
        return catalogoRepositoryPort.findByCodigo(codigo)
                .map(this::toResponse);
    }

    public List<CatalogoDetalleResponse> listarDetallesPorCatalogo(String codigoCatalogo) {
        List<CatalogoDetalle> detalles = catalogoDetalleRepositoryPort.findActivosByCodigoCatalogo(codigoCatalogo);
        return detalles.stream()
                .map(this::toDetalleResponse)
                .collect(Collectors.toList());
    }

    public Optional<Catalogo> obtenerCatalogoCompleto(String codigo) {
        return catalogoRepositoryPort.findByCodigo(codigo);
    }

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

    public List<SeccionDocumentalResponse> listarSecciones() {
        List<SeccionDocumental> secciones = seccionDocumentalRepositoryPort.findActivas();
        return secciones.stream()
                .map(this::toSeccionResponse)
                .collect(Collectors.toList());
    }

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
