package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Paises;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.TituloAcademicoProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.TituloAcademicoProfesorService;
import co.usco.facultad.ingenieria.pagina.service.dto.TituloAcademicoProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.TituloAcademicoProfesorMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link TituloAcademicoProfesorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TituloAcademicoProfesorResourceIT {

    private static final String DEFAULT_NOMBRE_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_UNIVERSIDAD_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_UNIVERSIDAD_TITULO = "BBBBBBBBBB";

    private static final Instant DEFAULT_YEAR_TITULO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_YEAR_TITULO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/titulo-academico-profesors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TituloAcademicoProfesorRepository tituloAcademicoProfesorRepository;

    @Mock
    private TituloAcademicoProfesorRepository tituloAcademicoProfesorRepositoryMock;

    @Autowired
    private TituloAcademicoProfesorMapper tituloAcademicoProfesorMapper;

    @Mock
    private TituloAcademicoProfesorService tituloAcademicoProfesorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TituloAcademicoProfesor tituloAcademicoProfesor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TituloAcademicoProfesor createEntity(EntityManager em) {
        TituloAcademicoProfesor tituloAcademicoProfesor = new TituloAcademicoProfesor()
            .nombreTitulo(DEFAULT_NOMBRE_TITULO)
            .nombreUniversidadTitulo(DEFAULT_NOMBRE_UNIVERSIDAD_TITULO)
            .yearTitulo(DEFAULT_YEAR_TITULO);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createEntity(em)).block();
        tituloAcademicoProfesor.setTablaElementoCatalogo(tablaElementoCatalogo);
        // Add required entity
        Profesor profesor;
        profesor = em.insert(ProfesorResourceIT.createEntity(em)).block();
        tituloAcademicoProfesor.setProfesor(profesor);
        // Add required entity
        Paises paises;
        paises = em.insert(PaisesResourceIT.createEntity(em)).block();
        tituloAcademicoProfesor.setPaises(paises);
        return tituloAcademicoProfesor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TituloAcademicoProfesor createUpdatedEntity(EntityManager em) {
        TituloAcademicoProfesor tituloAcademicoProfesor = new TituloAcademicoProfesor()
            .nombreTitulo(UPDATED_NOMBRE_TITULO)
            .nombreUniversidadTitulo(UPDATED_NOMBRE_UNIVERSIDAD_TITULO)
            .yearTitulo(UPDATED_YEAR_TITULO);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createUpdatedEntity(em)).block();
        tituloAcademicoProfesor.setTablaElementoCatalogo(tablaElementoCatalogo);
        // Add required entity
        Profesor profesor;
        profesor = em.insert(ProfesorResourceIT.createUpdatedEntity(em)).block();
        tituloAcademicoProfesor.setProfesor(profesor);
        // Add required entity
        Paises paises;
        paises = em.insert(PaisesResourceIT.createUpdatedEntity(em)).block();
        tituloAcademicoProfesor.setPaises(paises);
        return tituloAcademicoProfesor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TituloAcademicoProfesor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TablaElementoCatalogoResourceIT.deleteEntities(em);
        ProfesorResourceIT.deleteEntities(em);
        PaisesResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        tituloAcademicoProfesor = createEntity(em);
    }

    @Test
    void createTituloAcademicoProfesor() throws Exception {
        int databaseSizeBeforeCreate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        // Create the TituloAcademicoProfesor
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeCreate + 1);
        TituloAcademicoProfesor testTituloAcademicoProfesor = tituloAcademicoProfesorList.get(tituloAcademicoProfesorList.size() - 1);
        assertThat(testTituloAcademicoProfesor.getNombreTitulo()).isEqualTo(DEFAULT_NOMBRE_TITULO);
        assertThat(testTituloAcademicoProfesor.getNombreUniversidadTitulo()).isEqualTo(DEFAULT_NOMBRE_UNIVERSIDAD_TITULO);
        assertThat(testTituloAcademicoProfesor.getYearTitulo()).isEqualTo(DEFAULT_YEAR_TITULO);
    }

    @Test
    void createTituloAcademicoProfesorWithExistingId() throws Exception {
        // Create the TituloAcademicoProfesor with an existing ID
        tituloAcademicoProfesor.setId(1L);
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        int databaseSizeBeforeCreate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        // set the field null
        tituloAcademicoProfesor.setNombreTitulo(null);

        // Create the TituloAcademicoProfesor, which fails.
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreUniversidadTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        // set the field null
        tituloAcademicoProfesor.setNombreUniversidadTitulo(null);

        // Create the TituloAcademicoProfesor, which fails.
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkYearTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        // set the field null
        tituloAcademicoProfesor.setYearTitulo(null);

        // Create the TituloAcademicoProfesor, which fails.
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTituloAcademicoProfesors() {
        // Initialize the database
        tituloAcademicoProfesorRepository.save(tituloAcademicoProfesor).block();

        // Get all the tituloAcademicoProfesorList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(tituloAcademicoProfesor.getId().intValue()))
            .jsonPath("$.[*].nombreTitulo")
            .value(hasItem(DEFAULT_NOMBRE_TITULO))
            .jsonPath("$.[*].nombreUniversidadTitulo")
            .value(hasItem(DEFAULT_NOMBRE_UNIVERSIDAD_TITULO))
            .jsonPath("$.[*].yearTitulo")
            .value(hasItem(DEFAULT_YEAR_TITULO.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTituloAcademicoProfesorsWithEagerRelationshipsIsEnabled() {
        when(tituloAcademicoProfesorServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tituloAcademicoProfesorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTituloAcademicoProfesorsWithEagerRelationshipsIsNotEnabled() {
        when(tituloAcademicoProfesorServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tituloAcademicoProfesorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getTituloAcademicoProfesor() {
        // Initialize the database
        tituloAcademicoProfesorRepository.save(tituloAcademicoProfesor).block();

        // Get the tituloAcademicoProfesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tituloAcademicoProfesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tituloAcademicoProfesor.getId().intValue()))
            .jsonPath("$.nombreTitulo")
            .value(is(DEFAULT_NOMBRE_TITULO))
            .jsonPath("$.nombreUniversidadTitulo")
            .value(is(DEFAULT_NOMBRE_UNIVERSIDAD_TITULO))
            .jsonPath("$.yearTitulo")
            .value(is(DEFAULT_YEAR_TITULO.toString()));
    }

    @Test
    void getNonExistingTituloAcademicoProfesor() {
        // Get the tituloAcademicoProfesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTituloAcademicoProfesor() throws Exception {
        // Initialize the database
        tituloAcademicoProfesorRepository.save(tituloAcademicoProfesor).block();

        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();

        // Update the tituloAcademicoProfesor
        TituloAcademicoProfesor updatedTituloAcademicoProfesor = tituloAcademicoProfesorRepository
            .findById(tituloAcademicoProfesor.getId())
            .block();
        updatedTituloAcademicoProfesor
            .nombreTitulo(UPDATED_NOMBRE_TITULO)
            .nombreUniversidadTitulo(UPDATED_NOMBRE_UNIVERSIDAD_TITULO)
            .yearTitulo(UPDATED_YEAR_TITULO);
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(updatedTituloAcademicoProfesor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tituloAcademicoProfesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
        TituloAcademicoProfesor testTituloAcademicoProfesor = tituloAcademicoProfesorList.get(tituloAcademicoProfesorList.size() - 1);
        assertThat(testTituloAcademicoProfesor.getNombreTitulo()).isEqualTo(UPDATED_NOMBRE_TITULO);
        assertThat(testTituloAcademicoProfesor.getNombreUniversidadTitulo()).isEqualTo(UPDATED_NOMBRE_UNIVERSIDAD_TITULO);
        assertThat(testTituloAcademicoProfesor.getYearTitulo()).isEqualTo(UPDATED_YEAR_TITULO);
    }

    @Test
    void putNonExistingTituloAcademicoProfesor() throws Exception {
        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        tituloAcademicoProfesor.setId(count.incrementAndGet());

        // Create the TituloAcademicoProfesor
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tituloAcademicoProfesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTituloAcademicoProfesor() throws Exception {
        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        tituloAcademicoProfesor.setId(count.incrementAndGet());

        // Create the TituloAcademicoProfesor
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTituloAcademicoProfesor() throws Exception {
        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        tituloAcademicoProfesor.setId(count.incrementAndGet());

        // Create the TituloAcademicoProfesor
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTituloAcademicoProfesorWithPatch() throws Exception {
        // Initialize the database
        tituloAcademicoProfesorRepository.save(tituloAcademicoProfesor).block();

        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();

        // Update the tituloAcademicoProfesor using partial update
        TituloAcademicoProfesor partialUpdatedTituloAcademicoProfesor = new TituloAcademicoProfesor();
        partialUpdatedTituloAcademicoProfesor.setId(tituloAcademicoProfesor.getId());

        partialUpdatedTituloAcademicoProfesor.nombreUniversidadTitulo(UPDATED_NOMBRE_UNIVERSIDAD_TITULO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTituloAcademicoProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTituloAcademicoProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
        TituloAcademicoProfesor testTituloAcademicoProfesor = tituloAcademicoProfesorList.get(tituloAcademicoProfesorList.size() - 1);
        assertThat(testTituloAcademicoProfesor.getNombreTitulo()).isEqualTo(DEFAULT_NOMBRE_TITULO);
        assertThat(testTituloAcademicoProfesor.getNombreUniversidadTitulo()).isEqualTo(UPDATED_NOMBRE_UNIVERSIDAD_TITULO);
        assertThat(testTituloAcademicoProfesor.getYearTitulo()).isEqualTo(DEFAULT_YEAR_TITULO);
    }

    @Test
    void fullUpdateTituloAcademicoProfesorWithPatch() throws Exception {
        // Initialize the database
        tituloAcademicoProfesorRepository.save(tituloAcademicoProfesor).block();

        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();

        // Update the tituloAcademicoProfesor using partial update
        TituloAcademicoProfesor partialUpdatedTituloAcademicoProfesor = new TituloAcademicoProfesor();
        partialUpdatedTituloAcademicoProfesor.setId(tituloAcademicoProfesor.getId());

        partialUpdatedTituloAcademicoProfesor
            .nombreTitulo(UPDATED_NOMBRE_TITULO)
            .nombreUniversidadTitulo(UPDATED_NOMBRE_UNIVERSIDAD_TITULO)
            .yearTitulo(UPDATED_YEAR_TITULO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTituloAcademicoProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTituloAcademicoProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
        TituloAcademicoProfesor testTituloAcademicoProfesor = tituloAcademicoProfesorList.get(tituloAcademicoProfesorList.size() - 1);
        assertThat(testTituloAcademicoProfesor.getNombreTitulo()).isEqualTo(UPDATED_NOMBRE_TITULO);
        assertThat(testTituloAcademicoProfesor.getNombreUniversidadTitulo()).isEqualTo(UPDATED_NOMBRE_UNIVERSIDAD_TITULO);
        assertThat(testTituloAcademicoProfesor.getYearTitulo()).isEqualTo(UPDATED_YEAR_TITULO);
    }

    @Test
    void patchNonExistingTituloAcademicoProfesor() throws Exception {
        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        tituloAcademicoProfesor.setId(count.incrementAndGet());

        // Create the TituloAcademicoProfesor
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tituloAcademicoProfesorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTituloAcademicoProfesor() throws Exception {
        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        tituloAcademicoProfesor.setId(count.incrementAndGet());

        // Create the TituloAcademicoProfesor
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTituloAcademicoProfesor() throws Exception {
        int databaseSizeBeforeUpdate = tituloAcademicoProfesorRepository.findAll().collectList().block().size();
        tituloAcademicoProfesor.setId(count.incrementAndGet());

        // Create the TituloAcademicoProfesor
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = tituloAcademicoProfesorMapper.toDto(tituloAcademicoProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tituloAcademicoProfesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TituloAcademicoProfesor in the database
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTituloAcademicoProfesor() {
        // Initialize the database
        tituloAcademicoProfesorRepository.save(tituloAcademicoProfesor).block();

        int databaseSizeBeforeDelete = tituloAcademicoProfesorRepository.findAll().collectList().block().size();

        // Delete the tituloAcademicoProfesor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tituloAcademicoProfesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TituloAcademicoProfesor> tituloAcademicoProfesorList = tituloAcademicoProfesorRepository.findAll().collectList().block();
        assertThat(tituloAcademicoProfesorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
