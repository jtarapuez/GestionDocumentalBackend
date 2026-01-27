package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.CatalogoDetalle;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Repositorio Panache para la entidad CatalogoDetalle
 * Proporciona métodos de acceso a datos para GDOC_CATALOGOSDET_T
 */
@ApplicationScoped
public class CatalogoDetalleRepository implements PanacheRepository<CatalogoDetalle> {

    /**
     * Busca todos los detalles de un catálogo por su ID
     * @param idCatalogo ID del catálogo
     * @return Lista de detalles del catálogo
     */
    public List<CatalogoDetalle> findByCatalogoId(Long idCatalogo) {
        return find("idCatalogo", idCatalogo).list();
    }

    /**
     * Busca todos los detalles activos de un catálogo por su ID
     * @param idCatalogo ID del catálogo
     * @return Lista de detalles activos del catálogo
     */
    public List<CatalogoDetalle> findActivosByCatalogoId(Long idCatalogo) {
        return find("idCatalogo = ?1 AND estado = ?2", idCatalogo, "A").list();
    }

    /**
     * Busca todos los detalles de un catálogo por su código
     * @param codigoCatalogo Código del catálogo (ej: "FORMATO", "SEGURIDAD")
     * @return Lista de detalles del catálogo
     */
    public List<CatalogoDetalle> findByCodigoCatalogo(String codigoCatalogo) {
        return find("SELECT d FROM CatalogoDetalle d JOIN d.catalogo c WHERE c.codigo = ?1", codigoCatalogo).list();
    }

    /**
     * Busca todos los detalles activos de un catálogo por su código
     * @param codigoCatalogo Código del catálogo
     * @return Lista de detalles activos del catálogo
     */
    public List<CatalogoDetalle> findActivosByCodigoCatalogo(String codigoCatalogo) {
        return find("SELECT d FROM CatalogoDetalle d JOIN d.catalogo c WHERE c.codigo = ?1 AND d.estado = ?2", 
                    codigoCatalogo, "A").list();
    }
}






