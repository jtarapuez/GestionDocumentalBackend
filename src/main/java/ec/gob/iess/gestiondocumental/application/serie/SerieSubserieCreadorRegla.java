package ec.gob.iess.gestiondocumental.application.serie;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;

/**
 * Regla: solo quien creó la serie/subserie ({@code USU_CREACION}) puede actualizarla.
 * Paridad con {@link ec.gob.iess.gestiondocumental.application.inventario.InventarioOperadorRegla}.
 */
public final class SerieSubserieCreadorRegla {

    private static final int HTTP_BAD_REQUEST = 400;

    private SerieSubserieCreadorRegla() {
    }

    /**
     * @throws NegocioApiException si la cédula del solicitante no coincide con {@code usuCreacionAlmacenado}
     */
    public static void assertMismoCreadorQueCreo(
            String cedulaSolicitante,
            String usuCreacionAlmacenado,
            String codigoError,
            String mensaje) {
        if (cedulaSolicitante == null
                || usuCreacionAlmacenado == null
                || !cedulaSolicitante.equals(usuCreacionAlmacenado)) {
            throw new NegocioApiException(codigoError, mensaje, HTTP_BAD_REQUEST);
        }
    }
}
