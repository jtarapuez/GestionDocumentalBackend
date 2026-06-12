package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.application.inventario.InventarioEstadoUtil;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.InventarioDocumentalEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para {@link InventarioDocumentalEntity}. Usado por el adaptador (PAS-GUI-047).
 * <p>
 * La búsqueda dinámica {@link #buscarConFiltros} está documentada en
 * {@code docs/CONSULTA_FILTROS_INVENTARIO.md} (Fase 5).
 */
@ApplicationScoped
public class InventarioDocumentalRepository implements PanacheRepository<InventarioDocumentalEntity> {

    public Optional<InventarioDocumentalEntity> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public List<InventarioDocumentalEntity> findByOperador(String operador) {
        return find("operador", operador).list();
    }

    public List<InventarioDocumentalEntity> findPendientesByOperador(String operador) {
        return find(
                "operador = ?1 AND estadoInventario IN (?2, ?3)",
                operador,
                InventarioEstadoUtil.PENDIENTE_APROBACION,
                InventarioEstadoUtil.PENDIENTE_APROBACION_SIN_TILDE)
                .list();
    }

    public List<InventarioDocumentalEntity> findPendientesAprobacion() {
        return find("estadoInventario IN (?1)", Arrays.asList("Registrado", "Actualizado")).list();
    }

    public List<InventarioDocumentalEntity> findByEstado(String estado) {
        return find("estadoInventario", estado).list();
    }

    public boolean tienePendientesVencidos(String operador) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(5);
        return count(
                "operador = ?1 AND estadoInventario IN (?2, ?3) AND fechaCambioEstado < ?4",
                operador,
                InventarioEstadoUtil.PENDIENTE_APROBACION,
                InventarioEstadoUtil.PENDIENTE_APROBACION_SIN_TILDE,
                fechaLimite) > 0;
    }

    /**
     * idsSubseriesCuandoFiltroPorSerie: cuando idSerie != null, el adaptador pasa aquí los IDs de subseries de esa serie (o lista vacía).
     */
    public List<InventarioDocumentalEntity> buscarConFiltros(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            LocalDate fechaDesde, LocalDate fechaHasta,
            String supervisor,
            List<Long> idsSubseriesCuandoFiltroPorSerie) {
        return queryConFiltros(filtros(
                idSeccion, idSerie, idSubserie, numeroExpediente, estado,
                numeroCedula, numeroRuc, operador, nombresApellidos, razonSocial, descripcionSerie,
                tipoContenedor, numeroContenedor, tipoArchivo, fechaDesde, fechaHasta,
                supervisor, idsSubseriesCuandoFiltroPorSerie)).list();
    }

    /**
     * Misma consulta que {@link #buscarConFiltros} con paginación Panache (page base 0).
     */
    public PaginatedEntities<InventarioDocumentalEntity> buscarConFiltrosPaginado(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            LocalDate fechaDesde, LocalDate fechaHasta,
            String supervisor,
            List<Long> idsSubseriesCuandoFiltroPorSerie,
            int page,
            int size) {
        PanacheQuery<InventarioDocumentalEntity> q = queryConFiltros(filtros(
                idSeccion, idSerie, idSubserie, numeroExpediente, estado,
                numeroCedula, numeroRuc, operador, nombresApellidos, razonSocial, descripcionSerie,
                tipoContenedor, numeroContenedor, tipoArchivo, fechaDesde, fechaHasta,
                supervisor, idsSubseriesCuandoFiltroPorSerie));
        long total = q.count();
        List<InventarioDocumentalEntity> items = q.page(Page.of(page, size)).list();
        return new PaginatedEntities<>(items, total);
    }

    public record PaginatedEntities<T>(List<T> items, long totalItems) {
    }

    private static InventarioFiltrosQuery filtros(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            LocalDate fechaDesde, LocalDate fechaHasta,
            String supervisor,
            List<Long> idsSubseriesCuandoFiltroPorSerie) {
        return new InventarioFiltrosQuery(
                idSeccion, idSerie, idSubserie, numeroExpediente, estado,
                numeroCedula, numeroRuc, operador, nombresApellidos, razonSocial, descripcionSerie,
                tipoContenedor, numeroContenedor, tipoArchivo, fechaDesde, fechaHasta,
                supervisor, idsSubseriesCuandoFiltroPorSerie);
    }

    private PanacheQuery<InventarioDocumentalEntity> queryConFiltros(InventarioFiltrosQuery filtros) {
        InventarioFiltrosQuery.BuiltQuery built = filtros.build();
        if (built.hasWhere()) {
            return find(built.jpql, built.params);
        }
        return findAll();
    }

    static void appendAndSiNecesario(StringBuilder q) {
        if (q.length() > 0) {
            q.append(" AND ");
        }
    }

    static void appendIgualLong(StringBuilder q, List<Object> params, int[] idx, String campo, Long valor) {
        if (valor == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append(campo).append(" = ?").append(idx[0]);
        params.add(valor);
        idx[0]++;
    }

    static void appendIgualInteger(StringBuilder q, List<Object> params, int[] idx, String campo, Integer valor) {
        if (valor == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append(campo).append(" = ?").append(idx[0]);
        params.add(valor);
        idx[0]++;
    }

    static void appendIgualString(StringBuilder q, List<Object> params, int[] idx, String campo, String valor) {
        if (valor == null || valor.isEmpty()) {
            return;
        }
        appendAndSiNecesario(q);
        q.append(campo).append(" = ?").append(idx[0]);
        params.add(valor);
        idx[0]++;
    }

    /** Filtro estado con variantes históricas (p. ej. Pendiente con/sin tilde en BD). */
    static void appendEstadoInventario(StringBuilder q, List<Object> params, int[] idx, String estadoFiltro) {
        List<String> variantes = InventarioEstadoUtil.variantesParaFiltroApi(estadoFiltro);
        if (variantes.isEmpty()) {
            return;
        }
        appendAndSiNecesario(q);
        if (variantes.size() == 1) {
            q.append("estadoInventario = ?").append(idx[0]);
            params.add(variantes.get(0));
            idx[0]++;
            return;
        }
        q.append("estadoInventario IN (");
        for (int i = 0; i < variantes.size(); i++) {
            if (i > 0) {
                q.append(", ");
            }
            q.append("?").append(idx[0]);
            params.add(variantes.get(i));
            idx[0]++;
        }
        q.append(")");
    }

    static void appendLikeUpper(StringBuilder q, List<Object> params, int[] idx, String campo, String valor) {
        if (valor == null || valor.isEmpty()) {
            return;
        }
        appendAndSiNecesario(q);
        q.append("UPPER(").append(campo).append(") LIKE UPPER(?").append(idx[0]).append(")");
        params.add("%" + valor + "%");
        idx[0]++;
    }

    static void appendFechaDesde(StringBuilder q, List<Object> params, int[] idx, LocalDate fechaDesde) {
        if (fechaDesde == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append("fechaDesde >= ?").append(idx[0]);
        params.add(fechaDesde);
        idx[0]++;
    }

    static void appendFechaHasta(StringBuilder q, List<Object> params, int[] idx, LocalDate fechaHasta) {
        if (fechaHasta == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append("fechaHasta <= ?").append(idx[0]);
        params.add(fechaHasta);
        idx[0]++;
    }

    static void appendSupervisor(StringBuilder q, List<Object> params, int[] idx, String supervisor) {
        if (supervisor == null || supervisor.isEmpty()) {
            return;
        }
        appendAndSiNecesario(q);
        q.append("supervisor = ?").append(idx[0]);
        params.add(supervisor.trim());
        idx[0]++;
    }

    /**
     * Replica la lógica original: con lista de subseries no vacía usa (idSerie = ? OR idSubserie IN (...)).
     */
    static void appendFiltroSerieYSubseries(
            StringBuilder query,
            List<Object> params,
            int[] paramIndex,
            Long idSerie,
            List<Long> idsSubseriesCuandoFiltroPorSerie) {
        if (idSerie == null) {
            return;
        }
        boolean usarOrSubseries = idsSubseriesCuandoFiltroPorSerie != null
                && !idsSubseriesCuandoFiltroPorSerie.isEmpty();
        if (!usarOrSubseries) {
            appendAndSiNecesario(query);
            query.append("idSerie = ?").append(paramIndex[0]);
            params.add(idSerie);
            paramIndex[0]++;
        } else {
            appendAndSiNecesario(query);
            query.append("(idSerie = ?").append(paramIndex[0]);
            params.add(idSerie);
            paramIndex[0]++;
            query.append(" OR idSubserie IN (");
            for (int i = 0; i < idsSubseriesCuandoFiltroPorSerie.size(); i++) {
                if (i > 0) {
                    query.append(", ");
                }
                query.append("?").append(paramIndex[0]);
                params.add(idsSubseriesCuandoFiltroPorSerie.get(i));
                paramIndex[0]++;
            }
            query.append("))");
        }
    }
}
