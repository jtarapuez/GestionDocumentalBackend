package ec.gob.iess.gestiondocumental.domain;

/**
 * Estados de serie/subserie documental en BD y API: solo {@link #CREADO} y {@link #ACTUALIZADO}
 * (columna {@code ESTADO} VARCHAR2(15), alineado a scripts y datos históricos).
 * <p>
 * Si el cliente envía la descripción larga del catálogo ESTADO_SERIE, {@link #normalizarParaColumnaEstado}
 * la convierte a estos dos valores.
 */
public final class SerieDocumentalEstados {

    /** Estado al crear o cuando aplica “creada”. */
    public static final String CREADO = "Creado";

    /** Estado al actualizar o cuando aplica “actualizada”. */
    public static final String ACTUALIZADO = "Actualizado";

    private static final String DESC_CATALOGO_CREADA = "Serie documental creada";
    private static final String DESC_CATALOGO_ACTUALIZADA = "Serie documental actualizada";

    private SerieDocumentalEstados() {
    }

    /**
     * Convierte entrada del API (descripción de catálogo, código o ya "Creado"/"Actualizado") al valor
     * que se guarda. Devuelve {@code null} si viene vacío (actualización parcial: no cambiar estado).
     */
    public static String normalizarParaColumnaEstado(String estadoEntrada) {
        if (estadoEntrada == null || estadoEntrada.isBlank()) {
            return null;
        }
        String s = estadoEntrada.trim();
        if (s.equalsIgnoreCase(DESC_CATALOGO_CREADA)
                || s.equalsIgnoreCase(CREADO)
                || s.equalsIgnoreCase("CREADO")) {
            return CREADO;
        }
        if (s.equalsIgnoreCase(DESC_CATALOGO_ACTUALIZADA)
                || s.equalsIgnoreCase(ACTUALIZADO)
                || s.equalsIgnoreCase("ACTUALIZADO")) {
            return ACTUALIZADO;
        }
        if (s.length() <= 15) {
            return s;
        }
        return s.substring(0, 15);
    }
}
