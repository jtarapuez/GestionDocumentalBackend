package ec.gob.iess.ejemplo;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Clase principal de la aplicación Quarkus
 * Configura el path base para todas las APIs REST
 * 
 * Según PAS-EST-043: Estructura estándar de Quarkus
 */
@ApplicationPath("/api")
public class EjemploApplication extends Application {
    // Configuración automática de Quarkus
}
