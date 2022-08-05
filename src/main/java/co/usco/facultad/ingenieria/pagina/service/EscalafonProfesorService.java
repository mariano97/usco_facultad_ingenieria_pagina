package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.EscalafonProfesorDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor}.
 */
public interface EscalafonProfesorService {
    /**
     * Save a escalafonProfesor.
     *
     * @param escalafonProfesorDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EscalafonProfesorDTO> save(EscalafonProfesorDTO escalafonProfesorDTO);

    /**
     * Updates a escalafonProfesor.
     *
     * @param escalafonProfesorDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EscalafonProfesorDTO> update(EscalafonProfesorDTO escalafonProfesorDTO);

    /**
     * Partially updates a escalafonProfesor.
     *
     * @param escalafonProfesorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EscalafonProfesorDTO> partialUpdate(EscalafonProfesorDTO escalafonProfesorDTO);

    /**
     * Get all the escalafonProfesors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EscalafonProfesorDTO> findAll(Pageable pageable);

    Flux<EscalafonProfesorDTO> findByProfesorId(Long profesorId);

    /**
     * Returns the number of escalafonProfesors available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" escalafonProfesor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EscalafonProfesorDTO> findOne(Long id);

    /**
     * Delete the "id" escalafonProfesor.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
