package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Repositorio Panache para la entidad SeccionDocumental
 * Proporciona métodos de acceso a datos para GDOC_SECCIONES_TP
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
