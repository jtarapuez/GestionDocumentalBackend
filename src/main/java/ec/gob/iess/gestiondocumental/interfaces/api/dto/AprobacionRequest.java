package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de request para aprobar un inventario
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AprobacionRequest {

    private String observaciones; // Opcional: observaciones de la aprobación

    // Constructores
    public AprobacionRequest() {
    }

    // Getters y Setters
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}






