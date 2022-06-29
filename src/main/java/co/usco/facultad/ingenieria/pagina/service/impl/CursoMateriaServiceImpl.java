package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import co.usco.facultad.ingenieria.pagina.repository.CursoMateriaRepository;
import co.usco.facultad.ingenieria.pagina.service.CursoMateriaService;
import co.usco.facultad.ingenieria.pagina.service.dto.CursoMateriaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.CursoMateriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CursoMateria}.
 */
@Service
@Transactional
public class CursoMateriaServiceImpl implements CursoMateriaService {

    private final Logger log = LoggerFactory.getLogger(CursoMateriaServiceImpl.class);

    private final CursoMateriaRepository cursoMateriaRepository;

    private final CursoMateriaMapper cursoMateriaMapper;

    public CursoMateriaServiceImpl(CursoMateriaRepository cursoMateriaRepository, CursoMateriaMapper cursoMateriaMapper) {
        this.cursoMateriaRepository = cursoMateriaRepository;
        this.cursoMateriaMapper = cursoMateriaMapper;
    }

    @Override
    public Mono<CursoMateriaDTO> save(CursoMateriaDTO cursoMateriaDTO) {
        log.debug("Request to save CursoMateria : {}", cursoMateriaDTO);
        return cursoMateriaRepository.save(cursoMateriaMapper.toEntity(cursoMateriaDTO)).map(cursoMateriaMapper::toDto);
    }

    @Override
    public Mono<CursoMateriaDTO> update(CursoMateriaDTO cursoMateriaDTO) {
        log.debug("Request to save CursoMateria : {}", cursoMateriaDTO);
        return cursoMateriaRepository.save(cursoMateriaMapper.toEntity(cursoMateriaDTO)).map(cursoMateriaMapper::toDto);
    }

    @Override
    public Mono<CursoMateriaDTO> partialUpdate(CursoMateriaDTO cursoMateriaDTO) {
        log.debug("Request to partially update CursoMateria : {}", cursoMateriaDTO);

        return cursoMateriaRepository
            .findById(cursoMateriaDTO.getId())
            .map(existingCursoMateria -> {
                cursoMateriaMapper.partialUpdate(existingCursoMateria, cursoMateriaDTO);

                return existingCursoMateria;
            })
            .flatMap(cursoMateriaRepository::save)
            .map(cursoMateriaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CursoMateriaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CursoMaterias");
        return cursoMateriaRepository.findAllBy(pageable).map(cursoMateriaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CursoMateriaDTO> findAllByProfesorIdRelation(Long profesorId) {
        return cursoMateriaRepository.findAllByProfesorRelation(profesorId).map(cursoMateriaMapper::toDto);
    }

    public Flux<CursoMateriaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cursoMateriaRepository.findAllWithEagerRelationships(pageable).map(cursoMateriaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return cursoMateriaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CursoMateriaDTO> findOne(Long id) {
        log.debug("Request to get CursoMateria : {}", id);
        return cursoMateriaRepository.findOneWithEagerRelationships(id).map(cursoMateriaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CursoMateria : {}", id);
        return cursoMateriaRepository.deleteById(id);
    }
}
