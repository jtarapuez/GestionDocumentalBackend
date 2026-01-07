package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.InventarioDocumentalRepository;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Caso de uso para la gestión de inventarios documentales
 * Contiene la lógica de negocio para operaciones con inventarios
 */
@ApplicationScoped
public class InventarioDocumentalUseCase {

    @Inject
    InventarioDocumentalRepository inventarioRepository;

    /**
     * Registra un nuevo inventario documental
     * @param request Datos del inventario a crear
     * @param usuarioCedula Cédula del usuario operador
     * @param ipEquipo IP del equipo
     * @return Inventario creado
     * @throws IllegalStateException Si el operador tiene pendientes vencidos
     */
    @Transactional
    public InventarioDocumentalResponse registrarInventario(InventarioDocumentalRequest request, 
                                                              String usuarioCedula, String ipEquipo) {
        // Validar que no haya pendientes vencidos
        if (inventarioRepository.tienePendientesVencidos(usuarioCedula)) {
            throw new IllegalStateException(
                "No se puede registrar nuevo inventario. Tiene registros pendientes de aprobación vencidos (más de 5 días). " +
                "Por favor actualice los registros pendientes primero."
            );
        }

        InventarioDocumental inventario = new InventarioDocumental();
        inventario.setIdSeccion(request.getIdSeccion());
        inventario.setIdSerie(request.getIdSerie());
        inventario.setIdSubserie(request.getIdSubserie());
        inventario.setNumeroExpediente(request.getNumeroExpediente());
        inventario.setNumeroCedula(request.getNumeroCedula());
        inventario.setNumeroRuc(request.getNumeroRuc());
        inventario.setNombresApellidos(request.getNombresApellidos());
        inventario.setRazonSocial(request.getRazonSocial());
        inventario.setDescripcionSerie(request.getDescripcionSerie());
        inventario.setNumeroExtremoDesde(request.getNumeroExtremoDesde());
        inventario.setNumeroExtremoHasta(request.getNumeroExtremoHasta());
        inventario.setFechaDesde(request.getFechaDesde());
        inventario.setFechaHasta(request.getFechaHasta());
        inventario.setCantidadFojas(request.getCantidadFojas());
        inventario.setTipoContenedor(request.getTipoContenedor());
        inventario.setNumeroContenedor(request.getNumeroContenedor());
        inventario.setSoporte(request.getSoporte());
        inventario.setTipoArchivo(request.getTipoArchivo());
        
        // Construir posición archivo pasivo si aplica
        if ("Archivo pasivo".equals(request.getTipoArchivo()) && request.getNumeroRac() != null) {
            String posicion = String.format("%02d.%02d.%02d.%02d.%02d",
                request.getNumeroRac(),
                request.getNumeroFila() != null ? request.getNumeroFila() : 0,
                request.getNumeroColumna() != null ? request.getNumeroColumna() : 0,
                request.getNumeroPosicion() != null ? request.getNumeroPosicion() : 0,
                request.getBodega() != null ? request.getBodega() : 0
            );
            inventario.setPosicionPasivo(posicion);
            inventario.setNumeroRac(request.getNumeroRac());
            inventario.setNumeroFila(request.getNumeroFila());
            inventario.setNumeroColumna(request.getNumeroColumna());
            inventario.setNumeroPosicion(request.getNumeroPosicion());
            inventario.setBodega(request.getBodega());
        }
        
        inventario.setObservaciones(request.getObservaciones());
        inventario.setOperador(usuarioCedula);
        inventario.setSupervisor(request.getSupervisor());
        inventario.setEstadoInventario("Registrado");
        inventario.setFechaCambioEstado(LocalDateTime.now());
        inventario.setCedulaUsuarioCambio(usuarioCedula);
        inventario.setUsuCreacion(usuarioCedula);
        inventario.setFecCreacion(LocalDateTime.now());
        inventario.setIpEquipo(ipEquipo);

        inventarioRepository.persist(inventario);
        return toResponse(inventario);
    }

    /**
     * Actualiza un inventario existente (solo si está en estado Pendiente de Aprobación)
     * @param id ID del inventario a actualizar
     * @param request Datos actualizados
     * @param usuarioCedula Cédula del usuario operador
     * @return Inventario actualizado
     * @throws IllegalStateException Si el inventario no está en estado Pendiente de Aprobación
     */
    @Transactional
    public Optional<InventarioDocumentalResponse> actualizarInventario(Long id, 
                                                                        InventarioDocumentalRequest request, 
                                                                        String usuarioCedula) {
        return inventarioRepository.findByIdOptional(id).map(inventario -> {
            // Validar que solo se puedan actualizar inventarios en estado Registrado
            // (dentro de los 5 días desde su creación)
            if (!"Registrado".equals(inventario.getEstadoInventario())) {
                throw new IllegalStateException(
                    "Solo se pueden actualizar inventarios en estado 'Registrado'. " +
                    "Estado actual: " + inventario.getEstadoInventario()
                );
            }

            // Validar que el operador sea el mismo que creó el inventario
            if (!usuarioCedula.equals(inventario.getOperador())) {
                throw new IllegalStateException(
                    "Solo el operador que creó el inventario puede actualizarlo"
                );
            }

            // Validar que no haya pasado más de 5 días desde la creación
            // Para inventarios en estado "Registrado", usar fecCreacion
            LocalDateTime fechaReferencia = inventario.getFechaCambioEstado() != null 
                ? inventario.getFechaCambioEstado() 
                : inventario.getFecCreacion();
            
            if (fechaReferencia != null) {
                LocalDateTime fechaLimite = LocalDateTime.now().minusDays(5);
                if (fechaReferencia.isBefore(fechaLimite)) {
                    throw new IllegalStateException(
                        "No se puede actualizar. Ha pasado más de 5 días calendario desde la creación"
                    );
                }
            }

            // Actualizar campos
            if (request.getNumeroExpediente() != null) {
                inventario.setNumeroExpediente(request.getNumeroExpediente());
            }
            if (request.getNumeroCedula() != null) {
                inventario.setNumeroCedula(request.getNumeroCedula());
            }
            if (request.getNumeroRuc() != null) {
                inventario.setNumeroRuc(request.getNumeroRuc());
            }
            if (request.getNombresApellidos() != null) {
                inventario.setNombresApellidos(request.getNombresApellidos());
            }
            if (request.getRazonSocial() != null) {
                inventario.setRazonSocial(request.getRazonSocial());
            }
            if (request.getDescripcionSerie() != null) {
                inventario.setDescripcionSerie(request.getDescripcionSerie());
            }
            if (request.getNumeroExtremoDesde() != null) {
                inventario.setNumeroExtremoDesde(request.getNumeroExtremoDesde());
            }
            if (request.getNumeroExtremoHasta() != null) {
                inventario.setNumeroExtremoHasta(request.getNumeroExtremoHasta());
            }
            if (request.getFechaDesde() != null) {
                inventario.setFechaDesde(request.getFechaDesde());
            }
            if (request.getFechaHasta() != null) {
                inventario.setFechaHasta(request.getFechaHasta());
            }
            if (request.getCantidadFojas() != null) {
                inventario.setCantidadFojas(request.getCantidadFojas());
            }
            if (request.getTipoContenedor() != null) {
                inventario.setTipoContenedor(request.getTipoContenedor());
            }
            if (request.getNumeroContenedor() != null) {
                inventario.setNumeroContenedor(request.getNumeroContenedor());
            }
            if (request.getSoporte() != null) {
                inventario.setSoporte(request.getSoporte());
            }
            if (request.getTipoArchivo() != null) {
                inventario.setTipoArchivo(request.getTipoArchivo());
            }
            if (request.getObservaciones() != null) {
                inventario.setObservaciones(request.getObservaciones());
            }

            // Actualizar posición archivo pasivo si aplica
            if ("Archivo pasivo".equals(request.getTipoArchivo()) && request.getNumeroRac() != null) {
                String posicion = String.format("%02d.%02d.%02d.%02d.%02d",
                    request.getNumeroRac(),
                    request.getNumeroFila() != null ? request.getNumeroFila() : 0,
                    request.getNumeroColumna() != null ? request.getNumeroColumna() : 0,
                    request.getNumeroPosicion() != null ? request.getNumeroPosicion() : 0,
                    request.getBodega() != null ? request.getBodega() : 0
                );
                inventario.setPosicionPasivo(posicion);
                inventario.setNumeroRac(request.getNumeroRac());
                inventario.setNumeroFila(request.getNumeroFila());
                inventario.setNumeroColumna(request.getNumeroColumna());
                inventario.setNumeroPosicion(request.getNumeroPosicion());
                inventario.setBodega(request.getBodega());
            }

            // Cambiar estado a Actualizado
            inventario.setEstadoInventario("Actualizado");
            inventario.setFechaCambioEstado(LocalDateTime.now());
            inventario.setCedulaUsuarioCambio(usuarioCedula);

            inventarioRepository.persist(inventario);
            return toResponse(inventario);
        });
    }

    /**
     * Obtiene un inventario por su ID
     * @param id ID del inventario
     * @return Optional con el inventario encontrado
     */
    public Optional<InventarioDocumentalResponse> obtenerPorId(Long id) {
        return inventarioRepository.findByIdOptional(id).map(this::toResponse);
    }

    /**
     * Lista inventarios pendientes de aprobación (para supervisores)
     * @return Lista de inventarios pendientes
     */
    public List<InventarioDocumentalResponse> listarPendientesAprobacion() {
        return inventarioRepository.findPendientesAprobacion().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista inventarios pendientes de un operador
     * @param operador Cédula del operador
     * @return Lista de inventarios pendientes
     */
    public List<InventarioDocumentalResponse> listarPendientesPorOperador(String operador) {
        return inventarioRepository.findPendientesByOperador(operador).stream()
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
     * @return Lista de inventarios
     */
    public List<InventarioDocumentalResponse> listarConFiltros(Long idSeccion, Long idSerie, Long idSubserie,
                                                                 String numeroExpediente, String estado) {
        return inventarioRepository.buscarConFiltros(idSeccion, idSerie, idSubserie, numeroExpediente, estado)
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
        return response;
    }

    /**
     * Aprueba un inventario (solo supervisores)
     * @param id ID del inventario a aprobar
     * @param usuarioCedula Cédula del supervisor
     * @param observaciones Observaciones opcionales
     * @return Inventario aprobado
     * @throws IllegalStateException Si el inventario no está en estado válido para aprobar
     */
    @Transactional
    public Optional<InventarioDocumentalResponse> aprobarInventario(Long id, String usuarioCedula, String observaciones) {
        return inventarioRepository.findByIdOptional(id).map(inventario -> {
            String estadoActual = inventario.getEstadoInventario();
            
            // Validar que el estado permita aprobación
            if (!"Registrado".equals(estadoActual) && !"Actualizado".equals(estadoActual)) {
                throw new IllegalStateException(
                    "Solo se pueden aprobar inventarios en estado 'Registrado' o 'Actualizado'. " +
                    "Estado actual: " + estadoActual
                );
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

            inventarioRepository.persist(inventario);
            return toResponse(inventario);
        });
    }

    /**
     * Rechaza un inventario (Pendiente de Aprobación)
     * @param id ID del inventario a rechazar
     * @param usuarioCedula Cédula del supervisor
     * @param observaciones Observaciones del rechazo (obligatorio)
     * @return Inventario rechazado
     * @throws IllegalStateException Si el inventario no está en estado válido para rechazar
     */
    @Transactional
    public Optional<InventarioDocumentalResponse> rechazarInventario(Long id, String usuarioCedula, String observaciones) {
        if (observaciones == null || observaciones.trim().isEmpty()) {
            throw new IllegalArgumentException("Las observaciones del rechazo son obligatorias");
        }

        return inventarioRepository.findByIdOptional(id).map(inventario -> {
            String estadoActual = inventario.getEstadoInventario();
            
            // Validar que el estado permita rechazo
            if (!"Registrado".equals(estadoActual) && !"Actualizado".equals(estadoActual)) {
                throw new IllegalStateException(
                    "Solo se pueden rechazar inventarios en estado 'Registrado' o 'Actualizado'. " +
                    "Estado actual: " + estadoActual
                );
            }

            inventario.setEstadoInventario("Pendiente de Aprobación");
            inventario.setFechaCambioEstado(LocalDateTime.now());
            inventario.setCedulaUsuarioCambio(usuarioCedula);
            
            // Agregar observaciones del rechazo
            String obsActual = inventario.getObservaciones();
            String nuevasObs = (obsActual != null ? obsActual + "\n" : "") + 
                              "[RECHAZO] " + observaciones;
            inventario.setObservaciones(nuevasObs);

            inventarioRepository.persist(inventario);
            return toResponse(inventario);
        });
    }
}

