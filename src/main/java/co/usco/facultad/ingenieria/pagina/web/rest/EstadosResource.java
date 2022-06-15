package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.EstadosRepository;
import co.usco.facultad.ingenieria.pagina.service.EstadosService;
import co.usco.facultad.ingenieria.pagina.service.dto.EstadosDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.Estados}.
 */
@RestController
@RequestMapping("/api")
public class EstadosResource {

    private final Logger log = LoggerFactory.getLogger(EstadosResource.class);

    private static final String ENTITY_NAME = "estados";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstadosService estadosService;

    private final EstadosRepository estadosRepository;

    public EstadosResource(EstadosService estadosService, EstadosRepository estadosRepository) {
        this.estadosService = estadosService;
        this.estadosRepository = estadosRepository;
    }

    /**
     * {@code POST  /estados} : Create a new estados.
     *
     * @param estadosDTO the estadosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estadosDTO, or with status {@code 400 (Bad Request)} if the estados has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/estados")
    public Mono<ResponseEntity<EstadosDTO>> createEstados(@Valid @RequestBody EstadosDTO estadosDTO) throws URISyntaxException {
        log.debug("REST request to save Estados : {}", estadosDTO);
        if (estadosDTO.getId() != null) {
            throw new BadRequestAlertException("A new estados cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return estadosService
            .save(estadosDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/estados/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /estados/:id} : Updates an existing estados.
     *
     * @param id the id of the estadosDTO to save.
     * @param estadosDTO the estadosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadosDTO,
     * or with status {@code 400 (Bad Request)} if the estadosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estadosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/estados/{id}")
    public Mono<ResponseEntity<EstadosDTO>> updateEstados(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstadosDTO estadosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Estados : {}, {}", id, estadosDTO);
        if (estadosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return estadosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return estadosService
                    .update(estadosDTO)
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
     * {@code PATCH  /estados/:id} : Partial updates given fields of an existing estados, field will ignore if it is null
     *
     * @param id the id of the estadosDTO to save.
     * @param estadosDTO the estadosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadosDTO,
     * or with status {@code 400 (Bad Request)} if the estadosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estadosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estadosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/estados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EstadosDTO>> partialUpdateEstados(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstadosDTO estadosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estados partially : {}, {}", id, estadosDTO);
        if (estadosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return estadosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EstadosDTO> result = estadosService.partialUpdate(estadosDTO);

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
     * {@code GET  /estados} : get all the estados.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estados in body.
     */
    @GetMapping("/estados")
    public Mono<ResponseEntity<List<EstadosDTO>>> getAllEstados(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Estados");
        return estadosService
            .countAll()
            .zipWith(estadosService.findAll(pageable).collectList())
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
     * {@code GET  /estados/:id} : get the "id" estados.
     *
     * @param id the id of the estadosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estadosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/estados/{id}")
    public Mono<ResponseEntity<EstadosDTO>> getEstados(@PathVariable Long id) {
        log.debug("REST request to get Estados : {}", id);
        Mono<EstadosDTO> estadosDTO = estadosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estadosDTO);
    }

    /**
     * {@code DELETE  /estados/:id} : delete the "id" estados.
     *
     * @param id the id of the estadosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/estados/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEstados(@PathVariable Long id) {
        log.debug("REST request to delete Estados : {}", id);
        return estadosService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
