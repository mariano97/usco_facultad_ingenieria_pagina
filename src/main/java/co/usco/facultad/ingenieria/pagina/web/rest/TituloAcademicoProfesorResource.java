package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.TituloAcademicoProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.TituloAcademicoProfesorService;
import co.usco.facultad.ingenieria.pagina.service.dto.TituloAcademicoProfesorDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor}.
 */
@RestController
@RequestMapping("/api")
public class TituloAcademicoProfesorResource {

    private final Logger log = LoggerFactory.getLogger(TituloAcademicoProfesorResource.class);

    private static final String ENTITY_NAME = "tituloAcademicoProfesor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TituloAcademicoProfesorService tituloAcademicoProfesorService;

    private final TituloAcademicoProfesorRepository tituloAcademicoProfesorRepository;

    public TituloAcademicoProfesorResource(
        TituloAcademicoProfesorService tituloAcademicoProfesorService,
        TituloAcademicoProfesorRepository tituloAcademicoProfesorRepository
    ) {
        this.tituloAcademicoProfesorService = tituloAcademicoProfesorService;
        this.tituloAcademicoProfesorRepository = tituloAcademicoProfesorRepository;
    }

    /**
     * {@code POST  /titulo-academico-profesors} : Create a new tituloAcademicoProfesor.
     *
     * @param tituloAcademicoProfesorDTO the tituloAcademicoProfesorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tituloAcademicoProfesorDTO, or with status {@code 400 (Bad Request)} if the tituloAcademicoProfesor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/titulo-academico-profesors")
    public Mono<ResponseEntity<TituloAcademicoProfesorDTO>> createTituloAcademicoProfesor(
        @Valid @RequestBody TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TituloAcademicoProfesor : {}", tituloAcademicoProfesorDTO);
        if (tituloAcademicoProfesorDTO.getId() != null) {
            throw new BadRequestAlertException("A new tituloAcademicoProfesor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tituloAcademicoProfesorService
            .save(tituloAcademicoProfesorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/titulo-academico-profesors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /titulo-academico-profesors/:id} : Updates an existing tituloAcademicoProfesor.
     *
     * @param id the id of the tituloAcademicoProfesorDTO to save.
     * @param tituloAcademicoProfesorDTO the tituloAcademicoProfesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tituloAcademicoProfesorDTO,
     * or with status {@code 400 (Bad Request)} if the tituloAcademicoProfesorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tituloAcademicoProfesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/titulo-academico-profesors/{id}")
    public Mono<ResponseEntity<TituloAcademicoProfesorDTO>> updateTituloAcademicoProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TituloAcademicoProfesor : {}, {}", id, tituloAcademicoProfesorDTO);
        if (tituloAcademicoProfesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tituloAcademicoProfesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tituloAcademicoProfesorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tituloAcademicoProfesorService
                    .update(tituloAcademicoProfesorDTO)
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
     * {@code PATCH  /titulo-academico-profesors/:id} : Partial updates given fields of an existing tituloAcademicoProfesor, field will ignore if it is null
     *
     * @param id the id of the tituloAcademicoProfesorDTO to save.
     * @param tituloAcademicoProfesorDTO the tituloAcademicoProfesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tituloAcademicoProfesorDTO,
     * or with status {@code 400 (Bad Request)} if the tituloAcademicoProfesorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tituloAcademicoProfesorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tituloAcademicoProfesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/titulo-academico-profesors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TituloAcademicoProfesorDTO>> partialUpdateTituloAcademicoProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TituloAcademicoProfesor partially : {}, {}", id, tituloAcademicoProfesorDTO);
        if (tituloAcademicoProfesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tituloAcademicoProfesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tituloAcademicoProfesorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TituloAcademicoProfesorDTO> result = tituloAcademicoProfesorService.partialUpdate(tituloAcademicoProfesorDTO);

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
     * {@code GET  /titulo-academico-profesors} : get all the tituloAcademicoProfesors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tituloAcademicoProfesors in body.
     */
    @GetMapping("/titulo-academico-profesors")
    public Mono<ResponseEntity<List<TituloAcademicoProfesorDTO>>> getAllTituloAcademicoProfesors(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TituloAcademicoProfesors");
        return tituloAcademicoProfesorService
            .countAll()
            .zipWith(tituloAcademicoProfesorService.findAll(pageable).collectList())
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

    @GetMapping(value = {
        "/titulo-academico-profesors/by-profesor-id/{id}",
        "/open/titulo-academico-profesors/by-profesor-id/{id}"
    })
    public Mono<ResponseEntity<List<TituloAcademicoProfesorDTO>>> getAllByProfesorId(@PathVariable("id") Long profesorId) {
        return tituloAcademicoProfesorService
            .findAllByProfesorId(profesorId)
            .collectList()
            .map(tituloAcademicoProfesorDTOS ->
                ResponseEntity
                    .ok()
                    .body(tituloAcademicoProfesorDTOS));
    }

    /**
     * {@code GET  /titulo-academico-profesors/:id} : get the "id" tituloAcademicoProfesor.
     *
     * @param id the id of the tituloAcademicoProfesorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tituloAcademicoProfesorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/titulo-academico-profesors/{id}")
    public Mono<ResponseEntity<TituloAcademicoProfesorDTO>> getTituloAcademicoProfesor(@PathVariable Long id) {
        log.debug("REST request to get TituloAcademicoProfesor : {}", id);
        Mono<TituloAcademicoProfesorDTO> tituloAcademicoProfesorDTO = tituloAcademicoProfesorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tituloAcademicoProfesorDTO);
    }

    /**
     * {@code DELETE  /titulo-academico-profesors/:id} : delete the "id" tituloAcademicoProfesor.
     *
     * @param id the id of the tituloAcademicoProfesorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/titulo-academico-profesors/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTituloAcademicoProfesor(@PathVariable Long id) {
        log.debug("REST request to delete TituloAcademicoProfesor : {}", id);
        return tituloAcademicoProfesorService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
