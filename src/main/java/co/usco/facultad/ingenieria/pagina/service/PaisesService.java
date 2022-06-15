package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.PaisesDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Paises}.
 */
public interface PaisesService {
    /**
     * Save a paises.
     *
     * @param paisesDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaisesDTO> save(PaisesDTO paisesDTO);

    /**
     * Updates a paises.
     *
     * @param paisesDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaisesDTO> update(PaisesDTO paisesDTO);

    /**
     * Partially updates a paises.
     *
     * @param paisesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaisesDTO> partialUpdate(PaisesDTO paisesDTO);

    /**
     * Get all the paises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaisesDTO> findAll(Pageable pageable);

    /**
     * Returns the number of paises available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" paises.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaisesDTO> findOne(Long id);

    /**
     * Delete the "id" paises.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
