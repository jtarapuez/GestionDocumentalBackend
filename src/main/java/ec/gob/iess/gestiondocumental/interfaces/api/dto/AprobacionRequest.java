package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de entrada para la operación de aprobación de un inventario documental.
 * Se utiliza en {@code PUT /v1/inventarios/{id}/aprobar}. Permite enviar observaciones
 * opcionales de la aprobación.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AprobacionRequest {

    /** Observaciones opcionales de la aprobación. */
    private String observaciones;

    /** Constructor por defecto. */
    public AprobacionRequest() {
    }

    /** @return observaciones opcionales de la aprobación */
    public String getObservaciones() {
        return observaciones;
    }

    /** @param observaciones observaciones opcionales de la aprobación */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}







