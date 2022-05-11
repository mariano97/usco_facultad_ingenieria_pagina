package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Programa}.
 */
public interface ProgramaService {
    /**
     * Save a programa.
     *
     * @param programaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProgramaDTO> save(ProgramaDTO programaDTO);

    /**
     * Updates a programa.
     *
     * @param programaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProgramaDTO> update(ProgramaDTO programaDTO);

    /**
     * Partially updates a programa.
     *
     * @param programaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProgramaDTO> partialUpdate(ProgramaDTO programaDTO);

    /**
     * Get all the programas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProgramaDTO> findAll(Pageable pageable);

    /**
     * Get all the programas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProgramaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of programas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" programa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProgramaDTO> findOne(Long id);

    /**
     * Delete the "id" programa.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
