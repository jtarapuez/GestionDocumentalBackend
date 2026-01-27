package ec.gob.iess.gestiondocumental.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla GDOC_SECCIONES_TP
 * Secciones documentales del sistema de gestión documental
 */
@Entity
@Table(name = "GDOC_SECCIONES_TP", schema = "DOCUMENTAL_OWNER")
public class SeccionDocumental {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seccion_seq")
    @SequenceGenerator(name = "seccion_seq", sequenceName = "GDOC_SECCIONES_S", allocationSize = 1)
    @Column(name = "ID_SECCION")
    private Long id;

    @Column(name = "NOM_SECCION", length = 120)
    private String nombre;

    @Column(name = "DESCR_SECCION", length = 250)
    private String descripcion;

    @Column(name = "EST_REGISTRO", length = 15)
    private String estadoRegistro; // Creado, Actualizado

    @Column(name = "USU_CREACION", length = 10, nullable = false)
    private String usuCreacion;

    @Column(name = "FEC_CREACION", nullable = false)
    private LocalDateTime fecCreacion;

    @Column(name = "IP_EQUIPO", length = 40, nullable = false)
    private String ipEquipo;

    // Constructores
    public SeccionDocumental() {
    }

    public SeccionDocumental(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
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






