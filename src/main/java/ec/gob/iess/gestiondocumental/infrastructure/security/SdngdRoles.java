package ec.gob.iess.gestiondocumental.infrastructure.security;

/**
 * Roles institucionales SDNGD en el JWT de Keycloak ({@code user.rolesDisponibles}).
 * Incluye sinónimos legacy ({@code OPERADOR}, {@code SUPERVISOR}, {@code ADMINISTRADOR}) alineados al MFE.
 */
public final class SdngdRoles {

    public static final String ADMINISTRADOR_SDNGD = "ADMINISTRADOR_SDNGD";
    public static final String ADMINISTRADOR_LEGACY = "ADMINISTRADOR";
    public static final String OPERADOR_SDNGD = "OPERADOR_SDNGD";
    public static final String OPERADOR_LEGACY = "OPERADOR";
    public static final String SUPERVISOR_SDNGD = "SUPERVISOR_SDNGD";
    public static final String SUPERVISOR_LEGACY = "SUPERVISOR";

    /** Crear/actualizar series y subseries. */
    public static final String[] ADMINISTRADOR = {
            ADMINISTRADOR_SDNGD, ADMINISTRADOR_LEGACY
    };

    /** Registrar/actualizar inventarios y pendientes del operador. */
    public static final String[] OPERADOR = {
            OPERADOR_SDNGD, OPERADOR_LEGACY
    };

    /** Aprobar/rechazar y listados de supervisión. */
    public static final String[] SUPERVISOR = {
            SUPERVISOR_SDNGD, SUPERVISOR_LEGACY
    };

    /** POST /v1/consultas — los tres perfiles SDNGD. */
    public static final String[] CONSULTAS = {
            ADMINISTRADOR_SDNGD, ADMINISTRADOR_LEGACY,
            OPERADOR_SDNGD, OPERADOR_LEGACY,
            SUPERVISOR_SDNGD, SUPERVISOR_LEGACY
    };

    /** Exportación reportes — administrador y supervisor. */
    public static final String[] REPORTES = {
            ADMINISTRADOR_SDNGD, ADMINISTRADOR_LEGACY,
            SUPERVISOR_SDNGD, SUPERVISOR_LEGACY
    };

    private SdngdRoles() {
    }
}
