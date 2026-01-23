package ec.gob.iess.gestiondocumental.application.usecases;

import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.InventarioDocumentalRepository;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalRequest;
import ec.gob.iess.gestiondocumental.interfaces.api.dto.InventarioDocumentalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
     */
    @Transactional
    public InventarioDocumentalResponse registrarInventario(InventarioDocumentalRequest request, 
                                                              String usuarioCedula, String ipEquipo) {
        // El registro de nuevos inventarios siempre se permite
        // La validación de pendientes vencidos solo aplica al actualizar inventarios existentes

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
        // Aceptar tanto "PASIVO" (del frontend) como "Archivo pasivo" (legacy)
        boolean esArchivoPasivo = "PASIVO".equals(request.getTipoArchivo()) 
            || "Archivo pasivo".equals(request.getTipoArchivo());
        
        if (esArchivoPasivo && request.getNumeroRac() != null) {
            // Si viene posicionPasivo del frontend, usarlo directamente
            // Si no, construir desde los campos individuales
            if (request.getPosicionPasivo() != null && !request.getPosicionPasivo().trim().isEmpty()) {
                inventario.setPosicionPasivo(request.getPosicionPasivo());
            } else {
                String posicion = String.format("%02d.%02d.%02d.%02d.%02d",
                    request.getNumeroRac(),
                    request.getNumeroFila() != null ? request.getNumeroFila() : 0,
                    request.getNumeroColumna() != null ? request.getNumeroColumna() : 0,
                    request.getNumeroPosicion() != null ? request.getNumeroPosicion() : 0,
                    request.getBodega() != null ? request.getBodega() : 0
                );
                inventario.setPosicionPasivo(posicion);
            }
            
            // Guardar campos individuales
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
     * Actualiza un inventario existente
     * @param id ID del inventario a actualizar
     * @param request Datos actualizados
     * @param usuarioCedula Cédula del usuario operador
     * @return Inventario actualizado
     * @throws IllegalStateException Si el inventario no está en estado válido para actualizar
     */
    @Transactional
    public Optional<InventarioDocumentalResponse> actualizarInventario(Long id, 
                                                                        InventarioDocumentalRequest request, 
                                                                        String usuarioCedula) {
        return inventarioRepository.findByIdOptional(id).map(inventario -> {
            String estadoActual = inventario.getEstadoInventario();
            
            // ✅ LOG: Verificar valores de operador para debugging
            System.out.println("🔍 [DEBUG] actualizarInventario - Comparando operadores:");
            System.out.println("🔍 [DEBUG]   - operadorId recibido (usuarioCedula): '" + usuarioCedula + "'");
            System.out.println("🔍 [DEBUG]   - operadorId en BD (inventario.getOperador()): '" + inventario.getOperador() + "'");
            System.out.println("🔍 [DEBUG]   - ¿Son iguales?: " + usuarioCedula.equals(inventario.getOperador()));
            
            // Validar que el operador sea el mismo que creó el inventario
            if (!usuarioCedula.equals(inventario.getOperador())) {
                throw new IllegalStateException(
                    "Solo el operador que creó el inventario puede actualizarlo"
                );
            }

            // Validar estados permitidos para actualización
            if ("Aprobado".equals(estadoActual) || "Aprobado con Modificaciones".equals(estadoActual)) {
                throw new IllegalStateException(
                    "Los inventarios aprobados no pueden ser modificados. " +
                    "Estado actual: " + estadoActual
                );
            }

            // ✅ Validar 5 días para "Pendiente de Aprobación"
            boolean esPendienteAprobacion = "Pendiente de Aprobación".equals(estadoActual);
            
            if (esPendienteAprobacion) {
                // Validar 5 días desde fechaCambioEstado cuando cambió a PENDIENTE
                LocalDateTime fechaReferencia = inventario.getFechaCambioEstado() != null 
                    ? inventario.getFechaCambioEstado() 
                    : inventario.getFecCreacion();
                
                // ✅ LOG: Debug para validación de 5 días
                System.out.println("🔍 [DEBUG] Validación 5 días PENDIENTE:");
                System.out.println("🔍 [DEBUG]   - Estado actual: " + estadoActual);
                System.out.println("🔍 [DEBUG]   - fechaCambioEstado: " + inventario.getFechaCambioEstado());
                System.out.println("🔍 [DEBUG]   - fecCreacion: " + inventario.getFecCreacion());
                System.out.println("🔍 [DEBUG]   - fechaReferencia usada: " + fechaReferencia);
                
                if (fechaReferencia != null) {
                    LocalDateTime fechaLimite = LocalDateTime.now().minusDays(5);
                    long diasTranscurridos = java.time.Duration.between(fechaReferencia, LocalDateTime.now()).toDays();
                    System.out.println("🔍 [DEBUG]   - Fecha límite (hoy - 5 días): " + fechaLimite);
                    System.out.println("🔍 [DEBUG]   - Días transcurridos: " + diasTranscurridos);
                    System.out.println("🔍 [DEBUG]   - ¿Es antes del límite?: " + fechaReferencia.isBefore(fechaLimite));
                    
                    if (fechaReferencia.isBefore(fechaLimite)) {
                        System.out.println("🔍 [DEBUG]   - ❌ BLOQUEADO: Pasaron más de 5 días");
                        throw new IllegalStateException(
                            "No se puede actualizar. Ha pasado más de 5 días calendario desde que fue marcado como Pendiente de Aprobación. " +
                            "Por favor actualice este inventario antes de continuar."
                        );
                    } else {
                        System.out.println("🔍 [DEBUG]   - ✅ PERMITIDO: Dentro de 5 días");
                    }
                }
            }
            
            // Para "Registrado", validar que no haya pasado más de 5 días
            if (!esPendienteAprobacion && "Registrado".equals(estadoActual)) {
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
            }
            
            // Validar que el estado sea uno de los permitidos para actualización
            if (!esPendienteAprobacion && !"Registrado".equals(estadoActual)) {
                throw new IllegalStateException(
                    "Solo se pueden actualizar inventarios en estado 'Registrado' o 'Pendiente de Aprobación'. " +
                    "Estado actual: " + estadoActual
                );
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
            // Aceptar tanto "PASIVO" (del frontend) como "Archivo pasivo" (legacy)
            boolean esArchivoPasivo = "PASIVO".equals(request.getTipoArchivo()) 
                || "Archivo pasivo".equals(request.getTipoArchivo());
            
            if (esArchivoPasivo && request.getNumeroRac() != null) {
                // Si viene posicionPasivo del frontend, usarlo directamente
                // Si no, construir desde los campos individuales
                if (request.getPosicionPasivo() != null && !request.getPosicionPasivo().trim().isEmpty()) {
                    inventario.setPosicionPasivo(request.getPosicionPasivo());
                } else {
                    String posicion = String.format("%02d.%02d.%02d.%02d.%02d",
                        request.getNumeroRac(),
                        request.getNumeroFila() != null ? request.getNumeroFila() : 0,
                        request.getNumeroColumna() != null ? request.getNumeroColumna() : 0,
                        request.getNumeroPosicion() != null ? request.getNumeroPosicion() : 0,
                        request.getBodega() != null ? request.getBodega() : 0
                    );
                    inventario.setPosicionPasivo(posicion);
                }
                
                // Guardar campos individuales
                inventario.setNumeroRac(request.getNumeroRac());
                inventario.setNumeroFila(request.getNumeroFila());
                inventario.setNumeroColumna(request.getNumeroColumna());
                inventario.setNumeroPosicion(request.getNumeroPosicion());
                inventario.setBodega(request.getBodega());
            }

            // ✅ Supervisor NO se actualiza - se mantiene el asignado originalmente al crear el inventario
            // El campo supervisor del request se ignora intencionalmente según requerimiento funcional EF-2-2
            
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
    public List<InventarioDocumentalResponse> listarConFiltros(Long idSeccion, Long idSerie, Long idSubserie,
                                                                 String numeroExpediente, String estado,
                                                                 String numeroCedula, String numeroRuc, String operador,
                                                                 String nombresApellidos, String razonSocial, String descripcionSerie,
                                                                 String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
                                                                 java.time.LocalDate fechaDesde, java.time.LocalDate fechaHasta,
                                                                 String supervisor) {
        return inventarioRepository.buscarConFiltros(idSeccion, idSerie, idSubserie, numeroExpediente, estado,
                                                      numeroCedula, numeroRuc, operador,
                                                      nombresApellidos, razonSocial, descripcionSerie,
                                                      tipoContenedor, numeroContenedor, tipoArchivo, fechaDesde, fechaHasta,
                                                      supervisor)
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


