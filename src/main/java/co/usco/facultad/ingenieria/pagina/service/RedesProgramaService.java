package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.RedesProgramaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.RedesPrograma}.
 */
public interface RedesProgramaService {
    /**
     * Save a redesPrograma.
     *
     * @param redesProgramaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RedesProgramaDTO> save(RedesProgramaDTO redesProgramaDTO);

    /**
     * Updates a redesPrograma.
     *
     * @param redesProgramaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RedesProgramaDTO> update(RedesProgramaDTO redesProgramaDTO);

    /**
     * Partially updates a redesPrograma.
     *
     * @param redesProgramaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RedesProgramaDTO> partialUpdate(RedesProgramaDTO redesProgramaDTO);

    /**
     * Get all the redesProgramas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RedesProgramaDTO> findAll(Pageable pageable);

    /**
     * Get all the redesProgramas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RedesProgramaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of redesProgramas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" redesPrograma.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RedesProgramaDTO> findOne(Long id);

    Flux<RedesProgramaDTO> findAllByProgramaId(Long programaId);

    /**
     * Delete the "id" redesPrograma.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
