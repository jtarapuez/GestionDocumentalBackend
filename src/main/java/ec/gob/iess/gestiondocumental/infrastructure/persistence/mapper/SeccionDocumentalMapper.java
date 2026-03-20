package ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper;

import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SeccionDocumentalEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SeccionDocumentalMapper {

    public SeccionDocumental toDomain(SeccionDocumentalEntity entity) {
        if (entity == null) return null;
        SeccionDocumental d = new SeccionDocumental();
        d.setId(entity.getId());
        d.setNombre(entity.getNombre());
        d.setDescripcion(entity.getDescripcion());
        d.setEstadoRegistro(entity.getEstadoRegistro());
        d.setUsuCreacion(entity.getUsuCreacion());
        d.setFecCreacion(entity.getFecCreacion());
        d.setIpEquipo(entity.getIpEquipo());
        return d;
    }

    public SeccionDocumentalEntity toEntity(SeccionDocumental domain) {
        if (domain == null) return null;
        SeccionDocumentalEntity e = new SeccionDocumentalEntity();
        e.setId(domain.getId());
        e.setNombre(domain.getNombre());
        e.setDescripcion(domain.getDescripcion());
        e.setEstadoRegistro(domain.getEstadoRegistro());
        e.setUsuCreacion(domain.getUsuCreacion());
        e.setFecCreacion(domain.getFecCreacion());
        e.setIpEquipo(domain.getIpEquipo());
        return e;
    }
}
