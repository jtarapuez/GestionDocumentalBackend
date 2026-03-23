package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servicio de prueba de conexión a la base de datos Oracle.
 * No se ejecuta automáticamente al arranque (el test se realiza en {@link ConnectionWarmupService}).
 * Útil para invocar manualmente o desde un endpoint de diagnóstico.
 * <p>
 * Fase 1: logging vía {@link Logger}; no imprime URL/usuario en consola. La configuración JDBC
 * proviene de {@code application-*.properties} (no de cadenas fijas aquí).
 */
@ApplicationScoped
public class TestConexionBD {

    private static final Logger LOG = Logger.getLogger(TestConexionBD.class);

    @Inject
    AgroalDataSource dataSource;

    /**
     * Prueba la conexión a la base de datos Oracle: obtiene una conexión, ejecuta
     * una consulta contra DUAL y verifica la existencia de tablas GDOC_*.
     *
     * @throws Exception si la conexión o las consultas fallan
     */
    public void testConexion() throws Exception {
        LOG.info("Test manual de conexión: obteniendo conexión del pool configurado");

        try (Connection conn = dataSource.getConnection()) {
            LOG.info("Conexión obtenida correctamente desde el datasource");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 'OK' AS STATUS, SYSDATE FROM DUAL");

            if (rs.next()) {
                LOG.debugf("Consulta DUAL: status=%s", rs.getString(1));
            }

            rs.close();

            LOG.debug("Verificando tablas GDOC_* en USER_TABLES");
            ResultSet rs2 = stmt.executeQuery(
                    "SELECT COUNT(*) AS TOTAL FROM USER_TABLES WHERE TABLE_NAME LIKE 'GDOC_%'");

            if (rs2.next()) {
                int total = rs2.getInt("TOTAL");
                LOG.infof("Tablas GDOC_* visibles para el usuario: %d", total);
            }

            rs2.close();
            stmt.close();

            LOG.info("Test de conexión Oracle completado correctamente");
        } catch (Exception e) {
            LOG.error("Error en test de conexión Oracle", e);
            throw e;
        }
    }
}
