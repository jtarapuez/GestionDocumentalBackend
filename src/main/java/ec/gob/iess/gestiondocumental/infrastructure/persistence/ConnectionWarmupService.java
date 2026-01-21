package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.sql.Connection;
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
     * Se ejecuta automáticamente al iniciar la aplicación
     * Pre-inicializa las conexiones del pool según initial-size configurado
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
     * Pre-inicializa las conexiones del pool ejecutando queries simples
     * Esto fuerza a Quarkus a crear las conexiones iniciales configuradas en initial-size
     */
    private void warmupConnections() throws Exception {
        // Obtener y liberar múltiples conexiones para pre-inicializar el pool
        // El número de iteraciones debe ser igual o mayor a initial-size (3)
        int warmupConnections = 3; // Coincide con initial-size configurado
        
        for (int i = 1; i <= warmupConnections; i++) {
            try (Connection conn = dataSource.getConnection()) {
                // Ejecutar query simple para validar la conexión
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeQuery("SELECT 1 FROM DUAL");
                }
                LOG.info("✅ Conexión " + i + "/" + warmupConnections + " pre-inicializada");
            } catch (Exception e) {
                LOG.warning("⚠️ Error al pre-inicializar conexión " + i + ": " + e.getMessage());
                // Continuar con las demás conexiones
            }
        }
        
        LOG.info("==========================================");
    }
}

