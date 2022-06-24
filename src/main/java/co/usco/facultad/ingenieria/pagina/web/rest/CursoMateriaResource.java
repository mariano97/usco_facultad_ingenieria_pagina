package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.CursoMateriaRepository;
import co.usco.facultad.ingenieria.pagina.service.CursoMateriaService;
import co.usco.facultad.ingenieria.pagina.service.dto.CursoMateriaDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.CursoMateria}.
 */
@RestController
@RequestMapping("/api")
public class CursoMateriaResource {

    private final Logger log = LoggerFactory.getLogger(CursoMateriaResource.class);

    private static final String ENTITY_NAME = "cursoMateria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CursoMateriaService cursoMateriaService;

    private final CursoMateriaRepository cursoMateriaRepository;

    public CursoMateriaResource(CursoMateriaService cursoMateriaService, CursoMateriaRepository cursoMateriaRepository) {
        this.cursoMateriaService = cursoMateriaService;
        this.cursoMateriaRepository = cursoMateriaRepository;
    }

    /**
     * {@code POST  /curso-materias} : Create a new cursoMateria.
     *
     * @param cursoMateriaDTO the cursoMateriaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cursoMateriaDTO, or with status {@code 400 (Bad Request)} if the cursoMateria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/curso-materias")
    public Mono<ResponseEntity<CursoMateriaDTO>> createCursoMateria(@Valid @RequestBody CursoMateriaDTO cursoMateriaDTO)
        throws URISyntaxException {
        log.debug("REST request to save CursoMateria : {}", cursoMateriaDTO);
        if (cursoMateriaDTO.getId() != null) {
            throw new BadRequestAlertException("A new cursoMateria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cursoMateriaService
            .save(cursoMateriaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/curso-materias/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /curso-materias/:id} : Updates an existing cursoMateria.
     *
     * @param id the id of the cursoMateriaDTO to save.
     * @param cursoMateriaDTO the cursoMateriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cursoMateriaDTO,
     * or with status {@code 400 (Bad Request)} if the cursoMateriaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cursoMateriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/curso-materias/{id}")
    public Mono<ResponseEntity<CursoMateriaDTO>> updateCursoMateria(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CursoMateriaDTO cursoMateriaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CursoMateria : {}, {}", id, cursoMateriaDTO);
        if (cursoMateriaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cursoMateriaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cursoMateriaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cursoMateriaService
                    .update(cursoMateriaDTO)
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
     * {@code PATCH  /curso-materias/:id} : Partial updates given fields of an existing cursoMateria, field will ignore if it is null
     *
     * @param id the id of the cursoMateriaDTO to save.
     * @param cursoMateriaDTO the cursoMateriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cursoMateriaDTO,
     * or with status {@code 400 (Bad Request)} if the cursoMateriaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cursoMateriaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cursoMateriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/curso-materias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CursoMateriaDTO>> partialUpdateCursoMateria(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CursoMateriaDTO cursoMateriaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CursoMateria partially : {}, {}", id, cursoMateriaDTO);
        if (cursoMateriaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cursoMateriaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cursoMateriaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CursoMateriaDTO> result = cursoMateriaService.partialUpdate(cursoMateriaDTO);

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
     * {@code GET  /curso-materias} : get all the cursoMaterias.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cursoMaterias in body.
     */
    @GetMapping("/curso-materias")
    public Mono<ResponseEntity<List<CursoMateriaDTO>>> getAllCursoMaterias(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CursoMaterias");
        return cursoMateriaService
            .countAll()
            .zipWith(cursoMateriaService.findAll(pageable).collectList())
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
     * {@code GET  /curso-materias/:id} : get the "id" cursoMateria.
     *
     * @param id the id of the cursoMateriaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cursoMateriaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/curso-materias/{id}")
    public Mono<ResponseEntity<CursoMateriaDTO>> getCursoMateria(@PathVariable Long id) {
        log.debug("REST request to get CursoMateria : {}", id);
        Mono<CursoMateriaDTO> cursoMateriaDTO = cursoMateriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cursoMateriaDTO);
    }

    /**
     * {@code DELETE  /curso-materias/:id} : delete the "id" cursoMateria.
     *
     * @param id the id of the cursoMateriaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/curso-materias/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCursoMateria(@PathVariable Long id) {
        log.debug("REST request to delete CursoMateria : {}", id);
        return cursoMateriaService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
