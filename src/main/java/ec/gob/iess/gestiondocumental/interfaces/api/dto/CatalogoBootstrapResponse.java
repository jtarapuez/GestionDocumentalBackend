package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import java.util.List;
import java.util.Map;

/**
 * Respuesta agregada para precargar catálogos frecuentes del MFE en una sola petición.
 */
public class CatalogoBootstrapResponse {

    private List<SeccionDocumentalResponse> secciones;
    private Map<String, List<CatalogoDetalleResponse>> detallesPorCodigo;

    public CatalogoBootstrapResponse() {
    }

    public CatalogoBootstrapResponse(
            List<SeccionDocumentalResponse> secciones,
            Map<String, List<CatalogoDetalleResponse>> detallesPorCodigo) {
        this.secciones = secciones;
        this.detallesPorCodigo = detallesPorCodigo;
    }

    public List<SeccionDocumentalResponse> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<SeccionDocumentalResponse> secciones) {
        this.secciones = secciones;
    }

    public Map<String, List<CatalogoDetalleResponse>> getDetallesPorCodigo() {
        return detallesPorCodigo;
    }

    public void setDetallesPorCodigo(Map<String, List<CatalogoDetalleResponse>> detallesPorCodigo) {
        this.detallesPorCodigo = detallesPorCodigo;
    }
}
