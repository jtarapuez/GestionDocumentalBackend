package ec.gob.iess.gestiondocumental.infrastructure.persistence.adapter;

import ec.gob.iess.gestiondocumental.application.port.out.SubserieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.SubserieDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.SubserieDocumentalRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SubserieDocumentalEntity;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper.SubserieDocumentalMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SubserieDocumentalPersistenceAdapter implements SubserieDocumentalRepositoryPort {

    @Inject
    SubserieDocumentalRepository subserieDocumentalRepository;

    @Inject
    SubserieDocumentalMapper subserieDocumentalMapper;

    @Override
    public Optional<SubserieDocumental> findByIdOptional(Long id) {
        return subserieDocumentalRepository.findByIdOptional(id).map(subserieDocumentalMapper::toDomain);
    }

    @Override
    public List<SubserieDocumental> findBySerie(Long idSerie) {
        return subserieDocumentalRepository.findBySerie(idSerie).stream()
                .map(subserieDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubserieDocumental> findActivas() {
        return subserieDocumentalRepository.findActivas().stream()
                .map(subserieDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubserieDocumental> findBySerieAndEstado(Long idSerie, String estado) {
        return subserieDocumentalRepository.findBySerieAndEstado(idSerie, estado).stream()
                .map(subserieDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void persist(SubserieDocumental subserie) {
        SubserieDocumentalEntity entity = subserieDocumentalMapper.toEntity(subserie);
        if (subserie.getId() != null) {
            subserieDocumentalRepository.getEntityManager().merge(entity);
        } else {
            subserieDocumentalRepository.persist(entity);
            subserie.setId(entity.getId());
        }
    }
}
