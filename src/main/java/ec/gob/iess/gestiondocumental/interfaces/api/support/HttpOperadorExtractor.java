package ec.gob.iess.gestiondocumental.interfaces.api.support;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.inventario.InventarioCodigosError;

/**
 * Extrae la cédula del operador/supervisor desde {@code X-Operador-Id} (Seguridades → MFE).
 */
public final class HttpOperadorExtractor {

    public static final String HEADER_OPERADOR_ID = "X-Operador-Id";

    /**
     * @deprecated Usar {@link #fromHeaderRequired(String)}; evita persistir {@code "1"} sin sesión.
     */
    @Deprecated
    public static final String DEFAULT_OPERADOR_FALLBACK = "1";

    private static final int HTTP_BAD_REQUEST = 400;

    private HttpOperadorExtractor() {
    }

    /**
     * Cédula obligatoria (10 dígitos o valor normalizado desde Seguridades).
     *
     * @throws NegocioApiException si el header falta o está vacío
     */
    public static String fromHeaderRequired(String operadorIdHeader) {
        if (operadorIdHeader == null || operadorIdHeader.trim().isEmpty()) {
            throw new NegocioApiException(
                    InventarioCodigosError.INV_OPERADOR_REQUERIDO,
                    "Header "
                            + HEADER_OPERADOR_ID
                            + " es obligatorio (cédula del usuario en Seguridades)",
                    HTTP_BAD_REQUEST);
        }
        return HttpUsuarioCreacionExtractor.normalizarParaColumna(operadorIdHeader.trim());
    }

    /**
     * @deprecated Solo compatibilidad legacy; no usar en inventarios/series nuevos.
     */
    @Deprecated
    public static String fromHeaderOrFallback(String operadorIdHeader) {
        if (operadorIdHeader != null && !operadorIdHeader.trim().isEmpty()) {
            return HttpUsuarioCreacionExtractor.normalizarParaColumna(operadorIdHeader.trim());
        }
        return DEFAULT_OPERADOR_FALLBACK;
    }
}
