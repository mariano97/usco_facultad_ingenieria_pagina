package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.SemilleroDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Semillero}.
 */
public interface SemilleroService {
    /**
     * Save a semillero.
     *
     * @param semilleroDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SemilleroDTO> save(SemilleroDTO semilleroDTO);

    /**
     * Updates a semillero.
     *
     * @param semilleroDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SemilleroDTO> update(SemilleroDTO semilleroDTO);

    /**
     * Partially updates a semillero.
     *
     * @param semilleroDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SemilleroDTO> partialUpdate(SemilleroDTO semilleroDTO);

    /**
     * Get all the semilleros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SemilleroDTO> findAll(Pageable pageable);

    /**
     * Get all the semilleros with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SemilleroDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of semilleros available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" semillero.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SemilleroDTO> findOne(Long id);

    /**
     * Delete the "id" semillero.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
