package ec.gob.iess.gestiondocumental.application.port.in;

import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SubserieDocumentalResponse;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para el caso de uso de subseries documentales (PAS-GUI-047).
 */
public interface SubserieDocumentalUseCasePort {

    SubserieDocumentalResponse crearSubserie(SubserieDocumentalRequest request, String usuarioCedula, String ipEquipo);

    Optional<SubserieDocumentalResponse> actualizarSubserie(Long id, SubserieDocumentalRequest request, String usuarioCedula);

    Optional<SubserieDocumentalResponse> obtenerPorId(Long id);

    List<SubserieDocumentalResponse> listarPorSerie(Long idSerie);

    List<SubserieDocumentalResponse> listarActivas();
}
