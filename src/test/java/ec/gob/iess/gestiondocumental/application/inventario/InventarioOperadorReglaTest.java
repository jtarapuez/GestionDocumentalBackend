package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventarioOperadorReglaTest {

    @Test
    @DisplayName("no lanza cuando la cédula coincide con el operador almacenado")
    void mismoOperadorOk() {
        assertThatCode(() -> InventarioOperadorRegla.assertMismoOperadorQueCreo("1712345678", "1712345678"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("lanza NegocioApiException con mensaje estable si difieren")
    void distintoOperador() {
        assertThatThrownBy(() -> InventarioOperadorRegla.assertMismoOperadorQueCreo("1798765432", "1712345678"))
                .isInstanceOf(NegocioApiException.class)
                .hasFieldOrPropertyWithValue("codigo", InventarioCodigosError.INV_OPERADOR_NO_AUTORIZADO)
                .hasMessage(InventarioNegocioMessages.SOLO_OPERADOR_CREADOR_PUEDE_ACTUALIZAR);
    }
}
