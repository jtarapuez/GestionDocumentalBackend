package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.port.in.SubserieDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.SubserieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios del caso de uso de subserie documental (puerto mockeado).
 */
@QuarkusTest
class SubserieDocumentalUseCaseTest {

    @InjectMock
    SubserieDocumentalRepositoryPort subserieRepositoryPort;

    @Inject
    SubserieDocumentalUseCasePort useCase;

    private static SubserieDocumentalRequest requestMinimo() {
        SubserieDocumentalRequest r = new SubserieDocumentalRequest();
        r.setIdSerie(1L);
        r.setNombreSubserie("Subserie Test");
        r.setDescripcion("Descripción");
        r.setEstado("Creado");
        return r;
    }

    private static SubserieDocumental subserieDominio(Long id, String nombre, Long idSerie) {
        SubserieDocumental s = new SubserieDocumental();
        s.setId(id);
        s.setNombreSubserie(nombre);
        s.setIdSerie(idSerie);
        s.setEstado("Creado");
        s.setFecCreacion(LocalDateTime.now());
        return s;
    }

    @Test
    @DisplayName("crearSubserie persiste y devuelve respuesta")
    void crearSubseriePersisteYDevuelveRespuesta() {
        when(subserieRepositoryPort.findActivas()).thenReturn(List.of());
        when(subserieRepositoryPort.findBySerie(any())).thenReturn(List.of());

        SubserieDocumentalResponse response = useCase.crearSubserie(
                requestMinimo(), "1712345678", "192.168.1.1");

        assertThat(response).isNotNull();
        assertThat(response.getNombreSubserie()).isEqualTo("Subserie Test");
        assertThat(response.getEstado()).isEqualTo("Creado");
        verify(subserieRepositoryPort).persist(any(SubserieDocumental.class));
    }

    @Test
    @DisplayName("actualizarSubserie persiste el estado del request")
    void actualizarSubserieRespetaEstadoDelRequest() {
        SubserieDocumental existente = subserieDominio(1L, "Sub A", 1L);
        when(subserieRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(existente));

        SubserieDocumentalRequest r = new SubserieDocumentalRequest();
        r.setEstado("Actualizado");

        Optional<SubserieDocumentalResponse> result = useCase.actualizarSubserie(1L, r, "1712345678");

        assertThat(result).isPresent();
        assertThat(result.get().getEstado()).isEqualTo("Actualizado");
        verify(subserieRepositoryPort).persist(existente);
    }

    @Test
    @DisplayName("obtenerPorId devuelve vacío cuando no existe")
    void obtenerPorIdVacioSiNoExiste() {
        when(subserieRepositoryPort.findByIdOptional(999L)).thenReturn(Optional.empty());

        Optional<SubserieDocumentalResponse> result = useCase.obtenerPorId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("obtenerPorId devuelve respuesta cuando existe")
    void obtenerPorIdRespuestaCuandoExiste() {
        SubserieDocumental s = subserieDominio(1L, "Subserie A", 1L);
        when(subserieRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(s));

        Optional<SubserieDocumentalResponse> result = useCase.obtenerPorId(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getNombreSubserie()).isEqualTo("Subserie A");
    }

    @Test
    @DisplayName("listarPorSerie devuelve lista del puerto")
    void listarPorSerie() {
        SubserieDocumental s = subserieDominio(1L, "Subserie A", 10L);
        when(subserieRepositoryPort.findBySerie(10L)).thenReturn(List.of(s));

        List<SubserieDocumentalResponse> result = useCase.listarPorSerie(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdSerie()).isEqualTo(10L);
        verify(subserieRepositoryPort).findBySerie(10L);
    }

    @Test
    @DisplayName("listarActivas devuelve lista del puerto")
    void listarActivas() {
        SubserieDocumental s = subserieDominio(1L, "Subserie A", 1L);
        when(subserieRepositoryPort.findActivas()).thenReturn(List.of(s));

        List<SubserieDocumentalResponse> result = useCase.listarActivas();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEstado()).isEqualTo("Creado");
    }
}
