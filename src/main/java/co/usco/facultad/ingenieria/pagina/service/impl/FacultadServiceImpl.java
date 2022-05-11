package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.repository.FacultadRepository;
import co.usco.facultad.ingenieria.pagina.service.FacultadService;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.FacultadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Facultad}.
 */
@Service
@Transactional
public class FacultadServiceImpl implements FacultadService {

    private final Logger log = LoggerFactory.getLogger(FacultadServiceImpl.class);

    private final FacultadRepository facultadRepository;

    private final FacultadMapper facultadMapper;

    public FacultadServiceImpl(FacultadRepository facultadRepository, FacultadMapper facultadMapper) {
        this.facultadRepository = facultadRepository;
        this.facultadMapper = facultadMapper;
    }

    @Override
    public Mono<FacultadDTO> save(FacultadDTO facultadDTO) {
        log.debug("Request to save Facultad : {}", facultadDTO);
        return facultadRepository.save(facultadMapper.toEntity(facultadDTO)).map(facultadMapper::toDto);
    }

    @Override
    public Mono<FacultadDTO> update(FacultadDTO facultadDTO) {
        log.debug("Request to save Facultad : {}", facultadDTO);
        return facultadRepository.save(facultadMapper.toEntity(facultadDTO)).map(facultadMapper::toDto);
    }

    @Override
    public Mono<FacultadDTO> partialUpdate(FacultadDTO facultadDTO) {
        log.debug("Request to partially update Facultad : {}", facultadDTO);

        return facultadRepository
            .findById(facultadDTO.getId())
            .map(existingFacultad -> {
                facultadMapper.partialUpdate(existingFacultad, facultadDTO);

                return existingFacultad;
            })
            .flatMap(facultadRepository::save)
            .map(facultadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FacultadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facultads");
        return facultadRepository.findAllBy(pageable).map(facultadMapper::toDto);
    }

    public Mono<Long> countAll() {
        return facultadRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FacultadDTO> findOne(Long id) {
        log.debug("Request to get Facultad : {}", id);
        return facultadRepository.findById(id).map(facultadMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Facultad : {}", id);
        return facultadRepository.deleteById(id);
    }
}
