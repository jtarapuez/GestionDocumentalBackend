package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

/**
 * DTO de request para consultas avanzadas de inventarios
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultaRequest {

    private Long idSeccion;
    private Long idSerie;
    private Long idSubserie;
    private String numeroExpediente;
    private String tipoContenedor;
    private Integer numeroContenedor;
    private String tipoArchivo; // Archivo activo, Archivo pasivo
    private String operador; // Cédula del operador
    private String numeroCedula;
    private String numeroRuc;
    private String nombresApellidos;
    private String razonSocial;
    private String descripcionSerie;
    private String estado;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    // Constructores
    public ConsultaRequest() {
    }

    // Getters y Setters
    public Long getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Long idSeccion) {
        this.idSeccion = idSeccion;
    }

    public Long getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(Long idSerie) {
        this.idSerie = idSerie;
    }

    public Long getIdSubserie() {
        return idSubserie;
    }

    public void setIdSubserie(Long idSubserie) {
        this.idSubserie = idSubserie;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getTipoContenedor() {
        return tipoContenedor;
    }

    public void setTipoContenedor(String tipoContenedor) {
        this.tipoContenedor = tipoContenedor;
    }

    public Integer getNumeroContenedor() {
        return numeroContenedor;
    }

    public void setNumeroContenedor(Integer numeroContenedor) {
        this.numeroContenedor = numeroContenedor;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getNumeroCedula() {
        return numeroCedula;
    }

    public void setNumeroCedula(String numeroCedula) {
        this.numeroCedula = numeroCedula;
    }

    public String getNumeroRuc() {
        return numeroRuc;
    }

    public void setNumeroRuc(String numeroRuc) {
        this.numeroRuc = numeroRuc;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDescripcionSerie() {
        return descripcionSerie;
    }

    public void setDescripcionSerie(String descripcionSerie) {
        this.descripcionSerie = descripcionSerie;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}






