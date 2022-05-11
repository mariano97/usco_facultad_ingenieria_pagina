package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.TablaTiposCatalogoRepository;
import co.usco.facultad.ingenieria.pagina.service.TablaTiposCatalogoService;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaTiposCatalogoDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.TablaTiposCatalogoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TablaTiposCatalogo}.
 */
@Service
@Transactional
public class TablaTiposCatalogoServiceImpl implements TablaTiposCatalogoService {

    private final Logger log = LoggerFactory.getLogger(TablaTiposCatalogoServiceImpl.class);

    private final TablaTiposCatalogoRepository tablaTiposCatalogoRepository;

    private final TablaTiposCatalogoMapper tablaTiposCatalogoMapper;

    public TablaTiposCatalogoServiceImpl(
        TablaTiposCatalogoRepository tablaTiposCatalogoRepository,
        TablaTiposCatalogoMapper tablaTiposCatalogoMapper
    ) {
        this.tablaTiposCatalogoRepository = tablaTiposCatalogoRepository;
        this.tablaTiposCatalogoMapper = tablaTiposCatalogoMapper;
    }

    @Override
    public Mono<TablaTiposCatalogoDTO> save(TablaTiposCatalogoDTO tablaTiposCatalogoDTO) {
        log.debug("Request to save TablaTiposCatalogo : {}", tablaTiposCatalogoDTO);
        return tablaTiposCatalogoRepository
            .save(tablaTiposCatalogoMapper.toEntity(tablaTiposCatalogoDTO))
            .map(tablaTiposCatalogoMapper::toDto);
    }

    @Override
    public Mono<TablaTiposCatalogoDTO> update(TablaTiposCatalogoDTO tablaTiposCatalogoDTO) {
        log.debug("Request to save TablaTiposCatalogo : {}", tablaTiposCatalogoDTO);
        return tablaTiposCatalogoRepository
            .save(tablaTiposCatalogoMapper.toEntity(tablaTiposCatalogoDTO))
            .map(tablaTiposCatalogoMapper::toDto);
    }

    @Override
    public Mono<TablaTiposCatalogoDTO> partialUpdate(TablaTiposCatalogoDTO tablaTiposCatalogoDTO) {
        log.debug("Request to partially update TablaTiposCatalogo : {}", tablaTiposCatalogoDTO);

        return tablaTiposCatalogoRepository
            .findById(tablaTiposCatalogoDTO.getId())
            .map(existingTablaTiposCatalogo -> {
                tablaTiposCatalogoMapper.partialUpdate(existingTablaTiposCatalogo, tablaTiposCatalogoDTO);

                return existingTablaTiposCatalogo;
            })
            .flatMap(tablaTiposCatalogoRepository::save)
            .map(tablaTiposCatalogoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TablaTiposCatalogoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TablaTiposCatalogos");
        return tablaTiposCatalogoRepository.findAllBy(pageable).map(tablaTiposCatalogoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tablaTiposCatalogoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TablaTiposCatalogoDTO> findOne(Long id) {
        log.debug("Request to get TablaTiposCatalogo : {}", id);
        return tablaTiposCatalogoRepository.findById(id).map(tablaTiposCatalogoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TablaTiposCatalogo : {}", id);
        return tablaTiposCatalogoRepository.deleteById(id);
    }
}
