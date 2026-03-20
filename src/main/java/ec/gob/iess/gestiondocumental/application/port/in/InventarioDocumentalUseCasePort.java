package ec.gob.iess.gestiondocumental.application.port.in;

import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para el caso de uso de inventarios documentales (PAS-GUI-047).
 */
public interface InventarioDocumentalUseCasePort {

    InventarioDocumentalResponse registrarInventario(InventarioDocumentalRequest request, String usuarioCedula, String ipEquipo);

    Optional<InventarioDocumentalResponse> actualizarInventario(Long id, InventarioDocumentalRequest request, String usuarioCedula);

    Optional<InventarioDocumentalResponse> obtenerPorId(Long id);

    List<InventarioDocumentalResponse> listarConFiltros(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            LocalDate fechaDesde, LocalDate fechaHasta,
            String supervisor);

    List<InventarioDocumentalResponse> listarPendientesAprobacion();

    List<InventarioDocumentalResponse> listarPendientesPorOperador(String operador);

    Optional<InventarioDocumentalResponse> aprobarInventario(Long id, String usuarioCedula, String observaciones);

    Optional<InventarioDocumentalResponse> rechazarInventario(Long id, String usuarioCedula, String observaciones);
}
