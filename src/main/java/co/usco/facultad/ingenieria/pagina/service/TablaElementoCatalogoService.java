package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo}.
 */
public interface TablaElementoCatalogoService {
    /**
     * Save a tablaElementoCatalogo.
     *
     * @param tablaElementoCatalogoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TablaElementoCatalogoDTO> save(TablaElementoCatalogoDTO tablaElementoCatalogoDTO);

    /**
     * Updates a tablaElementoCatalogo.
     *
     * @param tablaElementoCatalogoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TablaElementoCatalogoDTO> update(TablaElementoCatalogoDTO tablaElementoCatalogoDTO);

    /**
     * Partially updates a tablaElementoCatalogo.
     *
     * @param tablaElementoCatalogoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TablaElementoCatalogoDTO> partialUpdate(TablaElementoCatalogoDTO tablaElementoCatalogoDTO);

    /**
     * Get all the tablaElementoCatalogos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TablaElementoCatalogoDTO> findAll(Pageable pageable);

    /**
     * Get all the tablaElementoCatalogos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TablaElementoCatalogoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     *
     * Obtener todos los elementos por el campo TiposCatalogo keyIdentificador
     *
     * @param keyIdentificador
     * @return
     */
    Flux<TablaElementoCatalogoDTO> findByTiposCatalogoKeyIdentificador(String keyIdentificador);

    /**
     * Returns the number of tablaElementoCatalogos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tablaElementoCatalogo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TablaElementoCatalogoDTO> findOne(Long id);

    /**
     * Delete the "id" tablaElementoCatalogo.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
