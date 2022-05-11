package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.TablaTiposCatalogoRepository;
import co.usco.facultad.ingenieria.pagina.service.TablaTiposCatalogoService;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaTiposCatalogoDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo}.
 */
@RestController
@RequestMapping("/api")
public class TablaTiposCatalogoResource {

    private final Logger log = LoggerFactory.getLogger(TablaTiposCatalogoResource.class);

    private static final String ENTITY_NAME = "tablaTiposCatalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TablaTiposCatalogoService tablaTiposCatalogoService;

    private final TablaTiposCatalogoRepository tablaTiposCatalogoRepository;

    public TablaTiposCatalogoResource(
        TablaTiposCatalogoService tablaTiposCatalogoService,
        TablaTiposCatalogoRepository tablaTiposCatalogoRepository
    ) {
        this.tablaTiposCatalogoService = tablaTiposCatalogoService;
        this.tablaTiposCatalogoRepository = tablaTiposCatalogoRepository;
    }

    /**
     * {@code POST  /tabla-tipos-catalogos} : Create a new tablaTiposCatalogo.
     *
     * @param tablaTiposCatalogoDTO the tablaTiposCatalogoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tablaTiposCatalogoDTO, or with status {@code 400 (Bad Request)} if the tablaTiposCatalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tabla-tipos-catalogos")
    public Mono<ResponseEntity<TablaTiposCatalogoDTO>> createTablaTiposCatalogo(
        @Valid @RequestBody TablaTiposCatalogoDTO tablaTiposCatalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TablaTiposCatalogo : {}", tablaTiposCatalogoDTO);
        if (tablaTiposCatalogoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tablaTiposCatalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tablaTiposCatalogoService
            .save(tablaTiposCatalogoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tabla-tipos-catalogos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tabla-tipos-catalogos/:id} : Updates an existing tablaTiposCatalogo.
     *
     * @param id the id of the tablaTiposCatalogoDTO to save.
     * @param tablaTiposCatalogoDTO the tablaTiposCatalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaTiposCatalogoDTO,
     * or with status {@code 400 (Bad Request)} if the tablaTiposCatalogoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tablaTiposCatalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tabla-tipos-catalogos/{id}")
    public Mono<ResponseEntity<TablaTiposCatalogoDTO>> updateTablaTiposCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TablaTiposCatalogoDTO tablaTiposCatalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TablaTiposCatalogo : {}, {}", id, tablaTiposCatalogoDTO);
        if (tablaTiposCatalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaTiposCatalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaTiposCatalogoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tablaTiposCatalogoService
                    .update(tablaTiposCatalogoDTO)
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
     * {@code PATCH  /tabla-tipos-catalogos/:id} : Partial updates given fields of an existing tablaTiposCatalogo, field will ignore if it is null
     *
     * @param id the id of the tablaTiposCatalogoDTO to save.
     * @param tablaTiposCatalogoDTO the tablaTiposCatalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaTiposCatalogoDTO,
     * or with status {@code 400 (Bad Request)} if the tablaTiposCatalogoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tablaTiposCatalogoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tablaTiposCatalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tabla-tipos-catalogos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TablaTiposCatalogoDTO>> partialUpdateTablaTiposCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TablaTiposCatalogoDTO tablaTiposCatalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TablaTiposCatalogo partially : {}, {}", id, tablaTiposCatalogoDTO);
        if (tablaTiposCatalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaTiposCatalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaTiposCatalogoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TablaTiposCatalogoDTO> result = tablaTiposCatalogoService.partialUpdate(tablaTiposCatalogoDTO);

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
     * {@code GET  /tabla-tipos-catalogos} : get all the tablaTiposCatalogos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tablaTiposCatalogos in body.
     */
    @GetMapping("/tabla-tipos-catalogos")
    public Mono<ResponseEntity<List<TablaTiposCatalogoDTO>>> getAllTablaTiposCatalogos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of TablaTiposCatalogos");
        return tablaTiposCatalogoService
            .countAll()
            .zipWith(tablaTiposCatalogoService.findAll(pageable).collectList())
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
     * {@code GET  /tabla-tipos-catalogos/:id} : get the "id" tablaTiposCatalogo.
     *
     * @param id the id of the tablaTiposCatalogoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tablaTiposCatalogoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tabla-tipos-catalogos/{id}")
    public Mono<ResponseEntity<TablaTiposCatalogoDTO>> getTablaTiposCatalogo(@PathVariable Long id) {
        log.debug("REST request to get TablaTiposCatalogo : {}", id);
        Mono<TablaTiposCatalogoDTO> tablaTiposCatalogoDTO = tablaTiposCatalogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tablaTiposCatalogoDTO);
    }

    /**
     * {@code DELETE  /tabla-tipos-catalogos/:id} : delete the "id" tablaTiposCatalogo.
     *
     * @param id the id of the tablaTiposCatalogoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tabla-tipos-catalogos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTablaTiposCatalogo(@PathVariable Long id) {
        log.debug("REST request to delete TablaTiposCatalogo : {}", id);
        return tablaTiposCatalogoService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
