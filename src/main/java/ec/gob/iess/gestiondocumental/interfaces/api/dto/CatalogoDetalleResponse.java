package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de respuesta que representa un detalle o valor de un catálogo (ej. un valor de FORMATO, SEGURIDAD).
 * Se usa en listados de detalles por catálogo: {@code GET /v1/catalogos/{codigo}/detalles} y en
 * endpoints derivados (formatos, seguridad, estados-serie, etc.).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogoDetalleResponse {

    private Long id;
    private String codigo;
    private String descripcion;
    private String estado;
    private String observacion;
    private Long idCatalogo;

    /** Constructor por defecto. */
    public CatalogoDetalleResponse() {
    }

    /**
     * Constructor con campos principales.
     * @param id identificador del detalle
     * @param codigo código del valor
     * @param descripcion descripción
     * @param estado estado del registro
     * @param idCatalogo identificador del catálogo al que pertenece
     */
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






