package ec.gob.iess.gestiondocumental.infrastructure.persistence.adapter;

import ec.gob.iess.gestiondocumental.application.port.out.InventarioDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.application.port.out.SubserieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.InventarioDocumental;
import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.InventarioDocumentalRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.InventarioDocumentalEntity;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper.InventarioDocumentalMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class InventarioDocumentalPersistenceAdapter implements InventarioDocumentalRepositoryPort {

    @Inject
    InventarioDocumentalRepository inventarioDocumentalRepository;

    @Inject
    InventarioDocumentalMapper inventarioDocumentalMapper;

    @Inject
    SubserieDocumentalRepositoryPort subserieDocumentalRepositoryPort;

    @Override
    public Optional<InventarioDocumental> findByIdOptional(Long id) {
        return inventarioDocumentalRepository.findByIdOptional(id).map(inventarioDocumentalMapper::toDomain);
    }

    @Override
    public List<InventarioDocumental> findByOperador(String operador) {
        return inventarioDocumentalRepository.findByOperador(operador).stream()
                .map(inventarioDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioDocumental> findPendientesByOperador(String operador) {
        return inventarioDocumentalRepository.findPendientesByOperador(operador).stream()
                .map(inventarioDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioDocumental> findPendientesAprobacion() {
        return inventarioDocumentalRepository.findPendientesAprobacion().stream()
                .map(inventarioDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioDocumental> findByEstado(String estado) {
        return inventarioDocumentalRepository.findByEstado(estado).stream()
                .map(inventarioDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean tienePendientesVencidos(String operador) {
        return inventarioDocumentalRepository.tienePendientesVencidos(operador);
    }

    @Override
    public List<InventarioDocumental> buscarConFiltros(
            Long idSeccion, Long idSerie, Long idSubserie,
            String numeroExpediente, String estado,
            String numeroCedula, String numeroRuc, String operador,
            String nombresApellidos, String razonSocial, String descripcionSerie,
            String tipoContenedor, Integer numeroContenedor, String tipoArchivo,
            LocalDate fechaDesde, LocalDate fechaHasta,
            String supervisor,
            List<Long> idsSubseriesCuandoFiltroPorSerie) {
        List<Long> ids = idsSubseriesCuandoFiltroPorSerie;
        if (idSerie != null && (ids == null || ids.isEmpty())) {
            List<SubserieDocumental> subseries = subserieDocumentalRepositoryPort.findBySerie(idSerie);
            ids = subseries.stream().map(SubserieDocumental::getId).collect(Collectors.toList());
        }
        if (idSerie != null && ids == null) {
            ids = Collections.emptyList();
        }
        return inventarioDocumentalRepository.buscarConFiltros(
                idSeccion, idSerie, idSubserie, numeroExpediente, estado,
                numeroCedula, numeroRuc, operador,
                nombresApellidos, razonSocial, descripcionSerie,
                tipoContenedor, numeroContenedor, tipoArchivo, fechaDesde, fechaHasta,
                supervisor, ids)
                .stream()
                .map(inventarioDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void persist(InventarioDocumental inventario) {
        InventarioDocumentalEntity entity = inventarioDocumentalMapper.toEntity(inventario);
        if (inventario.getId() != null) {
            inventarioDocumentalRepository.getEntityManager().merge(entity);
        } else {
            inventarioDocumentalRepository.persist(entity);
            inventario.setId(entity.getId());
        }
    }
}
