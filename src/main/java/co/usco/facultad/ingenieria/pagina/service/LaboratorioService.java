package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.LaboratorioDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Laboratorio}.
 */
public interface LaboratorioService {
    /**
     * Save a laboratorio.
     *
     * @param laboratorioDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<LaboratorioDTO> save(LaboratorioDTO laboratorioDTO);

    /**
     * Updates a laboratorio.
     *
     * @param laboratorioDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<LaboratorioDTO> update(LaboratorioDTO laboratorioDTO);

    /**
     * Partially updates a laboratorio.
     *
     * @param laboratorioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LaboratorioDTO> partialUpdate(LaboratorioDTO laboratorioDTO);

    /**
     * Get all the laboratorios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LaboratorioDTO> findAll(Pageable pageable);

    /**
     * Get all the laboratorios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LaboratorioDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of laboratorios available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" laboratorio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LaboratorioDTO> findOne(Long id);

    /**
     * Delete the "id" laboratorio.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
