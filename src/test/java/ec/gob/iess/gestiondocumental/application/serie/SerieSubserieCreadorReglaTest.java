package ec.gob.iess.gestiondocumental.application.serie;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.subserie.SubserieCodigosError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SerieSubserieCreadorReglaTest {

    @Test
    @DisplayName("no lanza cuando la cédula coincide con USU_CREACION almacenado")
    void mismoCreadorOk() {
        assertThatCode(() -> SerieSubserieCreadorRegla.assertMismoCreadorQueCreo(
                        "1712345678",
                        "1712345678",
                        SerieCodigosError.SER_USUARIO_NO_AUTORIZADO,
                        SerieSubserieNegocioMessages.SOLO_CREADOR_PUEDE_ACTUALIZAR_SERIE))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("lanza NegocioApiException con código estable si difieren (serie)")
    void distintoCreadorSerie() {
        assertThatThrownBy(() -> SerieSubserieCreadorRegla.assertMismoCreadorQueCreo(
                        "1798765432",
                        "1712345678",
                        SerieCodigosError.SER_USUARIO_NO_AUTORIZADO,
                        SerieSubserieNegocioMessages.SOLO_CREADOR_PUEDE_ACTUALIZAR_SERIE))
                .isInstanceOf(NegocioApiException.class)
                .hasFieldOrPropertyWithValue("codigo", SerieCodigosError.SER_USUARIO_NO_AUTORIZADO)
                .hasMessage(SerieSubserieNegocioMessages.SOLO_CREADOR_PUEDE_ACTUALIZAR_SERIE);
    }

    @Test
    @DisplayName("lanza NegocioApiException con código estable si difieren (subserie)")
    void distintoCreadorSubserie() {
        assertThatThrownBy(() -> SerieSubserieCreadorRegla.assertMismoCreadorQueCreo(
                        "1798765432",
                        "1712345678",
                        SubserieCodigosError.SUB_USUARIO_NO_AUTORIZADO,
                        SerieSubserieNegocioMessages.SOLO_CREADOR_PUEDE_ACTUALIZAR_SUBSERIE))
                .isInstanceOf(NegocioApiException.class)
                .hasFieldOrPropertyWithValue("codigo", SubserieCodigosError.SUB_USUARIO_NO_AUTORIZADO)
                .hasMessage(SerieSubserieNegocioMessages.SOLO_CREADOR_PUEDE_ACTUALIZAR_SUBSERIE);
    }

    @Test
    @DisplayName("lanza si USU_CREACION almacenado es nulo")
    void usuCreacionNulo() {
        assertThatThrownBy(() -> SerieSubserieCreadorRegla.assertMismoCreadorQueCreo(
                        "1712345678",
                        null,
                        SerieCodigosError.SER_USUARIO_NO_AUTORIZADO,
                        SerieSubserieNegocioMessages.SOLO_CREADOR_PUEDE_ACTUALIZAR_SERIE))
                .isInstanceOf(NegocioApiException.class)
                .hasFieldOrPropertyWithValue("codigo", SerieCodigosError.SER_USUARIO_NO_AUTORIZADO);
    }
}
