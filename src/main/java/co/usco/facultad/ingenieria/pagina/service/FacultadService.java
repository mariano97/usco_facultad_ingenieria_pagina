package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Facultad}.
 */
public interface FacultadService {
    /**
     * Save a facultad.
     *
     * @param facultadDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FacultadDTO> save(FacultadDTO facultadDTO);

    /**
     * Updates a facultad.
     *
     * @param facultadDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FacultadDTO> update(FacultadDTO facultadDTO);

    /**
     * Partially updates a facultad.
     *
     * @param facultadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FacultadDTO> partialUpdate(FacultadDTO facultadDTO);

    /**
     * Get all the facultads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FacultadDTO> findAll(Pageable pageable);

    /**
     * Returns the number of facultads available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" facultad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FacultadDTO> findOne(Long id);

    /**
     * Delete the "id" facultad.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
