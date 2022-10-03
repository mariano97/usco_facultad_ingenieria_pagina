package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.repository.ProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.ArchivosProgramaService;
import co.usco.facultad.ingenieria.pagina.service.GoogleCloudStorageService;
import co.usco.facultad.ingenieria.pagina.service.ProgramaService;
import co.usco.facultad.ingenieria.pagina.service.RedesProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.ProgramaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Programa}.
 */
@Service
@Transactional
public class ProgramaServiceImpl implements ProgramaService {

    private final Logger log = LoggerFactory.getLogger(ProgramaServiceImpl.class);

    private final GoogleCloudStorageService googleCloudStorageService;

    private final ArchivosProgramaService archivosProgramaService;

    private final RedesProgramaService redesProgramaService;

    private final ProgramaRepository programaRepository;

    private final ProgramaMapper programaMapper;

    public ProgramaServiceImpl(GoogleCloudStorageService googleCloudStorageService, ArchivosProgramaService archivosProgramaService, RedesProgramaService redesProgramaService, ProgramaRepository programaRepository, ProgramaMapper programaMapper) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.archivosProgramaService = archivosProgramaService;
        this.redesProgramaService = redesProgramaService;
        this.programaRepository = programaRepository;
        this.programaMapper = programaMapper;
    }

    @Override
    public Mono<ProgramaDTO> save(ProgramaDTO programaDTO) {
        log.debug("Request to save Programa : {}", programaDTO);
        return programaRepository.save(programaMapper.toEntity(programaDTO)).map(programaMapper::toDto);
    }

    @Override
    public Mono<ProgramaDTO> update(ProgramaDTO programaDTO) {
        log.debug("Request to save Programa : {}", programaDTO);
        return programaRepository.save(programaMapper.toEntity(programaDTO)).map(programaMapper::toDto);
    }

    @Override
    public Mono<ProgramaDTO> partialUpdate(ProgramaDTO programaDTO) {
        log.debug("Request to partially update Programa : {}", programaDTO);

        return programaRepository
            .findById(programaDTO.getId())
            .map(existingPrograma -> {
                programaMapper.partialUpdate(existingPrograma, programaDTO);

                return existingPrograma;
            })
            .flatMap(programaRepository::save)
            .map(programaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProgramaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Programas");
        return programaRepository.findAllBy(pageable).map(programaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProgramaDTO> findByCodigoSnies(Long codigoSnies) {
        log.debug("Request to get by codigoSnies: {}", codigoSnies);
        return programaRepository.findByCodigoSnies(codigoSnies).map(programaMapper::toDto);
    }

    @Override
    public Mono<String> findNameProgramaByCodigoSnies(Long codigoSnies) {
        return findByCodigoSnies(codigoSnies).map(ProgramaDTO::getNombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProgramaDTO> findAllByCursoMateriaId(Long cursoMateriaId) {
        return programaRepository.findAllByCursoMateriaId(cursoMateriaId).map(programaMapper::toDto);
    }

    public Flux<ProgramaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return programaRepository.findAllWithEagerRelationships(pageable).map(programaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return programaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProgramaDTO> findOne(Long id) {
        log.debug("Request to get Programa : {}", id);
        return programaRepository.findOneWithEagerRelationships(id).map(programaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Programa : {}", id);
        // return programaRepository.deleteById(id);
        return archivosProgramaService.deleteAllByProgramaId(id).then()
            .then(redesProgramaService.deleteAllByProgramaId(id)).then()
            .then(programaRepository.deleteProgramaProfesorByProgramaId(id)).then()
            .then(programaRepository.deleteProgramaSedesByProgramaId(id)).then()
            .then(findOne(id))
            .flatMap(programaDTO -> {
                if (programaDTO.getUrlFotoPrograma() != null && !programaDTO.getUrlFotoPrograma().isBlank()) {
                    googleCloudStorageService.deleteFileOfStorage(programaDTO.getUrlFotoPrograma())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete"))
                        .subscribe(aBoolean -> log.info(">>>>>>>>>>>>>>>>> File delete: {}", programaDTO.getUrlFotoPrograma()));
                }
                return programaRepository.deleteById(id);
            }).then();
        /* return archivosProgramaService.deleteAllByProgramaId(id).then()
            .then(redesProgramaService.deleteAllByProgramaId(id)).then()
            .then(programaRepository.deleteProgramaProfesorByProgramaId(id)).then()
            .then(programaRepository.deleteProgramaSedesByProgramaId(id)).then()
            .then(programaRepository.deleteById(id)).then(); */
    }
}
