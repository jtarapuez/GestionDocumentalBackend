package ec.gob.iess.gestiondocumental.domain.model;

import java.time.LocalDateTime;

/**
 * Entidad de dominio: Subserie documental. Sin JPA (PAS-GUI-047).
 */
public class SubserieDocumental {

    private Long id;
    private String nombreSubserie;
    private String descripcion;
    private String formatoDoc;
    private String seguridad;
    private String normativa;
    private String responsable;
    private String estado;
    private String justificacion;
    private Long idSerie;
    private String usuCreacion;
    private LocalDateTime fecCreacion;
    private String ipEquipo;

    public SubserieDocumental() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreSubserie() { return nombreSubserie; }
    public void setNombreSubserie(String nombreSubserie) { this.nombreSubserie = nombreSubserie; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getFormatoDoc() { return formatoDoc; }
    public void setFormatoDoc(String formatoDoc) { this.formatoDoc = formatoDoc; }
    public String getSeguridad() { return seguridad; }
    public void setSeguridad(String seguridad) { this.seguridad = seguridad; }
    public String getNormativa() { return normativa; }
    public void setNormativa(String normativa) { this.normativa = normativa; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
    public Long getIdSerie() { return idSerie; }
    public void setIdSerie(Long idSerie) { this.idSerie = idSerie; }
    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String usuCreacion) { this.usuCreacion = usuCreacion; }
    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime fecCreacion) { this.fecCreacion = fecCreacion; }
    public String getIpEquipo() { return ipEquipo; }
    public void setIpEquipo(String ipEquipo) { this.ipEquipo = ipEquipo; }
}
