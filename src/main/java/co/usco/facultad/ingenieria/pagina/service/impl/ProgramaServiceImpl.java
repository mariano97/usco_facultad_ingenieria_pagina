package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.repository.ProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.ProgramaService;
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

    private final ProgramaRepository programaRepository;

    private final ProgramaMapper programaMapper;

    public ProgramaServiceImpl(ProgramaRepository programaRepository, ProgramaMapper programaMapper) {
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
        return programaRepository.deleteById(id);
    }
}
