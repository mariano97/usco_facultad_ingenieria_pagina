package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.EventoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Evento}.
 */
public interface EventoService {
    /**
     * Save a evento.
     *
     * @param eventoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EventoDTO> save(EventoDTO eventoDTO);

    /**
     * Updates a evento.
     *
     * @param eventoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EventoDTO> update(EventoDTO eventoDTO);

    /**
     * Partially updates a evento.
     *
     * @param eventoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EventoDTO> partialUpdate(EventoDTO eventoDTO);

    /**
     * Get all the eventos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EventoDTO> findAll(Pageable pageable);

    Flux<EventoDTO> findAllFechaMayorQue(Pageable pageable, Instant fechaInicial);

    /**
     * Get all the eventos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EventoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of eventos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" evento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EventoDTO> findOne(Long id);

    /**
     * Delete the "id" evento.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
