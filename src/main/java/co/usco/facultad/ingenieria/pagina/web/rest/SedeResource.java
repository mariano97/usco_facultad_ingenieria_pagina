package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.SedeRepository;
import co.usco.facultad.ingenieria.pagina.service.SedeService;
import co.usco.facultad.ingenieria.pagina.service.dto.SedeDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Sede}.
 */
@RestController
@RequestMapping("/api")
public class SedeResource {

    private final Logger log = LoggerFactory.getLogger(SedeResource.class);

    private static final String ENTITY_NAME = "sede";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SedeService sedeService;

    private final SedeRepository sedeRepository;

    public SedeResource(SedeService sedeService, SedeRepository sedeRepository) {
        this.sedeService = sedeService;
        this.sedeRepository = sedeRepository;
    }

    /**
     * {@code POST  /sedes} : Create a new sede.
     *
     * @param sedeDTO the sedeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sedeDTO, or with status {@code 400 (Bad Request)} if the sede has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sedes")
    public Mono<ResponseEntity<SedeDTO>> createSede(@Valid @RequestBody SedeDTO sedeDTO) throws URISyntaxException {
        log.debug("REST request to save Sede : {}", sedeDTO);
        if (sedeDTO.getId() != null) {
            throw new BadRequestAlertException("A new sede cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sedeService
            .save(sedeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/sedes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sedes/:id} : Updates an existing sede.
     *
     * @param id the id of the sedeDTO to save.
     * @param sedeDTO the sedeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sedeDTO,
     * or with status {@code 400 (Bad Request)} if the sedeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sedeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sedes/{id}")
    public Mono<ResponseEntity<SedeDTO>> updateSede(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SedeDTO sedeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sede : {}, {}", id, sedeDTO);
        if (sedeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sedeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sedeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sedeService
                    .update(sedeDTO)
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
     * {@code PATCH  /sedes/:id} : Partial updates given fields of an existing sede, field will ignore if it is null
     *
     * @param id the id of the sedeDTO to save.
     * @param sedeDTO the sedeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sedeDTO,
     * or with status {@code 400 (Bad Request)} if the sedeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sedeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sedeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sedes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SedeDTO>> partialUpdateSede(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SedeDTO sedeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sede partially : {}, {}", id, sedeDTO);
        if (sedeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sedeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sedeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SedeDTO> result = sedeService.partialUpdate(sedeDTO);

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
     * {@code GET  /sedes} : get all the sedes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sedes in body.
     */
    @GetMapping("/sedes")
    public Mono<ResponseEntity<List<SedeDTO>>> getAllSedes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Sedes");
        return sedeService
            .countAll()
            .zipWith(sedeService.findAll(pageable).collectList())
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

    @GetMapping("/sedes/find-by-codigo-indicativo")
    public Mono<ResponseEntity<SedeDTO>> getSedeByCodigoIndicativo(@RequestParam("codigo_indicativo") String codigoIndicativo) {
        log.debug("dentro de getSedeByCodigoIndicativo: {}", codigoIndicativo);
        return sedeService.findByCodigoIndicativo(codigoIndicativo)
            .map(sede ->
                ResponseEntity.ok()
                    .headers(
                        HeaderUtil.createAlert(applicationName, "getSedeByCodigoIndicativo", codigoIndicativo)
                    )
                    .body(sede));
    }

    /**
     * {@code GET  /sedes/:id} : get the "id" sede.
     *
     * @param id the id of the sedeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sedeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sedes/{id}")
    public Mono<ResponseEntity<SedeDTO>> getSede(@PathVariable Long id) {
        log.debug("REST request to get Sede : {}", id);
        Mono<SedeDTO> sedeDTO = sedeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sedeDTO);
    }

    /**
     * {@code DELETE  /sedes/:id} : delete the "id" sede.
     *
     * @param id the id of the sedeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sedes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSede(@PathVariable Long id) {
        log.debug("REST request to delete Sede : {}", id);
        return sedeService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
