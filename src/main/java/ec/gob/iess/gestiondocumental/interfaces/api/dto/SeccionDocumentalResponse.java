package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO de respuesta para SeccionDocumental
 * Representa una sección documental en las respuestas de la API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeccionDocumentalResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String estadoRegistro;

    // Constructores
    public SeccionDocumentalResponse() {
    }

    public SeccionDocumentalResponse(Long id, String nombre, String descripcion, String estadoRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estadoRegistro = estadoRegistro;
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
}






