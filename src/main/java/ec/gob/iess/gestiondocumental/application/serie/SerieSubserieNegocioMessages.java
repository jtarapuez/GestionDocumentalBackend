package ec.gob.iess.gestiondocumental.application.serie;

/**
 * Mensajes de negocio estables para series y subseries (actualización por creador).
 */
public final class SerieSubserieNegocioMessages {

    public static final String SOLO_CREADOR_PUEDE_ACTUALIZAR_SERIE =
            "Solo el usuario que creó la serie documental puede actualizarla";

    public static final String SOLO_CREADOR_PUEDE_ACTUALIZAR_SUBSERIE =
            "Solo el usuario que creó la subserie documental puede actualizarla";

    private SerieSubserieNegocioMessages() {
    }
}
