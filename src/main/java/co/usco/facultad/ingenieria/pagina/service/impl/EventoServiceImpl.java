package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.domain.Evento;
import co.usco.facultad.ingenieria.pagina.repository.EventoRepository;
import co.usco.facultad.ingenieria.pagina.service.EventoService;
import co.usco.facultad.ingenieria.pagina.service.GoogleCloudStorageService;
import co.usco.facultad.ingenieria.pagina.service.dto.EventoDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.EventoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service Implementation for managing {@link Evento}.
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private final Logger log = LoggerFactory.getLogger(EventoServiceImpl.class);

    private final GoogleCloudStorageService googleCloudStorageService;

    private final EventoRepository eventoRepository;

    private final EventoMapper eventoMapper;

    public EventoServiceImpl(GoogleCloudStorageService googleCloudStorageService, EventoRepository eventoRepository, EventoMapper eventoMapper) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
    }

    @Override
    public Mono<EventoDTO> save(EventoDTO eventoDTO) {
        log.debug("Request to save Evento : {}", eventoDTO);
        return eventoRepository.save(eventoMapper.toEntity(eventoDTO)).map(eventoMapper::toDto);
    }

    @Override
    public Mono<EventoDTO> update(EventoDTO eventoDTO) {
        log.debug("Request to save Evento : {}", eventoDTO);
        return eventoRepository.save(eventoMapper.toEntity(eventoDTO)).map(eventoMapper::toDto);
    }

    @Override
    public Mono<EventoDTO> partialUpdate(EventoDTO eventoDTO) {
        log.debug("Request to partially update Evento : {}", eventoDTO);

        return eventoRepository
            .findById(eventoDTO.getId())
            .map(existingEvento -> {
                eventoMapper.partialUpdate(existingEvento, eventoDTO);

                return existingEvento;
            })
            .flatMap(eventoRepository::save)
            .map(eventoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EventoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Eventos");
        return eventoRepository.findAllBy(pageable).map(eventoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EventoDTO> findAllFechaMayorQue(Pageable pageable, Instant fechaInicial) {
        return eventoRepository.findAllFechaMayorQue(pageable, fechaInicial).map(eventoMapper::toDto);
    }

    public Flux<EventoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return eventoRepository.findAllWithEagerRelationships(pageable).map(eventoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return eventoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EventoDTO> findOne(Long id) {
        log.debug("Request to get Evento : {}", id);
        return eventoRepository.findOneWithEagerRelationships(id).map(eventoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Evento : {}", id);
        //return eventoRepository.deleteById(id);
        return findOne(id)
            .flatMap(eventoDTO -> {
                if (eventoDTO.getUrlFoto() != null && !eventoDTO.getUrlFoto().isBlank()) {
                    googleCloudStorageService.deleteFileOfStorage(eventoDTO.getUrlFoto())
                        .doOnSuccess(aBoolean -> log.debug(">>>>>> File Object delete"))
                        .subscribe(aBoolean -> log.info(">>>>>>>>>>>>>>>>> File delete: {}", eventoDTO.getUrlFoto()));
                }
                return eventoRepository.deleteById(id);
            })
            .then();
    }
}
