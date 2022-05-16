package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.repository.TablaElementoCatalogoRepository;
import co.usco.facultad.ingenieria.pagina.service.TablaElementoCatalogoService;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
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
 * REST controller for managing {@link co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo}.
 */
@RestController
@RequestMapping("/api")
public class TablaElementoCatalogoResource {

    private final Logger log = LoggerFactory.getLogger(TablaElementoCatalogoResource.class);

    private static final String ENTITY_NAME = "tablaElementoCatalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TablaElementoCatalogoService tablaElementoCatalogoService;

    private final TablaElementoCatalogoRepository tablaElementoCatalogoRepository;

    public TablaElementoCatalogoResource(
        TablaElementoCatalogoService tablaElementoCatalogoService,
        TablaElementoCatalogoRepository tablaElementoCatalogoRepository
    ) {
        this.tablaElementoCatalogoService = tablaElementoCatalogoService;
        this.tablaElementoCatalogoRepository = tablaElementoCatalogoRepository;
    }

    /**
     * {@code POST  /tabla-elemento-catalogos} : Create a new tablaElementoCatalogo.
     *
     * @param tablaElementoCatalogoDTO the tablaElementoCatalogoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tablaElementoCatalogoDTO, or with status {@code 400 (Bad Request)} if the tablaElementoCatalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tabla-elemento-catalogos")
    public Mono<ResponseEntity<TablaElementoCatalogoDTO>> createTablaElementoCatalogo(
        @Valid @RequestBody TablaElementoCatalogoDTO tablaElementoCatalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TablaElementoCatalogo : {}", tablaElementoCatalogoDTO);
        if (tablaElementoCatalogoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tablaElementoCatalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tablaElementoCatalogoService
            .save(tablaElementoCatalogoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tabla-elemento-catalogos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tabla-elemento-catalogos/:id} : Updates an existing tablaElementoCatalogo.
     *
     * @param id the id of the tablaElementoCatalogoDTO to save.
     * @param tablaElementoCatalogoDTO the tablaElementoCatalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaElementoCatalogoDTO,
     * or with status {@code 400 (Bad Request)} if the tablaElementoCatalogoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tablaElementoCatalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tabla-elemento-catalogos/{id}")
    public Mono<ResponseEntity<TablaElementoCatalogoDTO>> updateTablaElementoCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TablaElementoCatalogoDTO tablaElementoCatalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TablaElementoCatalogo : {}, {}", id, tablaElementoCatalogoDTO);
        if (tablaElementoCatalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaElementoCatalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaElementoCatalogoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tablaElementoCatalogoService
                    .update(tablaElementoCatalogoDTO)
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
     * {@code PATCH  /tabla-elemento-catalogos/:id} : Partial updates given fields of an existing tablaElementoCatalogo, field will ignore if it is null
     *
     * @param id the id of the tablaElementoCatalogoDTO to save.
     * @param tablaElementoCatalogoDTO the tablaElementoCatalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tablaElementoCatalogoDTO,
     * or with status {@code 400 (Bad Request)} if the tablaElementoCatalogoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tablaElementoCatalogoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tablaElementoCatalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tabla-elemento-catalogos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TablaElementoCatalogoDTO>> partialUpdateTablaElementoCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TablaElementoCatalogoDTO tablaElementoCatalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TablaElementoCatalogo partially : {}, {}", id, tablaElementoCatalogoDTO);
        if (tablaElementoCatalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tablaElementoCatalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tablaElementoCatalogoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TablaElementoCatalogoDTO> result = tablaElementoCatalogoService.partialUpdate(tablaElementoCatalogoDTO);

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

    @GetMapping(value = {
        "/open/tabla-elemento-catalogos/tipo-catalogo-key-identificador",
        "/tabla-elemento-catalogos/tipo-catalogo-key-identificador"
    })
    public Mono<ResponseEntity<List<TablaElementoCatalogoDTO>>> getElementosCatalogoByTipoCatalogoKeyIdentificador(
        @RequestParam("key_identificador") String keyIdentificador
    ) {
        log.debug("REST request get elementos catalogo by Tipo Catalogo Key Identificador");
        log.debug("param: key_identificador: {}", keyIdentificador);

        Mono<List<TablaElementoCatalogoDTO>> listaTablaElementoCatalogoDTO = tablaElementoCatalogoService
            .findByTiposCatalogoKeyIdentificador(keyIdentificador).collectList();

        return ResponseUtil.wrapOrNotFound(listaTablaElementoCatalogoDTO);
    }

    /**
     * {@code GET  /tabla-elemento-catalogos} : get all the tablaElementoCatalogos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tablaElementoCatalogos in body.
     */
    @GetMapping("/tabla-elemento-catalogos")
    public Mono<ResponseEntity<List<TablaElementoCatalogoDTO>>> getAllTablaElementoCatalogos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TablaElementoCatalogos");
        return tablaElementoCatalogoService
            .countAll()
            .zipWith(tablaElementoCatalogoService.findAll(pageable).collectList())
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
     * {@code GET  /tabla-elemento-catalogos/:id} : get the "id" tablaElementoCatalogo.
     *
     * @param id the id of the tablaElementoCatalogoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tablaElementoCatalogoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tabla-elemento-catalogos/{id}")
    public Mono<ResponseEntity<TablaElementoCatalogoDTO>> getTablaElementoCatalogo(@PathVariable Long id) {
        log.debug("REST request to get TablaElementoCatalogo : {}", id);
        Mono<TablaElementoCatalogoDTO> tablaElementoCatalogoDTO = tablaElementoCatalogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tablaElementoCatalogoDTO);
    }

    /**
     * {@code DELETE  /tabla-elemento-catalogos/:id} : delete the "id" tablaElementoCatalogo.
     *
     * @param id the id of the tablaElementoCatalogoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tabla-elemento-catalogos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTablaElementoCatalogo(@PathVariable Long id) {
        log.debug("REST request to delete TablaElementoCatalogo : {}", id);
        return tablaElementoCatalogoService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
