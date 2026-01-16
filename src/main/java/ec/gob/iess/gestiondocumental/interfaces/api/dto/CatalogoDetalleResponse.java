package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de respuesta para CatalogoDetalle
 * Representa un detalle/valor específico de un catálogo en las respuestas de la API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogoDetalleResponse {

    private Long id;
    private String codigo;
    private String descripcion;
    private String estado;
    private String observacion;
    private Long idCatalogo;

    // Constructores
    public CatalogoDetalleResponse() {
    }

    public CatalogoDetalleResponse(Long id, String codigo, String descripcion, String estado, Long idCatalogo) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idCatalogo = idCatalogo;
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

    public Long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }
}

