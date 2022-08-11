package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.LaboratorioRepository;
import co.usco.facultad.ingenieria.pagina.service.LaboratorioService;
import co.usco.facultad.ingenieria.pagina.service.dto.LaboratorioDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Laboratorio}.
 */
@RestController
@RequestMapping("/api")
public class LaboratorioResource {

    private final Logger log = LoggerFactory.getLogger(LaboratorioResource.class);

    private static final String ENTITY_NAME = "laboratorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LaboratorioService laboratorioService;

    private final LaboratorioRepository laboratorioRepository;

    public LaboratorioResource(LaboratorioService laboratorioService, LaboratorioRepository laboratorioRepository) {
        this.laboratorioService = laboratorioService;
        this.laboratorioRepository = laboratorioRepository;
    }

    /**
     * {@code POST  /laboratorios} : Create a new laboratorio.
     *
     * @param laboratorioDTO the laboratorioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new laboratorioDTO, or with status {@code 400 (Bad Request)} if the laboratorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/laboratorios")
    public Mono<ResponseEntity<LaboratorioDTO>> createLaboratorio(@Valid @RequestBody LaboratorioDTO laboratorioDTO)
        throws URISyntaxException {
        log.debug("REST request to save Laboratorio : {}", laboratorioDTO);
        if (laboratorioDTO.getId() != null) {
            throw new BadRequestAlertException("A new laboratorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return laboratorioService
            .save(laboratorioDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/laboratorios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /laboratorios/:id} : Updates an existing laboratorio.
     *
     * @param id the id of the laboratorioDTO to save.
     * @param laboratorioDTO the laboratorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratorioDTO,
     * or with status {@code 400 (Bad Request)} if the laboratorioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the laboratorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/laboratorios/{id}")
    public Mono<ResponseEntity<LaboratorioDTO>> updateLaboratorio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LaboratorioDTO laboratorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Laboratorio : {}, {}", id, laboratorioDTO);
        if (laboratorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return laboratorioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return laboratorioService
                    .update(laboratorioDTO)
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
     * {@code PATCH  /laboratorios/:id} : Partial updates given fields of an existing laboratorio, field will ignore if it is null
     *
     * @param id the id of the laboratorioDTO to save.
     * @param laboratorioDTO the laboratorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratorioDTO,
     * or with status {@code 400 (Bad Request)} if the laboratorioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the laboratorioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the laboratorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/laboratorios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LaboratorioDTO>> partialUpdateLaboratorio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LaboratorioDTO laboratorioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Laboratorio partially : {}, {}", id, laboratorioDTO);
        if (laboratorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return laboratorioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LaboratorioDTO> result = laboratorioService.partialUpdate(laboratorioDTO);

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
     * {@code GET  /laboratorios} : get all the laboratorios.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of laboratorios in body.
     */
    @GetMapping("/laboratorios")
    public Mono<ResponseEntity<List<LaboratorioDTO>>> getAllLaboratorios(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Laboratorios");
        return laboratorioService
            .countAll()
            .zipWith(laboratorioService.findAll(pageable).collectList())
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
     * {@code GET  /laboratorios/:id} : get the "id" laboratorio.
     *
     * @param id the id of the laboratorioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the laboratorioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/laboratorios/{id}")
    public Mono<ResponseEntity<LaboratorioDTO>> getLaboratorio(@PathVariable Long id) {
        log.debug("REST request to get Laboratorio : {}", id);
        Mono<LaboratorioDTO> laboratorioDTO = laboratorioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(laboratorioDTO);
    }

    /**
     * {@code DELETE  /laboratorios/:id} : delete the "id" laboratorio.
     *
     * @param id the id of the laboratorioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/laboratorios/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteLaboratorio(@PathVariable Long id) {
        log.debug("REST request to delete Laboratorio : {}", id);
        return laboratorioService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
