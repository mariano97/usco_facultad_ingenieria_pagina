package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.TablaElementoCatalogoRepository;
import co.usco.facultad.ingenieria.pagina.service.TablaElementoCatalogoService;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaTiposCatalogoDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.TablaElementoCatalogoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TablaElementoCatalogo}.
 */
@Service
@Transactional
public class TablaElementoCatalogoServiceImpl implements TablaElementoCatalogoService {

    private final Logger log = LoggerFactory.getLogger(TablaElementoCatalogoServiceImpl.class);

    private final TablaElementoCatalogoRepository tablaElementoCatalogoRepository;

    private final TablaElementoCatalogoMapper tablaElementoCatalogoMapper;

    public TablaElementoCatalogoServiceImpl(
        TablaElementoCatalogoRepository tablaElementoCatalogoRepository,
        TablaElementoCatalogoMapper tablaElementoCatalogoMapper
    ) {
        this.tablaElementoCatalogoRepository = tablaElementoCatalogoRepository;
        this.tablaElementoCatalogoMapper = tablaElementoCatalogoMapper;
    }

    @Override
    public Mono<TablaElementoCatalogoDTO> save(TablaElementoCatalogoDTO tablaElementoCatalogoDTO) {
        log.debug("Request to save TablaElementoCatalogo : {}", tablaElementoCatalogoDTO);
        return tablaElementoCatalogoRepository
            .save(tablaElementoCatalogoMapper.toEntity(tablaElementoCatalogoDTO))
            .map(tablaElementoCatalogoMapper::toDto);
    }

    @Override
    public Mono<TablaElementoCatalogoDTO> update(TablaElementoCatalogoDTO tablaElementoCatalogoDTO) {
        log.debug("Request to save TablaElementoCatalogo : {}", tablaElementoCatalogoDTO);
        return tablaElementoCatalogoRepository
            .save(tablaElementoCatalogoMapper.toEntity(tablaElementoCatalogoDTO))
            .map(tablaElementoCatalogoMapper::toDto);
    }

    @Override
    public Mono<TablaElementoCatalogoDTO> partialUpdate(TablaElementoCatalogoDTO tablaElementoCatalogoDTO) {
        log.debug("Request to partially update TablaElementoCatalogo : {}", tablaElementoCatalogoDTO);

        return tablaElementoCatalogoRepository
            .findById(tablaElementoCatalogoDTO.getId())
            .map(existingTablaElementoCatalogo -> {
                tablaElementoCatalogoMapper.partialUpdate(existingTablaElementoCatalogo, tablaElementoCatalogoDTO);

                return existingTablaElementoCatalogo;
            })
            .flatMap(tablaElementoCatalogoRepository::save)
            .map(tablaElementoCatalogoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TablaElementoCatalogoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TablaElementoCatalogos");
        return tablaElementoCatalogoRepository.findAllBy(pageable).map(tablaElementoCatalogoMapper::toDto);
    }

    public Flux<TablaElementoCatalogoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tablaElementoCatalogoRepository.findAllWithEagerRelationships(pageable).map(tablaElementoCatalogoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TablaElementoCatalogoDTO> findByTiposCatalogoKeyIdentificador(String keyIdentificador) {
        log.debug("dentro de servicios: {}", keyIdentificador);
        Flux<TablaElementoCatalogoDTO> tablacatalofo = tablaElementoCatalogoRepository.findByTablaTiposCatalogoKeyIdentificador(keyIdentificador)
            .map(tablaElementoCatalogoMapper::toDto);
        tablacatalofo.subscribe(data -> {
            log.debug("data: {}", data.toString());
        });
        return tablacatalofo;
    }

    public Mono<Long> countAll() {
        return tablaElementoCatalogoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TablaElementoCatalogoDTO> findOne(Long id) {
        log.debug("Request to get TablaElementoCatalogo : {}", id);
        return tablaElementoCatalogoRepository.findOneWithEagerRelationships(id).map(tablaElementoCatalogoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TablaElementoCatalogo : {}", id);
        return tablaElementoCatalogoRepository.deleteById(id);
    }
}
