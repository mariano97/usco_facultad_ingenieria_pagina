package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.RedesPrograma;
import co.usco.facultad.ingenieria.pagina.repository.RedesProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.RedesProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.RedesProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.RedesProgramaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RedesPrograma}.
 */
@Service
@Transactional
public class RedesProgramaServiceImpl implements RedesProgramaService {

    private final Logger log = LoggerFactory.getLogger(RedesProgramaServiceImpl.class);

    private final RedesProgramaRepository redesProgramaRepository;

    private final RedesProgramaMapper redesProgramaMapper;

    public RedesProgramaServiceImpl(RedesProgramaRepository redesProgramaRepository, RedesProgramaMapper redesProgramaMapper) {
        this.redesProgramaRepository = redesProgramaRepository;
        this.redesProgramaMapper = redesProgramaMapper;
    }

    @Override
    public Mono<RedesProgramaDTO> save(RedesProgramaDTO redesProgramaDTO) {
        log.debug("Request to save RedesPrograma : {}", redesProgramaDTO);
        return redesProgramaRepository.save(redesProgramaMapper.toEntity(redesProgramaDTO)).map(redesProgramaMapper::toDto);
    }

    @Override
    public Mono<RedesProgramaDTO> update(RedesProgramaDTO redesProgramaDTO) {
        log.debug("Request to save RedesPrograma : {}", redesProgramaDTO);
        return redesProgramaRepository.save(redesProgramaMapper.toEntity(redesProgramaDTO)).map(redesProgramaMapper::toDto);
    }

    @Override
    public Mono<RedesProgramaDTO> partialUpdate(RedesProgramaDTO redesProgramaDTO) {
        log.debug("Request to partially update RedesPrograma : {}", redesProgramaDTO);

        return redesProgramaRepository
            .findById(redesProgramaDTO.getId())
            .map(existingRedesPrograma -> {
                redesProgramaMapper.partialUpdate(existingRedesPrograma, redesProgramaDTO);

                return existingRedesPrograma;
            })
            .flatMap(redesProgramaRepository::save)
            .map(redesProgramaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<RedesProgramaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RedesProgramas");
        return redesProgramaRepository.findAllBy(pageable).map(redesProgramaMapper::toDto);
    }

    public Flux<RedesProgramaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return redesProgramaRepository.findAllWithEagerRelationships(pageable).map(redesProgramaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return redesProgramaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RedesProgramaDTO> findOne(Long id) {
        log.debug("Request to get RedesPrograma : {}", id);
        return redesProgramaRepository.findOneWithEagerRelationships(id).map(redesProgramaMapper::toDto);
    }

    @Override
    public Flux<RedesProgramaDTO> findAllByProgramaId(Long programaId) {
        return redesProgramaRepository.findAllByProgramaId(programaId).map(redesProgramaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete RedesPrograma : {}", id);
        return redesProgramaRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAllByProgramaId(Long programaId) {
        return findAllByProgramaId(programaId)
            .flatMap(redesProgramaDTO -> delete(redesProgramaDTO.getId()))
            .then();
    }
}
