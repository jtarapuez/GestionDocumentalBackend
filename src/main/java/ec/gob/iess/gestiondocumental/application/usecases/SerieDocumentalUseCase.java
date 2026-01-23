package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.SerieDocumentalRepository;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para la gestión de series documentales
 * Contiene la lógica de negocio para operaciones con series
 */
@ApplicationScoped
public class SerieDocumentalUseCase {

    @Inject
    SerieDocumentalRepository serieRepository;

    /**
     * Crea una nueva serie documental
     * @param request Datos de la serie a crear
     * @param usuarioCedula Cédula del usuario que crea
     * @param ipEquipo IP del equipo
     * @return Serie creada
     */
    @Transactional
    public SerieDocumentalResponse crearSerie(SerieDocumentalRequest request, String usuarioCedula, String ipEquipo) {
        SerieDocumental serie = new SerieDocumental();
        serie.setIdSeccion(request.getIdSeccion());
        serie.setNombreSerie(request.getNombreSerie());
        serie.setDescripcion(request.getDescripcion());
        serie.setFormatoDoc(request.getFormatoDoc());
        serie.setSeguridad(request.getSeguridad());
        serie.setNormativa(request.getNormativa());
        serie.setResponsable(request.getResponsable());
        serie.setEstado(request.getEstado() != null ? request.getEstado() : "Creado");
        serie.setJustificacion(request.getJustificacion());
        serie.setUsuCreacion(usuarioCedula);
        serie.setFecCreacion(LocalDateTime.now());
        serie.setIpEquipo(ipEquipo);

        serieRepository.persist(serie);
        return toResponse(serie);
    }

    /**
     * Actualiza una serie documental existente
     * @param id ID de la serie a actualizar
     * @param request Datos actualizados
     * @param usuarioCedula Cédula del usuario que actualiza
     * @return Serie actualizada
     */
    @Transactional
    public Optional<SerieDocumentalResponse> actualizarSerie(Long id, SerieDocumentalRequest request, String usuarioCedula) {
        return serieRepository.findByIdOptional(id).map(serie -> {
            if (request.getNombreSerie() != null) {
                serie.setNombreSerie(request.getNombreSerie());
            }
            if (request.getDescripcion() != null) {
                serie.setDescripcion(request.getDescripcion());
            }
            if (request.getFormatoDoc() != null) {
                serie.setFormatoDoc(request.getFormatoDoc());
            }
            if (request.getSeguridad() != null) {
                serie.setSeguridad(request.getSeguridad());
            }
            if (request.getNormativa() != null) {
                serie.setNormativa(request.getNormativa());
            }
            if (request.getResponsable() != null) {
                serie.setResponsable(request.getResponsable());
            }
            if (request.getEstado() != null) {
                serie.setEstado(request.getEstado());
            }
            if (request.getJustificacion() != null) {
                serie.setJustificacion(request.getJustificacion());
            }
            // Estado se actualiza a "Actualizado" cuando se modifica
            serie.setEstado("Actualizado");
            
            serieRepository.persist(serie);
            return toResponse(serie);
        });
    }

    /**
     * Obtiene una serie por su ID
     * @param id ID de la serie
     * @return Optional con la serie encontrada
     */
    public Optional<SerieDocumentalResponse> obtenerPorId(Long id) {
        return serieRepository.findByIdOptional(id).map(this::toResponse);
    }

    /**
     * Lista todas las series de una sección
     * @param idSeccion ID de la sección
     * @return Lista de series
     */
    public List<SerieDocumentalResponse> listarPorSeccion(Long idSeccion) {
        return serieRepository.findBySeccion(idSeccion).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas las series activas
     * @return Lista de series activas
     */
    public List<SerieDocumentalResponse> listarActivas() {
        return serieRepository.findActivas().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad SerieDocumental a DTO SerieDocumentalResponse
     * @param serie Entidad SerieDocumental
     * @return DTO SerieDocumentalResponse
     */
    public SerieDocumentalResponse toResponse(SerieDocumental serie) {
        if (serie == null) {
            return null;
        }
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
}






