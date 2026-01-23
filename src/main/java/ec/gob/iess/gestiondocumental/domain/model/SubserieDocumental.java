package ec.gob.iess.gestiondocumental.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla GDOC_SUBSERIES_T
 * Subseries documentales del sistema de gestión documental
 */
@Entity
@Table(name = "GDOC_SUBSERIES_T", schema = "DOCUMENTAL_OWNER")
public class SubserieDocumental {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subserie_seq")
    @SequenceGenerator(name = "subserie_seq", sequenceName = "GDOC_SUBSERIES_S", allocationSize = 1)
    @Column(name = "ID_SUBSERIE")
    private Long id;

    @Column(name = "NOM_SUBSERIE", length = 120)
    private String nombreSubserie;

    @Column(name = "DESCR_SUBSERIE", length = 250)
    private String descripcion;

    @Column(name = "FORMATO_DOC", length = 10)
    private String formatoDoc; // Físico, Digital, Mixto

    @Column(name = "SEGURIDAD", length = 15)
    private String seguridad; // Pública, Confidencial, Reservada

    @Column(name = "NORMATIVA", length = 150)
    private String normativa;

    @Column(name = "RESPONSABLE", length = 10)
    private String responsable; // Cédula del usuario responsable

    @Column(name = "ESTADO", length = 15)
    private String estado; // Creado, Actualizado

    @Column(name = "JUSTIFICACION", length = 150)
    private String justificacion;

    @Column(name = "ID_SERIE", nullable = false)
    private Long idSerie;

    @Column(name = "USU_CREACION", length = 10, nullable = false)
    private String usuCreacion;

    @Column(name = "FEC_CREACION", nullable = false)
    private LocalDateTime fecCreacion;

    @Column(name = "IP_EQUIPO", length = 40, nullable = false)
    private String ipEquipo;

    // Constructores
    public SubserieDocumental() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(Long idSerie) {
        this.idSerie = idSerie;
    }

    public String getUsuCreacion() {
        return usuCreacion;
    }

    public void setUsuCreacion(String usuCreacion) {
        this.usuCreacion = usuCreacion;
    }

    public LocalDateTime getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(LocalDateTime fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getIpEquipo() {
        return ipEquipo;
    }

    public void setIpEquipo(String ipEquipo) {
        this.ipEquipo = ipEquipo;
    }
}






