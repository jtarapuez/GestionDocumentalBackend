package ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper;

import ec.gob.iess.gestiondocumental.domain.model.Catalogo;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.CatalogoEntity;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapeo entre entidad de dominio Catalogo y entidad JPA CatalogoEntity.
 */
@ApplicationScoped
public class CatalogoMapper {

    public Catalogo toDomain(CatalogoEntity entity) {
        if (entity == null) return null;
        Catalogo domain = new Catalogo();
        domain.setId(entity.getId());
        domain.setCodigo(entity.getCodigo());
        domain.setDescripcion(entity.getDescripcion());
        domain.setEstado(entity.getEstado());
        domain.setObservacion(entity.getObservacion());
        domain.setUsuCreacion(entity.getUsuCreacion());
        domain.setFecCreacion(entity.getFecCreacion());
        domain.setIpEquipo(entity.getIpEquipo());
        return domain;
    }

    public CatalogoEntity toEntity(Catalogo domain) {
        if (domain == null) return null;
        CatalogoEntity entity = new CatalogoEntity();
        entity.setId(domain.getId());
        entity.setCodigo(domain.getCodigo());
        entity.setDescripcion(domain.getDescripcion());
        entity.setEstado(domain.getEstado());
        entity.setObservacion(domain.getObservacion());
        entity.setUsuCreacion(domain.getUsuCreacion());
        entity.setFecCreacion(domain.getFecCreacion());
        entity.setIpEquipo(domain.getIpEquipo());
        return entity;
    }
}
