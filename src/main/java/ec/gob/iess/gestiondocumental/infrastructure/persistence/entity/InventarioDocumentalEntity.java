package ec.gob.iess.gestiondocumental.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA para GDOC_INVENTARIO_T. Solo infraestructura (PAS-GUI-047).
 */
@Entity
@Table(name = "GDOC_INVENTARIO_T", schema = "DOCUMENTAL_OWNER")
public class InventarioDocumentalEntity {

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
    private String tipoContenedor;
    @Column(name = "NUM_CONTENEDOR")
    private Integer numeroContenedor;
    @Column(name = "SOPORTE", length = 10)
    private String soporte;
    @Column(name = "TIPO_ARCHIVO", length = 15)
    private String tipoArchivo;
    @Column(name = "POSICION_PASIVO", length = 15)
    private String posicionPasivo;
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
    private String operador;
    @Column(name = "SUPERVISOR", length = 10)
    private String supervisor;
    @Column(name = "ESTADO_INVENTARIO", length = 30)
    private String estadoInventario;
    @Column(name = "FEC_CAMBIO_ESTADO")
    private LocalDateTime fechaCambioEstado;
    @Column(name = "CED_USUARIO_CAMBIO", length = 10)
    private String cedulaUsuarioCambio;
    @Column(name = "ID_SECCION", nullable = false)
    private Long idSeccion;
    @Column(name = "ID_SERIE", nullable = false)
    private Long idSerie;
    @Column(name = "ID_SUBSERIE", nullable = true)
    private Long idSubserie;
    @Column(name = "USU_CREACION", length = 10, nullable = false)
    private String usuCreacion;
    @Column(name = "FEC_CREACION", nullable = false)
    private LocalDateTime fecCreacion;
    @Column(name = "IP_EQUIPO", length = 40, nullable = false)
    private String ipEquipo;

    public InventarioDocumentalEntity() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroExpediente() { return numeroExpediente; }
    public void setNumeroExpediente(String v) { this.numeroExpediente = v; }
    public String getNumeroCedula() { return numeroCedula; }
    public void setNumeroCedula(String v) { this.numeroCedula = v; }
    public String getNumeroRuc() { return numeroRuc; }
    public void setNumeroRuc(String v) { this.numeroRuc = v; }
    public String getNombresApellidos() { return nombresApellidos; }
    public void setNombresApellidos(String v) { this.nombresApellidos = v; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String v) { this.razonSocial = v; }
    public String getDescripcionSerie() { return descripcionSerie; }
    public void setDescripcionSerie(String v) { this.descripcionSerie = v; }
    public Integer getNumeroExtremoDesde() { return numeroExtremoDesde; }
    public void setNumeroExtremoDesde(Integer v) { this.numeroExtremoDesde = v; }
    public Integer getNumeroExtremoHasta() { return numeroExtremoHasta; }
    public void setNumeroExtremoHasta(Integer v) { this.numeroExtremoHasta = v; }
    public LocalDate getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDate v) { this.fechaDesde = v; }
    public LocalDate getFechaHasta() { return fechaHasta; }
    public void setFechaHasta(LocalDate v) { this.fechaHasta = v; }
    public Integer getCantidadFojas() { return cantidadFojas; }
    public void setCantidadFojas(Integer v) { this.cantidadFojas = v; }
    public String getTipoContenedor() { return tipoContenedor; }
    public void setTipoContenedor(String v) { this.tipoContenedor = v; }
    public Integer getNumeroContenedor() { return numeroContenedor; }
    public void setNumeroContenedor(Integer v) { this.numeroContenedor = v; }
    public String getSoporte() { return soporte; }
    public void setSoporte(String v) { this.soporte = v; }
    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String v) { this.tipoArchivo = v; }
    public String getPosicionPasivo() { return posicionPasivo; }
    public void setPosicionPasivo(String v) { this.posicionPasivo = v; }
    public Integer getNumeroRac() { return numeroRac; }
    public void setNumeroRac(Integer v) { this.numeroRac = v; }
    public Integer getNumeroFila() { return numeroFila; }
    public void setNumeroFila(Integer v) { this.numeroFila = v; }
    public Integer getNumeroColumna() { return numeroColumna; }
    public void setNumeroColumna(Integer v) { this.numeroColumna = v; }
    public Integer getNumeroPosicion() { return numeroPosicion; }
    public void setNumeroPosicion(Integer v) { this.numeroPosicion = v; }
    public Integer getBodega() { return bodega; }
    public void setBodega(Integer v) { this.bodega = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
    public String getOperador() { return operador; }
    public void setOperador(String v) { this.operador = v; }
    public String getSupervisor() { return supervisor; }
    public void setSupervisor(String v) { this.supervisor = v; }
    public String getEstadoInventario() { return estadoInventario; }
    public void setEstadoInventario(String v) { this.estadoInventario = v; }
    public LocalDateTime getFechaCambioEstado() { return fechaCambioEstado; }
    public void setFechaCambioEstado(LocalDateTime v) { this.fechaCambioEstado = v; }
    public String getCedulaUsuarioCambio() { return cedulaUsuarioCambio; }
    public void setCedulaUsuarioCambio(String v) { this.cedulaUsuarioCambio = v; }
    public Long getIdSeccion() { return idSeccion; }
    public void setIdSeccion(Long v) { this.idSeccion = v; }
    public Long getIdSerie() { return idSerie; }
    public void setIdSerie(Long v) { this.idSerie = v; }
    public Long getIdSubserie() { return idSubserie; }
    public void setIdSubserie(Long v) { this.idSubserie = v; }
    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String v) { this.usuCreacion = v; }
    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime v) { this.fecCreacion = v; }
    public String getIpEquipo() { return ipEquipo; }
    public void setIpEquipo(String v) { this.ipEquipo = v; }
}
