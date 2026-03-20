package ec.gob.iess.gestiondocumental.application.port.out;

import ec.gob.iess.gestiondocumental.domain.model.CatalogoDetalle;

import java.util.List;

/**
 * Puerto de salida para persistencia de detalles de catálogo (PAS-GUI-047).
 * La aplicación depende de esta interfaz; la implementación está en infraestructura.
 */
public interface CatalogoDetalleRepositoryPort {

    List<CatalogoDetalle> findByCatalogoId(Long idCatalogo);

    List<CatalogoDetalle> findActivosByCatalogoId(Long idCatalogo);

    List<CatalogoDetalle> findByCodigoCatalogo(String codigoCatalogo);

    List<CatalogoDetalle> findActivosByCodigoCatalogo(String codigoCatalogo);
}
