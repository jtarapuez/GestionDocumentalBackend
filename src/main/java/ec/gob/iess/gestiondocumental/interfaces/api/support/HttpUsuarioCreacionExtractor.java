package ec.gob.iess.gestiondocumental.interfaces.api.support;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.serie.SerieCodigosError;

/**
 * Usuario de auditoría para series/subseries ({@code USU_CREACION}).
 * A diferencia de {@link HttpOperadorExtractor}, no usa fallback {@code "1"} ni cédulas de prueba.
 */
public final class HttpUsuarioCreacionExtractor {

  /** Mismo header que inventarios; el MFE lo rellena desde la sesión NextAuth. */
  public static final String HEADER_USUARIO_CREACION = "X-Operador-Id";

  private static final int MAX_AUDITORIA_LENGTH = 10;
  private static final int HTTP_BAD_REQUEST = 400;

  private HttpUsuarioCreacionExtractor() {}

  /**
   * @throws NegocioApiException si el header falta o queda vacío tras normalizar
   */
  public static String fromHeaderRequired(String usuarioCreacionHeader) {
    if (usuarioCreacionHeader == null || usuarioCreacionHeader.trim().isEmpty()) {
      throw new NegocioApiException(
          SerieCodigosError.SER_USUARIO_CREACION_REQUERIDO,
          "Header "
              + HEADER_USUARIO_CREACION
              + " es obligatorio (usuario de sesión: cédula o preferred_username)",
          HTTP_BAD_REQUEST);
    }
    return normalizarParaColumna(usuarioCreacionHeader.trim());
  }

  /** Ajusta a VARCHAR(10) de {@code USU_CREACION} en Oracle. */
  public static String normalizarParaColumna(String valor) {
    if (valor.length() <= MAX_AUDITORIA_LENGTH) {
      return valor;
    }
    return valor.substring(0, MAX_AUDITORIA_LENGTH);
  }
}
