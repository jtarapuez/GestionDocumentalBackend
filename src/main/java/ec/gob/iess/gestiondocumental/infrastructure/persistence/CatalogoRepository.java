package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.CatalogoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para {@link CatalogoEntity}.
 * Usado por el adaptador de persistencia; la aplicación usa {@link ec.gob.iess.gestiondocumental.application.port.out.CatalogoRepositoryPort}.
 */
@ApplicationScoped
public class CatalogoRepository implements PanacheRepository<CatalogoEntity> {

    public Optional<CatalogoEntity> findByCodigo(String codigo) {
        return find("codigo", codigo).firstResultOptional();
    }

    public List<CatalogoEntity> findActivos() {
        return find("estado", "A").list();
    }

    public boolean existsByCodigo(String codigo) {
        return count("codigo", codigo) > 0;
    }
}
