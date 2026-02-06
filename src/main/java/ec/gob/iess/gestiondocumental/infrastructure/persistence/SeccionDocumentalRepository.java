package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Repositorio Panache para la entidad {@link ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental}.
 * Persiste las secciones documentales en GDOC_SECCIONES_TP.
 * Criterios de búsqueda: por estado (activas: Creado, Actualizado).
 */
@ApplicationScoped
public class SeccionDocumentalRepository implements PanacheRepository<SeccionDocumental> {

    /**
     * Busca todas las secciones activas (estado "Creado" o "Actualizado")
     * @return Lista de secciones activas
     */
    public List<SeccionDocumental> findActivas() {
        return find("estadoRegistro IN (?1, ?2)", "Creado", "Actualizado").list();
    }
}






