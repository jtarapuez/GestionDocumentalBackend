package ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper;

import ec.gob.iess.gestiondocumental.domain.model.CatalogoDetalle;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.CatalogoDetalleEntity;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapeo entre entidad de dominio CatalogoDetalle y entidad JPA CatalogoDetalleEntity.
 */
@ApplicationScoped
public class CatalogoDetalleMapper {

    public CatalogoDetalle toDomain(CatalogoDetalleEntity entity) {
        if (entity == null) return null;
        CatalogoDetalle domain = new CatalogoDetalle();
        domain.setId(entity.getId());
        domain.setCodigo(entity.getCodigo());
        domain.setDescripcion(entity.getDescripcion());
        domain.setEstado(entity.getEstado());
        domain.setObservacion(entity.getObservacion());
        domain.setUsuCreacion(entity.getUsuCreacion());
        domain.setFecCreacion(entity.getFecCreacion());
        domain.setIpEquipo(entity.getIpEquipo());
        domain.setIdCatalogo(entity.getIdCatalogo());
        return domain;
    }

    public CatalogoDetalleEntity toEntity(CatalogoDetalle domain) {
        if (domain == null) return null;
        CatalogoDetalleEntity entity = new CatalogoDetalleEntity();
        entity.setId(domain.getId());
        entity.setCodigo(domain.getCodigo());
        entity.setDescripcion(domain.getDescripcion());
        entity.setEstado(domain.getEstado());
        entity.setObservacion(domain.getObservacion());
        entity.setUsuCreacion(domain.getUsuCreacion());
        entity.setFecCreacion(domain.getFecCreacion());
        entity.setIpEquipo(domain.getIpEquipo());
        entity.setIdCatalogo(domain.getIdCatalogo());
        return entity;
    }
}
