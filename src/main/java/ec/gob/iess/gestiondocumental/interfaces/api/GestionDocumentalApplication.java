package ec.gob.iess.gestiondocumental.interfaces.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

/**
 * Configuración JAX-RS de la aplicación.
 * Define el path base para todas las APIs REST: /api
 */
@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(title = "Sistema de Gestión Documental API", version = "1.0.0"),
        security = @SecurityRequirement(name = GestionDocumentalApplication.BEARER_SCHEME)
)
public class GestionDocumentalApplication extends Application {

    public static final String BEARER_SCHEME = "BearerAuth";
}
