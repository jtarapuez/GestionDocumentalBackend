package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.port.in.SubserieDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.SerieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SubserieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.subserie.SubserieCodigosError;
import ec.gob.iess.gestiondocumental.domain.SerieDocumentalEstados;
import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SubserieDocumentalUseCase implements SubserieDocumentalUseCasePort {

    private static final int HTTP_BAD_REQUEST = 400;

    @Inject
    SubserieDocumentalRepositoryPort subserieRepositoryPort;

    @Inject
    SerieDocumentalRepositoryPort serieRepositoryPort;

    @Transactional
    public SubserieDocumentalResponse crearSubserie(
            SubserieDocumentalRequest request, String usuarioCedula, String ipEquipo) {
        validarSerieParaCreacion(request.getIdSerie());

        SubserieDocumental subserie = new SubserieDocumental();
        subserie.setIdSerie(request.getIdSerie());
        subserie.setNombreSubserie(request.getNombreSubserie());
        subserie.setDescripcion(request.getDescripcion());
        subserie.setFormatoDoc(request.getFormatoDoc());
        subserie.setSeguridad(request.getSeguridad());
        subserie.setNormativa(request.getNormativa());
        subserie.setResponsable(request.getResponsable());
        String normCreacion = SerieDocumentalEstados.normalizarParaColumnaEstado(request.getEstado());
        subserie.setEstado(
                normCreacion != null
                        ? normCreacion
                        : SerieDocumentalEstados.CREADO);
        subserie.setJustificacion(request.getJustificacion());
        subserie.setUsuCreacion(usuarioCedula);
        subserie.setFecCreacion(LocalDateTime.now());
        subserie.setIpEquipo(ipEquipo);

        subserieRepositoryPort.persist(subserie);
        return toResponse(subserie);
    }

    @Transactional
    public Optional<SubserieDocumentalResponse> actualizarSubserie(
            Long id, SubserieDocumentalRequest request, String usuarioCedula) {
        return subserieRepositoryPort.findByIdOptional(id).map(subserie -> {
            if (request.getNombreSubserie() != null) subserie.setNombreSubserie(request.getNombreSubserie());
            if (request.getDescripcion() != null) subserie.setDescripcion(request.getDescripcion());
            if (request.getFormatoDoc() != null) subserie.setFormatoDoc(request.getFormatoDoc());
            if (request.getSeguridad() != null) subserie.setSeguridad(request.getSeguridad());
            if (request.getNormativa() != null) subserie.setNormativa(request.getNormativa());
            if (request.getResponsable() != null) subserie.setResponsable(request.getResponsable());
            if (request.getEstado() != null) {
                String norm = SerieDocumentalEstados.normalizarParaColumnaEstado(request.getEstado());
                if (norm != null) {
                    subserie.setEstado(norm);
                }
            }
            if (request.getJustificacion() != null) subserie.setJustificacion(request.getJustificacion());

            subserieRepositoryPort.persist(subserie);
            return toResponse(subserie);
        });
    }

    public Optional<SubserieDocumentalResponse> obtenerPorId(Long id) {
        return subserieRepositoryPort.findByIdOptional(id).map(this::toResponse);
    }

    public List<SubserieDocumentalResponse> listarPorSerie(Long idSerie) {
        return subserieRepositoryPort.findBySerie(idSerie).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubserieDocumentalResponse> listarActivas() {
        return subserieRepositoryPort.findActivas().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SubserieDocumentalResponse toResponse(SubserieDocumental subserie) {
        if (subserie == null) return null;
        SubserieDocumentalResponse response = new SubserieDocumentalResponse();
        response.setId(subserie.getId());
        response.setIdSerie(subserie.getIdSerie());
        response.setNombreSubserie(subserie.getNombreSubserie());
        response.setDescripcion(subserie.getDescripcion());
        response.setFormatoDoc(subserie.getFormatoDoc());
        response.setSeguridad(subserie.getSeguridad());
        response.setNormativa(subserie.getNormativa());
        response.setResponsable(subserie.getResponsable());
        response.setEstado(subserie.getEstado());
        response.setJustificacion(subserie.getJustificacion());
        response.setUsuCreacion(subserie.getUsuCreacion());
        response.setFecCreacion(subserie.getFecCreacion());
        return response;
    }

    private void validarSerieParaCreacion(Long idSerie) {
        if (idSerie == null) {
            throw new NegocioApiException(
                    SubserieCodigosError.SUB_ID_SERIE_REQUERIDO,
                    "El identificador de serie (idSerie) es obligatorio para crear una subserie.",
                    HTTP_BAD_REQUEST);
        }
        if (serieRepositoryPort.findByIdOptional(idSerie).isEmpty()) {
            throw new NegocioApiException(
                    SubserieCodigosError.SUB_ID_SERIE_NO_EXISTE,
                    "No existe una serie documental con idSerie=" + idSerie + ". "
                            + "Use GET /api/v1/series con idSeccion o consulte los listados de series.",
                    HTTP_BAD_REQUEST);
        }
    }
}
