package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

/**
 * Mapeo {@link InventarioDocumentalRequest} ↔ dominio para alta y parches de actualización (sin JPA).
 */
@ApplicationScoped
public class InventarioDocumentalRegistroMapper {

    public InventarioDocumental toNuevoInventario(
            InventarioDocumentalRequest request,
            String usuarioCedula,
            String ipEquipo,
            LocalDateTime ahora) {
        InventarioDocumental inventario = new InventarioDocumental();
        inventario.setIdSeccion(request.getIdSeccion());
        inventario.setIdSerie(request.getIdSerie());
        inventario.setIdSubserie(request.getIdSubserie());
        inventario.setNumeroExpediente(request.getNumeroExpediente());
        inventario.setNumeroCedula(request.getNumeroCedula());
        inventario.setNumeroRuc(request.getNumeroRuc());
        inventario.setNombresApellidos(request.getNombresApellidos());
        inventario.setRazonSocial(request.getRazonSocial());
        inventario.setDescripcionSerie(request.getDescripcionSerie());
        inventario.setNumeroExtremoDesde(request.getNumeroExtremoDesde());
        inventario.setNumeroExtremoHasta(request.getNumeroExtremoHasta());
        inventario.setFechaDesde(request.getFechaDesde());
        inventario.setFechaHasta(request.getFechaHasta());
        inventario.setCantidadFojas(request.getCantidadFojas());
        inventario.setTipoContenedor(request.getTipoContenedor());
        inventario.setNumeroContenedor(request.getNumeroContenedor());
        inventario.setSoporte(request.getSoporte());
        inventario.setTipoArchivo(request.getTipoArchivo());
        ArchivoPasivoPosicion.copiarCamposPasivoAlDominio(inventario, request);
        inventario.setObservaciones(request.getObservaciones());
        inventario.setOperador(usuarioCedula);
        inventario.setSupervisor(request.getSupervisor());
        inventario.setEstadoInventario("Registrado");
        inventario.setFechaCambioEstado(ahora);
        inventario.setCedulaUsuarioCambio(usuarioCedula);
        inventario.setUsuCreacion(usuarioCedula);
        inventario.setFecCreacion(ahora);
        inventario.setIpEquipo(ipEquipo);
        return inventario;
    }

    /**
     * Actualiza solo los campos presentes en el request (no nulos). Ubicación pasiva con las mismas reglas que en alta.
     */
    public void aplicarParchesDesdeRequest(InventarioDocumental inventario, InventarioDocumentalRequest request) {
        if (request.getNumeroExpediente() != null) {
            inventario.setNumeroExpediente(request.getNumeroExpediente());
        }
        if (request.getNumeroCedula() != null) {
            inventario.setNumeroCedula(request.getNumeroCedula());
        }
        if (request.getNumeroRuc() != null) {
            inventario.setNumeroRuc(request.getNumeroRuc());
        }
        if (request.getNombresApellidos() != null) {
            inventario.setNombresApellidos(request.getNombresApellidos());
        }
        if (request.getRazonSocial() != null) {
            inventario.setRazonSocial(request.getRazonSocial());
        }
        if (request.getDescripcionSerie() != null) {
            inventario.setDescripcionSerie(request.getDescripcionSerie());
        }
        if (request.getNumeroExtremoDesde() != null) {
            inventario.setNumeroExtremoDesde(request.getNumeroExtremoDesde());
        }
        if (request.getNumeroExtremoHasta() != null) {
            inventario.setNumeroExtremoHasta(request.getNumeroExtremoHasta());
        }
        if (request.getFechaDesde() != null) {
            inventario.setFechaDesde(request.getFechaDesde());
        }
        if (request.getFechaHasta() != null) {
            inventario.setFechaHasta(request.getFechaHasta());
        }
        if (request.getCantidadFojas() != null) {
            inventario.setCantidadFojas(request.getCantidadFojas());
        }
        if (request.getTipoContenedor() != null) {
            inventario.setTipoContenedor(request.getTipoContenedor());
        }
        if (request.getNumeroContenedor() != null) {
            inventario.setNumeroContenedor(request.getNumeroContenedor());
        }
        if (request.getSoporte() != null) {
            inventario.setSoporte(request.getSoporte());
        }
        if (request.getTipoArchivo() != null) {
            inventario.setTipoArchivo(request.getTipoArchivo());
        }
        if (request.getObservaciones() != null) {
            inventario.setObservaciones(request.getObservaciones());
        }
        ArchivoPasivoPosicion.copiarCamposPasivoAlDominio(inventario, request);
    }
}
