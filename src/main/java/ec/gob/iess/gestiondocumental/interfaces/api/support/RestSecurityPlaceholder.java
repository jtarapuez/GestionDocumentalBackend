package ec.gob.iess.gestiondocumental.interfaces.api.support;

/**
 * Valores temporales hasta integrar contexto de seguridad (p. ej. Keycloak).
 */
public final class RestSecurityPlaceholder {

    /**
     * Cédula usada en aprobar/rechazar hasta leer el usuario autenticado del contenedor de seguridad.
     */
    public static final String SUPERVISOR_CEDULA_TEMPORAL = "0987654321";

    /**
     * Cédula usada en alta/edición de series y subseries hasta integrar seguridad.
     */
    public static final String ADMIN_CEDULA_TEMPORAL = "1234567890";

    private RestSecurityPlaceholder() {
    }
}
