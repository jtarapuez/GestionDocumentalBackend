package ec.gob.iess.gestiondocumental.application.inventario;

/**
 * Códigos de error estables para el dominio inventario (contrato API / Fase 4).
 */
public final class InventarioCodigosError {

    public static final String INV_PENDIENTES_VENCIDOS = "INV_PENDIENTES_VENCIDOS";
    public static final String INV_OPERADOR_NO_AUTORIZADO = "INV_OPERADOR_NO_AUTORIZADO";
    public static final String INV_ACTUALIZACION_APROBADO_NO_MODIFICABLE = "INV_ACTUALIZACION_APROBADO_NO_MODIFICABLE";
    public static final String INV_ACTUALIZACION_PLAZO_VENCIDO = "INV_ACTUALIZACION_PLAZO_VENCIDO";
    public static final String INV_ACTUALIZACION_ESTADO_NO_PERMITIDO = "INV_ACTUALIZACION_ESTADO_NO_PERMITIDO";
    public static final String INV_APROBACION_ESTADO_INVALIDO = "INV_APROBACION_ESTADO_INVALIDO";
    public static final String INV_RECHAZO_OBSERVACIONES_REQUERIDAS = "INV_RECHAZO_OBSERVACIONES_REQUERIDAS";
    public static final String INV_RECHAZO_ESTADO_INVALIDO = "INV_RECHAZO_ESTADO_INVALIDO";

    private InventarioCodigosError() {
    }
}
