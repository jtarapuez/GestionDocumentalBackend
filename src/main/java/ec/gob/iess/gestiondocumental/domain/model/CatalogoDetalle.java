package ec.gob.iess.gestiondocumental.domain.model;

import java.time.LocalDateTime;

/**
 * Entidad de dominio: detalle/valor de un catálogo.
 * Sin dependencias de JPA ni infraestructura (PAS-GUI-047).
 */
public class CatalogoDetalle {

    private Long id;
    private String codigo;
    private String descripcion;
    private String estado; // A = Activo, I = Inactivo
    private String observacion;
    private String usuCreacion;
    private LocalDateTime fecCreacion;
    private String ipEquipo;
    private Long idCatalogo;

    public CatalogoDetalle() {
    }

    public CatalogoDetalle(String codigo, String descripcion, String estado, Long idCatalogo) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idCatalogo = idCatalogo;
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

    public Long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public boolean isActivo() {
        return "A".equalsIgnoreCase(estado);
    }
}
