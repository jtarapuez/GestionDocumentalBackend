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
        int paramIndex = 1;

        if (idSeccion != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("idSeccion = ?").append(paramIndex);
            params.add(idSeccion);
            paramIndex++;
        }
        if (idSerie != null) {
            boolean usarOrSubseries = idsSubseriesCuandoFiltroPorSerie != null && !idsSubseriesCuandoFiltroPorSerie.isEmpty();
            if (!usarOrSubseries) {
                query.append(query.length() == 0 ? "" : " AND ").append("idSerie = ?").append(paramIndex);
                params.add(idSerie);
                paramIndex++;
            } else {
                query.append(query.length() == 0 ? "" : " AND ").append("(idSerie = ?").append(paramIndex);
                params.add(idSerie);
                paramIndex++;
                query.append(" OR idSubserie IN (");
                for (int i = 0; i < idsSubseriesCuandoFiltroPorSerie.size(); i++) {
                    if (i > 0) query.append(", ");
                    query.append("?").append(paramIndex);
                    params.add(idsSubseriesCuandoFiltroPorSerie.get(i));
                    paramIndex++;
                }
                query.append("))");
            }
        }
        if (idSubserie != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("idSubserie = ?").append(paramIndex);
            params.add(idSubserie);
            paramIndex++;
        }
        if (numeroExpediente != null && !numeroExpediente.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ")
                    .append("UPPER(numeroExpediente) LIKE UPPER(?").append(paramIndex).append(")");
            params.add("%" + numeroExpediente + "%");
            paramIndex++;
        }
        if (estado != null && !estado.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("estadoInventario = ?").append(paramIndex);
            params.add(estado);
            paramIndex++;
        }
        if (numeroCedula != null && !numeroCedula.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("numeroCedula = ?").append(paramIndex);
            params.add(numeroCedula);
            paramIndex++;
        }
        if (numeroRuc != null && !numeroRuc.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("numeroRuc = ?").append(paramIndex);
            params.add(numeroRuc);
            paramIndex++;
        }
        if (operador != null && !operador.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("operador = ?").append(paramIndex);
            params.add(operador);
            paramIndex++;
        }
        if (nombresApellidos != null && !nombresApellidos.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ")
                    .append("UPPER(nombresApellidos) LIKE UPPER(?").append(paramIndex).append(")");
            params.add("%" + nombresApellidos + "%");
            paramIndex++;
        }
        if (razonSocial != null && !razonSocial.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ")
                    .append("UPPER(razonSocial) LIKE UPPER(?").append(paramIndex).append(")");
            params.add("%" + razonSocial + "%");
            paramIndex++;
        }
        if (descripcionSerie != null && !descripcionSerie.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ")
                    .append("UPPER(descripcionSerie) LIKE UPPER(?").append(paramIndex).append(")");
            params.add("%" + descripcionSerie + "%");
            paramIndex++;
        }
        if (tipoContenedor != null && !tipoContenedor.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("tipoContenedor = ?").append(paramIndex);
            params.add(tipoContenedor);
            paramIndex++;
        }
        if (numeroContenedor != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("numeroContenedor = ?").append(paramIndex);
            params.add(numeroContenedor);
            paramIndex++;
        }
        if (tipoArchivo != null && !tipoArchivo.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("tipoArchivo = ?").append(paramIndex);
            params.add(tipoArchivo);
            paramIndex++;
        }
        if (fechaDesde != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("fechaDesde >= ?").append(paramIndex);
            params.add(fechaDesde);
            paramIndex++;
        }
        if (fechaHasta != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("fechaHasta <= ?").append(paramIndex);
            params.add(fechaHasta);
            paramIndex++;
        }
        if (supervisor != null && !supervisor.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("supervisor = ?").append(paramIndex);
            params.add(supervisor.trim());
            paramIndex++;
        }

        if (query.length() > 0) {
            return find(query.toString(), params.toArray()).list();
        }
        return listAll();
    }
}
