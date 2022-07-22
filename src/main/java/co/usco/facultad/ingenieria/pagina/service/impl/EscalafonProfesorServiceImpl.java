package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor;
import co.usco.facultad.ingenieria.pagina.repository.EscalafonProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.EscalafonProfesorService;
import co.usco.facultad.ingenieria.pagina.service.dto.EscalafonProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.EscalafonProfesorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link EscalafonProfesor}.
 */
@Service
@Transactional
public class EscalafonProfesorServiceImpl implements EscalafonProfesorService {

    private final Logger log = LoggerFactory.getLogger(EscalafonProfesorServiceImpl.class);

    private final EscalafonProfesorRepository escalafonProfesorRepository;

    private final EscalafonProfesorMapper escalafonProfesorMapper;

    public EscalafonProfesorServiceImpl(
        EscalafonProfesorRepository escalafonProfesorRepository,
        EscalafonProfesorMapper escalafonProfesorMapper
    ) {
        this.escalafonProfesorRepository = escalafonProfesorRepository;
        this.escalafonProfesorMapper = escalafonProfesorMapper;
    }

    @Override
    public Mono<EscalafonProfesorDTO> save(EscalafonProfesorDTO escalafonProfesorDTO) {
        log.debug("Request to save EscalafonProfesor : {}", escalafonProfesorDTO);
        return escalafonProfesorRepository.save(escalafonProfesorMapper.toEntity(escalafonProfesorDTO)).map(escalafonProfesorMapper::toDto);
    }

    @Override
    public Mono<EscalafonProfesorDTO> update(EscalafonProfesorDTO escalafonProfesorDTO) {
        log.debug("Request to save EscalafonProfesor : {}", escalafonProfesorDTO);
        return escalafonProfesorRepository.save(escalafonProfesorMapper.toEntity(escalafonProfesorDTO)).map(escalafonProfesorMapper::toDto);
    }

    @Override
    public Mono<EscalafonProfesorDTO> partialUpdate(EscalafonProfesorDTO escalafonProfesorDTO) {
        log.debug("Request to partially update EscalafonProfesor : {}", escalafonProfesorDTO);

        return escalafonProfesorRepository
            .findById(escalafonProfesorDTO.getId())
            .map(existingEscalafonProfesor -> {
                escalafonProfesorMapper.partialUpdate(existingEscalafonProfesor, escalafonProfesorDTO);

                return existingEscalafonProfesor;
            })
            .flatMap(escalafonProfesorRepository::save)
            .map(escalafonProfesorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EscalafonProfesorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EscalafonProfesors");
        return escalafonProfesorRepository.findAllBy(pageable).map(escalafonProfesorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return escalafonProfesorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EscalafonProfesorDTO> findOne(Long id) {
        log.debug("Request to get EscalafonProfesor : {}", id);
        return escalafonProfesorRepository.findById(id).map(escalafonProfesorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete EscalafonProfesor : {}", id);
        return escalafonProfesorRepository.deleteById(id);
    }
}
