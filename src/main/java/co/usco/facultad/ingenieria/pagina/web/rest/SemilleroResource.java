package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.SemilleroRepository;
import co.usco.facultad.ingenieria.pagina.service.SemilleroService;
import co.usco.facultad.ingenieria.pagina.service.dto.SemilleroDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Semillero}.
 */
@RestController
@RequestMapping("/api")
public class SemilleroResource {

    private final Logger log = LoggerFactory.getLogger(SemilleroResource.class);

    private static final String ENTITY_NAME = "semillero";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemilleroService semilleroService;

    private final SemilleroRepository semilleroRepository;

    public SemilleroResource(SemilleroService semilleroService, SemilleroRepository semilleroRepository) {
        this.semilleroService = semilleroService;
        this.semilleroRepository = semilleroRepository;
    }

    /**
     * {@code POST  /semilleros} : Create a new semillero.
     *
     * @param semilleroDTO the semilleroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semilleroDTO, or with status {@code 400 (Bad Request)} if the semillero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/semilleros")
    public Mono<ResponseEntity<SemilleroDTO>> createSemillero(@Valid @RequestBody SemilleroDTO semilleroDTO) throws URISyntaxException {
        log.debug("REST request to save Semillero : {}", semilleroDTO);
        if (semilleroDTO.getId() != null) {
            throw new BadRequestAlertException("A new semillero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return semilleroService
            .save(semilleroDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/semilleros/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /semilleros/:id} : Updates an existing semillero.
     *
     * @param id the id of the semilleroDTO to save.
     * @param semilleroDTO the semilleroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semilleroDTO,
     * or with status {@code 400 (Bad Request)} if the semilleroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semilleroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/semilleros/{id}")
    public Mono<ResponseEntity<SemilleroDTO>> updateSemillero(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SemilleroDTO semilleroDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Semillero : {}, {}", id, semilleroDTO);
        if (semilleroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semilleroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return semilleroRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return semilleroService
                    .update(semilleroDTO)
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
     * {@code PATCH  /semilleros/:id} : Partial updates given fields of an existing semillero, field will ignore if it is null
     *
     * @param id the id of the semilleroDTO to save.
     * @param semilleroDTO the semilleroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semilleroDTO,
     * or with status {@code 400 (Bad Request)} if the semilleroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the semilleroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the semilleroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/semilleros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SemilleroDTO>> partialUpdateSemillero(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SemilleroDTO semilleroDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Semillero partially : {}, {}", id, semilleroDTO);
        if (semilleroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semilleroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return semilleroRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SemilleroDTO> result = semilleroService.partialUpdate(semilleroDTO);

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
     * {@code GET  /semilleros} : get all the semilleros.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semilleros in body.
     */
    @GetMapping(value = {
        "/semilleros",
        "/open/semilleros"
    })
    public Mono<ResponseEntity<List<SemilleroDTO>>> getAllSemilleros(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Semilleros");
        return semilleroService
            .countAll()
            .zipWith(semilleroService.findAll(pageable).collectList())
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
     * {@code GET  /semilleros/:id} : get the "id" semillero.
     *
     * @param id the id of the semilleroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semilleroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = {
        "/semilleros/{id}",
        "/open/semilleros/{id}"
    })
    public Mono<ResponseEntity<SemilleroDTO>> getSemillero(@PathVariable Long id) {
        log.debug("REST request to get Semillero : {}", id);
        Mono<SemilleroDTO> semilleroDTO = semilleroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(semilleroDTO);
    }

    /**
     * {@code DELETE  /semilleros/:id} : delete the "id" semillero.
     *
     * @param id the id of the semilleroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/semilleros/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSemillero(@PathVariable Long id) {
        log.debug("REST request to delete Semillero : {}", id);
        return semilleroService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
