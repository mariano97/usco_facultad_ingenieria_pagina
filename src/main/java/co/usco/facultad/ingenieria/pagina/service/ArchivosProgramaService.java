package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma}.
 */
public interface ArchivosProgramaService {
    /**
     * Save a archivosPrograma.
     *
     * @param archivosProgramaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ArchivosProgramaDTO> save(ArchivosProgramaDTO archivosProgramaDTO);

    /**
     * Updates a archivosPrograma.
     *
     * @param archivosProgramaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ArchivosProgramaDTO> update(ArchivosProgramaDTO archivosProgramaDTO);

    /**
     * Partially updates a archivosPrograma.
     *
     * @param archivosProgramaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ArchivosProgramaDTO> partialUpdate(ArchivosProgramaDTO archivosProgramaDTO);

    /**
     * Get all the archivosProgramas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ArchivosProgramaDTO> findAll(Pageable pageable);

    Flux<ArchivosProgramaDTO> findAllByPrograma(Long programaId);

    /**
     * Get all the archivosProgramas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ArchivosProgramaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of archivosProgramas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" archivosPrograma.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ArchivosProgramaDTO> findOne(Long id);

    /**
     * Delete the "id" archivosPrograma.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
