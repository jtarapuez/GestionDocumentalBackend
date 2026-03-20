package ec.gob.iess.gestiondocumental.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SerieDocumentalEstadosTest {

    @Test
    @DisplayName("normalizarParaColumnaEstado mapea descripciones de catálogo a valores de 15 caracteres")
    void normalizaDescripcionesLargas() {
        assertThat(SerieDocumentalEstados.normalizarParaColumnaEstado("Serie documental creada"))
                .isEqualTo(SerieDocumentalEstados.CREADO);
        assertThat(SerieDocumentalEstados.normalizarParaColumnaEstado("Serie documental actualizada"))
                .isEqualTo(SerieDocumentalEstados.ACTUALIZADO);
    }

    @Test
    @DisplayName("normalizarParaColumnaEstado devuelve null si viene vacío")
    void vacioEsNull() {
        assertThat(SerieDocumentalEstados.normalizarParaColumnaEstado(null)).isNull();
        assertThat(SerieDocumentalEstados.normalizarParaColumnaEstado("")).isNull();
        assertThat(SerieDocumentalEstados.normalizarParaColumnaEstado("   ")).isNull();
    }
}
