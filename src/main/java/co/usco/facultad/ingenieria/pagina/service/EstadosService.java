package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.EstadosDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Estados}.
 */
public interface EstadosService {
    /**
     * Save a estados.
     *
     * @param estadosDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EstadosDTO> save(EstadosDTO estadosDTO);

    /**
     * Updates a estados.
     *
     * @param estadosDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EstadosDTO> update(EstadosDTO estadosDTO);

    /**
     * Partially updates a estados.
     *
     * @param estadosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EstadosDTO> partialUpdate(EstadosDTO estadosDTO);

    /**
     * Get all the estados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EstadosDTO> findAll(Pageable pageable);

    /**
     * Get all the estados with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EstadosDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of estados available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" estados.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EstadosDTO> findOne(Long id);

    /**
     * Delete the "id" estados.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
