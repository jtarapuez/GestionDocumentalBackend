package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio Panache para la entidad {@link ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental}.
 * Persiste los inventarios documentales en GDOC_INVENTARIO_T.
 * Criterios: por ID, operador, estado, pendientes de aprobación, pendientes por operador,
 * pendientes vencidos, y consultas con filtros múltiples (sección, serie, subserie, expediente, fechas, etc.).
 */
@ApplicationScoped
public class InventarioDocumentalRepository implements PanacheRepository<InventarioDocumental> {

    @Inject
    SubserieDocumentalRepository subserieRepository;

    /**
     * Busca un inventario por su ID
     * @param id ID del inventario
     * @return Optional con el inventario encontrado
     */
    public Optional<InventarioDocumental> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    /**
     * Busca inventarios por operador
     * @param operador Cédula del operador
     * @return Lista de inventarios del operador
     */
    public List<InventarioDocumental> findByOperador(String operador) {
        return find("operador", operador).list();
    }

    /**
     * Busca inventarios pendientes de aprobación de un operador
     * @param operador Cédula del operador
     * @return Lista de inventarios pendientes
     */
    public List<InventarioDocumental> findPendientesByOperador(String operador) {
        return find("operador = ?1 AND estadoInventario = ?2", operador, "Pendiente de Aprobación").list();
    }

    /**
     * Busca inventarios pendientes de aprobación (para supervisores)
     * @return Lista de inventarios pendientes
     */
    public List<InventarioDocumental> findPendientesAprobacion() {
        return find("estadoInventario IN (?1)", Arrays.asList("Registrado", "Actualizado")).list();
    }

    /**
     * Busca inventarios por estado
     * @param estado Estado del inventario
     * @return Lista de inventarios
     */
    public List<InventarioDocumental> findByEstado(String estado) {
        return find("estadoInventario", estado).list();
    }

    /**
     * Verifica si el operador tiene inventarios pendientes vencidos (más de 5 días)
     * @param operador Cédula del operador
     * @return true si tiene pendientes vencidos
     */
    public boolean tienePendientesVencidos(String operador) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(5);
        return count("operador = ?1 AND estadoInventario = ?2 AND fechaCambioEstado < ?3", 
                     operador, "Pendiente de Aprobación", fechaLimite) > 0;
    }

    /**
     * Busca inventarios con múltiples filtros (para consultas avanzadas)
     * @param idSeccion Filtro por sección
     * @param idSerie Filtro por serie
     * @param idSubserie Filtro por subserie
     * @param numeroExpediente Filtro por número de expediente
     * @param estado Filtro por estado
     * @param numeroCedula Filtro por cédula
     * @param numeroRuc Filtro por RUC
     * @param operador Filtro por operador
     * @param nombresApellidos Filtro por nombres y apellidos (búsqueda parcial)
     * @param razonSocial Filtro por razón social (búsqueda parcial)
     * @param descripcionSerie Filtro por descripción de serie (búsqueda parcial)
     * @param tipoContenedor Filtro por tipo de contenedor
     * @param numeroContenedor Filtro por número de contenedor
     * @param tipoArchivo Filtro por tipo de archivo
     * @param fechaDesde Filtro por fecha desde
     * @param fechaHasta Filtro por fecha hasta
     * @return Lista de inventarios que cumplen los filtros
     */
    public List<InventarioDocumental> buscarConFiltros(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            LocalDate fechaDesde, LocalDate fechaHasta,
            String supervisor) {
        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();
        int paramIndex = 1;

        if (idSeccion != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("idSeccion = ?").append(paramIndex);
            params.add(idSeccion);
            paramIndex++;
        }
        if (idSerie != null) {
            // ✅ Buscar inventarios con la serie directamente O con subseries de esa serie
            // Obtener todas las subseries de la serie
            List<SubserieDocumental> subseries = subserieRepository.findBySerie(idSerie);
            List<Long> idsSubseries = subseries.stream()
                    .map(SubserieDocumental::getId)
                    .collect(Collectors.toList());
            
            if (idsSubseries.isEmpty()) {
                // Si no hay subseries, solo buscar por idSerie
                query.append(query.length() == 0 ? "" : " AND ").append("idSerie = ?").append(paramIndex);
                params.add(idSerie);
                paramIndex++;
            } else {
                // Buscar por idSerie O por idSubserie en la lista de subseries
                query.append(query.length() == 0 ? "" : " AND ").append("(idSerie = ?").append(paramIndex);
                params.add(idSerie);
                paramIndex++;
                query.append(" OR idSubserie IN (");
                for (int i = 0; i < idsSubseries.size(); i++) {
                    if (i > 0) {
                        query.append(", ");
                    }
                    query.append("?").append(paramIndex);
                    params.add(idsSubseries.get(i));
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
            // ✅ DEBUG: Log para verificar filtro de estado
            System.out.println("🔍 [DEBUG] buscarConFiltros - Aplicando filtro estado: '" + estado + "'");
            query.append(query.length() == 0 ? "" : " AND ").append("estadoInventario = ?").append(paramIndex);
            params.add(estado);
            System.out.println("🔍 [DEBUG] buscarConFiltros - Query construida hasta ahora: " + query.toString());
            System.out.println("🔍 [DEBUG] buscarConFiltros - Parámetros hasta ahora: " + params);
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
            // ✅ El frontend ahora envía directamente el ID ("1" o "2")
            // Buscar directamente por ese ID en la base de datos
            String supervisorValue = supervisor.trim();
            query.append(query.length() == 0 ? "" : " AND ").append("supervisor = ?").append(paramIndex);
            params.add(supervisorValue);
            paramIndex++;
            System.out.println("🔍 [DEBUG] buscarConFiltros - Filtro por supervisor: " + supervisorValue);
        } else {
            System.out.println("🔍 [DEBUG] buscarConFiltros - NO se aplicará filtro por supervisor "
                    + "(supervisor es null o vacío)");
        }

        if (query.length() > 0) {
            String hqlQuery = query.toString();
            return find(hqlQuery, params.toArray()).list();
        }
        
        return listAll();
    }
}




