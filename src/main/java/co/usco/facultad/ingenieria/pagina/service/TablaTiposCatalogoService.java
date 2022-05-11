package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.TablaTiposCatalogoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo}.
 */
public interface TablaTiposCatalogoService {
    /**
     * Save a tablaTiposCatalogo.
     *
     * @param tablaTiposCatalogoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TablaTiposCatalogoDTO> save(TablaTiposCatalogoDTO tablaTiposCatalogoDTO);

    /**
     * Updates a tablaTiposCatalogo.
     *
     * @param tablaTiposCatalogoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TablaTiposCatalogoDTO> update(TablaTiposCatalogoDTO tablaTiposCatalogoDTO);

    /**
     * Partially updates a tablaTiposCatalogo.
     *
     * @param tablaTiposCatalogoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TablaTiposCatalogoDTO> partialUpdate(TablaTiposCatalogoDTO tablaTiposCatalogoDTO);

    /**
     * Get all the tablaTiposCatalogos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TablaTiposCatalogoDTO> findAll(Pageable pageable);

    /**
     * Returns the number of tablaTiposCatalogos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tablaTiposCatalogo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TablaTiposCatalogoDTO> findOne(Long id);

    /**
     * Delete the "id" tablaTiposCatalogo.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
