package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.SedeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Sede}.
 */
public interface SedeService {
    /**
     * Save a sede.
     *
     * @param sedeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SedeDTO> save(SedeDTO sedeDTO);

    /**
     * Updates a sede.
     *
     * @param sedeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SedeDTO> update(SedeDTO sedeDTO);

    /**
     * Partially updates a sede.
     *
     * @param sedeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SedeDTO> partialUpdate(SedeDTO sedeDTO);

    /**
     * Get all the sedes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SedeDTO> findAll(Pageable pageable);

    Mono<SedeDTO> findByCodigoIndicativo(String codigoIndicativo);

    /**
     * Get all the sedes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SedeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of sedes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" sede.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SedeDTO> findOne(Long id);

    /**
     * Delete the "id" sede.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
