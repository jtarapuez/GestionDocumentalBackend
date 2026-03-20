package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SeccionDocumentalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para {@link SeccionDocumentalEntity}. Usado por el adaptador (PAS-GUI-047).
 */
@ApplicationScoped
public class SeccionDocumentalRepository implements PanacheRepository<SeccionDocumentalEntity> {

    public List<SeccionDocumentalEntity> findActivas() {
        return find("estadoRegistro IN (?1, ?2)", "Creado", "Actualizado").list();
    }

    public Optional<SeccionDocumentalEntity> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }
}
