package ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.InventarioDocumentalEntity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventarioDocumentalMapper {

    public InventarioDocumental toDomain(InventarioDocumentalEntity entity) {
        if (entity == null) return null;
        InventarioDocumental d = new InventarioDocumental();
        d.setId(entity.getId());
        d.setNumeroExpediente(entity.getNumeroExpediente());
        d.setNumeroCedula(entity.getNumeroCedula());
        d.setNumeroRuc(entity.getNumeroRuc());
        d.setNombresApellidos(entity.getNombresApellidos());
        d.setRazonSocial(entity.getRazonSocial());
        d.setDescripcionSerie(entity.getDescripcionSerie());
        d.setNumeroExtremoDesde(entity.getNumeroExtremoDesde());
        d.setNumeroExtremoHasta(entity.getNumeroExtremoHasta());
        d.setFechaDesde(entity.getFechaDesde());
        d.setFechaHasta(entity.getFechaHasta());
        d.setCantidadFojas(entity.getCantidadFojas());
        d.setTipoContenedor(entity.getTipoContenedor());
        d.setNumeroContenedor(entity.getNumeroContenedor());
        d.setSoporte(entity.getSoporte());
        d.setTipoArchivo(entity.getTipoArchivo());
        d.setPosicionPasivo(entity.getPosicionPasivo());
        d.setNumeroRac(entity.getNumeroRac());
        d.setNumeroFila(entity.getNumeroFila());
        d.setNumeroColumna(entity.getNumeroColumna());
        d.setNumeroPosicion(entity.getNumeroPosicion());
        d.setBodega(entity.getBodega());
        d.setObservaciones(entity.getObservaciones());
        d.setOperador(entity.getOperador());
        d.setSupervisor(entity.getSupervisor());
        d.setEstadoInventario(entity.getEstadoInventario());
        d.setFechaCambioEstado(entity.getFechaCambioEstado());
        d.setCedulaUsuarioCambio(entity.getCedulaUsuarioCambio());
        d.setIdSeccion(entity.getIdSeccion());
        d.setIdSerie(entity.getIdSerie());
        d.setIdSubserie(entity.getIdSubserie());
        d.setUsuCreacion(entity.getUsuCreacion());
        d.setFecCreacion(entity.getFecCreacion());
        d.setIpEquipo(entity.getIpEquipo());
        return d;
    }

    public InventarioDocumentalEntity toEntity(InventarioDocumental domain) {
        if (domain == null) return null;
        InventarioDocumentalEntity e = new InventarioDocumentalEntity();
        e.setId(domain.getId());
        e.setNumeroExpediente(domain.getNumeroExpediente());
        e.setNumeroCedula(domain.getNumeroCedula());
        e.setNumeroRuc(domain.getNumeroRuc());
        e.setNombresApellidos(domain.getNombresApellidos());
        e.setRazonSocial(domain.getRazonSocial());
        e.setDescripcionSerie(domain.getDescripcionSerie());
        e.setNumeroExtremoDesde(domain.getNumeroExtremoDesde());
        e.setNumeroExtremoHasta(domain.getNumeroExtremoHasta());
        e.setFechaDesde(domain.getFechaDesde());
        e.setFechaHasta(domain.getFechaHasta());
        e.setCantidadFojas(domain.getCantidadFojas());
        e.setTipoContenedor(domain.getTipoContenedor());
        e.setNumeroContenedor(domain.getNumeroContenedor());
        e.setSoporte(domain.getSoporte());
        e.setTipoArchivo(domain.getTipoArchivo());
        e.setPosicionPasivo(domain.getPosicionPasivo());
        e.setNumeroRac(domain.getNumeroRac());
        e.setNumeroFila(domain.getNumeroFila());
        e.setNumeroColumna(domain.getNumeroColumna());
        e.setNumeroPosicion(domain.getNumeroPosicion());
        e.setBodega(domain.getBodega());
        e.setObservaciones(domain.getObservaciones());
        e.setOperador(domain.getOperador());
        e.setSupervisor(domain.getSupervisor());
        e.setEstadoInventario(domain.getEstadoInventario());
        e.setFechaCambioEstado(domain.getFechaCambioEstado());
        e.setCedulaUsuarioCambio(domain.getCedulaUsuarioCambio());
        e.setIdSeccion(domain.getIdSeccion());
        e.setIdSerie(domain.getIdSerie());
        e.setIdSubserie(domain.getIdSubserie());
        e.setUsuCreacion(domain.getUsuCreacion());
        e.setFecCreacion(domain.getFecCreacion());
        e.setIpEquipo(domain.getIpEquipo());
        return e;
    }
}
