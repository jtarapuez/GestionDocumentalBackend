package ec.gob.iess.gestiondocumental.application.inventario;

/**
 * Mensajes de negocio estables para inventario documental (registro / actualización).
 */
public final class InventarioNegocioMessages {

    public static final String REGISTRO_BLOQUEADO_POR_PENDIENTES_VENCIDOS =
            "No se puede registrar nuevo inventario. Tiene registros pendientes de aprobación vencidos "
                    + "(más de 5 días). Por favor actualice los registros pendientes primero.";

    public static final String SOLO_OPERADOR_CREADOR_PUEDE_ACTUALIZAR =
            "Solo el operador que creó el inventario puede actualizarlo";

    private InventarioNegocioMessages() {
    }
}
