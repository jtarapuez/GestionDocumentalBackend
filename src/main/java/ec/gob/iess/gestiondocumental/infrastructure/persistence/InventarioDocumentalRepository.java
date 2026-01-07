package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Panache para la entidad InventarioDocumental
 * Proporciona métodos de acceso a datos para GDOC_INVENTARIO_T
 */
@ApplicationScoped
public class InventarioDocumentalRepository implements PanacheRepository<InventarioDocumental> {

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
    public List<InventarioDocumental> buscarConFiltros(Long idSeccion, Long idSerie, Long idSubserie, 
                                                         String numeroExpediente, String estado,
                                                         String numeroCedula, String numeroRuc, String operador,
                                                         String nombresApellidos, String razonSocial, String descripcionSerie,
                                                         String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
                                                         LocalDate fechaDesde, LocalDate fechaHasta) {
        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();
        int paramIndex = 1;

        if (idSeccion != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("idSeccion = ?").append(paramIndex);
            params.add(idSeccion);
            paramIndex++;
        }
        if (idSerie != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("idSerie = ?").append(paramIndex);
            params.add(idSerie);
            paramIndex++;
        }
        if (idSubserie != null) {
            query.append(query.length() == 0 ? "" : " AND ").append("idSubserie = ?").append(paramIndex);
            params.add(idSubserie);
            paramIndex++;
        }
        if (numeroExpediente != null && !numeroExpediente.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("UPPER(numeroExpediente) LIKE UPPER(?").append(paramIndex).append(")");
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
            query.append(query.length() == 0 ? "" : " AND ").append("UPPER(nombresApellidos) LIKE UPPER(?").append(paramIndex).append(")");
            params.add("%" + nombresApellidos + "%");
            paramIndex++;
        }
        if (razonSocial != null && !razonSocial.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("UPPER(razonSocial) LIKE UPPER(?").append(paramIndex).append(")");
            params.add("%" + razonSocial + "%");
            paramIndex++;
        }
        if (descripcionSerie != null && !descripcionSerie.isEmpty()) {
            query.append(query.length() == 0 ? "" : " AND ").append("UPPER(descripcionSerie) LIKE UPPER(?").append(paramIndex).append(")");
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

        if (query.length() > 0) {
            String hqlQuery = query.toString();
            return find(hqlQuery, params.toArray()).list();
        }
        
        return listAll();
    }
}

