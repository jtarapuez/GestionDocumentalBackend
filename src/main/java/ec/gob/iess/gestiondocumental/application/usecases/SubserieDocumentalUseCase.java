package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.SubserieDocumentalRepository;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para la gestión de subseries documentales
 * Contiene la lógica de negocio para operaciones con subseries
 */
@ApplicationScoped
public class SubserieDocumentalUseCase {

    @Inject
    SubserieDocumentalRepository subserieRepository;

    /**
     * Crea una nueva subserie documental
     * @param request Datos de la subserie a crear
     * @param usuarioCedula Cédula del usuario que crea
     * @param ipEquipo IP del equipo
     * @return Subserie creada
     */
    @Transactional
    public SubserieDocumentalResponse crearSubserie(SubserieDocumentalRequest request, String usuarioCedula, String ipEquipo) {
        SubserieDocumental subserie = new SubserieDocumental();
        subserie.setIdSerie(request.getIdSerie());
        subserie.setNombreSubserie(request.getNombreSubserie());
        subserie.setDescripcion(request.getDescripcion());
        subserie.setFormatoDoc(request.getFormatoDoc());
        subserie.setSeguridad(request.getSeguridad());
        subserie.setNormativa(request.getNormativa());
        subserie.setResponsable(request.getResponsable());
        subserie.setEstado(request.getEstado() != null ? request.getEstado() : "Creado");
        subserie.setJustificacion(request.getJustificacion());
        subserie.setUsuCreacion(usuarioCedula);
        subserie.setFecCreacion(LocalDateTime.now());
        subserie.setIpEquipo(ipEquipo);

        subserieRepository.persist(subserie);
        return toResponse(subserie);
    }

    /**
     * Actualiza una subserie documental existente
     * @param id ID de la subserie a actualizar
     * @param request Datos actualizados
     * @param usuarioCedula Cédula del usuario que actualiza
     * @return Subserie actualizada
     */
    @Transactional
    public Optional<SubserieDocumentalResponse> actualizarSubserie(Long id, SubserieDocumentalRequest request, String usuarioCedula) {
        return subserieRepository.findByIdOptional(id).map(subserie -> {
            if (request.getNombreSubserie() != null) {
                subserie.setNombreSubserie(request.getNombreSubserie());
            }
            if (request.getDescripcion() != null) {
                subserie.setDescripcion(request.getDescripcion());
            }
            if (request.getFormatoDoc() != null) {
                subserie.setFormatoDoc(request.getFormatoDoc());
            }
            if (request.getSeguridad() != null) {
                subserie.setSeguridad(request.getSeguridad());
            }
            if (request.getNormativa() != null) {
                subserie.setNormativa(request.getNormativa());
            }
            if (request.getResponsable() != null) {
                subserie.setResponsable(request.getResponsable());
            }
            if (request.getEstado() != null) {
                subserie.setEstado(request.getEstado());
            }
            if (request.getJustificacion() != null) {
                subserie.setJustificacion(request.getJustificacion());
            }
            // Estado se actualiza a "Actualizado" cuando se modifica
            subserie.setEstado("Actualizado");
            
            subserieRepository.persist(subserie);
            return toResponse(subserie);
        });
    }

    /**
     * Obtiene una subserie por su ID
     * @param id ID de la subserie
     * @return Optional con la subserie encontrada
     */
    public Optional<SubserieDocumentalResponse> obtenerPorId(Long id) {
        return subserieRepository.findByIdOptional(id).map(this::toResponse);
    }

    /**
     * Lista todas las subseries de una serie
     * @param idSerie ID de la serie
     * @return Lista de subseries
     */
    public List<SubserieDocumentalResponse> listarPorSerie(Long idSerie) {
        return subserieRepository.findBySerie(idSerie).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas las subseries activas
     * @return Lista de subseries activas
     */
    public List<SubserieDocumentalResponse> listarActivas() {
        return subserieRepository.findActivas().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad SubserieDocumental a DTO SubserieDocumentalResponse
     * @param subserie Entidad SubserieDocumental
     * @return DTO SubserieDocumentalResponse
     */
    public SubserieDocumentalResponse toResponse(SubserieDocumental subserie) {
        if (subserie == null) {
            return null;
        }
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
}




