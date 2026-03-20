package ec.gob.iess.gestiondocumental.infrastructure.persistence.adapter;

import ec.gob.iess.gestiondocumental.application.port.out.CatalogoRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.Catalogo;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.CatalogoRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper.CatalogoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de salida: implementa el puerto de persistencia de catálogos (PAS-GUI-047).
 */
@ApplicationScoped
public class CatalogoPersistenceAdapter implements CatalogoRepositoryPort {

    @Inject
    CatalogoRepository catalogoRepository;

    @Inject
    CatalogoMapper catalogoMapper;

    @Override
    public Optional<Catalogo> findByCodigo(String codigo) {
        return catalogoRepository.findByCodigo(codigo).map(catalogoMapper::toDomain);
    }

    @Override
    public List<Catalogo> findActivos() {
        return catalogoRepository.findActivos().stream()
                .map(catalogoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return catalogoRepository.existsByCodigo(codigo);
    }
}
