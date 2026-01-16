package ec.gob.iess.gestiondocumental.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad JPA que representa la tabla GDOC_CATALOGOS_T
 * Catálogos maestros del sistema de gestión documental
 */
@Entity
@Table(name = "GDOC_CATALOGOS_T", schema = "DOCUMENTAL_OWNER")
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "catalogo_seq")
    @SequenceGenerator(name = "catalogo_seq", sequenceName = "GDOC_CATALOGOS_S", allocationSize = 1)
    @Column(name = "ID_CATALOGO")
    private Long id;

    @Column(name = "COD_CATALOGO", length = 20, nullable = false, unique = true)
    private String codigo;

    @Column(name = "DESCRIPCION", length = 500, nullable = false)
    private String descripcion;

    @Column(name = "ESTADO", length = 1, nullable = false)
    private String estado; // A = Activo, I = Inactivo

    @Column(name = "OBSERVACION", length = 200)
    private String observacion;

    @Column(name = "USU_CREACION", length = 10, nullable = false)
    private String usuCreacion;

    @Column(name = "FEC_CREACION", nullable = false)
    private LocalDateTime fecCreacion;

    @Column(name = "IP_EQUIPO", length = 40, nullable = false)
    private String ipEquipo;

    @OneToMany(mappedBy = "catalogo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CatalogoDetalle> detalles;

    // Constructores
    public Catalogo() {
    }

    public Catalogo(String codigo, String descripcion, String estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public List<CatalogoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CatalogoDetalle> detalles) {
        this.detalles = detalles;
    }

    // Métodos de utilidad
    public boolean isActivo() {
        return "A".equalsIgnoreCase(estado);
    }
}

