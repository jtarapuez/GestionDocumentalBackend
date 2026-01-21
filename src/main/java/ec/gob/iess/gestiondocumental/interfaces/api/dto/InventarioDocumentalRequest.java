package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

/**
 * DTO de request para crear/actualizar un inventario documental
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventarioDocumentalRequest {

    private Long idSeccion;
    private Long idSerie;
    private Long idSubserie;
    private String numeroExpediente;
    private String numeroCedula;
    private String numeroRuc;
    private String nombresApellidos;
    private String razonSocial;
    private String descripcionSerie;
    private Integer numeroExtremoDesde;
    private Integer numeroExtremoHasta;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Integer cantidadFojas;
    private String tipoContenedor;
    private Integer numeroContenedor;
    private String soporte;
    private String tipoArchivo;
    private String posicionPasivo;
    private Integer numeroRac;
    private Integer numeroFila;
    private Integer numeroColumna;
    private Integer numeroPosicion;
    private Integer bodega;
    private String observaciones;
    private String supervisor;

    // Constructores
    public InventarioDocumentalRequest() {
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

    public Integer getNumeroExtremoDesde() {
        return numeroExtremoDesde;
    }

    public void setNumeroExtremoDesde(Integer numeroExtremoDesde) {
        this.numeroExtremoDesde = numeroExtremoDesde;
    }

    public Integer getNumeroExtremoHasta() {
        return numeroExtremoHasta;
    }

    public void setNumeroExtremoHasta(Integer numeroExtremoHasta) {
        this.numeroExtremoHasta = numeroExtremoHasta;
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

    public Integer getCantidadFojas() {
        return cantidadFojas;
    }

    public void setCantidadFojas(Integer cantidadFojas) {
        this.cantidadFojas = cantidadFojas;
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

    public String getSoporte() {
        return soporte;
    }

    public void setSoporte(String soporte) {
        this.soporte = soporte;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getPosicionPasivo() {
        return posicionPasivo;
    }

    public void setPosicionPasivo(String posicionPasivo) {
        this.posicionPasivo = posicionPasivo;
    }

    public Integer getNumeroRac() {
        return numeroRac;
    }

    public void setNumeroRac(Integer numeroRac) {
        this.numeroRac = numeroRac;
    }

    public Integer getNumeroFila() {
        return numeroFila;
    }

    public void setNumeroFila(Integer numeroFila) {
        this.numeroFila = numeroFila;
    }

    public Integer getNumeroColumna() {
        return numeroColumna;
    }

    public void setNumeroColumna(Integer numeroColumna) {
        this.numeroColumna = numeroColumna;
    }

    public Integer getNumeroPosicion() {
        return numeroPosicion;
    }

    public void setNumeroPosicion(Integer numeroPosicion) {
        this.numeroPosicion = numeroPosicion;
    }

    public Integer getBodega() {
        return bodega;
    }

    public void setBodega(Integer bodega) {
        this.bodega = bodega;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}




