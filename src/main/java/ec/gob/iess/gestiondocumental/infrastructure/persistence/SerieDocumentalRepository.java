package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.SerieDocumentalEstados;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SerieDocumentalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para {@link SerieDocumentalEntity}. Usado por el adaptador (PAS-GUI-047).
 */
@ApplicationScoped
public class SerieDocumentalRepository implements PanacheRepository<SerieDocumentalEntity> {

    public Optional<SerieDocumentalEntity> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public List<SerieDocumentalEntity> findBySeccion(Long idSeccion) {
        return find("idSeccion", idSeccion).list();
    }

    /** Series en estado activo: solo valores históricos de columna ESTADO (15 caracteres). */
    public List<SerieDocumentalEntity> findActivas() {
        return find("estado IN (?1)", Arrays.asList(
                SerieDocumentalEstados.CREADO,
                SerieDocumentalEstados.ACTUALIZADO)).list();
    }

    public List<SerieDocumentalEntity> findBySeccionAndEstado(Long idSeccion, String estado) {
        return find("idSeccion = ?1 AND estado = ?2", idSeccion, estado).list();
    }
}
