package ec.gob.iess.gestiondocumental.application.port.out;

import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de secciones documentales (PAS-GUI-047).
 */
public interface SeccionDocumentalRepositoryPort {

    List<SeccionDocumental> findActivas();

    Optional<SeccionDocumental> findById(Long id);
}
