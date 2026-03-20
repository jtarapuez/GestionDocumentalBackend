package ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper;

import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SubserieDocumentalEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SubserieDocumentalMapper {

    public SubserieDocumental toDomain(SubserieDocumentalEntity entity) {
        if (entity == null) return null;
        SubserieDocumental d = new SubserieDocumental();
        d.setId(entity.getId());
        d.setNombreSubserie(entity.getNombreSubserie());
        d.setDescripcion(entity.getDescripcion());
        d.setFormatoDoc(entity.getFormatoDoc());
        d.setSeguridad(entity.getSeguridad());
        d.setNormativa(entity.getNormativa());
        d.setResponsable(entity.getResponsable());
        d.setEstado(entity.getEstado());
        d.setJustificacion(entity.getJustificacion());
        d.setIdSerie(entity.getIdSerie());
        d.setUsuCreacion(entity.getUsuCreacion());
        d.setFecCreacion(entity.getFecCreacion());
        d.setIpEquipo(entity.getIpEquipo());
        return d;
    }

    public SubserieDocumentalEntity toEntity(SubserieDocumental domain) {
        if (domain == null) return null;
        SubserieDocumentalEntity e = new SubserieDocumentalEntity();
        e.setId(domain.getId());
        e.setNombreSubserie(domain.getNombreSubserie());
        e.setDescripcion(domain.getDescripcion());
        e.setFormatoDoc(domain.getFormatoDoc());
        e.setSeguridad(domain.getSeguridad());
        e.setNormativa(domain.getNormativa());
        e.setResponsable(domain.getResponsable());
        e.setEstado(domain.getEstado());
        e.setJustificacion(domain.getJustificacion());
        e.setIdSerie(domain.getIdSerie());
        e.setUsuCreacion(domain.getUsuCreacion());
        e.setFecCreacion(domain.getFecCreacion());
        e.setIpEquipo(domain.getIpEquipo());
        return e;
    }
}
