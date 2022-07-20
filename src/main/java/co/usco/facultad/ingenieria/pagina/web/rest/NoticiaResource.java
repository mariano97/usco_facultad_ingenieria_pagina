package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.NoticiaRepository;
import co.usco.facultad.ingenieria.pagina.service.NoticiaService;
import co.usco.facultad.ingenieria.pagina.service.dto.NoticiaDTO;
import co.usco.facultad.ingenieria.pagina.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Noticia}.
 */
@RestController
@RequestMapping("/api")
public class NoticiaResource {

    private final Logger log = LoggerFactory.getLogger(NoticiaResource.class);

    private static final String ENTITY_NAME = "noticia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoticiaService noticiaService;

    private final NoticiaRepository noticiaRepository;

    public NoticiaResource(NoticiaService noticiaService, NoticiaRepository noticiaRepository) {
        this.noticiaService = noticiaService;
        this.noticiaRepository = noticiaRepository;
    }

    /**
     * {@code POST  /noticias} : Create a new noticia.
     *
     * @param noticiaDTO the noticiaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noticiaDTO, or with status {@code 400 (Bad Request)} if the noticia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/noticias")
    public Mono<ResponseEntity<NoticiaDTO>> createNoticia(@Valid @RequestBody NoticiaDTO noticiaDTO) throws URISyntaxException {
        log.debug("REST request to save Noticia : {}", noticiaDTO);
        if (noticiaDTO.getId() != null) {
            throw new BadRequestAlertException("A new noticia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return noticiaService
            .save(noticiaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/noticias/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /noticias/:id} : Updates an existing noticia.
     *
     * @param id the id of the noticiaDTO to save.
     * @param noticiaDTO the noticiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noticiaDTO,
     * or with status {@code 400 (Bad Request)} if the noticiaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noticiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/noticias/{id}")
    public Mono<ResponseEntity<NoticiaDTO>> updateNoticia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NoticiaDTO noticiaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Noticia : {}, {}", id, noticiaDTO);
        if (noticiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noticiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return noticiaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return noticiaService
                    .update(noticiaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /noticias/:id} : Partial updates given fields of an existing noticia, field will ignore if it is null
     *
     * @param id the id of the noticiaDTO to save.
     * @param noticiaDTO the noticiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noticiaDTO,
     * or with status {@code 400 (Bad Request)} if the noticiaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the noticiaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the noticiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/noticias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NoticiaDTO>> partialUpdateNoticia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NoticiaDTO noticiaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Noticia partially : {}, {}", id, noticiaDTO);
        if (noticiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noticiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return noticiaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NoticiaDTO> result = noticiaService.partialUpdate(noticiaDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /noticias} : get all the noticias.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of noticias in body.
     */
    @GetMapping("/noticias")
    public Mono<ResponseEntity<List<NoticiaDTO>>> getAllNoticias(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Noticias");
        return noticiaService
            .countAll()
            .zipWith(noticiaService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /noticias/:id} : get the "id" noticia.
     *
     * @param id the id of the noticiaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noticiaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/noticias/{id}")
    public Mono<ResponseEntity<NoticiaDTO>> getNoticia(@PathVariable Long id) {
        log.debug("REST request to get Noticia : {}", id);
        Mono<NoticiaDTO> noticiaDTO = noticiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(noticiaDTO);
    }

    /**
     * {@code DELETE  /noticias/:id} : delete the "id" noticia.
     *
     * @param id the id of the noticiaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/noticias/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteNoticia(@PathVariable Long id) {
        log.debug("REST request to delete Noticia : {}", id);
        return noticiaService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
