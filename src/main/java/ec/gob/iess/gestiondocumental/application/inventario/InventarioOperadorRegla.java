package ec.gob.iess.gestiondocumental.application.inventario;

/**
 * Regla: solo quien creó el inventario (operador almacenado) puede actualizarlo.
 */
public final class InventarioOperadorRegla {

    private InventarioOperadorRegla() {
    }

    /**
     * @throws IllegalStateException si la cédula del solicitante no coincide con el operador del registro
     */
    public static void assertMismoOperadorQueCreo(String cedulaSolicitante, String operadorAlmacenado) {
        if (!cedulaSolicitante.equals(operadorAlmacenado)) {
            throw new IllegalStateException(InventarioNegocioMessages.SOLO_OPERADOR_CREADOR_PUEDE_ACTUALIZAR);
        }
    }
}
