package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Servicio de warm-up del pool de conexiones.
 * En dev la conexión Oracle puede tardar ~40–60 s; el warm-up corre en background
 * para no bloquear el arranque de Quarkus ni disparar "Thread blocked" en hot reload.
 */
@ApplicationScoped
public class ConnectionWarmupService {
    
    private static final Logger LOG = Logger.getLogger(ConnectionWarmupService.class.getName());
    
    @Inject
    AgroalDataSource dataSource;

    /**
     * Asíncrono: Quarkus escucha en 8080 de inmediato; el pool se calienta en paralelo.
     */
    void onStart(@ObservesAsync StartupEvent ev) {
        LOG.info("WARM-UP: iniciando pre-carga del pool en background...");
        try {
            warmupConnections();
            LOG.info("✅ WARM-UP completado: Pool de conexiones pre-inicializado");
        } catch (Exception e) {
            LOG.warning("⚠️ WARM-UP: Error al pre-inicializar conexiones (no crítico): " + e.getMessage());
        }
    }
    
    /**
     * Una sola conexión y una consulta mínima (SELECT 1) para validar Oracle sin alargar el warm-up.
     */
    private void warmupConnections() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1 FROM DUAL")) {
            if (rs.next()) {
                LOG.info("✅ Conexión Oracle pre-inicializada (warm-up)");
            }
        }
    }
}
