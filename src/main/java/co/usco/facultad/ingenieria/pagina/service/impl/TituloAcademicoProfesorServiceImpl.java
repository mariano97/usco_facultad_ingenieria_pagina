package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor;
import co.usco.facultad.ingenieria.pagina.repository.TituloAcademicoProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.TituloAcademicoProfesorService;
import co.usco.facultad.ingenieria.pagina.service.dto.TituloAcademicoProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.TituloAcademicoProfesorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TituloAcademicoProfesor}.
 */
@Service
@Transactional
public class TituloAcademicoProfesorServiceImpl implements TituloAcademicoProfesorService {

    private final Logger log = LoggerFactory.getLogger(TituloAcademicoProfesorServiceImpl.class);

    private final TituloAcademicoProfesorRepository tituloAcademicoProfesorRepository;

    private final TituloAcademicoProfesorMapper tituloAcademicoProfesorMapper;

    public TituloAcademicoProfesorServiceImpl(
        TituloAcademicoProfesorRepository tituloAcademicoProfesorRepository,
        TituloAcademicoProfesorMapper tituloAcademicoProfesorMapper
    ) {
        this.tituloAcademicoProfesorRepository = tituloAcademicoProfesorRepository;
        this.tituloAcademicoProfesorMapper = tituloAcademicoProfesorMapper;
    }

    @Override
    public Mono<TituloAcademicoProfesorDTO> save(TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO) {
        log.debug("Request to save TituloAcademicoProfesor : {}", tituloAcademicoProfesorDTO);
        return tituloAcademicoProfesorRepository
            .save(tituloAcademicoProfesorMapper.toEntity(tituloAcademicoProfesorDTO))
            .map(tituloAcademicoProfesorMapper::toDto);
    }

    @Override
    public Mono<TituloAcademicoProfesorDTO> update(TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO) {
        log.debug("Request to save TituloAcademicoProfesor : {}", tituloAcademicoProfesorDTO);
        return tituloAcademicoProfesorRepository
            .save(tituloAcademicoProfesorMapper.toEntity(tituloAcademicoProfesorDTO))
            .map(tituloAcademicoProfesorMapper::toDto);
    }

    @Override
    public Mono<TituloAcademicoProfesorDTO> partialUpdate(TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO) {
        log.debug("Request to partially update TituloAcademicoProfesor : {}", tituloAcademicoProfesorDTO);

        return tituloAcademicoProfesorRepository
            .findById(tituloAcademicoProfesorDTO.getId())
            .map(existingTituloAcademicoProfesor -> {
                tituloAcademicoProfesorMapper.partialUpdate(existingTituloAcademicoProfesor, tituloAcademicoProfesorDTO);

                return existingTituloAcademicoProfesor;
            })
            .flatMap(tituloAcademicoProfesorRepository::save)
            .map(tituloAcademicoProfesorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TituloAcademicoProfesorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TituloAcademicoProfesors");
        return tituloAcademicoProfesorRepository.findAllBy(pageable).map(tituloAcademicoProfesorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TituloAcademicoProfesorDTO> findAllByProfesorId(Long profesorId) {
        return tituloAcademicoProfesorRepository.findAllByProfesorId(profesorId).map(tituloAcademicoProfesorMapper::toDto);
    }

    public Flux<TituloAcademicoProfesorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tituloAcademicoProfesorRepository.findAllWithEagerRelationships(pageable).map(tituloAcademicoProfesorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tituloAcademicoProfesorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TituloAcademicoProfesorDTO> findOne(Long id) {
        log.debug("Request to get TituloAcademicoProfesor : {}", id);
        return tituloAcademicoProfesorRepository.findOneWithEagerRelationships(id).map(tituloAcademicoProfesorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TituloAcademicoProfesor : {}", id);
        return tituloAcademicoProfesorRepository.deleteById(id);
    }
}
