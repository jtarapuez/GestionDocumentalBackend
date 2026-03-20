package ec.gob.iess.gestiondocumental.domain.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad de dominio: Catálogo maestro.
 * Sin dependencias de JPA ni infraestructura (PAS-GUI-047).
 */
public class Catalogo {

    private Long id;
    private String codigo;
    private String descripcion;
    private String estado; // A = Activo, I = Inactivo
    private String observacion;
    private String usuCreacion;
    private LocalDateTime fecCreacion;
    private String ipEquipo;
    private List<CatalogoDetalle> detalles;

    public Catalogo() {
    }

    public Catalogo(String codigo, String descripcion, String estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

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

    public boolean isActivo() {
        return "A".equalsIgnoreCase(estado);
    }
}
