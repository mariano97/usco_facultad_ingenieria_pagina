package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.FacultadRepository;
import co.usco.facultad.ingenieria.pagina.service.FacultadService;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Facultad}.
 */
@RestController
@RequestMapping("/api")
public class FacultadResource {

    private final Logger log = LoggerFactory.getLogger(FacultadResource.class);

    private static final String ENTITY_NAME = "facultad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacultadService facultadService;

    private final FacultadRepository facultadRepository;

    public FacultadResource(FacultadService facultadService, FacultadRepository facultadRepository) {
        this.facultadService = facultadService;
        this.facultadRepository = facultadRepository;
    }

    /**
     * {@code POST  /facultads} : Create a new facultad.
     *
     * @param facultadDTO the facultadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facultadDTO, or with status {@code 400 (Bad Request)} if the facultad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facultads")
    public Mono<ResponseEntity<FacultadDTO>> createFacultad(@Valid @RequestBody FacultadDTO facultadDTO) throws URISyntaxException {
        log.debug("REST request to save Facultad : {}", facultadDTO);
        if (facultadDTO.getId() != null) {
            throw new BadRequestAlertException("A new facultad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return facultadService
            .save(facultadDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/facultads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /facultads/:id} : Updates an existing facultad.
     *
     * @param id the id of the facultadDTO to save.
     * @param facultadDTO the facultadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facultadDTO,
     * or with status {@code 400 (Bad Request)} if the facultadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facultadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facultads/{id}")
    public Mono<ResponseEntity<FacultadDTO>> updateFacultad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacultadDTO facultadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Facultad : {}, {}", id, facultadDTO);
        if (facultadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facultadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return facultadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return facultadService
                    .update(facultadDTO)
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
     * {@code PATCH  /facultads/:id} : Partial updates given fields of an existing facultad, field will ignore if it is null
     *
     * @param id the id of the facultadDTO to save.
     * @param facultadDTO the facultadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facultadDTO,
     * or with status {@code 400 (Bad Request)} if the facultadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facultadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facultadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facultads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FacultadDTO>> partialUpdateFacultad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacultadDTO facultadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facultad partially : {}, {}", id, facultadDTO);
        if (facultadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facultadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return facultadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FacultadDTO> result = facultadService.partialUpdate(facultadDTO);

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
     * {@code GET  /facultads} : get all the facultads.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facultads in body.
     */
    @GetMapping("/facultads")
    public Mono<ResponseEntity<List<FacultadDTO>>> getAllFacultads(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Facultads");
        return facultadService
            .countAll()
            .zipWith(facultadService.findAll(pageable).collectList())
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
     * {@code GET  /facultads/:id} : get the "id" facultad.
     *
     * @param id the id of the facultadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facultadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facultads/{id}")
    public Mono<ResponseEntity<FacultadDTO>> getFacultad(@PathVariable Long id) {
        log.debug("REST request to get Facultad : {}", id);
        Mono<FacultadDTO> facultadDTO = facultadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facultadDTO);
    }

    /**
     * {@code DELETE  /facultads/:id} : delete the "id" facultad.
     *
     * @param id the id of the facultadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facultads/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFacultad(@PathVariable Long id) {
        log.debug("REST request to delete Facultad : {}", id);
        return facultadService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
