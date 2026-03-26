package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.port.in.SerieDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.SeccionDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SerieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.serie.SerieCodigosError;
import ec.gob.iess.gestiondocumental.domain.SerieDocumentalEstados;
import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SerieDocumentalUseCase implements SerieDocumentalUseCasePort {

    private static final int HTTP_BAD_REQUEST = 400;

    @Inject
    SerieDocumentalRepositoryPort serieRepositoryPort;

    @Inject
    SeccionDocumentalRepositoryPort seccionRepositoryPort;

    @Transactional
    public SerieDocumentalResponse crearSerie(SerieDocumentalRequest request, String usuarioCedula, String ipEquipo) {
        validarSeccionParaCreacion(request.getIdSeccion());

        SerieDocumental serie = new SerieDocumental();
        serie.setIdSeccion(request.getIdSeccion());
        serie.setNombreSerie(request.getNombreSerie());
        serie.setDescripcion(request.getDescripcion());
        serie.setFormatoDoc(request.getFormatoDoc());
        serie.setSeguridad(request.getSeguridad());
        serie.setNormativa(request.getNormativa());
        serie.setResponsable(request.getResponsable());
        String normCreacion = SerieDocumentalEstados.normalizarParaColumnaEstado(request.getEstado());
        serie.setEstado(
                normCreacion != null
                        ? normCreacion
                        : SerieDocumentalEstados.CREADO);
        serie.setJustificacion(request.getJustificacion());
        serie.setUsuCreacion(usuarioCedula);
        serie.setFecCreacion(LocalDateTime.now());
        serie.setIpEquipo(ipEquipo);

        serieRepositoryPort.persist(serie);
        return toResponse(serie);
    }

    @Transactional
    public Optional<SerieDocumentalResponse> actualizarSerie(
            Long id, SerieDocumentalRequest request, String usuarioCedula) {
        return serieRepositoryPort.findByIdOptional(id).map(serie -> {
            if (request.getNombreSerie() != null) serie.setNombreSerie(request.getNombreSerie());
            if (request.getDescripcion() != null) serie.setDescripcion(request.getDescripcion());
            if (request.getFormatoDoc() != null) serie.setFormatoDoc(request.getFormatoDoc());
            if (request.getSeguridad() != null) serie.setSeguridad(request.getSeguridad());
            if (request.getNormativa() != null) serie.setNormativa(request.getNormativa());
            if (request.getResponsable() != null) serie.setResponsable(request.getResponsable());
            if (request.getJustificacion() != null) serie.setJustificacion(request.getJustificacion());
            if (request.getEstado() != null) {
                String norm = SerieDocumentalEstados.normalizarParaColumnaEstado(request.getEstado());
                if (norm != null) {
                    serie.setEstado(norm);
                }
            }

            serieRepositoryPort.persist(serie);
            return toResponse(serie);
        });
    }

    public Optional<SerieDocumentalResponse> obtenerPorId(Long id) {
        return serieRepositoryPort.findByIdOptional(id).map(this::toResponse);
    }

    public List<SerieDocumentalResponse> listarPorSeccion(Long idSeccion) {
        return serieRepositoryPort.findBySeccion(idSeccion).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SerieDocumentalResponse> listarActivas() {
        return serieRepositoryPort.findActivas().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SerieDocumentalResponse toResponse(SerieDocumental serie) {
        if (serie == null) return null;
        SerieDocumentalResponse response = new SerieDocumentalResponse();
        response.setId(serie.getId());
        response.setIdSeccion(serie.getIdSeccion());
        response.setNombreSerie(serie.getNombreSerie());
        response.setDescripcion(serie.getDescripcion());
        response.setFormatoDoc(serie.getFormatoDoc());
        response.setSeguridad(serie.getSeguridad());
        response.setNormativa(serie.getNormativa());
        response.setResponsable(serie.getResponsable());
        response.setEstado(serie.getEstado());
        response.setJustificacion(serie.getJustificacion());
        response.setUsuCreacion(serie.getUsuCreacion());
        response.setFecCreacion(serie.getFecCreacion());
        return response;
    }

    private void validarSeccionParaCreacion(Long idSeccion) {
        if (idSeccion == null) {
            throw new NegocioApiException(
                    SerieCodigosError.SER_ID_SECCION_REQUERIDO,
                    "El identificador de sección (idSeccion) es obligatorio para crear una serie.",
                    HTTP_BAD_REQUEST);
        }
        if (seccionRepositoryPort.findById(idSeccion).isEmpty()) {
            throw new NegocioApiException(
                    SerieCodigosError.SER_ID_SECCION_NO_EXISTE,
                    "No existe una sección documental con idSeccion=" + idSeccion + ". "
                            + "Use GET /api/v1/catalogos/secciones para obtener identificadores válidos.",
                    HTTP_BAD_REQUEST);
        }
    }
}
