package ec.gob.iess.gestiondocumental.infrastructure.persistence.adapter;

import ec.gob.iess.gestiondocumental.application.port.out.SerieDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.SerieDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.SerieDocumentalRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.entity.SerieDocumentalEntity;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper.SerieDocumentalMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SerieDocumentalPersistenceAdapter implements SerieDocumentalRepositoryPort {

    @Inject
    SerieDocumentalRepository serieDocumentalRepository;

    @Inject
    SerieDocumentalMapper serieDocumentalMapper;

    @Override
    public Optional<SerieDocumental> findByIdOptional(Long id) {
        return serieDocumentalRepository.findByIdOptional(id).map(serieDocumentalMapper::toDomain);
    }

    @Override
    public List<SerieDocumental> findBySeccion(Long idSeccion) {
        return serieDocumentalRepository.findBySeccion(idSeccion).stream()
                .map(serieDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SerieDocumental> findActivas() {
        return serieDocumentalRepository.findActivas().stream()
                .map(serieDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SerieDocumental> findBySeccionAndEstado(Long idSeccion, String estado) {
        return serieDocumentalRepository.findBySeccionAndEstado(idSeccion, estado).stream()
                .map(serieDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void persist(SerieDocumental serie) {
        SerieDocumentalEntity entity = serieDocumentalMapper.toEntity(serie);
        if (serie.getId() != null) {
            // Actualización: merge (persist solo sirve para INSERT)
            serieDocumentalRepository.getEntityManager().merge(entity);
        } else {
            serieDocumentalRepository.persist(entity);
            serie.setId(entity.getId());
        }
    }
}
