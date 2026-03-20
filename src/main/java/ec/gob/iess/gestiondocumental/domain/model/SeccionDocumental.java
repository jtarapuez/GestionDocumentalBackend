package ec.gob.iess.gestiondocumental.domain.model;

import java.time.LocalDateTime;

/**
 * Entidad de dominio: Sección documental. Sin JPA (PAS-GUI-047).
 */
public class SeccionDocumental {

    private Long id;
    private String nombre;
    private String descripcion;
    private String estadoRegistro;
    private String usuCreacion;
    private LocalDateTime fecCreacion;
    private String ipEquipo;

    public SeccionDocumental() {
    }

    public SeccionDocumental(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String usuCreacion) { this.usuCreacion = usuCreacion; }
    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime fecCreacion) { this.fecCreacion = fecCreacion; }
    public String getIpEquipo() { return ipEquipo; }
    public void setIpEquipo(String ipEquipo) { this.ipEquipo = ipEquipo; }
}
