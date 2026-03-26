package ec.gob.iess.gestiondocumental.application.exception;

/**
 * Error de reglas de negocio con código estable y HTTP asociado.
 * El adaptador REST la propaga para mapeo centralizado en el ExceptionMapper.
 */
public class NegocioApiException extends RuntimeException {

    private final String codigo;
    private final int statusHttp;

    public NegocioApiException(String codigo, String message, int statusHttp) {
        super(message);
        this.codigo = codigo;
        this.statusHttp = statusHttp;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getStatusHttp() {
        return statusHttp;
    }
}
