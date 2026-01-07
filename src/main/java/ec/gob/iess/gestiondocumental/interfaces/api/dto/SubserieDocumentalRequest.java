package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de request para crear/actualizar una subserie documental
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubserieDocumentalRequest {

    private Long idSerie;
    private String nombreSubserie;
    private String descripcion;
    private String formatoDoc; // Físico, Digital, Mixto
    private String seguridad; // Pública, Confidencial, Reservada
    private String normativa;
    private String responsable; // Cédula del usuario responsable
    private String estado; // Creado, Actualizado
    private String justificacion;

    // Constructores
    public SubserieDocumentalRequest() {
    }

    // Getters y Setters
    public Long getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(Long idSerie) {
        this.idSerie = idSerie;
    }

    public String getNombreSubserie() {
        return nombreSubserie;
    }

    public void setNombreSubserie(String nombreSubserie) {
        this.nombreSubserie = nombreSubserie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFormatoDoc() {
        return formatoDoc;
    }

    public void setFormatoDoc(String formatoDoc) {
        this.formatoDoc = formatoDoc;
    }

    public String getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(String seguridad) {
        this.seguridad = seguridad;
    }

    public String getNormativa() {
        return normativa;
    }

    public void setNormativa(String normativa) {
        this.normativa = normativa;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }
}

