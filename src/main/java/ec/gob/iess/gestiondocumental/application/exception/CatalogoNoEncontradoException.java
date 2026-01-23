package ec.gob.iess.gestiondocumental.application.exception;

/**
 * Excepción lanzada cuando no se encuentra un catálogo
 */
public class CatalogoNoEncontradoException extends RuntimeException {

    private final String codigo;

    public CatalogoNoEncontradoException(String codigo) {
        super("Catálogo no encontrado con código: " + codigo);
        this.codigo = codigo;
    }

    public CatalogoNoEncontradoException(String codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}





