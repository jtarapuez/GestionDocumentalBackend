package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para la entidad SubserieDocumental
 * Proporciona métodos de acceso a datos para GDOC_SUBSERIES_T
 */
@ApplicationScoped
public class SubserieDocumentalRepository implements PanacheRepository<SubserieDocumental> {

    /**
     * Busca una subserie por su ID
     * @param id ID de la subserie
     * @return Optional con la subserie encontrada
     */
    public Optional<SubserieDocumental> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    /**
     * Busca todas las subseries de una serie
     * @param idSerie ID de la serie
     * @return Lista de subseries de la serie
     */
    public List<SubserieDocumental> findBySerie(Long idSerie) {
        return find("idSerie", idSerie).list();
    }

    /**
     * Busca todas las subseries activas (estado Creado o Actualizado)
     * @return Lista de subseries activas
     */
    public List<SubserieDocumental> findActivas() {
        return find("estado IN (?1)", "Creado", "Actualizado").list();
    }

    /**
     * Busca subseries por serie y estado
     * @param idSerie ID de la serie
     * @param estado Estado de la subserie
     * @return Lista de subseries
     */
    public List<SubserieDocumental> findBySerieAndEstado(Long idSerie, String estado) {
        return find("idSerie = ?1 AND estado = ?2", idSerie, estado).list();
    }
}

