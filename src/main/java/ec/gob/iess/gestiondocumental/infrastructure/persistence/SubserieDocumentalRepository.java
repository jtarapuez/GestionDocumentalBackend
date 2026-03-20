package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SubserieDocumentalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para {@link SubserieDocumentalEntity}. Usado por el adaptador (PAS-GUI-047).
 */
@ApplicationScoped
public class SubserieDocumentalRepository implements PanacheRepository<SubserieDocumentalEntity> {

    public Optional<SubserieDocumentalEntity> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public List<SubserieDocumentalEntity> findBySerie(Long idSerie) {
        return find("idSerie", idSerie).list();
    }

    public List<SubserieDocumentalEntity> findActivas() {
        return find("estado IN (?1)", Arrays.asList("Creado", "Actualizado")).list();
    }

    public List<SubserieDocumentalEntity> findBySerieAndEstado(Long idSerie, String estado) {
        return find("idSerie = ?1 AND estado = ?2", idSerie, estado).list();
    }
}
