package ec.gob.iess.gestiondocumental.application.inventario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InventarioEstadoUtilTest {

    @Test
    @DisplayName("reconoce Pendiente con y sin tilde")
    void pendienteVariantes() {
        assertThat(InventarioEstadoUtil.esPendienteAprobacion("Pendiente de Aprobación")).isTrue();
        assertThat(InventarioEstadoUtil.esPendienteAprobacion("Pendiente de Aprobacion")).isTrue();
        assertThat(InventarioEstadoUtil.esPendienteAprobacion("Actualizado")).isFalse();
    }

    @Test
    @DisplayName("filtro API devuelve ambas variantes de Pendiente")
    void variantesFiltroPendiente() {
        assertThat(InventarioEstadoUtil.variantesParaFiltroApi("Pendiente de Aprobación"))
                .containsExactly(
                        InventarioEstadoUtil.PENDIENTE_APROBACION,
                        InventarioEstadoUtil.PENDIENTE_APROBACION_SIN_TILDE);
    }
}
