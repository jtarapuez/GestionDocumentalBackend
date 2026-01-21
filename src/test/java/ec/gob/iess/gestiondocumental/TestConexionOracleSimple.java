package ec.gob.iess.gestiondocumental;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test SIMPLE de conexión a Oracle
 * Solo prueba la conexión básica, sin Quarkus ni configuraciones complejas
 */
@DisplayName("Test de Conexión a Oracle")
public class TestConexionOracleSimple {
    
    @Test
    @DisplayName("Probar conexión básica a Oracle")
    public void testConexionBasica() {
        // Credenciales de conexión
        String host = "192.168.29.208";
        String port = "1539";
        String serviceName = "PDBIESS_DESA";
        String username = "DOCUMENTAL_OWNER";
        String password = "DOC87desa";
        
        // URL de conexión Oracle
        String url = String.format("jdbc:oracle:thin:@%s:%s/%s", host, port, serviceName);
        
        System.out.println("==========================================");
        System.out.println("Test de Conexión a Oracle");
        System.out.println("==========================================");
        System.out.println("URL: " + url);
        System.out.println("Usuario: " + username);
        System.out.println("");
        
        Connection conn = null;
        
        try {
            // Cargar el driver de Oracle
            System.out.println("1. Cargando driver Oracle...");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("   ✅ Driver cargado correctamente");
            
            // Intentar conexión
            System.out.println("2. Intentando conectar a la base de datos...");
            System.out.println("   Espera, esto puede tomar unos segundos...");
            
            conn = DriverManager.getConnection(url, username, password);
            assertNotNull(conn, "La conexión no debe ser null");
            assertFalse(conn.isClosed(), "La conexión debe estar abierta");
            System.out.println("   ✅ Conexión exitosa!");
            
            // Probar una consulta simple
            System.out.println("3. Probando consulta simple...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 'Conexión exitosa!' AS MENSAJE, SYSDATE AS FECHA FROM DUAL");
            
            assertTrue(rs.next(), "Debe retornar al menos un resultado");
            String mensaje = rs.getString("MENSAJE");
            String fecha = rs.getString("FECHA");
            System.out.println("   ✅ Consulta ejecutada correctamente");
            System.out.println("   Mensaje: " + mensaje);
            System.out.println("   Fecha del servidor: " + fecha);
            
            rs.close();
            stmt.close();
            
            // Verificar si existen las tablas
            System.out.println("4. Verificando tablas del sistema...");
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(
                "SELECT COUNT(*) AS TOTAL FROM USER_TABLES WHERE TABLE_NAME LIKE 'GDOC_%'"
            );
            
            assertTrue(rs2.next(), "Debe retornar el conteo de tablas");
            int total = rs2.getInt("TOTAL");
            System.out.println("   ✅ Tablas encontradas: " + total);
            assertTrue(total >= 0, "Debe haber al menos 0 tablas");
            
            rs2.close();
            stmt2.close();
            
            System.out.println("");
            System.out.println("==========================================");
            System.out.println("✅ TEST EXITOSO - La conexión funciona!");
            System.out.println("==========================================");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: No se encontró el driver de Oracle");
            System.err.println("   Asegúrate de tener la dependencia 'quarkus-jdbc-oracle' en pom.xml");
            fail("Driver de Oracle no encontrado: " + e.getMessage());
        } catch (java.sql.SQLException e) {
            System.err.println("❌ ERROR: No se pudo conectar a la base de datos");
            System.err.println("   SQL State: " + e.getSQLState());
            System.err.println("   Error Code: " + e.getErrorCode());
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("");
            System.err.println("Verifica:");
            System.err.println("  - Que el servidor Oracle esté corriendo");
            System.err.println("  - Que tengas acceso de red a " + host + ":" + port);
            System.err.println("  - Que las credenciales sean correctas");
            System.err.println("  - Que el service name '" + serviceName + "' sea correcto");
            fail("Error de conexión: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ ERROR inesperado:");
            e.printStackTrace();
            fail("Error inesperado: " + e.getMessage());
        } finally {
            // Cerrar conexión
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Conexión cerrada correctamente");
                } catch (Exception e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }
}



