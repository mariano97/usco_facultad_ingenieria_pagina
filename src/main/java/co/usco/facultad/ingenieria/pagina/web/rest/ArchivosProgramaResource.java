package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.ArchivosProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.ArchivosProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma}.
 */
@RestController
@RequestMapping("/api")
public class ArchivosProgramaResource {

    private final Logger log = LoggerFactory.getLogger(ArchivosProgramaResource.class);

    private static final String ENTITY_NAME = "archivosPrograma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArchivosProgramaService archivosProgramaService;

    private final ArchivosProgramaRepository archivosProgramaRepository;

    public ArchivosProgramaResource(
        ArchivosProgramaService archivosProgramaService,
        ArchivosProgramaRepository archivosProgramaRepository
    ) {
        this.archivosProgramaService = archivosProgramaService;
        this.archivosProgramaRepository = archivosProgramaRepository;
    }

    /**
     * {@code POST  /archivos-programas} : Create a new archivosPrograma.
     *
     * @param archivosProgramaDTO the archivosProgramaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new archivosProgramaDTO, or with status {@code 400 (Bad Request)} if the archivosPrograma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/archivos-programas")
    public Mono<ResponseEntity<ArchivosProgramaDTO>> createArchivosPrograma(@Valid @RequestBody ArchivosProgramaDTO archivosProgramaDTO)
        throws URISyntaxException {
        log.debug("REST request to save ArchivosPrograma : {}", archivosProgramaDTO);
        if (archivosProgramaDTO.getId() != null) {
            throw new BadRequestAlertException("A new archivosPrograma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return archivosProgramaService
            .save(archivosProgramaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/archivos-programas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /archivos-programas/:id} : Updates an existing archivosPrograma.
     *
     * @param id the id of the archivosProgramaDTO to save.
     * @param archivosProgramaDTO the archivosProgramaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archivosProgramaDTO,
     * or with status {@code 400 (Bad Request)} if the archivosProgramaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the archivosProgramaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/archivos-programas/{id}")
    public Mono<ResponseEntity<ArchivosProgramaDTO>> updateArchivosPrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArchivosProgramaDTO archivosProgramaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ArchivosPrograma : {}, {}", id, archivosProgramaDTO);
        if (archivosProgramaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archivosProgramaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return archivosProgramaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return archivosProgramaService
                    .update(archivosProgramaDTO)
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
     * {@code PATCH  /archivos-programas/:id} : Partial updates given fields of an existing archivosPrograma, field will ignore if it is null
     *
     * @param id the id of the archivosProgramaDTO to save.
     * @param archivosProgramaDTO the archivosProgramaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archivosProgramaDTO,
     * or with status {@code 400 (Bad Request)} if the archivosProgramaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the archivosProgramaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the archivosProgramaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/archivos-programas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ArchivosProgramaDTO>> partialUpdateArchivosPrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArchivosProgramaDTO archivosProgramaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArchivosPrograma partially : {}, {}", id, archivosProgramaDTO);
        if (archivosProgramaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archivosProgramaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return archivosProgramaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ArchivosProgramaDTO> result = archivosProgramaService.partialUpdate(archivosProgramaDTO);

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
     * {@code GET  /archivos-programas} : get all the archivosProgramas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of archivosProgramas in body.
     */
    @GetMapping("/archivos-programas")
    public Mono<ResponseEntity<List<ArchivosProgramaDTO>>> getAllArchivosProgramas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ArchivosProgramas");
        return archivosProgramaService
            .countAll()
            .zipWith(archivosProgramaService.findAll(pageable).collectList())
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
        "/open/archivos-programas/by-programa-id/{id}",
        "/archivos-programas/by-programa-id/{id}"
    })
    public Mono<ResponseEntity<List<ArchivosProgramaDTO>>> getAllByProgramaId(
        @PathVariable("id") Long programaId
    ) {
        return archivosProgramaService.findAllByPrograma(programaId).collectList()
            .map(archivosProgramaDTOS ->
                ResponseEntity.ok()
                    .body(archivosProgramaDTOS)
            );
    }

    /**
     * {@code GET  /archivos-programas/:id} : get the "id" archivosPrograma.
     *
     * @param id the id of the archivosProgramaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the archivosProgramaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/archivos-programas/{id}")
    public Mono<ResponseEntity<ArchivosProgramaDTO>> getArchivosPrograma(@PathVariable Long id) {
        log.debug("REST request to get ArchivosPrograma : {}", id);
        Mono<ArchivosProgramaDTO> archivosProgramaDTO = archivosProgramaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(archivosProgramaDTO);
    }

    /**
     * {@code DELETE  /archivos-programas/:id} : delete the "id" archivosPrograma.
     *
     * @param id the id of the archivosProgramaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/archivos-programas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteArchivosPrograma(@PathVariable Long id) {
        log.debug("REST request to delete ArchivosPrograma : {}", id);
        return archivosProgramaService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
