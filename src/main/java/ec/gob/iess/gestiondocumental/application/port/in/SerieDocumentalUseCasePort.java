package ec.gob.iess.gestiondocumental.application.port.in;

import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.SerieDocumentalResponse;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para el caso de uso de series documentales (PAS-GUI-047).
 */
public interface SerieDocumentalUseCasePort {

    SerieDocumentalResponse crearSerie(SerieDocumentalRequest request, String usuarioCedula, String ipEquipo);

    Optional<SerieDocumentalResponse> actualizarSerie(Long id, SerieDocumentalRequest request, String usuarioCedula);

    Optional<SerieDocumentalResponse> obtenerPorId(Long id);

    List<SerieDocumentalResponse> listarPorSeccion(Long idSeccion);

    List<SerieDocumentalResponse> listarActivas();
}
