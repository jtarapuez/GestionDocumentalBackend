package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.port.in.SerieDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.SerieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.SerieDocumentalEstados;
import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalResponse;
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
 * Tests unitarios del caso de uso de serie documental (puerto mockeado).
 */
@QuarkusTest
class SerieDocumentalUseCaseTest {

    @InjectMock
    SerieDocumentalRepositoryPort serieRepositoryPort;

    @Inject
    SerieDocumentalUseCasePort useCase;

    private static SerieDocumentalRequest requestMinimo() {
        SerieDocumentalRequest r = new SerieDocumentalRequest();
        r.setIdSeccion(1L);
        r.setNombreSerie("Serie Test");
        r.setDescripcion("Descripción");
        r.setEstado("Serie documental creada");
        return r;
    }

    private static SerieDocumental serieDominio(Long id, String nombre, Long idSeccion) {
        SerieDocumental s = new SerieDocumental();
        s.setId(id);
        s.setNombreSerie(nombre);
        s.setIdSeccion(idSeccion);
        s.setEstado(SerieDocumentalEstados.CREADO);
        s.setFecCreacion(LocalDateTime.now());
        return s;
    }

    @Test
    @DisplayName("crearSerie persiste y devuelve respuesta")
    void crearSeriePersisteYDevuelveRespuesta() {
        when(serieRepositoryPort.findActivas()).thenReturn(List.of());
        when(serieRepositoryPort.findBySeccion(any())).thenReturn(List.of());

        SerieDocumentalResponse response = useCase.crearSerie(
                requestMinimo(), "1712345678", "192.168.1.1");

        assertThat(response).isNotNull();
        assertThat(response.getNombreSerie()).isEqualTo("Serie Test");
        assertThat(response.getEstado()).isEqualTo(SerieDocumentalEstados.CREADO);
        verify(serieRepositoryPort).persist(any(SerieDocumental.class));
    }

    @Test
    @DisplayName("crearSerie usa el estado del request cuando viene informado")
    void crearSerieRespetaEstadoDelRequest() {
        when(serieRepositoryPort.findActivas()).thenReturn(List.of());
        when(serieRepositoryPort.findBySeccion(any())).thenReturn(List.of());

        SerieDocumentalRequest r = requestMinimo();
        r.setEstado("Serie documental actualizada");

        SerieDocumentalResponse response = useCase.crearSerie(r, "1712345678", "192.168.1.1");

        assertThat(response.getEstado())
                .isEqualTo(SerieDocumentalEstados.ACTUALIZADO);
        verify(serieRepositoryPort).persist(any(SerieDocumental.class));
    }

    @Test
    @DisplayName("actualizarSerie persiste el estado enviado en el request")
    void actualizarSerieRespetaEstadoDelRequest() {
        SerieDocumental existente = serieDominio(1L, "Serie A", 1L);
        when(serieRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(existente));

        SerieDocumentalRequest r = new SerieDocumentalRequest();
        r.setEstado("Serie documental actualizada");

        Optional<SerieDocumentalResponse> result = useCase.actualizarSerie(1L, r, "1712345678");

        assertThat(result).isPresent();
        assertThat(result.get().getEstado())
                .isEqualTo(SerieDocumentalEstados.ACTUALIZADO);
        verify(serieRepositoryPort).persist(existente);
    }

    @Test
    @DisplayName("obtenerPorId devuelve vacío cuando no existe")
    void obtenerPorIdVacioSiNoExiste() {
        when(serieRepositoryPort.findByIdOptional(999L)).thenReturn(Optional.empty());

        Optional<SerieDocumentalResponse> result = useCase.obtenerPorId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("obtenerPorId devuelve respuesta cuando existe")
    void obtenerPorIdRespuestaCuandoExiste() {
        SerieDocumental s = serieDominio(1L, "Serie A", 1L);
        when(serieRepositoryPort.findByIdOptional(1L)).thenReturn(Optional.of(s));

        Optional<SerieDocumentalResponse> result = useCase.obtenerPorId(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getNombreSerie()).isEqualTo("Serie A");
    }

    @Test
    @DisplayName("listarPorSeccion devuelve lista del puerto")
    void listarPorSeccion() {
        SerieDocumental s = serieDominio(1L, "Serie A", 10L);
        when(serieRepositoryPort.findBySeccion(10L)).thenReturn(List.of(s));

        List<SerieDocumentalResponse> result = useCase.listarPorSeccion(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdSeccion()).isEqualTo(10L);
        verify(serieRepositoryPort).findBySeccion(10L);
    }

    @Test
    @DisplayName("listarActivas devuelve lista del puerto")
    void listarActivas() {
        SerieDocumental s = serieDominio(1L, "Serie A", 1L);
        when(serieRepositoryPort.findActivas()).thenReturn(List.of(s));

        List<SerieDocumentalResponse> result = useCase.listarActivas();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEstado()).isEqualTo(SerieDocumentalEstados.CREADO);
    }
}
