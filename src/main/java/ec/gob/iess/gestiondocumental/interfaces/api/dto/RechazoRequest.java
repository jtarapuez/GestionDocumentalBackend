package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de entrada para la operación de rechazo de un inventario documental.
 * Se utiliza en {@code PUT /v1/inventarios/{id}/rechazar}. El campo observaciones es obligatorio.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RechazoRequest {

    /** Observaciones del rechazo (obligatorio). */
    private String observaciones;

    /** Constructor por defecto. */
    public RechazoRequest() {
    }

    /** @return observaciones del rechazo */
    public String getObservaciones() {
        return observaciones;
    }

    /** @param observaciones observaciones del rechazo */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}







