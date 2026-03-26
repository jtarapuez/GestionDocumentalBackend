package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.inventario.InventarioNegocioMessages;
import ec.gob.iess.gestiondocumental.application.port.in.InventarioDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.InventarioDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SeccionDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SerieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SubserieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios del caso de uso de inventario documental (puertos mockeados).
 */
@QuarkusTest
class InventarioDocumentalUseCaseTest {

    @InjectMock
    InventarioDocumentalRepositoryPort inventarioRepositoryPort;

    @InjectMock
    SeccionDocumentalRepositoryPort seccionDocumentalRepositoryPort;

    @InjectMock
    SerieDocumentalRepositoryPort serieDocumentalRepositoryPort;

    @InjectMock
    SubserieDocumentalRepositoryPort subserieDocumentalRepositoryPort;

    @Inject
    InventarioDocumentalUseCasePort useCase;

    private static InventarioDocumentalRequest requestMinimo() {
        InventarioDocumentalRequest r = new InventarioDocumentalRequest();
        r.setIdSeccion(1L);
        r.setIdSerie(2L);
        r.setIdSubserie(3L);
        r.setNumeroExpediente("EXP-001");
        r.setTipoArchivo("Físico");
        return r;
    }

    private static InventarioDocumental inventarioDominio(Long id, String estado, String operador) {
        InventarioDocumental inv = new InventarioDocumental();
        inv.setId(id);
        inv.setEstadoInventario(estado);
        inv.setOperador(operador);
        inv.setFechaCambioEstado(LocalDateTime.now());
        inv.setFecCreacion(LocalDateTime.now());
        inv.setNumeroExpediente("EXP-001");
        return inv;
    }

    @Nested
    @DisplayName("registrarInventario")
    class RegistrarInventario {

        @Test
        @DisplayName("lanza cuando el operador tiene pendientes vencidos")
        void lanzaSiPendientesVencidos() {
            when(inventarioRepositoryPort.tienePendientesVencidos(eq("1712345678"))).thenReturn(true);

            assertThatThrownBy(() ->
                    useCase.registrarInventario(requestMinimo(), "1712345678", "192.168.1.1"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(InventarioNegocioMessages.REGISTRO_BLOQUEADO_POR_PENDIENTES_VENCIDOS);
            verify(inventarioRepositoryPort).tienePendientesVencidos("1712345678");
        }

        @Test
        @DisplayName("persiste y devuelve respuesta cuando no hay pendientes vencidos")
        void persisteYDevuelveRespuesta() {
            when(inventarioRepositoryPort.tienePendientesVencidos(anyString())).thenReturn(false);
            when(inventarioRepositoryPort.findPendientesAprobacion()).thenReturn(Collections.emptyList());
            when(inventarioRepositoryPort.findPendientesByOperador(anyString())).thenReturn(Collections.emptyList());
            when(seccionDocumentalRepositoryPort.findById(any())).thenReturn(Optional.empty());
            when(serieDocumentalRepositoryPort.findByIdOptional(any())).thenReturn(Optional.empty());
            when(subserieDocumentalRepositoryPort.findByIdOptional(any())).thenReturn(Optional.empty());

            InventarioDocumentalResponse response = useCase.registrarInventario(
                    requestMinimo(), "1712345678", "192.168.1.1");

            assertThat(response).isNotNull();
            assertThat(response.getNumeroExpediente()).isEqualTo("EXP-001");
            assertThat(response.getEstadoInventario()).isEqualTo("Registrado");
            assertThat(response.getOperador()).isEqualTo("1712345678");
            verify(inventarioRepositoryPort).persist(any(InventarioDocumental.class));
        }

        @Test
        @DisplayName("registra inventario PASIVO con posición RAC formateada")
        void registroArchivoPasivoConPosicion() {
            when(inventarioRepositoryPort.tienePendientesVencidos(anyString())).thenReturn(false);
            when(seccionDocumentalRepositoryPort.findById(any())).thenReturn(Optional.empty());
            when(serieDocumentalRepositoryPort.findByIdOptional(any())).thenReturn(Optional.empty());
            when(subserieDocumentalRepositoryPort.findByIdOptional(any())).thenReturn(Optional.empty());

            InventarioDocumentalRequest r = requestMinimo();
            r.setTipoArchivo("PASIVO");
            r.setNumeroRac(5);
            r.setNumeroFila(1);

            InventarioDocumentalResponse response = useCase.registrarInventario(r, "1712345678", "192.168.1.1");

            assertThat(response.getPosicionPasivo()).isEqualTo("05.01.00.00.00");
            assertThat(response.getNumeroRac()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("obtenerPorId")
    class ObtenerPorId {

        @Test
        @DisplayName("devuelve vacío cuando no existe")
        void vacioSiNoExiste() {
            when(inventarioRepositoryPort.findByIdOptional(999L)).thenReturn(Optional.empty());

            Optional<InventarioDocumentalResponse> result = useCase.obtenerPorId(999L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("devuelve respuesta cuando existe")
        void respuestaCuandoExiste() {
            InventarioDocumental inv = inventarioDominio(1L, "Registrado", "1712345678");
            when(inventarioRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(inv));

            Optional<InventarioDocumentalResponse> result = useCase.obtenerPorId(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            assertThat(result.get().getEstadoInventario()).isEqualTo("Registrado");
        }
    }

    @Nested
    @DisplayName("listarPendientesAprobacion")
    class ListarPendientesAprobacion {

        @Test
        @DisplayName("devuelve lista del puerto")
        void devuelveListaDelPuerto() {
            InventarioDocumental inv = inventarioDominio(1L, "Pendiente de Aprobación", "1712345678");
            when(inventarioRepositoryPort.findPendientesAprobacion()).thenReturn(List.of(inv));

            List<InventarioDocumentalResponse> result = useCase.listarPendientesAprobacion();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("listarPendientesPorOperador")
    class ListarPendientesPorOperador {

        @Test
        @DisplayName("devuelve lista del puerto")
        void devuelveListaDelPuerto() {
            InventarioDocumental inv = inventarioDominio(1L, "Pendiente de Aprobación", "1712345678");
            when(inventarioRepositoryPort.findPendientesByOperador("1712345678")).thenReturn(List.of(inv));

            List<InventarioDocumentalResponse> result = useCase.listarPendientesPorOperador("1712345678");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getOperador()).isEqualTo("1712345678");
        }
    }

    @Nested
    @DisplayName("actualizarInventario")
    class ActualizarInventario {

        @Test
        @DisplayName("devuelve vacío cuando el inventario no existe")
        void vacioSiNoExiste() {
            when(inventarioRepositoryPort.findByIdOptional(999L)).thenReturn(Optional.empty());

            InventarioDocumentalRequest req = requestMinimo();
            req.setNumeroExpediente("EXP-002");
            Optional<InventarioDocumentalResponse> result = useCase.actualizarInventario(999L, req, "1712345678");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("lanza cuando el operador no es el creador")
        void lanzaSiOperadorDistinto() {
            InventarioDocumental inv = inventarioDominio(1L, "Registrado", "1712345678");
            when(inventarioRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(inv));

            InventarioDocumentalRequest req = requestMinimo();
            assertThatThrownBy(() -> useCase.actualizarInventario(1L, req, "1798765432"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(InventarioNegocioMessages.SOLO_OPERADOR_CREADOR_PUEDE_ACTUALIZAR);
        }
    }

    @Nested
    @DisplayName("aprobarInventario")
    class AprobarInventario {

        @Test
        @DisplayName("devuelve vacío cuando el inventario no existe")
        void vacioSiNoExiste() {
            when(inventarioRepositoryPort.findByIdOptional(999L)).thenReturn(Optional.empty());

            Optional<InventarioDocumentalResponse> result = useCase.aprobarInventario(999L, "1798765432", null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("actualiza a Aprobado y devuelve respuesta cuando estado es Registrado")
        void actualizaAAprobadoCuandoRegistrado() {
            InventarioDocumental inv = inventarioDominio(1L, "Registrado", "1712345678");
            when(inventarioRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(inv));

            Optional<InventarioDocumentalResponse> result = useCase.aprobarInventario(1L, "1798765432", "OK");

            assertThat(result).isPresent();
            assertThat(result.get().getEstadoInventario()).isEqualTo("Aprobado");
            verify(inventarioRepositoryPort).persist(inv);
            assertThat(inv.getEstadoInventario()).isEqualTo("Aprobado");
        }
    }

    @Nested
    @DisplayName("rechazarInventario")
    class RechazarInventario {

        @Test
        @DisplayName("lanza cuando observaciones están vacías")
        void lanzaSiObservacionesVacias() {
            assertThatThrownBy(() -> useCase.rechazarInventario(1L, "1798765432", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("observaciones");
            assertThatThrownBy(() -> useCase.rechazarInventario(1L, "1798765432", "   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("actualiza a Pendiente de Aprobación cuando estado es Registrado")
        void actualizaAPendienteCuandoRegistrado() {
            InventarioDocumental inv = inventarioDominio(1L, "Registrado", "1712345678");
            when(inventarioRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(inv));

            Optional<InventarioDocumentalResponse> result = useCase.rechazarInventario(
                    1L, "1798765432", "Falta documentación");

            assertThat(result).isPresent();
            assertThat(result.get().getEstadoInventario()).isEqualTo("Pendiente de Aprobación");
            verify(inventarioRepositoryPort).persist(inv);
        }
    }
}
