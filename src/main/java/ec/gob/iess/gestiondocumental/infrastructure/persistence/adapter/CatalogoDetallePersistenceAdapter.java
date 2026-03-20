package ec.gob.iess.gestiondocumental.infrastructure.persistence.adapter;

import ec.gob.iess.gestiondocumental.application.port.out.CatalogoDetalleRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.CatalogoDetalle;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.CatalogoDetalleRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper.CatalogoDetalleMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador de salida: implementa el puerto de persistencia de detalles de catálogo (PAS-GUI-047).
 */
@ApplicationScoped
public class CatalogoDetallePersistenceAdapter implements CatalogoDetalleRepositoryPort {

    @Inject
    CatalogoDetalleRepository catalogoDetalleRepository;

    @Inject
    CatalogoDetalleMapper catalogoDetalleMapper;

    @Override
    public List<CatalogoDetalle> findByCatalogoId(Long idCatalogo) {
        return catalogoDetalleRepository.findByCatalogoId(idCatalogo).stream()
                .map(catalogoDetalleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoDetalle> findActivosByCatalogoId(Long idCatalogo) {
        return catalogoDetalleRepository.findActivosByCatalogoId(idCatalogo).stream()
                .map(catalogoDetalleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoDetalle> findByCodigoCatalogo(String codigoCatalogo) {
        return catalogoDetalleRepository.findByCodigoCatalogo(codigoCatalogo).stream()
                .map(catalogoDetalleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoDetalle> findActivosByCodigoCatalogo(String codigoCatalogo) {
        return catalogoDetalleRepository.findActivosByCodigoCatalogo(codigoCatalogo).stream()
                .map(catalogoDetalleMapper::toDomain)
                .collect(Collectors.toList());
    }
}
