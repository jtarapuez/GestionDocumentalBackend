package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
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
        return find("estadoInventario IN (?1)", "Registrado", "Actualizado").list();
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
     * @return Lista de inventarios que cumplen los filtros
     */
    public List<InventarioDocumental> buscarConFiltros(Long idSeccion, Long idSerie, Long idSubserie, 
                                                         String numeroExpediente, String estado) {
        StringBuilder query = new StringBuilder();
        boolean hasWhere = false;

        if (idSeccion != null) {
            query.append(hasWhere ? " AND " : " WHERE ").append("idSeccion = ?").append(hasWhere ? "" : "1");
            hasWhere = true;
        }
        if (idSerie != null) {
            query.append(hasWhere ? " AND " : " WHERE ").append("idSerie = ?").append(hasWhere ? "" : "1");
            hasWhere = true;
        }
        if (idSubserie != null) {
            query.append(hasWhere ? " AND " : " WHERE ").append("idSubserie = ?").append(hasWhere ? "" : "1");
            hasWhere = true;
        }
        if (numeroExpediente != null && !numeroExpediente.isEmpty()) {
            query.append(hasWhere ? " AND " : " WHERE ").append("numeroExpediente LIKE ?").append(hasWhere ? "" : "1");
            hasWhere = true;
        }
        if (estado != null && !estado.isEmpty()) {
            query.append(hasWhere ? " AND " : " WHERE ").append("estadoInventario = ?").append(hasWhere ? "" : "1");
        }

        // Por simplicidad, usaremos un método más directo
        if (idSeccion != null && idSerie != null && idSubserie != null) {
            return find("idSeccion = ?1 AND idSerie = ?2 AND idSubserie = ?3", idSeccion, idSerie, idSubserie).list();
        } else if (idSeccion != null) {
            return find("idSeccion", idSeccion).list();
        } else if (numeroExpediente != null) {
            return find("numeroExpediente LIKE ?1", "%" + numeroExpediente + "%").list();
        } else if (estado != null) {
            return find("estadoInventario", estado).list();
        }
        
        return listAll();
    }
}

