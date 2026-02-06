package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Servicio de warm-up del pool de conexiones
 * Pre-inicializa las conexiones del pool al arrancar la aplicación
 * para reducir la latencia en las primeras peticiones
 */
@ApplicationScoped
public class ConnectionWarmupService {
    
    private static final Logger LOG = Logger.getLogger(ConnectionWarmupService.class.getName());
    
    @Inject
    AgroalDataSource dataSource;
    
    /**
     * Se ejecuta automáticamente al iniciar la aplicación.
     * Pre-inicializa una conexión del pool y ejecuta consultas de prueba para reducir
     * la latencia en las primeras peticiones y evitar ORA-17002 por segunda conexión en arranque.
     * @param ev evento de arranque de Quarkus (no utilizado)
     */
    void onStart(@Observes StartupEvent ev) {
        // Esperar un poco para que el datasource esté completamente listo
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        LOG.info("==========================================");
        LOG.info("WARM-UP: Pre-inicializando pool de conexiones");
        LOG.info("==========================================");
        
        try {
            warmupConnections();
            LOG.info("✅ WARM-UP completado: Pool de conexiones pre-inicializado");
        } catch (Exception e) {
            LOG.warning("⚠️ WARM-UP: Error al pre-inicializar conexiones (no crítico): " + e.getMessage());
            // No lanzar excepción para no bloquear el arranque de la aplicación
        }
    }
    
    /**
     * Pre-inicializa UNA conexión y ejecuta el test de BD en la misma conexión.
     * Solo hay una petición getConnection() al arranque para evitar que Agroal
     * cree una segunda conexión en background (agroal-11), que provocaba
     * ORA-17002 "Connection establishment interrupted externally".
     */
    private void warmupConnections() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL")) {
                    if (rs.next()) { /* consumir */ }
                }
                LOG.info("✅ Conexión 1/1 pre-inicializada");
                try (ResultSet rs = stmt.executeQuery("SELECT 'OK' AS STATUS, SYSDATE FROM DUAL")) {
                    if (rs.next()) {
                        LOG.info("✅ Test Oracle: " + rs.getString(1) + " | Fecha servidor: " + rs.getTimestamp(2));
                    }
                }
                try (ResultSet rs = stmt.executeQuery(
                        "SELECT COUNT(*) AS TOTAL FROM USER_TABLES WHERE TABLE_NAME LIKE 'GDOC_%'")) {
                    if (rs.next()) {
                        LOG.info("✅ Tablas GDOC_*: " + rs.getInt("TOTAL"));
                    }
                }
            }
        }
        LOG.info("==========================================");
    }
}




