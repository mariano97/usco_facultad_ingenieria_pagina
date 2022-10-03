package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma;
import co.usco.facultad.ingenieria.pagina.repository.ArchivosProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.ArchivosProgramaService;
import co.usco.facultad.ingenieria.pagina.service.GoogleCloudStorageService;
import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.ArchivosProgramaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ArchivosPrograma}.
 */
@Service
@Transactional
public class ArchivosProgramaServiceImpl implements ArchivosProgramaService {

    private final Logger log = LoggerFactory.getLogger(ArchivosProgramaServiceImpl.class);

    private final GoogleCloudStorageService googleCloudStorageService;

    private final ArchivosProgramaRepository archivosProgramaRepository;

    private final ArchivosProgramaMapper archivosProgramaMapper;

    public ArchivosProgramaServiceImpl(
        GoogleCloudStorageService googleCloudStorageService, ArchivosProgramaRepository archivosProgramaRepository,
        ArchivosProgramaMapper archivosProgramaMapper
    ) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.archivosProgramaRepository = archivosProgramaRepository;
        this.archivosProgramaMapper = archivosProgramaMapper;
    }

    @Override
    public Mono<ArchivosProgramaDTO> save(ArchivosProgramaDTO archivosProgramaDTO) {
        log.debug("Request to save ArchivosPrograma : {}", archivosProgramaDTO);
        return archivosProgramaRepository.save(archivosProgramaMapper.toEntity(archivosProgramaDTO)).map(archivosProgramaMapper::toDto);
    }

    @Override
    public Mono<ArchivosProgramaDTO> update(ArchivosProgramaDTO archivosProgramaDTO) {
        log.debug("Request to save ArchivosPrograma : {}", archivosProgramaDTO);
        return archivosProgramaRepository.save(archivosProgramaMapper.toEntity(archivosProgramaDTO)).map(archivosProgramaMapper::toDto);
    }

    @Override
    public Mono<ArchivosProgramaDTO> partialUpdate(ArchivosProgramaDTO archivosProgramaDTO) {
        log.debug("Request to partially update ArchivosPrograma : {}", archivosProgramaDTO);

        return archivosProgramaRepository
            .findById(archivosProgramaDTO.getId())
            .map(existingArchivosPrograma -> {
                archivosProgramaMapper.partialUpdate(existingArchivosPrograma, archivosProgramaDTO);

                return existingArchivosPrograma;
            })
            .flatMap(archivosProgramaRepository::save)
            .map(archivosProgramaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ArchivosProgramaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArchivosProgramas");
        return archivosProgramaRepository.findAllBy(pageable).map(archivosProgramaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ArchivosProgramaDTO> findAllByPrograma(Long programaId) {
        return archivosProgramaRepository.findByProgramaId(programaId).map(archivosProgramaMapper::toDto);
    }

    public Flux<ArchivosProgramaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return archivosProgramaRepository.findAllWithEagerRelationships(pageable).map(archivosProgramaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return archivosProgramaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ArchivosProgramaDTO> findOne(Long id) {
        log.debug("Request to get ArchivosPrograma : {}", id);
        return archivosProgramaRepository.findOneWithEagerRelationships(id).map(archivosProgramaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ArchivosPrograma : {}", id);
        return archivosProgramaRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAllByProgramaId(Long programaId) {
        return findAllByPrograma(programaId)
            .flatMap(archivosProgramaDTO -> {
                if (archivosProgramaDTO.getUrlName() != null && !archivosProgramaDTO.getUrlName().isBlank()) {
                    googleCloudStorageService.deleteFileOfStorage(archivosProgramaDTO.getUrlName())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete"))
                        .subscribe(aBoolean -> log.info(">>>>>>>>>>>>>>>>> File delete: {}", archivosProgramaDTO.getUrlName()));
                }
                return delete(archivosProgramaDTO.getId());
            })
            .then();
        /*return findAllByPrograma(programaId)
            .flatMap(archivosProgramaDTO -> delete(archivosProgramaDTO.getId()))
            .then();*/
    }
}
