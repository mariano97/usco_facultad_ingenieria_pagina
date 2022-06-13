package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.RedesProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.RedesProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.RedesProgramaDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.RedesPrograma}.
 */
@RestController
@RequestMapping("/api")
public class RedesProgramaResource {

    private final Logger log = LoggerFactory.getLogger(RedesProgramaResource.class);

    private static final String ENTITY_NAME = "redesPrograma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RedesProgramaService redesProgramaService;

    private final RedesProgramaRepository redesProgramaRepository;

    public RedesProgramaResource(RedesProgramaService redesProgramaService, RedesProgramaRepository redesProgramaRepository) {
        this.redesProgramaService = redesProgramaService;
        this.redesProgramaRepository = redesProgramaRepository;
    }

    /**
     * {@code POST  /redes-programas} : Create a new redesPrograma.
     *
     * @param redesProgramaDTO the redesProgramaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new redesProgramaDTO, or with status {@code 400 (Bad Request)} if the redesPrograma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/redes-programas")
    public Mono<ResponseEntity<RedesProgramaDTO>> createRedesPrograma(@Valid @RequestBody RedesProgramaDTO redesProgramaDTO)
        throws URISyntaxException {
        log.debug("REST request to save RedesPrograma : {}", redesProgramaDTO);
        if (redesProgramaDTO.getId() != null) {
            throw new BadRequestAlertException("A new redesPrograma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return redesProgramaService
            .save(redesProgramaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/redes-programas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /redes-programas/:id} : Updates an existing redesPrograma.
     *
     * @param id the id of the redesProgramaDTO to save.
     * @param redesProgramaDTO the redesProgramaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated redesProgramaDTO,
     * or with status {@code 400 (Bad Request)} if the redesProgramaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the redesProgramaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/redes-programas/{id}")
    public Mono<ResponseEntity<RedesProgramaDTO>> updateRedesPrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RedesProgramaDTO redesProgramaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RedesPrograma : {}, {}", id, redesProgramaDTO);
        if (redesProgramaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, redesProgramaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return redesProgramaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return redesProgramaService
                    .update(redesProgramaDTO)
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
     * {@code PATCH  /redes-programas/:id} : Partial updates given fields of an existing redesPrograma, field will ignore if it is null
     *
     * @param id the id of the redesProgramaDTO to save.
     * @param redesProgramaDTO the redesProgramaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated redesProgramaDTO,
     * or with status {@code 400 (Bad Request)} if the redesProgramaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the redesProgramaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the redesProgramaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/redes-programas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RedesProgramaDTO>> partialUpdateRedesPrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RedesProgramaDTO redesProgramaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RedesPrograma partially : {}, {}", id, redesProgramaDTO);
        if (redesProgramaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, redesProgramaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return redesProgramaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RedesProgramaDTO> result = redesProgramaService.partialUpdate(redesProgramaDTO);

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
     * {@code GET  /redes-programas} : get all the redesProgramas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of redesProgramas in body.
     */
    @GetMapping("/redes-programas")
    public Mono<ResponseEntity<List<RedesProgramaDTO>>> getAllRedesProgramas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of RedesProgramas");
        return redesProgramaService
            .countAll()
            .zipWith(redesProgramaService.findAll(pageable).collectList())
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
        "/redes-programas/by-programa-id/{id}",
        "/open/redes-programas/by-programa-id/{id}"
    })
    public Mono<ResponseEntity<List<RedesProgramaDTO>>> getAllByProgramaId(@PathVariable("id") Long programaId) {
        return redesProgramaService.findAllByProgramaId(programaId)
            .collectList().map(redesProgramaDTOS -> ResponseEntity.ok().body(redesProgramaDTOS));
    }

    /**
     * {@code GET  /redes-programas/:id} : get the "id" redesPrograma.
     *
     * @param id the id of the redesProgramaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the redesProgramaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/redes-programas/{id}")
    public Mono<ResponseEntity<RedesProgramaDTO>> getRedesPrograma(@PathVariable Long id) {
        log.debug("REST request to get RedesPrograma : {}", id);
        Mono<RedesProgramaDTO> redesProgramaDTO = redesProgramaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(redesProgramaDTO);
    }

    /**
     * {@code DELETE  /redes-programas/:id} : delete the "id" redesPrograma.
     *
     * @param id the id of the redesProgramaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/redes-programas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRedesPrograma(@PathVariable Long id) {
        log.debug("REST request to delete RedesPrograma : {}", id);
        return redesProgramaService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
