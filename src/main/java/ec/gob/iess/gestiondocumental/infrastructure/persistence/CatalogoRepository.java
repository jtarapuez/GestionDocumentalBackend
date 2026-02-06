package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.Catalogo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para la entidad {@link ec.gob.iess.gestiondocumental.domain.model.Catalogo}.
 * Persiste los catálogos maestros en GDOC_CATALOGOS_T.
 * Criterios de búsqueda: por código, por estado activo; verificación de existencia por código.
 */
@ApplicationScoped
public class CatalogoRepository implements PanacheRepository<Catalogo> {

    /**
     * Busca un catálogo por su código
     * @param codigo Código del catálogo
     * @return Optional con el catálogo encontrado
     */
    public Optional<Catalogo> findByCodigo(String codigo) {
        return find("codigo", codigo).firstResultOptional();
    }

    /**
     * Busca todos los catálogos activos
     * @return Lista de catálogos con estado 'A' (Activo)
     */
    public List<Catalogo> findActivos() {
        return find("estado", "A").list();
    }

    /**
     * Verifica si existe un catálogo con el código dado
     * @param codigo Código del catálogo
     * @return true si existe, false en caso contrario
     */
    public boolean existsByCodigo(String codigo) {
        return count("codigo", codigo) > 0;
    }
}






