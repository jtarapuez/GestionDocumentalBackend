package ec.gob.iess.gestiondocumental.application.common;

/**
 * Normaliza parámetros de paginación de query HTTP (page base 0, size acotado).
 */
public final class PaginationParams {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    private PaginationParams() {
    }

    public static int normalizePage(Integer page) {
        if (page == null || page < 0) {
            return DEFAULT_PAGE;
        }
        return page;
    }

    public static int normalizeSize(Integer size) {
        if (size == null || size < 1) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }

    public static boolean usePagination(Integer page, Integer size) {
        return page != null || size != null;
    }
}
