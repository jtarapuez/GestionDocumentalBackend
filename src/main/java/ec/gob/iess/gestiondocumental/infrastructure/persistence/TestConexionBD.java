package ec.gob.iess.gestiondocumental.infrastructure.persistence;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test automático de conexión a la base de datos Oracle
 * Se ejecuta al iniciar la aplicación para validar la conexión
 */
@ApplicationScoped
public class TestConexionBD {
    
    @Inject
    AgroalDataSource dataSource;
    
    /**
     * Se ejecuta automáticamente al iniciar la aplicación
     */
    void onStart(@Observes StartupEvent ev) {
        // Esperar un poco para que el datasource esté listo
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n");
        System.out.println("==========================================");
        System.out.println("TEST AUTOMÁTICO - Conexión Oracle");
        System.out.println("==========================================");
        
        try {
            testConexion();
        } catch (Exception e) {
            System.err.println("\n❌ ERROR AL INICIAR - Revisa la configuración de BD");
            System.err.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Causa: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
    }
    
    /**
     * Prueba la conexión a la base de datos Oracle
     */
    public void testConexion() throws Exception {
        System.out.println("Intentando conectar a Oracle...");
        System.out.println("URL: jdbc:oracle:thin:@192.168.29.208:1539/PDBIESS_DESA");
        System.out.println("Usuario: DOCUMENTAL_OWNER");
        System.out.println("");
        
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ Conexión exitosa!");
            
            // Probar consulta simple
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 'OK' AS STATUS, SYSDATE FROM DUAL");
            
            if (rs.next()) {
                System.out.println("✅ Consulta exitosa: " + rs.getString(1));
                System.out.println("✅ Fecha servidor: " + rs.getTimestamp(2));
            }
            
            rs.close();
            
            // Verificar tablas del sistema
            System.out.println("\nVerificando tablas del sistema...");
            ResultSet rs2 = stmt.executeQuery(
                "SELECT COUNT(*) AS TOTAL FROM USER_TABLES WHERE TABLE_NAME LIKE 'GDOC_%'"
            );
            
            if (rs2.next()) {
                int total = rs2.getInt("TOTAL");
                System.out.println("✅ Tablas encontradas: " + total);
            }
            
            rs2.close();
            stmt.close();
            
            System.out.println("");
            System.out.println("==========================================");
            System.out.println("✅ CONEXIÓN ORACLE FUNCIONA CORRECTAMENTE");
            System.out.println("==========================================");
            System.out.println("");
        } catch (Exception e) {
            System.err.println("");
            System.err.println("❌ ERROR DE CONEXIÓN");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("");
            if (e.getCause() != null) {
                System.err.println("Causa: " + e.getCause().getMessage());
            }
            System.err.println("");
            throw e;
        }
    }
}






