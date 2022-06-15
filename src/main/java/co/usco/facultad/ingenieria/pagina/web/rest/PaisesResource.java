package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.PaisesRepository;
import co.usco.facultad.ingenieria.pagina.service.PaisesService;
import co.usco.facultad.ingenieria.pagina.service.dto.PaisesDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Paises}.
 */
@RestController
@RequestMapping("/api")
public class PaisesResource {

    private final Logger log = LoggerFactory.getLogger(PaisesResource.class);

    private static final String ENTITY_NAME = "paises";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaisesService paisesService;

    private final PaisesRepository paisesRepository;

    public PaisesResource(PaisesService paisesService, PaisesRepository paisesRepository) {
        this.paisesService = paisesService;
        this.paisesRepository = paisesRepository;
    }

    /**
     * {@code POST  /paises} : Create a new paises.
     *
     * @param paisesDTO the paisesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paisesDTO, or with status {@code 400 (Bad Request)} if the paises has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paises")
    public Mono<ResponseEntity<PaisesDTO>> createPaises(@Valid @RequestBody PaisesDTO paisesDTO) throws URISyntaxException {
        log.debug("REST request to save Paises : {}", paisesDTO);
        if (paisesDTO.getId() != null) {
            throw new BadRequestAlertException("A new paises cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return paisesService
            .save(paisesDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/paises/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /paises/:id} : Updates an existing paises.
     *
     * @param id the id of the paisesDTO to save.
     * @param paisesDTO the paisesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paisesDTO,
     * or with status {@code 400 (Bad Request)} if the paisesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paisesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paises/{id}")
    public Mono<ResponseEntity<PaisesDTO>> updatePaises(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaisesDTO paisesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Paises : {}, {}", id, paisesDTO);
        if (paisesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paisesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paisesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return paisesService
                    .update(paisesDTO)
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
     * {@code PATCH  /paises/:id} : Partial updates given fields of an existing paises, field will ignore if it is null
     *
     * @param id the id of the paisesDTO to save.
     * @param paisesDTO the paisesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paisesDTO,
     * or with status {@code 400 (Bad Request)} if the paisesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paisesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paisesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paises/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PaisesDTO>> partialUpdatePaises(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaisesDTO paisesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Paises partially : {}, {}", id, paisesDTO);
        if (paisesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paisesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paisesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PaisesDTO> result = paisesService.partialUpdate(paisesDTO);

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
     * {@code GET  /paises} : get all the paises.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paises in body.
     */
    @GetMapping("/paises")
    public Mono<ResponseEntity<List<PaisesDTO>>> getAllPaises(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Paises");
        return paisesService
            .countAll()
            .zipWith(paisesService.findAll(pageable).collectList())
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
     * {@code GET  /paises/:id} : get the "id" paises.
     *
     * @param id the id of the paisesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paisesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paises/{id}")
    public Mono<ResponseEntity<PaisesDTO>> getPaises(@PathVariable Long id) {
        log.debug("REST request to get Paises : {}", id);
        Mono<PaisesDTO> paisesDTO = paisesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paisesDTO);
    }

    /**
     * {@code DELETE  /paises/:id} : delete the "id" paises.
     *
     * @param id the id of the paisesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paises/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePaises(@PathVariable Long id) {
        log.debug("REST request to delete Paises : {}", id);
        return paisesService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
