package ec.gob.iess.ejemplo.interfaces.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Controlador REST de ejemplo - Hola Mundo
 * 
 * Según PAS-EST-043:
 * - Ubicación: interfaces/api/ (capa de presentación)
 * - Usa JAX-RS (RESTEasy Reactive)
 * - Documentado con OpenAPI
 */
@Path("/hola-mundo")
@Tag(name = "Ejemplo", description = "Endpoints de ejemplo para probar Quarkus")
public class HolaMundoController {

    /**
     * Endpoint simple de Hola Mundo
     * 
     * @return Mensaje de saludo
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Saludo simple",
        description = "Retorna un mensaje de saludo básico"
    )
    @APIResponse(
        responseCode = "200",
        description = "Saludo exitoso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = MensajeResponse.class)
        )
    )
    public Response holaMundo() {
        MensajeResponse respuesta = new MensajeResponse();
        respuesta.setMensaje("¡Hola Mundo desde Quarkus!");
        respuesta.setVersion("1.0.0");
        respuesta.setFramework("Quarkus 3.9.5");
        
        return Response.ok(respuesta).build();
    }

    /**
     * Endpoint con parámetro - Saludo personalizado
     * 
     * @param nombre Nombre de la persona a saludar
     * @return Mensaje de saludo personalizado
     */
    @GET
    @Path("/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Saludo personalizado",
        description = "Retorna un mensaje de saludo personalizado con el nombre proporcionado"
    )
    @APIResponse(
        responseCode = "200",
        description = "Saludo personalizado exitoso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = MensajeResponse.class)
        )
    )
    public Response holaMundoPersonalizado(@PathParam("nombre") String nombre) {
        MensajeResponse respuesta = new MensajeResponse();
        respuesta.setMensaje("¡Hola " + nombre + "! Bienvenido a Quarkus");
        respuesta.setVersion("1.0.0");
        respuesta.setFramework("Quarkus 3.9.5");
        
        return Response.ok(respuesta).build();
    }

    /**
     * Clase interna para la respuesta JSON
     */
    @Schema(description = "Respuesta de saludo")
    public static class MensajeResponse {
        @Schema(description = "Mensaje de saludo", example = "¡Hola Mundo desde Quarkus!")
        private String mensaje;
        
        @Schema(description = "Versión de la API", example = "1.0.0")
        private String version;
        
        @Schema(description = "Framework utilizado", example = "Quarkus 3.9.5")
        private String framework;

        // Getters y Setters
        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getFramework() {
            return framework;
        }

        public void setFramework(String framework) {
            this.framework = framework;
        }
    }
}
