package ec.gob.iess.gestiondocumental.application.inventario;

import ec.gob.iess.gestiondocumental.application.port.out.InventarioDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InventarioPendientesReglaTest {

    @Test
    @DisplayName("consulta el puerto y lanza si hay pendientes vencidos")
    void lanzaCuandoPuertoIndicaVencidos() {
        InventarioDocumentalRepositoryPort port = mock(InventarioDocumentalRepositoryPort.class);
        when(port.tienePendientesVencidos("1712345678")).thenReturn(true);

        assertThatThrownBy(() -> InventarioPendientesRegla.validarPuedeRegistrarNuevo(port, "1712345678"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(InventarioNegocioMessages.REGISTRO_BLOQUEADO_POR_PENDIENTES_VENCIDOS);

        verify(port).tienePendientesVencidos("1712345678");
    }

    @Test
    @DisplayName("no lanza cuando el puerto indica que no hay pendientes vencidos")
    void okCuandoPuertoFalse() {
        InventarioDocumentalRepositoryPort port = mock(InventarioDocumentalRepositoryPort.class);
        when(port.tienePendientesVencidos("1712345678")).thenReturn(false);

        assertThatCode(() -> InventarioPendientesRegla.validarPuedeRegistrarNuevo(port, "1712345678"))
                .doesNotThrowAnyException();

        verify(port).tienePendientesVencidos("1712345678");
    }

    @Test
    @DisplayName("no consulta otros métodos del puerto")
    void soloTienePendientes() {
        InventarioDocumentalRepositoryPort port = mock(InventarioDocumentalRepositoryPort.class);
        when(port.tienePendientesVencidos("1")).thenReturn(false);
        InventarioPendientesRegla.validarPuedeRegistrarNuevo(port, "1");
        verify(port, never()).persist(any(InventarioDocumental.class));
    }
}
