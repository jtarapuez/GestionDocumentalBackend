package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;

/**
 * Unifica la detección de archivo pasivo y la construcción de {@code posicionPasivo} (formato RAC).
 */
public final class ArchivoPasivoPosicion {

    public static final String TIPO_PASIVO_FRONTEND = "PASIVO";
    public static final String TIPO_PASIVO_LEGACY = "Archivo pasivo";

    private ArchivoPasivoPosicion() {
    }

    public static boolean esArchivoPasivo(String tipoArchivo) {
        return TIPO_PASIVO_FRONTEND.equals(tipoArchivo) || TIPO_PASIVO_LEGACY.equals(tipoArchivo);
    }

    /**
     * Aplica ubicación pasiva solo cuando el tipo es pasivo y viene {@code numeroRac} (mismo criterio que el flujo original).
     */
    public static boolean debeAplicarUbicacionPasiva(InventarioDocumentalRequest request) {
        return request != null
                && esArchivoPasivo(request.getTipoArchivo())
                && request.getNumeroRac() != null;
    }

    /**
     * Posición a persistir: literal del request si viene; si no, formato {@code %02d.%02d.%02d.%02d.%02d}.
     */
    public static String resolverPosicionPasivo(InventarioDocumentalRequest request) {
        if (request.getPosicionPasivo() != null && !request.getPosicionPasivo().trim().isEmpty()) {
            return request.getPosicionPasivo().trim();
        }
        return String.format(
                "%02d.%02d.%02d.%02d.%02d",
                request.getNumeroRac(),
                nz(request.getNumeroFila()),
                nz(request.getNumeroColumna()),
                nz(request.getNumeroPosicion()),
                nz(request.getBodega()));
    }

    public static void copiarCamposPasivoAlDominio(InventarioDocumental destino, InventarioDocumentalRequest request) {
        if (!debeAplicarUbicacionPasiva(request)) {
            return;
        }
        destino.setPosicionPasivo(resolverPosicionPasivo(request));
        destino.setNumeroRac(request.getNumeroRac());
        destino.setNumeroFila(request.getNumeroFila());
        destino.setNumeroColumna(request.getNumeroColumna());
        destino.setNumeroPosicion(request.getNumeroPosicion());
        destino.setBodega(request.getBodega());
    }

    private static int nz(Integer value) {
        return value != null ? value : 0;
    }
}
