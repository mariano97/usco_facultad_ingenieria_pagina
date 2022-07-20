package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Semillero;
import co.usco.facultad.ingenieria.pagina.repository.SemilleroRepository;
import co.usco.facultad.ingenieria.pagina.service.SemilleroService;
import co.usco.facultad.ingenieria.pagina.service.dto.SemilleroDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.SemilleroMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Semillero}.
 */
@Service
@Transactional
public class SemilleroServiceImpl implements SemilleroService {

    private final Logger log = LoggerFactory.getLogger(SemilleroServiceImpl.class);

    private final SemilleroRepository semilleroRepository;

    private final SemilleroMapper semilleroMapper;

    public SemilleroServiceImpl(SemilleroRepository semilleroRepository, SemilleroMapper semilleroMapper) {
        this.semilleroRepository = semilleroRepository;
        this.semilleroMapper = semilleroMapper;
    }

    @Override
    public Mono<SemilleroDTO> save(SemilleroDTO semilleroDTO) {
        log.debug("Request to save Semillero : {}", semilleroDTO);
        return semilleroRepository.save(semilleroMapper.toEntity(semilleroDTO)).map(semilleroMapper::toDto);
    }

    @Override
    public Mono<SemilleroDTO> update(SemilleroDTO semilleroDTO) {
        log.debug("Request to save Semillero : {}", semilleroDTO);
        return semilleroRepository.save(semilleroMapper.toEntity(semilleroDTO)).map(semilleroMapper::toDto);
    }

    @Override
    public Mono<SemilleroDTO> partialUpdate(SemilleroDTO semilleroDTO) {
        log.debug("Request to partially update Semillero : {}", semilleroDTO);

        return semilleroRepository
            .findById(semilleroDTO.getId())
            .map(existingSemillero -> {
                semilleroMapper.partialUpdate(existingSemillero, semilleroDTO);

                return existingSemillero;
            })
            .flatMap(semilleroRepository::save)
            .map(semilleroMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SemilleroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Semilleros");
        return semilleroRepository.findAllBy(pageable).map(semilleroMapper::toDto);
    }

    public Flux<SemilleroDTO> findAllWithEagerRelationships(Pageable pageable) {
        return semilleroRepository.findAllWithEagerRelationships(pageable).map(semilleroMapper::toDto);
    }

    public Mono<Long> countAll() {
        return semilleroRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SemilleroDTO> findOne(Long id) {
        log.debug("Request to get Semillero : {}", id);
        return semilleroRepository.findOneWithEagerRelationships(id).map(semilleroMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Semillero : {}", id);
        return semilleroRepository.deleteById(id);
    }
}
