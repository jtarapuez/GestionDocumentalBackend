package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Parámetros de filtro para consultas dinámicas de inventario (reutilizado en list y page).
 */
public record InventarioFiltrosQuery(
        Long idSeccion,
        Long idSerie,
        Long idSubserie,
        String numeroExpediente,
        String estado,
        String numeroCedula,
        String numeroRuc,
        String operador,
        String nombresApellidos,
        String razonSocial,
        String descripcionSerie,
        String tipoContenedor,
        Integer numeroContenedor,
        String tipoArchivo,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        String supervisor,
        List<Long> idsSubseriesCuandoFiltroPorSerie) {

    public static final class BuiltQuery {
        public final String jpql;
        public final Object[] params;

        BuiltQuery(String jpql, Object[] params) {
            this.jpql = jpql;
            this.params = params;
        }

        public boolean hasWhere() {
            return jpql != null && !jpql.isEmpty();
        }
    }

    public BuiltQuery build() {
        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();
        int[] paramIndex = {1};

        InventarioDocumentalRepository.appendIgualLong(query, params, paramIndex, "idSeccion", idSeccion);
        InventarioDocumentalRepository.appendFiltroSerieYSubseries(
                query, params, paramIndex, idSerie, idsSubseriesCuandoFiltroPorSerie);
        InventarioDocumentalRepository.appendIgualLong(query, params, paramIndex, "idSubserie", idSubserie);
        InventarioDocumentalRepository.appendLikeUpper(query, params, paramIndex, "numeroExpediente", numeroExpediente);
        InventarioDocumentalRepository.appendEstadoInventario(query, params, paramIndex, estado);
        InventarioDocumentalRepository.appendIgualString(query, params, paramIndex, "numeroCedula", numeroCedula);
        InventarioDocumentalRepository.appendIgualString(query, params, paramIndex, "numeroRuc", numeroRuc);
        InventarioDocumentalRepository.appendIgualString(query, params, paramIndex, "operador", operador);
        InventarioDocumentalRepository.appendLikeUpper(query, params, paramIndex, "nombresApellidos", nombresApellidos);
        InventarioDocumentalRepository.appendLikeUpper(query, params, paramIndex, "razonSocial", razonSocial);
        InventarioDocumentalRepository.appendLikeUpper(query, params, paramIndex, "descripcionSerie", descripcionSerie);
        InventarioDocumentalRepository.appendIgualString(query, params, paramIndex, "tipoContenedor", tipoContenedor);
        InventarioDocumentalRepository.appendIgualInteger(query, params, paramIndex, "numeroContenedor", numeroContenedor);
        InventarioDocumentalRepository.appendIgualString(query, params, paramIndex, "tipoArchivo", tipoArchivo);
        InventarioDocumentalRepository.appendFechaDesde(query, params, paramIndex, fechaDesde);
        InventarioDocumentalRepository.appendFechaHasta(query, params, paramIndex, fechaHasta);
        InventarioDocumentalRepository.appendSupervisor(query, params, paramIndex, supervisor);

        return new BuiltQuery(query.toString(), params.toArray());
    }
}
