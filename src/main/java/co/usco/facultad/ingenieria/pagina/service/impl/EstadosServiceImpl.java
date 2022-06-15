package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Estados;
import co.usco.facultad.ingenieria.pagina.repository.EstadosRepository;
import co.usco.facultad.ingenieria.pagina.service.EstadosService;
import co.usco.facultad.ingenieria.pagina.service.dto.EstadosDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.EstadosMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Estados}.
 */
@Service
@Transactional
public class EstadosServiceImpl implements EstadosService {

    private final Logger log = LoggerFactory.getLogger(EstadosServiceImpl.class);

    private final EstadosRepository estadosRepository;

    private final EstadosMapper estadosMapper;

    public EstadosServiceImpl(EstadosRepository estadosRepository, EstadosMapper estadosMapper) {
        this.estadosRepository = estadosRepository;
        this.estadosMapper = estadosMapper;
    }

    @Override
    public Mono<EstadosDTO> save(EstadosDTO estadosDTO) {
        log.debug("Request to save Estados : {}", estadosDTO);
        return estadosRepository.save(estadosMapper.toEntity(estadosDTO)).map(estadosMapper::toDto);
    }

    @Override
    public Mono<EstadosDTO> update(EstadosDTO estadosDTO) {
        log.debug("Request to save Estados : {}", estadosDTO);
        return estadosRepository.save(estadosMapper.toEntity(estadosDTO)).map(estadosMapper::toDto);
    }

    @Override
    public Mono<EstadosDTO> partialUpdate(EstadosDTO estadosDTO) {
        log.debug("Request to partially update Estados : {}", estadosDTO);

        return estadosRepository
            .findById(estadosDTO.getId())
            .map(existingEstados -> {
                estadosMapper.partialUpdate(existingEstados, estadosDTO);

                return existingEstados;
            })
            .flatMap(estadosRepository::save)
            .map(estadosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EstadosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estados");
        return estadosRepository.findAllBy(pageable).map(estadosMapper::toDto);
    }

    public Flux<EstadosDTO> findAllWithEagerRelationships(Pageable pageable) {
        return estadosRepository.findAllWithEagerRelationships(pageable).map(estadosMapper::toDto);
    }

    public Mono<Long> countAll() {
        return estadosRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EstadosDTO> findOne(Long id) {
        log.debug("Request to get Estados : {}", id);
        return estadosRepository.findOneWithEagerRelationships(id).map(estadosMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Estados : {}", id);
        return estadosRepository.deleteById(id);
    }
}
