package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.port.out.InventarioDocumentalRepositoryPort;

/**
 * Regla: no registrar nuevos inventarios si el operador tiene pendientes de aprobación vencidos.
 */
public final class InventarioPendientesRegla {

    private static final int HTTP_BAD_REQUEST = 400;

    private InventarioPendientesRegla() {
    }

    /**
     * @throws NegocioApiException si el repositorio indica pendientes vencidos para la cédula
     */
    public static void validarPuedeRegistrarNuevo(
            InventarioDocumentalRepositoryPort inventarioRepositoryPort,
            String operadorCedula) {
        if (inventarioRepositoryPort.tienePendientesVencidos(operadorCedula)) {
            throw new NegocioApiException(
                    InventarioCodigosError.INV_PENDIENTES_VENCIDOS,
                    InventarioNegocioMessages.REGISTRO_BLOQUEADO_POR_PENDIENTES_VENCIDOS,
                    HTTP_BAD_REQUEST);
        }
    }
}
