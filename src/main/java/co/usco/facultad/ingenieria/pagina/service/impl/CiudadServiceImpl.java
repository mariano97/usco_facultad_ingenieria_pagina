package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import co.usco.facultad.ingenieria.pagina.repository.CiudadRepository;
import co.usco.facultad.ingenieria.pagina.service.CiudadService;
import co.usco.facultad.ingenieria.pagina.service.dto.CiudadDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.CiudadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Ciudad}.
 */
@Service
@Transactional
public class CiudadServiceImpl implements CiudadService {

    private final Logger log = LoggerFactory.getLogger(CiudadServiceImpl.class);

    private final CiudadRepository ciudadRepository;

    private final CiudadMapper ciudadMapper;

    public CiudadServiceImpl(CiudadRepository ciudadRepository, CiudadMapper ciudadMapper) {
        this.ciudadRepository = ciudadRepository;
        this.ciudadMapper = ciudadMapper;
    }

    @Override
    public Mono<CiudadDTO> save(CiudadDTO ciudadDTO) {
        log.debug("Request to save Ciudad : {}", ciudadDTO);
        return ciudadRepository.save(ciudadMapper.toEntity(ciudadDTO)).map(ciudadMapper::toDto);
    }

    @Override
    public Mono<CiudadDTO> update(CiudadDTO ciudadDTO) {
        log.debug("Request to save Ciudad : {}", ciudadDTO);
        return ciudadRepository.save(ciudadMapper.toEntity(ciudadDTO)).map(ciudadMapper::toDto);
    }

    @Override
    public Mono<CiudadDTO> partialUpdate(CiudadDTO ciudadDTO) {
        log.debug("Request to partially update Ciudad : {}", ciudadDTO);

        return ciudadRepository
            .findById(ciudadDTO.getId())
            .map(existingCiudad -> {
                ciudadMapper.partialUpdate(existingCiudad, ciudadDTO);

                return existingCiudad;
            })
            .flatMap(ciudadRepository::save)
            .map(ciudadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CiudadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ciudads");
        return ciudadRepository.findAllBy(pageable).map(ciudadMapper::toDto);
    }

    public Flux<CiudadDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ciudadRepository.findAllWithEagerRelationships(pageable).map(ciudadMapper::toDto);
    }

    public Mono<Long> countAll() {
        return ciudadRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CiudadDTO> findOne(Long id) {
        log.debug("Request to get Ciudad : {}", id);
        return ciudadRepository.findOneWithEagerRelationships(id).map(ciudadMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Ciudad : {}", id);
        return ciudadRepository.deleteById(id);
    }
}
