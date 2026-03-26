package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.application.exception.NegocioApiException;
import ec.gob.iess.gestiondocumental.application.inventario.InventarioCodigosError;
import ec.gob.iess.gestiondocumental.application.inventario.InventarioDocumentalRegistroMapper;
import ec.gob.iess.gestiondocumental.application.inventario.InventarioOperadorRegla;
import ec.gob.iess.gestiondocumental.application.inventario.InventarioPendientesRegla;
import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;
import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import ec.gob.iess.gestiondocumental.application.port.in.InventarioDocumentalUseCasePort;
import ec.gob.iess.gestiondocumental.application.port.out.InventarioDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SeccionDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SerieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SubserieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para la gestión de inventarios documentales
 * Contiene la lógica de negocio para operaciones con inventarios
 */
@ApplicationScoped
public class InventarioDocumentalUseCase implements InventarioDocumentalUseCasePort {

    private static final int HTTP_BAD_REQUEST = 400;

    private static final Logger LOG = Logger.getLogger(InventarioDocumentalUseCase.class);

    @Inject
    InventarioDocumentalRepositoryPort inventarioRepositoryPort;

    @Inject
    SeccionDocumentalRepositoryPort seccionRepositoryPort;

    @Inject
    SerieDocumentalRepositoryPort serieRepositoryPort;

    @Inject
    SubserieDocumentalRepositoryPort subserieRepositoryPort;

    @Inject
    InventarioDocumentalRegistroMapper inventarioRegistroMapper;

    /**
     * Registra un nuevo inventario documental
     * @param request Datos del inventario a crear
     * @param usuarioCedula Cédula del usuario operador
     * @param ipEquipo IP del equipo
     * @return Inventario creado
     */
    @Transactional
    public InventarioDocumentalResponse registrarInventario(InventarioDocumentalRequest request, 
                                                              String usuarioCedula, String ipEquipo) {
        InventarioPendientesRegla.validarPuedeRegistrarNuevo(inventarioRepositoryPort, usuarioCedula);
        LocalDateTime ahora = LocalDateTime.now();
        InventarioDocumental inventario = inventarioRegistroMapper.toNuevoInventario(
                request, usuarioCedula, ipEquipo, ahora);
        inventarioRepositoryPort.persist(inventario);
        return toResponse(inventario);
    }

    /**
     * Actualiza un inventario existente
     * @param id ID del inventario a actualizar
     * @param request Datos actualizados
     * @param usuarioCedula Cédula del usuario operador
     * @return Inventario actualizado
     * @throws NegocioApiException Si el inventario no está en estado válido para actualizar
     */
    @Transactional
    public Optional<InventarioDocumentalResponse> actualizarInventario(Long id, 
                                                                        InventarioDocumentalRequest request, 
                                                                        String usuarioCedula) {
        return inventarioRepositoryPort.findByIdOptional(id).map(inventario -> {
            String estadoActual = inventario.getEstadoInventario();
            
            LOG.debug(String.format(
                    "actualizarInventario: comparación operador, len solicitud=%d, len registro=%d, coincide=%s",
                    usuarioCedula != null ? usuarioCedula.length() : 0,
                    inventario.getOperador() != null ? inventario.getOperador().length() : 0,
                    usuarioCedula != null && usuarioCedula.equals(inventario.getOperador())));
            
            InventarioOperadorRegla.assertMismoOperadorQueCreo(usuarioCedula, inventario.getOperador());

            // Validar estados permitidos para actualización
            if ("Aprobado".equals(estadoActual) || "Aprobado con Modificaciones".equals(estadoActual)) {
                throw new NegocioApiException(
                        InventarioCodigosError.INV_ACTUALIZACION_APROBADO_NO_MODIFICABLE,
                        "Los inventarios aprobados no pueden ser modificados. "
                                + "Estado actual: " + estadoActual,
                        HTTP_BAD_REQUEST);
            }

            // ✅ Permitir actualizar "Pendiente de Aprobación" siempre (sin límite de días)
            // El bloqueo de 5 días aplica solo para REGISTRAR nuevos inventarios, no para actualizar pendientes
            boolean esPendienteAprobacion = "Pendiente de Aprobación".equals(estadoActual);
            
            // Para "Registrado", validar que no haya pasado más de 5 días
            if (!esPendienteAprobacion && "Registrado".equals(estadoActual)) {
                LocalDateTime fechaReferencia = inventario.getFechaCambioEstado() != null 
                    ? inventario.getFechaCambioEstado() 
                    : inventario.getFecCreacion();
                
                if (fechaReferencia != null) {
                    LocalDateTime fechaLimite = LocalDateTime.now().minusDays(5);
                    if (fechaReferencia.isBefore(fechaLimite)) {
                        throw new NegocioApiException(
                                InventarioCodigosError.INV_ACTUALIZACION_PLAZO_VENCIDO,
                                "No se puede actualizar. Ha pasado más de 5 días calendario desde la creación",
                                HTTP_BAD_REQUEST);
                    }
                }
            }
            
            // Validar que el estado sea uno de los permitidos para actualización
            if (!esPendienteAprobacion && !"Registrado".equals(estadoActual)) {
                throw new NegocioApiException(
                        InventarioCodigosError.INV_ACTUALIZACION_ESTADO_NO_PERMITIDO,
                        "Solo se pueden actualizar inventarios en estado 'Registrado' o 'Pendiente de Aprobación'. "
                                + "Estado actual: " + estadoActual,
                        HTTP_BAD_REQUEST);
            }

            inventarioRegistroMapper.aplicarParchesDesdeRequest(inventario, request);

            // ✅ Supervisor NO se actualiza - se mantiene el asignado originalmente al crear el inventario
            // El campo supervisor del request se ignora intencionalmente según requerimiento funcional EF-2-2
            
            // Cambiar estado a Actualizado
            inventario.setEstadoInventario("Actualizado");
            inventario.setFechaCambioEstado(LocalDateTime.now());
            inventario.setCedulaUsuarioCambio(usuarioCedula);

            inventarioRepositoryPort.persist(inventario);
            return toResponse(inventario);
        });
    }

    /**
     * Obtiene un inventario por su ID
     * @param id ID del inventario
     * @return Optional con el inventario encontrado
     */
    public Optional<InventarioDocumentalResponse> obtenerPorId(Long id) {
        return inventarioRepositoryPort.findByIdOptional(id).map(this::toResponse);
    }

    /**
     * Lista inventarios pendientes de aprobación (para supervisores)
     * @return Lista de inventarios pendientes
     */
    public List<InventarioDocumentalResponse> listarPendientesAprobacion() {
        return inventarioRepositoryPort.findPendientesAprobacion().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista inventarios pendientes de un operador
     * @param operador Cédula del operador
     * @return Lista de inventarios pendientes
     */
    public List<InventarioDocumentalResponse> listarPendientesPorOperador(String operador) {
        return inventarioRepositoryPort.findPendientesByOperador(operador).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista inventarios con filtros
     * @param idSeccion Filtro por sección
     * @param idSerie Filtro por serie
     * @param idSubserie Filtro por subserie
     * @param numeroExpediente Filtro por número de expediente
     * @param estado Filtro por estado
     * @param numeroCedula Filtro por cédula
     * @param numeroRuc Filtro por RUC
     * @param operador Filtro por operador
     * @param nombresApellidos Filtro por nombres y apellidos
     * @param razonSocial Filtro por razón social
     * @param descripcionSerie Filtro por descripción de serie
     * @param tipoContenedor Filtro por tipo de contenedor
     * @param numeroContenedor Filtro por número de contenedor
     * @param tipoArchivo Filtro por tipo de archivo
     * @param fechaDesde Filtro por fecha desde
     * @param fechaHasta Filtro por fecha hasta
     * @return Lista de inventarios
     */
    public List<InventarioDocumentalResponse> listarConFiltros(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            java.time.LocalDate fechaDesde, java.time.LocalDate fechaHasta,
            String supervisor) {
        return inventarioRepositoryPort.buscarConFiltros(
                idSeccion, idSerie, idSubserie, numeroExpediente, estado,
                numeroCedula, numeroRuc, operador,
                nombresApellidos, razonSocial, descripcionSerie,
                tipoContenedor, numeroContenedor, tipoArchivo, fechaDesde, fechaHasta,
                supervisor, null)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad InventarioDocumental a DTO InventarioDocumentalResponse
     * @param inventario Entidad InventarioDocumental
     * @return DTO InventarioDocumentalResponse
     */
    public InventarioDocumentalResponse toResponse(InventarioDocumental inventario) {
        if (inventario == null) {
            return null;
        }
        InventarioDocumentalResponse response = new InventarioDocumentalResponse();
        response.setId(inventario.getId());
        response.setIdSeccion(inventario.getIdSeccion());
        response.setIdSerie(inventario.getIdSerie());
        response.setIdSubserie(inventario.getIdSubserie());
        response.setNumeroExpediente(inventario.getNumeroExpediente());
        response.setNumeroCedula(inventario.getNumeroCedula());
        response.setNumeroRuc(inventario.getNumeroRuc());
        response.setNombresApellidos(inventario.getNombresApellidos());
        response.setRazonSocial(inventario.getRazonSocial());
        response.setDescripcionSerie(inventario.getDescripcionSerie());
        response.setNumeroExtremoDesde(inventario.getNumeroExtremoDesde());
        response.setNumeroExtremoHasta(inventario.getNumeroExtremoHasta());
        response.setFechaDesde(inventario.getFechaDesde());
        response.setFechaHasta(inventario.getFechaHasta());
        response.setCantidadFojas(inventario.getCantidadFojas());
        response.setTipoContenedor(inventario.getTipoContenedor());
        response.setNumeroContenedor(inventario.getNumeroContenedor());
        response.setSoporte(inventario.getSoporte());
        response.setTipoArchivo(inventario.getTipoArchivo());
        response.setPosicionPasivo(inventario.getPosicionPasivo());
        response.setNumeroRac(inventario.getNumeroRac());
        response.setNumeroFila(inventario.getNumeroFila());
        response.setNumeroColumna(inventario.getNumeroColumna());
        response.setNumeroPosicion(inventario.getNumeroPosicion());
        response.setBodega(inventario.getBodega());
        response.setObservaciones(inventario.getObservaciones());
        response.setOperador(inventario.getOperador());
        response.setSupervisor(inventario.getSupervisor());
        response.setEstadoInventario(inventario.getEstadoInventario());
        response.setFechaCambioEstado(inventario.getFechaCambioEstado());
        response.setCedulaUsuarioCambio(inventario.getCedulaUsuarioCambio());
        response.setUsuCreacion(inventario.getUsuCreacion());
        response.setFecCreacion(inventario.getFecCreacion());

        // Enriquecer respuesta con nombres descriptivos (opcionales) para consultas/reportes
        // Estos campos NO reemplazan a los IDs existentes, solo facilitan la visualización en el frontend.
        if (inventario.getIdSeccion() != null) {
            SeccionDocumental seccion = seccionRepositoryPort.findById(inventario.getIdSeccion()).orElse(null);
            if (seccion != null) {
                response.setNombreSeccion(seccion.getNombre());
            }
        }

        if (inventario.getIdSerie() != null) {
            SerieDocumental serie = serieRepositoryPort.findByIdOptional(inventario.getIdSerie()).orElse(null);
            if (serie != null) {
                response.setNombreSerie(serie.getNombreSerie());
            }
        }

        if (inventario.getIdSubserie() != null) {
            SubserieDocumental subserie = subserieRepositoryPort.findByIdOptional(inventario.getIdSubserie()).orElse(null);
            if (subserie != null) {
                response.setNombreSubserie(subserie.getNombreSubserie());
            }
        }

        // Por ahora, operador y supervisor se exponen tal como están almacenados.
        // Si más adelante se integra con un catálogo de usuarios/Keycloak, estos campos
        // podrán mapearse a nombres completos sin cambiar el contrato existente.
        response.setOperadorNombre(inventario.getOperador());
        response.setSupervisorNombre(inventario.getSupervisor());

        return response;
    }

    /**
     * Aprueba un inventario (solo supervisores)
     * @param id ID del inventario a aprobar
     * @param usuarioCedula Cédula del supervisor
     * @param observaciones Observaciones opcionales
     * @return Inventario aprobado
     * @throws NegocioApiException Si el inventario no está en estado válido para aprobar
     */
    @Transactional
    public Optional<InventarioDocumentalResponse> aprobarInventario(
            Long id, String usuarioCedula, String observaciones) {
        return inventarioRepositoryPort.findByIdOptional(id).map(inventario -> {
            String estadoActual = inventario.getEstadoInventario();
            
            // Validar que el estado permita aprobación
            if (!"Registrado".equals(estadoActual) && !"Actualizado".equals(estadoActual)) {
                throw new NegocioApiException(
                        InventarioCodigosError.INV_APROBACION_ESTADO_INVALIDO,
                        "Solo se pueden aprobar inventarios en estado 'Registrado' o 'Actualizado'. "
                                + "Estado actual: " + estadoActual,
                        HTTP_BAD_REQUEST);
            }

            // Determinar el estado final según el estado actual
            String nuevoEstado;
            if ("Actualizado".equals(estadoActual)) {
                nuevoEstado = "Aprobado con Modificaciones";
            } else {
                nuevoEstado = "Aprobado";
            }

            inventario.setEstadoInventario(nuevoEstado);
            inventario.setFechaCambioEstado(LocalDateTime.now());
            inventario.setCedulaUsuarioCambio(usuarioCedula);
            
            // Agregar observaciones si se proporcionaron
            if (observaciones != null && !observaciones.isEmpty()) {
                String obsActual = inventario.getObservaciones();
                String nuevasObs = (obsActual != null ? obsActual + "\n" : "") + 
                                  "[APROBACIÓN] " + observaciones;
                inventario.setObservaciones(nuevasObs);
            }

            inventarioRepositoryPort.persist(inventario);
            return toResponse(inventario);
        });
    }

    /**
     * Rechaza un inventario (Pendiente de Aprobación)
     * @param id ID del inventario a rechazar
     * @param usuarioCedula Cédula del supervisor
     * @param observaciones Observaciones del rechazo (obligatorio)
     * @return Inventario rechazado
     * @throws NegocioApiException Si observaciones inválidas o estado no válido para rechazar
     */
    @Transactional
    public Optional<InventarioDocumentalResponse> rechazarInventario(
            Long id, String usuarioCedula, String observaciones) {
        if (observaciones == null || observaciones.trim().isEmpty()) {
            throw new NegocioApiException(
                    InventarioCodigosError.INV_RECHAZO_OBSERVACIONES_REQUERIDAS,
                    "Las observaciones del rechazo son obligatorias",
                    HTTP_BAD_REQUEST);
        }

        return inventarioRepositoryPort.findByIdOptional(id).map(inventario -> {
            String estadoActual = inventario.getEstadoInventario();
            
            // Validar que el estado permita rechazo
            if (!"Registrado".equals(estadoActual) && !"Actualizado".equals(estadoActual)) {
                throw new NegocioApiException(
                        InventarioCodigosError.INV_RECHAZO_ESTADO_INVALIDO,
                        "Solo se pueden rechazar inventarios en estado 'Registrado' o 'Actualizado'. "
                                + "Estado actual: " + estadoActual,
                        HTTP_BAD_REQUEST);
            }

            inventario.setEstadoInventario("Pendiente de Aprobación");
            inventario.setFechaCambioEstado(LocalDateTime.now());
            inventario.setCedulaUsuarioCambio(usuarioCedula);
            
            // Agregar observaciones del rechazo
            String obsActual = inventario.getObservaciones();
            String nuevasObs = (obsActual != null ? obsActual + "\n" : "") + 
                              "[RECHAZO] " + observaciones;
            inventario.setObservaciones(nuevasObs);

            inventarioRepositoryPort.persist(inventario);
            return toResponse(inventario);
        });
    }
}


