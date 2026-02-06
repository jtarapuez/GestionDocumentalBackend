package ec.gob.iess.gestiondocumental.interfaces.api.context;

import jakarta.enterprise.context.RequestScoped;

/**
 * Contexto de la petición HTTP actual (PAS-EST-043).
 * Almacena path y requestId para incluir en meta de cada respuesta.
 */
@RequestScoped
public class RequestContext {

    private String path;
    private String requestId;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
