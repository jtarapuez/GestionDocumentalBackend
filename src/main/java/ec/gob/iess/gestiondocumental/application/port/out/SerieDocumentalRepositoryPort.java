package ec.gob.iess.gestiondocumental.application.port.out;

import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de series documentales (PAS-GUI-047).
 */
public interface SerieDocumentalRepositoryPort {

    Optional<SerieDocumental> findByIdOptional(Long id);

    List<SerieDocumental> findBySeccion(Long idSeccion);

    List<SerieDocumental> findActivas();

    List<SerieDocumental> findBySeccionAndEstado(Long idSeccion, String estado);

    void persist(SerieDocumental serie);
}
