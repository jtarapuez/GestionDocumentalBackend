package ec.gob.iess.gestiondocumental.application.inventario;

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
    @DisplayName("lanza IllegalStateException con mensaje estable si difieren")
    void distintoOperador() {
        assertThatThrownBy(() -> InventarioOperadorRegla.assertMismoOperadorQueCreo("1798765432", "1712345678"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(InventarioNegocioMessages.SOLO_OPERADOR_CREADOR_PUEDE_ACTUALIZAR);
    }
}
