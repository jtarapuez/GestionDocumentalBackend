package ec.gob.iess.gestiondocumental.application.common;

import java.util.List;

/**
 * Resultado paginado de un listado (dominio o DTO).
 */
public record PaginatedResult<T>(List<T> items, long totalItems, int page, int pageSize) {

    public int totalPages() {
        if (pageSize <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalItems / pageSize);
    }
}
