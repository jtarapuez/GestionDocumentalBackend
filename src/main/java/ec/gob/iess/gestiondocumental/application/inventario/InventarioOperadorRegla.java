package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;

/**
 * Regla: solo quien creó el inventario (operador almacenado) puede actualizarlo.
 */
public final class InventarioOperadorRegla {

    private static final int HTTP_BAD_REQUEST = 400;

    private InventarioOperadorRegla() {
    }

    /**
     * @throws NegocioApiException si la cédula del solicitante no coincide con el operador del registro
     */
    public static void assertMismoOperadorQueCreo(String cedulaSolicitante, String operadorAlmacenado) {
        if (!cedulaSolicitante.equals(operadorAlmacenado)) {
            throw new NegocioApiException(
                    InventarioCodigosError.INV_OPERADOR_NO_AUTORIZADO,
                    InventarioNegocioMessages.SOLO_OPERADOR_CREADOR_PUEDE_ACTUALIZAR,
                    HTTP_BAD_REQUEST);
        }
    }
}
