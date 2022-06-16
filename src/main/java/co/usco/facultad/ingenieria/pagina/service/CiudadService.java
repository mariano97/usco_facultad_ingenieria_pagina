package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.CiudadDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Ciudad}.
 */
public interface CiudadService {
    /**
     * Save a ciudad.
     *
     * @param ciudadDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CiudadDTO> save(CiudadDTO ciudadDTO);

    /**
     * Updates a ciudad.
     *
     * @param ciudadDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CiudadDTO> update(CiudadDTO ciudadDTO);

    /**
     * Partially updates a ciudad.
     *
     * @param ciudadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CiudadDTO> partialUpdate(CiudadDTO ciudadDTO);

    /**
     * Get all the ciudads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CiudadDTO> findAll(Pageable pageable);

    Flux<CiudadDTO> findAllByEstadoId(Long estadoId);

    /**
     * Get all the ciudads with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CiudadDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of ciudads available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" ciudad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CiudadDTO> findOne(Long id);

    /**
     * Delete the "id" ciudad.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
