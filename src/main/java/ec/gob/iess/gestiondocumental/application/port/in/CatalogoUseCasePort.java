package ec.gob.iess.gestiondocumental.application.port.in;

import ec.gob.iess.gestiondocumental.domain.model.Catalogo;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoDetalleResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.CatalogoResponse;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SeccionDocumentalResponse;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para el caso de uso de catálogos (PAS-GUI-047).
 */
public interface CatalogoUseCasePort {

    List<CatalogoResponse> listarCatalogos();

    Optional<CatalogoResponse> obtenerCatalogoPorCodigo(String codigo);

    List<CatalogoDetalleResponse> listarDetallesPorCatalogo(String codigoCatalogo);

    Optional<Catalogo> obtenerCatalogoCompleto(String codigo);

    List<SeccionDocumentalResponse> listarSecciones();
}
