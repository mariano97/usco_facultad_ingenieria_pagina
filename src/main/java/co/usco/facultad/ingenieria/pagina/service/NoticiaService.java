package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.NoticiaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.Noticia}.
 */
public interface NoticiaService {
    /**
     * Save a noticia.
     *
     * @param noticiaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<NoticiaDTO> save(NoticiaDTO noticiaDTO);

    /**
     * Updates a noticia.
     *
     * @param noticiaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<NoticiaDTO> update(NoticiaDTO noticiaDTO);

    /**
     * Partially updates a noticia.
     *
     * @param noticiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<NoticiaDTO> partialUpdate(NoticiaDTO noticiaDTO);

    /**
     * Get all the noticias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<NoticiaDTO> findAll(Pageable pageable);

    Flux<NoticiaDTO> findAllFechaMayorQue(Pageable pageable, Instant fechaInicial);

    /**
     * Get all the noticias with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<NoticiaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of noticias available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" noticia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<NoticiaDTO> findOne(Long id);

    /**
     * Delete the "id" noticia.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
