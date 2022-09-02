package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.TituloAcademicoProfesorDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor}.
 */
public interface TituloAcademicoProfesorService {
    /**
     * Save a tituloAcademicoProfesor.
     *
     * @param tituloAcademicoProfesorDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TituloAcademicoProfesorDTO> save(TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO);

    /**
     * Updates a tituloAcademicoProfesor.
     *
     * @param tituloAcademicoProfesorDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TituloAcademicoProfesorDTO> update(TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO);

    /**
     * Partially updates a tituloAcademicoProfesor.
     *
     * @param tituloAcademicoProfesorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TituloAcademicoProfesorDTO> partialUpdate(TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO);

    /**
     * Get all the tituloAcademicoProfesors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TituloAcademicoProfesorDTO> findAll(Pageable pageable);

    Flux<TituloAcademicoProfesorDTO> findAllByProfesorId(Long profesorId);

    /**
     * Get all the tituloAcademicoProfesors with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TituloAcademicoProfesorDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of tituloAcademicoProfesors available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tituloAcademicoProfesor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TituloAcademicoProfesorDTO> findOne(Long id);

    /**
     * Delete the "id" tituloAcademicoProfesor.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    Mono<Void> deleteAllByProfesorId(Long profesorId);
}
