package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Profesor}.
 */
public interface ProfesorService {
    /**
     * Save a profesor.
     *
     * @param profesorDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProfesorDTO> save(ProfesorDTO profesorDTO);

    /**
     * Updates a profesor.
     *
     * @param profesorDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProfesorDTO> update(ProfesorDTO profesorDTO);

    /**
     * Partially updates a profesor.
     *
     * @param profesorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProfesorDTO> partialUpdate(ProfesorDTO profesorDTO);

    /**
     * Get all the profesors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProfesorDTO> findAll(Pageable pageable);

    /**
     * Get all the profesors with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProfesorDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of profesors available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" profesor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProfesorDTO> findOne(Long id);

    Mono<ProfesorDTO> findOneByUserId(Long userId);

    /**
     * Delete the "id" profesor.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
