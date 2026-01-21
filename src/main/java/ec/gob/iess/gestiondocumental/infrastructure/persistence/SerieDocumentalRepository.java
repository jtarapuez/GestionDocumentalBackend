package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para la entidad SerieDocumental
 * Proporciona métodos de acceso a datos para GDOC_SERIES_T
 */
@ApplicationScoped
public class SerieDocumentalRepository implements PanacheRepository<SerieDocumental> {

    /**
     * Busca una serie por su ID
     * @param id ID de la serie
     * @return Optional con la serie encontrada
     */
    public Optional<SerieDocumental> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    /**
     * Busca todas las series de una sección
     * @param idSeccion ID de la sección
     * @return Lista de series de la sección
     */
    public List<SerieDocumental> findBySeccion(Long idSeccion) {
        return find("idSeccion", idSeccion).list();
    }

    /**
     * Busca todas las series activas (estado Creado o Actualizado)
     * @return Lista de series activas
     */
    public List<SerieDocumental> findActivas() {
        return find("estado IN (?1)", Arrays.asList("Creado", "Actualizado")).list();
    }

    /**
     * Busca series por sección y estado
     * @param idSeccion ID de la sección
     * @param estado Estado de la serie
     * @return Lista de series
     */
    public List<SerieDocumental> findBySeccionAndEstado(Long idSeccion, String estado) {
        return find("idSeccion = ?1 AND estado = ?2", idSeccion, estado).list();
    }
}




