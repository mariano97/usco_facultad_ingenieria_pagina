package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.CursoMateriaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.CursoMateria}.
 */
public interface CursoMateriaService {
    /**
     * Save a cursoMateria.
     *
     * @param cursoMateriaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CursoMateriaDTO> save(CursoMateriaDTO cursoMateriaDTO);

    /**
     * Updates a cursoMateria.
     *
     * @param cursoMateriaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CursoMateriaDTO> update(CursoMateriaDTO cursoMateriaDTO);

    /**
     * Partially updates a cursoMateria.
     *
     * @param cursoMateriaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CursoMateriaDTO> partialUpdate(CursoMateriaDTO cursoMateriaDTO);

    /**
     * Get all the cursoMaterias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CursoMateriaDTO> findAll(Pageable pageable);

    Flux<CursoMateriaDTO> findAllByProfesorIdRelation(Long profesorId);

    /**
     * Get all the cursoMaterias with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CursoMateriaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of cursoMaterias available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" cursoMateria.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CursoMateriaDTO> findOne(Long id);

    /**
     * Delete the "id" cursoMateria.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
