package ec.gob.iess.gestiondocumental.interfaces.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuración JAX-RS de la aplicación.
 * Define el path base para todas las APIs REST: /api
 */
@ApplicationPath("/api")
public class GestionDocumentalApplication extends Application {
}
