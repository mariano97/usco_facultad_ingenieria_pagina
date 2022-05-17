package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.repository.ProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.ProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Programa}.
 */
@RestController
@RequestMapping("/api")
public class ProgramaResource {

    private final Logger log = LoggerFactory.getLogger(ProgramaResource.class);

    private static final String ENTITY_NAME = "programa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramaService programaService;

    private final ProgramaRepository programaRepository;

    public ProgramaResource(ProgramaService programaService, ProgramaRepository programaRepository) {
        this.programaService = programaService;
        this.programaRepository = programaRepository;
    }

    /**
     * {@code POST  /programas} : Create a new programa.
     *
     * @param programaDTO the programaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programaDTO, or with status {@code 400 (Bad Request)} if the programa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/programas")
    public Mono<ResponseEntity<ProgramaDTO>> createPrograma(@Valid @RequestBody ProgramaDTO programaDTO) throws URISyntaxException {
        log.debug("REST request to save Programa : {}", programaDTO);
        if (programaDTO.getId() != null) {
            throw new BadRequestAlertException("A new programa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return programaService
            .save(programaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/programas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /programas/:id} : Updates an existing programa.
     *
     * @param id the id of the programaDTO to save.
     * @param programaDTO the programaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programaDTO,
     * or with status {@code 400 (Bad Request)} if the programaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/programas/{id}")
    public Mono<ResponseEntity<ProgramaDTO>> updatePrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProgramaDTO programaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Programa : {}, {}", id, programaDTO);
        if (programaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return programaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return programaService
                    .update(programaDTO)
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
     * {@code PATCH  /programas/:id} : Partial updates given fields of an existing programa, field will ignore if it is null
     *
     * @param id the id of the programaDTO to save.
     * @param programaDTO the programaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programaDTO,
     * or with status {@code 400 (Bad Request)} if the programaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the programaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the programaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/programas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProgramaDTO>> partialUpdatePrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProgramaDTO programaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Programa partially : {}, {}", id, programaDTO);
        if (programaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return programaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProgramaDTO> result = programaService.partialUpdate(programaDTO);

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
     * {@code GET  /programas} : get all the programas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programas in body.
     */
    @GetMapping(value = {
        "/programas",
        "/open/programas"
    })
    public Mono<ResponseEntity<List<ProgramaDTO>>> getAllProgramas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Programas");
        return programaService
            .countAll()
            .zipWith(programaService.findAll(pageable).collectList())
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
     * {@code GET  /programas/:id} : get the "id" programa.
     *
     * @param id the id of the programaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/programas/{id}")
    public Mono<ResponseEntity<ProgramaDTO>> getPrograma(@PathVariable Long id) {
        log.debug("REST request to get Programa : {}", id);
        Mono<ProgramaDTO> programaDTO = programaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programaDTO);
    }

    @GetMapping(value = {
        "/open/programas/by-codigo-snies"
    })
    public Mono<ResponseEntity<ProgramaDTO>> getProgramaByCodigoSnies(@RequestParam("codigo_snies") Long codigoSnies) {
        log.debug("REST get programa by codigoSnies: {}", codigoSnies);
        Mono<ProgramaDTO> programaDTO = programaService.findByCodigoSnies(codigoSnies);
        return ResponseUtil.wrapOrNotFound(programaDTO);
    }

    /**
     * {@code DELETE  /programas/:id} : delete the "id" programa.
     *
     * @param id the id of the programaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/programas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePrograma(@PathVariable Long id) {
        log.debug("REST request to delete Programa : {}", id);
        return programaService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
