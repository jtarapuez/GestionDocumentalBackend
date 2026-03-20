package ec.gob.iess.gestiondocumental.application.port.out;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de inventarios documentales (PAS-GUI-047).
 */
public interface InventarioDocumentalRepositoryPort {

    Optional<InventarioDocumental> findByIdOptional(Long id);

    List<InventarioDocumental> findByOperador(String operador);

    List<InventarioDocumental> findPendientesByOperador(String operador);

    List<InventarioDocumental> findPendientesAprobacion();

    List<InventarioDocumental> findByEstado(String estado);

    boolean tienePendientesVencidos(String operador);

    List<InventarioDocumental> buscarConFiltros(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            LocalDate fechaDesde, LocalDate fechaHasta,
            String supervisor,
            List<Long> idsSubseriesCuandoFiltroPorSerie);

    void persist(InventarioDocumental inventario);
}
