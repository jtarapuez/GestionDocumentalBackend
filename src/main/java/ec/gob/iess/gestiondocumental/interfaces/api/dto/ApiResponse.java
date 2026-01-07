package ec.gob.iess.gestiondocumental.interfaces.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Respuesta estándar de la API según PAS-EST-043
 * Estructura: { data, meta, error }
 * 
 * @param <T> Tipo de datos en la respuesta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private T data;
    private Meta meta;
    private Error error;

    // Constructores
    public ApiResponse() {
    }

    public ApiResponse(T data) {
        this.data = data;
        this.meta = new Meta();
    }

    public ApiResponse(T data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public ApiResponse(Error error) {
        this.error = error;
    }

    // Métodos estáticos para crear respuestas
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> success(T data, Meta meta) {
        return new ApiResponse<>(data, meta);
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(new Error(message, code));
    }

    public static <T> ApiResponse<T> error(String message, String code, Map<String, Object> details) {
        return new ApiResponse<>(new Error(message, code, details));
    }

    // Getters y Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    /**
     * Clase interna para metadatos de la respuesta
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {
        private LocalDateTime timestamp;
        private Integer totalItems;
        private Integer totalPages;
        private Integer currentPage;
        private Integer pageSize;

        public Meta() {
            this.timestamp = LocalDateTime.now();
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Integer totalItems) {
            this.totalItems = totalItems;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }
    }

    /**
     * Clase interna para errores
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Error {
        private String message;
        private String code;
        private Map<String, Object> details;

        public Error() {
        }

        public Error(String message, String code) {
            this.message = message;
            this.code = code;
        }

        public Error(String message, String code, Map<String, Object> details) {
            this.message = message;
            this.code = code;
            this.details = details;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Map<String, Object> getDetails() {
            return details;
        }

        public void setDetails(Map<String, Object> details) {
            this.details = details;
        }
    }
}
