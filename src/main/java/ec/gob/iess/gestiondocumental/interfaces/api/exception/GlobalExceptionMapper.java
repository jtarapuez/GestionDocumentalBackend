package ec.gob.iess.gestiondocumental.interfaces.api.exception;

import ec.gob.iess.gestiondocumental.application.exception.CatalogoNoEncontradoException;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapeador global de excepciones para convertir excepciones a respuestas estándar
 * según el contrato PAS-EST-043
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof CatalogoNoEncontradoException) {
            CatalogoNoEncontradoException ex = (CatalogoNoEncontradoException) exception;
            ApiResponse<Object> errorResponse = ApiResponse.error(
                ex.getMessage(),
                "CATALOGO_NOT_FOUND"
            );
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorResponse)
                    .build();
        }

        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) exception;
            Map<String, Object> details = new HashMap<>();
            details.put("violations", ex.getConstraintViolations().stream()
                    .map(v -> {
                        Map<String, String> violation = new HashMap<>();
                        violation.put("field", v.getPropertyPath().toString());
                        violation.put("message", v.getMessage());
                        return violation;
                    })
                    .collect(Collectors.toList()));

            ApiResponse<Object> errorResponse = ApiResponse.error(
                "Error de validación",
                "VALIDATION_ERROR",
                details
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        }

        // Error genérico
        ApiResponse<Object> errorResponse = ApiResponse.error(
            "Error interno del servidor: " + exception.getMessage(),
            "INTERNAL_SERVER_ERROR"
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}
