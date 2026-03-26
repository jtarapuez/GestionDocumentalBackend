package ec.gob.iess.gestiondocumental.interfaces.api.support;

/**
 * Extrae la cédula/identificador del operador desde el header HTTP enviado por el cliente.
 */
public final class HttpOperadorExtractor {

    /**
     * Valor temporal si el cliente aún no envía {@code X-Operador-Id} (compatibilidad).
     */
    public static final String DEFAULT_OPERADOR_FALLBACK = "1";

    private HttpOperadorExtractor() {
    }

    public static String fromHeaderOrFallback(String operadorIdHeader) {
        if (operadorIdHeader != null && !operadorIdHeader.trim().isEmpty()) {
            return operadorIdHeader.trim();
        }
        return DEFAULT_OPERADOR_FALLBACK;
    }
}
