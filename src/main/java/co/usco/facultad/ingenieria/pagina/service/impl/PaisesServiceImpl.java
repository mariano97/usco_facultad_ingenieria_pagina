package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Paises;
import co.usco.facultad.ingenieria.pagina.repository.PaisesRepository;
import co.usco.facultad.ingenieria.pagina.service.PaisesService;
import co.usco.facultad.ingenieria.pagina.service.dto.PaisesDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.PaisesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Paises}.
 */
@Service
@Transactional
public class PaisesServiceImpl implements PaisesService {

    private final Logger log = LoggerFactory.getLogger(PaisesServiceImpl.class);

    private final PaisesRepository paisesRepository;

    private final PaisesMapper paisesMapper;

    public PaisesServiceImpl(PaisesRepository paisesRepository, PaisesMapper paisesMapper) {
        this.paisesRepository = paisesRepository;
        this.paisesMapper = paisesMapper;
    }

    @Override
    public Mono<PaisesDTO> save(PaisesDTO paisesDTO) {
        log.debug("Request to save Paises : {}", paisesDTO);
        return paisesRepository.save(paisesMapper.toEntity(paisesDTO)).map(paisesMapper::toDto);
    }

    @Override
    public Mono<PaisesDTO> update(PaisesDTO paisesDTO) {
        log.debug("Request to save Paises : {}", paisesDTO);
        return paisesRepository.save(paisesMapper.toEntity(paisesDTO)).map(paisesMapper::toDto);
    }

    @Override
    public Mono<PaisesDTO> partialUpdate(PaisesDTO paisesDTO) {
        log.debug("Request to partially update Paises : {}", paisesDTO);

        return paisesRepository
            .findById(paisesDTO.getId())
            .map(existingPaises -> {
                paisesMapper.partialUpdate(existingPaises, paisesDTO);

                return existingPaises;
            })
            .flatMap(paisesRepository::save)
            .map(paisesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaisesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Paises");
        return paisesRepository.findAllBy(pageable).map(paisesMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paisesRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PaisesDTO> findOne(Long id) {
        log.debug("Request to get Paises : {}", id);
        return paisesRepository.findById(id).map(paisesMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Paises : {}", id);
        return paisesRepository.deleteById(id);
    }
}
