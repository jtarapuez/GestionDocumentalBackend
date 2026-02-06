package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de respuesta que representa un catálogo maestro (ej. FORMATO, SEGURIDAD, ESTADO_INVENTARIO).
 * Se usa en {@code GET /v1/catalogos} y en {@code GET /v1/catalogos/{codigo}}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogoResponse {

    private Long id;
    private String codigo;
    private String descripcion;
    private String estado;
    private String observacion;

    /** Constructor por defecto. */
    public CatalogoResponse() {
    }

    /**
     * Constructor con campos principales.
     * @param id identificador del catálogo
     * @param codigo código del catálogo
     * @param descripcion descripción
     * @param estado estado del registro
     */
    public CatalogoResponse(Long id, String codigo, String descripcion, String estado) {
        this.id = id;
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
}






