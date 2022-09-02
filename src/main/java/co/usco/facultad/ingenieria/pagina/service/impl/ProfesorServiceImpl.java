package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.repository.ProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.*;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.ProfesorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Profesor}.
 */
@Service
@Transactional
public class ProfesorServiceImpl implements ProfesorService {

    private final Logger log = LoggerFactory.getLogger(ProfesorServiceImpl.class);

    private final UserService userService;

    private final GoogleCloudStorageService googleCloudStorageService;

    private final EscalafonProfesorService escalafonProfesorService;

    private final TituloAcademicoProfesorService tituloAcademicoProfesorService;

    private final ProfesorRepository profesorRepository;

    private final ProfesorMapper profesorMapper;

    public ProfesorServiceImpl(UserService userService, GoogleCloudStorageService googleCloudStorageService, EscalafonProfesorService escalafonProfesorService, TituloAcademicoProfesorService tituloAcademicoProfesorService, ProfesorRepository profesorRepository, ProfesorMapper profesorMapper) {
        this.userService = userService;
        this.googleCloudStorageService = googleCloudStorageService;
        this.escalafonProfesorService = escalafonProfesorService;
        this.tituloAcademicoProfesorService = tituloAcademicoProfesorService;
        this.profesorRepository = profesorRepository;
        this.profesorMapper = profesorMapper;
    }

    @Override
    public Mono<ProfesorDTO> save(ProfesorDTO profesorDTO) {
        log.debug("Request to save Profesor : {}", profesorDTO);
        return profesorRepository.save(profesorMapper.toEntity(profesorDTO)).map(profesorMapper::toDto);
    }

    @Override
    public Mono<ProfesorDTO> update(ProfesorDTO profesorDTO) {
        log.debug("Request to save Profesor : {}", profesorDTO);
        return profesorRepository.save(profesorMapper.toEntity(profesorDTO)).map(profesorMapper::toDto);
    }

    @Override
    public Mono<ProfesorDTO> partialUpdate(ProfesorDTO profesorDTO) {
        log.debug("Request to partially update Profesor : {}", profesorDTO);

        return profesorRepository
            .findById(profesorDTO.getId())
            .map(existingProfesor -> {
                profesorMapper.partialUpdate(existingProfesor, profesorDTO);

                return existingProfesor;
            })
            .flatMap(profesorRepository::save)
            .map(profesorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProfesorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Profesors");
        return profesorRepository.findAllBy(pageable).map(profesorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProfesorDTO> findAllByProgramaCodigoSnies(Pageable pageable, Long codigoSnies) {
        return profesorRepository.findAllByProgramaCodigoSnies(pageable,codigoSnies).map(profesorMapper::toDto);
    }

    public Flux<ProfesorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return profesorRepository.findAllWithEagerRelationships(pageable).map(profesorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return profesorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Long> countProfesorByProgramaCodigoSnies(Long codigoSnies) {
        return profesorRepository.countWithProgramaCodigoSnies(codigoSnies);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProfesorDTO> findOne(Long id) {
        log.debug("Request to get Profesor : {}", id);
        return profesorRepository.findOneWithEagerRelationships(id).map(profesorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProfesorDTO> findOneByUserId(Long userId) {
        return profesorRepository.findOneByUserId(userId).map(profesorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Profesor : {}", id);

        return escalafonProfesorService.deleteAllByProfesorId(id).then()
            .then(tituloAcademicoProfesorService.deleteAllByProfesorId(id)).then()
            .then(profesorRepository.deleteProfesorCursoMateriaByProfesorId(id)).then()
            .then(profesorRepository.deleteProfesorProgramaByProfesorId(id)).then()
            .then(findOne(id))
            .flatMap(profesorDTO -> {
                if (profesorDTO.getUrlFotoProfesor() != null && !profesorDTO.getUrlFotoProfesor().isBlank()) {
                    googleCloudStorageService.deleteFileOfStorage(profesorDTO.getUrlFotoProfesor())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete"))
                        .subscribe(aBoolean -> log.info(">>>>>>>>>>>>>>>>> File delete: {}", profesorDTO.getUrlFotoProfesor()));
                }
                return userService.deleteUserById(profesorDTO.getUserId());
            })
            .then()
            .then(profesorRepository.deleteById(id)).then();
    }
}
