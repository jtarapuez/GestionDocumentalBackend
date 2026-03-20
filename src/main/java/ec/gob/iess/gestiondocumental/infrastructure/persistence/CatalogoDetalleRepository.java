package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.CatalogoDetalleEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Repositorio Panache para {@link CatalogoDetalleEntity}.
 * Usado por el adaptador de persistencia; la aplicación usa {@link ec.gob.iess.gestiondocumental.application.port.out.CatalogoDetalleRepositoryPort}.
 */
@ApplicationScoped
public class CatalogoDetalleRepository implements PanacheRepository<CatalogoDetalleEntity> {

    public List<CatalogoDetalleEntity> findByCatalogoId(Long idCatalogo) {
        return find("idCatalogo", idCatalogo).list();
    }

    public List<CatalogoDetalleEntity> findActivosByCatalogoId(Long idCatalogo) {
        return find("idCatalogo = ?1 AND estado = ?2", idCatalogo, "A").list();
    }

    public List<CatalogoDetalleEntity> findByCodigoCatalogo(String codigoCatalogo) {
        return find("SELECT d FROM CatalogoDetalleEntity d JOIN d.catalogo c WHERE c.codigo = ?1", codigoCatalogo).list();
    }

    public List<CatalogoDetalleEntity> findActivosByCodigoCatalogo(String codigoCatalogo) {
        return find("SELECT d FROM CatalogoDetalleEntity d JOIN d.catalogo c WHERE c.codigo = ?1 AND d.estado = ?2",
                codigoCatalogo, "A").list();
    }
}
