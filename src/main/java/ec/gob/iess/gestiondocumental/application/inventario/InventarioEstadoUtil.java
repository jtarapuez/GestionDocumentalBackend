package ec.gob.iess.gestiondocumental.application.inventario;

import java.text.Normalizer;
import java.util.List;

/**
 * Estados de inventario: valores canónicos y variantes históricas en BD (con/sin tilde).
 */
public final class InventarioEstadoUtil {

    public static final String REGISTRADO = "Registrado";
    public static final String ACTUALIZADO = "Actualizado";
    public static final String PENDIENTE_APROBACION = "Pendiente de Aprobación";
    public static final String PENDIENTE_APROBACION_SIN_TILDE = "Pendiente de Aprobacion";
    public static final String APROBADO = "Aprobado";
    public static final String APROBADO_CON_MODIFICACIONES = "Aprobado con Modificaciones";

    private InventarioEstadoUtil() {
    }

    public static String normalizar(String estado) {
        if (estado == null) {
            return "";
        }
        return Normalizer.normalize(estado.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    public static boolean esPendienteAprobacion(String estado) {
        String n = normalizar(estado);
        return n.equals(normalizar(PENDIENTE_APROBACION))
                || n.equals(normalizar(PENDIENTE_APROBACION_SIN_TILDE));
    }

    public static boolean esRegistrado(String estado) {
        return normalizar(REGISTRADO).equals(normalizar(estado));
    }

    public static boolean esActualizado(String estado) {
        return normalizar(ACTUALIZADO).equals(normalizar(estado));
    }

    public static boolean esAprobado(String estado) {
        return normalizar(APROBADO).equals(normalizar(estado));
    }

    public static boolean esAprobadoConModificaciones(String estado) {
        return normalizar(APROBADO_CON_MODIFICACIONES).equals(normalizar(estado));
    }

    /**
     * Valores posibles en columna ESTADO_INVENTARIO para filtros GET (lista / paginado).
     */
    public static List<String> variantesParaFiltroApi(String estadoFiltro) {
        if (estadoFiltro == null || estadoFiltro.isBlank()) {
            return List.of();
        }
        if (esPendienteAprobacion(estadoFiltro)) {
            return List.of(PENDIENTE_APROBACION, PENDIENTE_APROBACION_SIN_TILDE);
        }
        if (esRegistrado(estadoFiltro)) {
            return List.of(REGISTRADO);
        }
        if (esActualizado(estadoFiltro)) {
            return List.of(ACTUALIZADO);
        }
        if (esAprobadoConModificaciones(estadoFiltro)) {
            return List.of(APROBADO_CON_MODIFICACIONES);
        }
        if (esAprobado(estadoFiltro)) {
            return List.of(APROBADO);
        }
        return List.of(estadoFiltro.trim());
    }
}
