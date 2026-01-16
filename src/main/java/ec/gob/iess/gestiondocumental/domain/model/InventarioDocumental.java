package ec.gob.iess.gestiondocumental.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla GDOC_INVENTARIO_T
 * Inventarios documentales del sistema de gestión documental
 */
@Entity
@Table(name = "GDOC_INVENTARIO_T", schema = "DOCUMENTAL_OWNER")
public class InventarioDocumental {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventario_seq")
    @SequenceGenerator(name = "inventario_seq", sequenceName = "GDOC_INVENTARIO_S", allocationSize = 1)
    @Column(name = "ID_INVENTARIO")
    private Long id;

    @Column(name = "NUM_EXPEDIENTE", length = 20)
    private String numeroExpediente;

    @Column(name = "NUM_CEDULA", length = 10)
    private String numeroCedula;

    @Column(name = "NUM_RUC", length = 13)
    private String numeroRuc;

    @Column(name = "NOMBRES_APELLIDOS", length = 120)
    private String nombresApellidos;

    @Column(name = "RAZON_SOCIAL", length = 120)
    private String razonSocial;

    @Column(name = "DESCR_SERIE", length = 300)
    private String descripcionSerie;

    @Column(name = "NUM_EXTDESDE")
    private Integer numeroExtremoDesde;

    @Column(name = "NUM_EXTHASTA")
    private Integer numeroExtremoHasta;

    @Column(name = "FEC_DESDE")
    private LocalDate fechaDesde;

    @Column(name = "FEC_HASTA")
    private LocalDate fechaHasta;

    @Column(name = "CANT_FOJAS")
    private Integer cantidadFojas;

    @Column(name = "TIPO_CONTENEDOR", length = 15)
    private String tipoContenedor; // Caja, Carpeta, Legajo o lote, Tomo

    @Column(name = "NUM_CONTENEDOR")
    private Integer numeroContenedor;

    @Column(name = "SOPORTE", length = 10)
    private String soporte; // Físico, Digital, Mixto

    @Column(name = "TIPO_ARCHIVO", length = 15)
    private String tipoArchivo; // Archivo activo, Archivo pasivo

    @Column(name = "POSICION_PASIVO", length = 15)
    private String posicionPasivo; // Formato: RAC.FILA.COLUMNA.POSICION.BODEGA

    @Column(name = "NUM_RAC")
    private Integer numeroRac;

    @Column(name = "NUM_FILA")
    private Integer numeroFila;

    @Column(name = "NUM_COLUMNA")
    private Integer numeroColumna;

    @Column(name = "NUM_POSICION")
    private Integer numeroPosicion;

    @Column(name = "BODEGA")
    private Integer bodega;

    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    @Column(name = "OPERADOR", length = 10)
    private String operador; // Cédula del operador

    @Column(name = "SUPERVISOR", length = 10)
    private String supervisor; // Cédula del supervisor

    @Column(name = "ESTADO_INVENTARIO", length = 30)
    private String estadoInventario; // Registrado, Actualizado, Aprobado, Pendiente de Aprobación, Aprobado con Modificaciones

    @Column(name = "FEC_CAMBIO_ESTADO")
    private LocalDateTime fechaCambioEstado;

    @Column(name = "CED_USUARIO_CAMBIO", length = 10)
    private String cedulaUsuarioCambio;

    @Column(name = "ID_SECCION", nullable = false)
    private Long idSeccion;

    @Column(name = "ID_SERIE", nullable = false)
    private Long idSerie;

    @Column(name = "ID_SUBSERIE", nullable = false)
    private Long idSubserie;

    @Column(name = "USU_CREACION", length = 10, nullable = false)
    private String usuCreacion;

    @Column(name = "FEC_CREACION", nullable = false)
    private LocalDateTime fecCreacion;

    @Column(name = "IP_EQUIPO", length = 40, nullable = false)
    private String ipEquipo;

    // Constructores
    public InventarioDocumental() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getEstadoInventario() {
        return estadoInventario;
    }

    public void setEstadoInventario(String estadoInventario) {
        this.estadoInventario = estadoInventario;
    }

    public LocalDateTime getFechaCambioEstado() {
        return fechaCambioEstado;
    }

    public void setFechaCambioEstado(LocalDateTime fechaCambioEstado) {
        this.fechaCambioEstado = fechaCambioEstado;
    }

    public String getCedulaUsuarioCambio() {
        return cedulaUsuarioCambio;
    }

    public void setCedulaUsuarioCambio(String cedulaUsuarioCambio) {
        this.cedulaUsuarioCambio = cedulaUsuarioCambio;
    }

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
}


