package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.InventarioDocumentalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        return find("operador = ?1 AND estadoInventario = ?2", operador, "Pendiente de Aprobación").list();
    }

    public List<InventarioDocumentalEntity> findPendientesAprobacion() {
        return find("estadoInventario IN (?1)", Arrays.asList("Registrado", "Actualizado")).list();
    }

    public List<InventarioDocumentalEntity> findByEstado(String estado) {
        return find("estadoInventario", estado).list();
    }

    public boolean tienePendientesVencidos(String operador) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(5);
        return count("operador = ?1 AND estadoInventario = ?2 AND fechaCambioEstado < ?3",
                operador, "Pendiente de Aprobación", fechaLimite) > 0;
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
        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();
        int[] paramIndex = {1};

        appendIgualLong(query, params, paramIndex, "idSeccion", idSeccion);
        appendFiltroSerieYSubseries(query, params, paramIndex, idSerie, idsSubseriesCuandoFiltroPorSerie);
        appendIgualLong(query, params, paramIndex, "idSubserie", idSubserie);
        appendLikeUpper(query, params, paramIndex, "numeroExpediente", numeroExpediente);
        appendIgualString(query, params, paramIndex, "estadoInventario", estado);
        appendIgualString(query, params, paramIndex, "numeroCedula", numeroCedula);
        appendIgualString(query, params, paramIndex, "numeroRuc", numeroRuc);
        appendIgualString(query, params, paramIndex, "operador", operador);
        appendLikeUpper(query, params, paramIndex, "nombresApellidos", nombresApellidos);
        appendLikeUpper(query, params, paramIndex, "razonSocial", razonSocial);
        appendLikeUpper(query, params, paramIndex, "descripcionSerie", descripcionSerie);
        appendIgualString(query, params, paramIndex, "tipoContenedor", tipoContenedor);
        appendIgualInteger(query, params, paramIndex, "numeroContenedor", numeroContenedor);
        appendIgualString(query, params, paramIndex, "tipoArchivo", tipoArchivo);
        appendFechaDesde(query, params, paramIndex, fechaDesde);
        appendFechaHasta(query, params, paramIndex, fechaHasta);
        appendSupervisor(query, params, paramIndex, supervisor);

        if (query.length() > 0) {
            return find(query.toString(), params.toArray()).list();
        }
        return listAll();
    }

    private static void appendAndSiNecesario(StringBuilder q) {
        if (q.length() > 0) {
            q.append(" AND ");
        }
    }

    private static void appendIgualLong(StringBuilder q, List<Object> params, int[] idx, String campo, Long valor) {
        if (valor == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append(campo).append(" = ?").append(idx[0]);
        params.add(valor);
        idx[0]++;
    }

    private static void appendIgualInteger(StringBuilder q, List<Object> params, int[] idx, String campo, Integer valor) {
        if (valor == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append(campo).append(" = ?").append(idx[0]);
        params.add(valor);
        idx[0]++;
    }

    private static void appendIgualString(StringBuilder q, List<Object> params, int[] idx, String campo, String valor) {
        if (valor == null || valor.isEmpty()) {
            return;
        }
        appendAndSiNecesario(q);
        q.append(campo).append(" = ?").append(idx[0]);
        params.add(valor);
        idx[0]++;
    }

    private static void appendLikeUpper(StringBuilder q, List<Object> params, int[] idx, String campo, String valor) {
        if (valor == null || valor.isEmpty()) {
            return;
        }
        appendAndSiNecesario(q);
        q.append("UPPER(").append(campo).append(") LIKE UPPER(?").append(idx[0]).append(")");
        params.add("%" + valor + "%");
        idx[0]++;
    }

    private static void appendFechaDesde(StringBuilder q, List<Object> params, int[] idx, LocalDate fechaDesde) {
        if (fechaDesde == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append("fechaDesde >= ?").append(idx[0]);
        params.add(fechaDesde);
        idx[0]++;
    }

    private static void appendFechaHasta(StringBuilder q, List<Object> params, int[] idx, LocalDate fechaHasta) {
        if (fechaHasta == null) {
            return;
        }
        appendAndSiNecesario(q);
        q.append("fechaHasta <= ?").append(idx[0]);
        params.add(fechaHasta);
        idx[0]++;
    }

    private static void appendSupervisor(StringBuilder q, List<Object> params, int[] idx, String supervisor) {
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
    private static void appendFiltroSerieYSubseries(
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
