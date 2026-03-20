package ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper;

import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SerieDocumentalEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SerieDocumentalMapper {

    public SerieDocumental toDomain(SerieDocumentalEntity entity) {
        if (entity == null) return null;
        SerieDocumental d = new SerieDocumental();
        d.setId(entity.getId());
        d.setNombreSerie(entity.getNombreSerie());
        d.setDescripcion(entity.getDescripcion());
        d.setFormatoDoc(entity.getFormatoDoc());
        d.setSeguridad(entity.getSeguridad());
        d.setNormativa(entity.getNormativa());
        d.setResponsable(entity.getResponsable());
        d.setEstado(entity.getEstado());
        d.setJustificacion(entity.getJustificacion());
        d.setIdSeccion(entity.getIdSeccion());
        d.setUsuCreacion(entity.getUsuCreacion());
        d.setFecCreacion(entity.getFecCreacion());
        d.setIpEquipo(entity.getIpEquipo());
        return d;
    }

    public SerieDocumentalEntity toEntity(SerieDocumental domain) {
        if (domain == null) return null;
        SerieDocumentalEntity e = new SerieDocumentalEntity();
        e.setId(domain.getId());
        e.setNombreSerie(domain.getNombreSerie());
        e.setDescripcion(domain.getDescripcion());
        e.setFormatoDoc(domain.getFormatoDoc());
        e.setSeguridad(domain.getSeguridad());
        e.setNormativa(domain.getNormativa());
        e.setResponsable(domain.getResponsable());
        e.setEstado(domain.getEstado());
        e.setJustificacion(domain.getJustificacion());
        e.setIdSeccion(domain.getIdSeccion());
        e.setUsuCreacion(domain.getUsuCreacion());
        e.setFecCreacion(domain.getFecCreacion());
        e.setIpEquipo(domain.getIpEquipo());
        return e;
    }
}
