package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Sede;
import co.usco.facultad.ingenieria.pagina.repository.SedeRepository;
import co.usco.facultad.ingenieria.pagina.service.SedeService;
import co.usco.facultad.ingenieria.pagina.service.dto.SedeDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.SedeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Sede}.
 */
@Service
@Transactional
public class SedeServiceImpl implements SedeService {

    private final Logger log = LoggerFactory.getLogger(SedeServiceImpl.class);

    private final SedeRepository sedeRepository;

    private final SedeMapper sedeMapper;

    public SedeServiceImpl(SedeRepository sedeRepository, SedeMapper sedeMapper) {
        this.sedeRepository = sedeRepository;
        this.sedeMapper = sedeMapper;
    }

    @Override
    public Mono<SedeDTO> save(SedeDTO sedeDTO) {
        log.debug("Request to save Sede : {}", sedeDTO);
        return sedeRepository.save(sedeMapper.toEntity(sedeDTO)).map(sedeMapper::toDto);
    }

    @Override
    public Mono<SedeDTO> update(SedeDTO sedeDTO) {
        log.debug("Request to save Sede : {}", sedeDTO);
        return sedeRepository.save(sedeMapper.toEntity(sedeDTO)).map(sedeMapper::toDto);
    }

    @Override
    public Mono<SedeDTO> partialUpdate(SedeDTO sedeDTO) {
        log.debug("Request to partially update Sede : {}", sedeDTO);

        return sedeRepository
            .findById(sedeDTO.getId())
            .map(existingSede -> {
                sedeMapper.partialUpdate(existingSede, sedeDTO);

                return existingSede;
            })
            .flatMap(sedeRepository::save)
            .map(sedeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SedeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sedes");
        return sedeRepository.findAllBy(pageable).map(sedeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SedeDTO> findByCodigoIndicativo(String codigoIndicativo) {
        return sedeRepository.fundByCodigoIndicativo(codigoIndicativo).map(sedeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return sedeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SedeDTO> findOne(Long id) {
        log.debug("Request to get Sede : {}", id);
        return sedeRepository.findById(id).map(sedeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Sede : {}", id);
        return sedeRepository.deleteById(id);
    }
}
