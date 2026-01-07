package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de request para rechazar un inventario (Pendiente de Aprobación)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RechazoRequest {

    private String observaciones; // Obligatorio: observaciones del rechazo

    // Constructores
    public RechazoRequest() {
    }

    // Getters y Setters
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

