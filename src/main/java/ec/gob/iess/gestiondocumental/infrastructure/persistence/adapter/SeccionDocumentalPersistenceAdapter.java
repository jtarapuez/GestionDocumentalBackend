package ec.gob.iess.gestiondocumental.infrastructure.persistence.adapter;

import ec.gob.iess.gestiondocumental.application.port.out.SeccionDocumentalRepositoryPort;
import ec.gob.iess.gestiondocumental.domain.model.SeccionDocumental;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.SeccionDocumentalRepository;
import ec.gob.iess.gestiondocumental.infrastructure.persistence.mapper.SeccionDocumentalMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SeccionDocumentalPersistenceAdapter implements SeccionDocumentalRepositoryPort {

    @Inject
    SeccionDocumentalRepository seccionDocumentalRepository;

    @Inject
    SeccionDocumentalMapper seccionDocumentalMapper;

    @Override
    public List<SeccionDocumental> findActivas() {
        return seccionDocumentalRepository.findActivas().stream()
                .map(seccionDocumentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SeccionDocumental> findById(Long id) {
        return seccionDocumentalRepository.findByIdOptional(id).map(seccionDocumentalMapper::toDomain);
    }
}
