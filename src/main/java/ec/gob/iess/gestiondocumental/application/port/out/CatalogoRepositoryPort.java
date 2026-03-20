package ec.gob.iess.gestiondocumental.application.port.out;

import ec.gob.iess.gestiondocumental.domain.model.Catalogo;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de catálogos (PAS-GUI-047).
 * La aplicación depende de esta interfaz; la implementación está en infraestructura.
 */
public interface CatalogoRepositoryPort {

    Optional<Catalogo> findByCodigo(String codigo);

    List<Catalogo> findActivos();

    boolean existsByCodigo(String codigo);
}
