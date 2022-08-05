package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.EscalafonProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.EscalafonProfesorService;
import co.usco.facultad.ingenieria.pagina.service.dto.EscalafonProfesorDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor}.
 */
@RestController
@RequestMapping("/api")
public class EscalafonProfesorResource {

    private final Logger log = LoggerFactory.getLogger(EscalafonProfesorResource.class);

    private static final String ENTITY_NAME = "escalafonProfesor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EscalafonProfesorService escalafonProfesorService;

    private final EscalafonProfesorRepository escalafonProfesorRepository;

    public EscalafonProfesorResource(
        EscalafonProfesorService escalafonProfesorService,
        EscalafonProfesorRepository escalafonProfesorRepository
    ) {
        this.escalafonProfesorService = escalafonProfesorService;
        this.escalafonProfesorRepository = escalafonProfesorRepository;
    }

    /**
     * {@code POST  /escalafon-profesors} : Create a new escalafonProfesor.
     *
     * @param escalafonProfesorDTO the escalafonProfesorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new escalafonProfesorDTO, or with status {@code 400 (Bad Request)} if the escalafonProfesor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/escalafon-profesors")
    public Mono<ResponseEntity<EscalafonProfesorDTO>> createEscalafonProfesor(
        @Valid @RequestBody EscalafonProfesorDTO escalafonProfesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to save EscalafonProfesor : {}", escalafonProfesorDTO);
        if (escalafonProfesorDTO.getId() != null) {
            throw new BadRequestAlertException("A new escalafonProfesor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return escalafonProfesorService
            .save(escalafonProfesorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/escalafon-profesors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /escalafon-profesors/:id} : Updates an existing escalafonProfesor.
     *
     * @param id the id of the escalafonProfesorDTO to save.
     * @param escalafonProfesorDTO the escalafonProfesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated escalafonProfesorDTO,
     * or with status {@code 400 (Bad Request)} if the escalafonProfesorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the escalafonProfesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/escalafon-profesors/{id}")
    public Mono<ResponseEntity<EscalafonProfesorDTO>> updateEscalafonProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EscalafonProfesorDTO escalafonProfesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EscalafonProfesor : {}, {}", id, escalafonProfesorDTO);
        if (escalafonProfesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, escalafonProfesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return escalafonProfesorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return escalafonProfesorService
                    .update(escalafonProfesorDTO)
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
     * {@code PATCH  /escalafon-profesors/:id} : Partial updates given fields of an existing escalafonProfesor, field will ignore if it is null
     *
     * @param id the id of the escalafonProfesorDTO to save.
     * @param escalafonProfesorDTO the escalafonProfesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated escalafonProfesorDTO,
     * or with status {@code 400 (Bad Request)} if the escalafonProfesorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the escalafonProfesorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the escalafonProfesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/escalafon-profesors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EscalafonProfesorDTO>> partialUpdateEscalafonProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EscalafonProfesorDTO escalafonProfesorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EscalafonProfesor partially : {}, {}", id, escalafonProfesorDTO);
        if (escalafonProfesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, escalafonProfesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return escalafonProfesorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EscalafonProfesorDTO> result = escalafonProfesorService.partialUpdate(escalafonProfesorDTO);

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
     * {@code GET  /escalafon-profesors} : get all the escalafonProfesors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of escalafonProfesors in body.
     */
    @GetMapping("/escalafon-profesors")
    public Mono<ResponseEntity<List<EscalafonProfesorDTO>>> getAllEscalafonProfesors(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of EscalafonProfesors");
        return escalafonProfesorService
            .countAll()
            .zipWith(escalafonProfesorService.findAll(pageable).collectList())
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
        "/escalafon-profesors/by-profesor-id",
        "/open/escalafon-profesors/by-profesor-id"
    })
    public Mono<ResponseEntity<List<EscalafonProfesorDTO>>> getEscalafonProfesorByProfesorId(@RequestParam("profesorId") Long profesorId) {
        return escalafonProfesorService.findByProfesorId(profesorId).collectList()
            .map(escalafonProfesorDTOS -> ResponseEntity.ok().body(escalafonProfesorDTOS));
    }

    /**
     * {@code GET  /escalafon-profesors/:id} : get the "id" escalafonProfesor.
     *
     * @param id the id of the escalafonProfesorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the escalafonProfesorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/escalafon-profesors/{id}")
    public Mono<ResponseEntity<EscalafonProfesorDTO>> getEscalafonProfesor(@PathVariable Long id) {
        log.debug("REST request to get EscalafonProfesor : {}", id);
        Mono<EscalafonProfesorDTO> escalafonProfesorDTO = escalafonProfesorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(escalafonProfesorDTO);
    }

    /**
     * {@code DELETE  /escalafon-profesors/:id} : delete the "id" escalafonProfesor.
     *
     * @param id the id of the escalafonProfesorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/escalafon-profesors/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEscalafonProfesor(@PathVariable Long id) {
        log.debug("REST request to delete EscalafonProfesor : {}", id);
        return escalafonProfesorService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
