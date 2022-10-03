package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Noticia;
import co.usco.facultad.ingenieria.pagina.repository.NoticiaRepository;
import co.usco.facultad.ingenieria.pagina.service.GoogleCloudStorageService;
import co.usco.facultad.ingenieria.pagina.service.NoticiaService;
import co.usco.facultad.ingenieria.pagina.service.dto.NoticiaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.NoticiaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service Implementation for managing {@link Noticia}.
 */
@Service
@Transactional
public class NoticiaServiceImpl implements NoticiaService {

    private final Logger log = LoggerFactory.getLogger(NoticiaServiceImpl.class);

    private final GoogleCloudStorageService googleCloudStorageService;

    private final NoticiaRepository noticiaRepository;

    private final NoticiaMapper noticiaMapper;

    public NoticiaServiceImpl(GoogleCloudStorageService googleCloudStorageService, NoticiaRepository noticiaRepository, NoticiaMapper noticiaMapper) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.noticiaRepository = noticiaRepository;
        this.noticiaMapper = noticiaMapper;
    }

    @Override
    public Mono<NoticiaDTO> save(NoticiaDTO noticiaDTO) {
        log.debug("Request to save Noticia : {}", noticiaDTO);
        return noticiaRepository.save(noticiaMapper.toEntity(noticiaDTO)).map(noticiaMapper::toDto);
    }

    @Override
    public Mono<NoticiaDTO> update(NoticiaDTO noticiaDTO) {
        log.debug("Request to save Noticia : {}", noticiaDTO);
        return noticiaRepository.save(noticiaMapper.toEntity(noticiaDTO)).map(noticiaMapper::toDto);
    }

    @Override
    public Mono<NoticiaDTO> partialUpdate(NoticiaDTO noticiaDTO) {
        log.debug("Request to partially update Noticia : {}", noticiaDTO);

        return noticiaRepository
            .findById(noticiaDTO.getId())
            .map(existingNoticia -> {
                noticiaMapper.partialUpdate(existingNoticia, noticiaDTO);

                return existingNoticia;
            })
            .flatMap(noticiaRepository::save)
            .map(noticiaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<NoticiaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Noticias");
        return noticiaRepository.findAllBy(pageable).map(noticiaMapper::toDto);
    }

    @Override
    public Flux<NoticiaDTO> findAllFechaMayorQue(Pageable pageable, Instant fechaInicial) {
        return noticiaRepository.findAllFechaMayorQue(pageable, fechaInicial).map(noticiaMapper::toDto);
    }

    public Flux<NoticiaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return noticiaRepository.findAllWithEagerRelationships(pageable).map(noticiaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return noticiaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<NoticiaDTO> findOne(Long id) {
        log.debug("Request to get Noticia : {}", id);
        return noticiaRepository.findOneWithEagerRelationships(id).map(noticiaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Noticia : {}", id);
        // return noticiaRepository.deleteById(id);
        return findOne(id)
            .flatMap(noticiaDTO -> {
                if (noticiaDTO.getUrlFoto() != null && !noticiaDTO.getUrlFoto().isBlank()) {
                    googleCloudStorageService.deleteFileOfStorage(noticiaDTO.getUrlFoto())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete"))
                        .subscribe(aBoolean -> log.info(">>>>>>>>>>>>>>>>> File delete: {}", noticiaDTO.getUrlFoto()));
                }
                return noticiaRepository.deleteById(id);
            })
            .then();
    }
}
