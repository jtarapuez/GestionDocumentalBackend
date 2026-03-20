package ec.gob.iess.gestiondocumental.application.port.out;

import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de subseries documentales (PAS-GUI-047).
 */
public interface SubserieDocumentalRepositoryPort {

    Optional<SubserieDocumental> findByIdOptional(Long id);

    List<SubserieDocumental> findBySerie(Long idSerie);

    List<SubserieDocumental> findActivas();

    List<SubserieDocumental> findBySerieAndEstado(Long idSerie, String estado);

    void persist(SubserieDocumental subserie);
}
