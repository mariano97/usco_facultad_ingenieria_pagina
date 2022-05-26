package co.usco.facultad.ingenieria.pagina.web.rest;

import static co.usco.facultad.ingenieria.pagina.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.ProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.ProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.ProgramaMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProgramaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProgramaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Long DEFAULT_CODIGO_SNIES = 1L;
    private static final Long UPDATED_CODIGO_SNIES = 2L;

    private static final Long DEFAULT_CODIGO_REGISTRO_CALIFICADO = 1L;
    private static final Long UPDATED_CODIGO_REGISTRO_CALIFICADO = 2L;

    private static final Instant DEFAULT_FECHA_REGISTRO_CALIFICADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO_CALIFICADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOMBRE_TITULO_OTORGADO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_TITULO_OTORGADO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_CREDITOS = 1;
    private static final Integer UPDATED_NUMERO_CREDITOS = 2;

    private static final Integer DEFAULT_DURACION_PROGRAMA = 1;
    private static final Integer UPDATED_DURACION_PROGRAMA = 2;

    private static final String DEFAULT_PRESENTACION_PROGRAMA = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTACION_PROGRAMA = "BBBBBBBBBB";

    private static final String DEFAULT_MISION = "AAAAAAAAAA";
    private static final String UPDATED_MISION = "BBBBBBBBBB";

    private static final String DEFAULT_VISION = "AAAAAAAAAA";
    private static final String UPDATED_VISION = "BBBBBBBBBB";

    private static final String DEFAULT_PERFIL_ESTUDIANTE = "AAAAAAAAAA";
    private static final String UPDATED_PERFIL_ESTUDIANTE = "BBBBBBBBBB";

    private static final String DEFAULT_PERFIL_EGRESADO = "AAAAAAAAAA";
    private static final String UPDATED_PERFIL_EGRESADO = "BBBBBBBBBB";

    private static final String DEFAULT_PERFIL_OCUPACIONAL = "AAAAAAAAAA";
    private static final String UPDATED_PERFIL_OCUPACIONAL = "BBBBBBBBBB";

    private static final String DEFAULT_URL_FOTO_PROGRAMA = "AAAAAAAAAA";
    private static final String UPDATED_URL_FOTO_PROGRAMA = "BBBBBBBBBB";

    private static final String DEFAULT_DIRIGIDO_A_QUIEN = "AAAAAAAAAA";
    private static final String UPDATED_DIRIGIDO_A_QUIEN = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COSTO_PROGRAMA = new BigDecimal(1);
    private static final BigDecimal UPDATED_COSTO_PROGRAMA = new BigDecimal(2);

    private static final Boolean DEFAULT_ESTADO = false;
    private static final Boolean UPDATED_ESTADO = true;

    private static final String ENTITY_API_URL = "/api/programas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgramaRepository programaRepository;

    @Mock
    private ProgramaRepository programaRepositoryMock;

    @Autowired
    private ProgramaMapper programaMapper;

    @Mock
    private ProgramaService programaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Programa programa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programa createEntity(EntityManager em) {
        Programa programa = new Programa()
            .nombre(DEFAULT_NOMBRE)
            .codigoSnies(DEFAULT_CODIGO_SNIES)
            .codigoRegistroCalificado(DEFAULT_CODIGO_REGISTRO_CALIFICADO)
            .fechaRegistroCalificado(DEFAULT_FECHA_REGISTRO_CALIFICADO)
            .nombreTituloOtorgado(DEFAULT_NOMBRE_TITULO_OTORGADO)
            .numeroCreditos(DEFAULT_NUMERO_CREDITOS)
            .duracionPrograma(DEFAULT_DURACION_PROGRAMA)
            .presentacionPrograma(DEFAULT_PRESENTACION_PROGRAMA)
            .mision(DEFAULT_MISION)
            .vision(DEFAULT_VISION)
            .perfilEstudiante(DEFAULT_PERFIL_ESTUDIANTE)
            .perfilEgresado(DEFAULT_PERFIL_EGRESADO)
            .perfilOcupacional(DEFAULT_PERFIL_OCUPACIONAL)
            .urlFotoPrograma(DEFAULT_URL_FOTO_PROGRAMA)
            .dirigidoAQuien(DEFAULT_DIRIGIDO_A_QUIEN)
            .costoPrograma(DEFAULT_COSTO_PROGRAMA)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createEntity(em)).block();
        programa.setNivelFormacion(tablaElementoCatalogo);
        // Add required entity
        programa.setTipoFormacion(tablaElementoCatalogo);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createEntity(em)).block();
        programa.setFacultad(facultad);
        return programa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programa createUpdatedEntity(EntityManager em) {
        Programa programa = new Programa()
            .nombre(UPDATED_NOMBRE)
            .codigoSnies(UPDATED_CODIGO_SNIES)
            .codigoRegistroCalificado(UPDATED_CODIGO_REGISTRO_CALIFICADO)
            .fechaRegistroCalificado(UPDATED_FECHA_REGISTRO_CALIFICADO)
            .nombreTituloOtorgado(UPDATED_NOMBRE_TITULO_OTORGADO)
            .numeroCreditos(UPDATED_NUMERO_CREDITOS)
            .duracionPrograma(UPDATED_DURACION_PROGRAMA)
            .presentacionPrograma(UPDATED_PRESENTACION_PROGRAMA)
            .mision(UPDATED_MISION)
            .vision(UPDATED_VISION)
            .perfilEstudiante(UPDATED_PERFIL_ESTUDIANTE)
            .perfilEgresado(UPDATED_PERFIL_EGRESADO)
            .perfilOcupacional(UPDATED_PERFIL_OCUPACIONAL)
            .urlFotoPrograma(UPDATED_URL_FOTO_PROGRAMA)
            .dirigidoAQuien(UPDATED_DIRIGIDO_A_QUIEN)
            .costoPrograma(UPDATED_COSTO_PROGRAMA)
            .estado(UPDATED_ESTADO);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createUpdatedEntity(em)).block();
        programa.setNivelFormacion(tablaElementoCatalogo);
        // Add required entity
        programa.setTipoFormacion(tablaElementoCatalogo);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createUpdatedEntity(em)).block();
        programa.setFacultad(facultad);
        return programa;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_programa__sede").block();
            em.deleteAll(Programa.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TablaElementoCatalogoResourceIT.deleteEntities(em);
        FacultadResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        programa = createEntity(em);
    }

    @Test
    void createPrograma() throws Exception {
        int databaseSizeBeforeCreate = programaRepository.findAll().collectList().block().size();
        // Create the Programa
        ProgramaDTO programaDTO = programaMapper.toDto(programa);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeCreate + 1);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPrograma.getCodigoSnies()).isEqualTo(DEFAULT_CODIGO_SNIES);
        assertThat(testPrograma.getCodigoRegistroCalificado()).isEqualTo(DEFAULT_CODIGO_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getFechaRegistroCalificado()).isEqualTo(DEFAULT_FECHA_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getNombreTituloOtorgado()).isEqualTo(DEFAULT_NOMBRE_TITULO_OTORGADO);
        assertThat(testPrograma.getNumeroCreditos()).isEqualTo(DEFAULT_NUMERO_CREDITOS);
        assertThat(testPrograma.getDuracionPrograma()).isEqualTo(DEFAULT_DURACION_PROGRAMA);
        assertThat(testPrograma.getPresentacionPrograma()).isEqualTo(DEFAULT_PRESENTACION_PROGRAMA);
        assertThat(testPrograma.getMision()).isEqualTo(DEFAULT_MISION);
        assertThat(testPrograma.getVision()).isEqualTo(DEFAULT_VISION);
        assertThat(testPrograma.getPerfilEstudiante()).isEqualTo(DEFAULT_PERFIL_ESTUDIANTE);
        assertThat(testPrograma.getPerfilEgresado()).isEqualTo(DEFAULT_PERFIL_EGRESADO);
        assertThat(testPrograma.getPerfilOcupacional()).isEqualTo(DEFAULT_PERFIL_OCUPACIONAL);
        assertThat(testPrograma.getUrlFotoPrograma()).isEqualTo(DEFAULT_URL_FOTO_PROGRAMA);
        assertThat(testPrograma.getDirigidoAQuien()).isEqualTo(DEFAULT_DIRIGIDO_A_QUIEN);
        assertThat(testPrograma.getCostoPrograma()).isEqualByComparingTo(DEFAULT_COSTO_PROGRAMA);
        assertThat(testPrograma.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void createProgramaWithExistingId() throws Exception {
        // Create the Programa with an existing ID
        programa.setId(1L);
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        int databaseSizeBeforeCreate = programaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setNombre(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoSniesIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setCodigoSnies(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoRegistroCalificadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setCodigoRegistroCalificado(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaRegistroCalificadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setFechaRegistroCalificado(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreTituloOtorgadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setNombreTituloOtorgado(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumeroCreditosIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setNumeroCreditos(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPresentacionProgramaIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setPresentacionPrograma(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setMision(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setVision(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPerfilEgresadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setPerfilEgresado(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCostoProgramaIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setCostoPrograma(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = programaRepository.findAll().collectList().block().size();
        // set the field null
        programa.setEstado(null);

        // Create the Programa, which fails.
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProgramas() {
        // Initialize the database
        programaRepository.save(programa).block();

        // Get all the programaList
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
            .value(hasItem(programa.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigoSnies")
            .value(hasItem(DEFAULT_CODIGO_SNIES.intValue()))
            .jsonPath("$.[*].codigoRegistroCalificado")
            .value(hasItem(DEFAULT_CODIGO_REGISTRO_CALIFICADO.intValue()))
            .jsonPath("$.[*].fechaRegistroCalificado")
            .value(hasItem(DEFAULT_FECHA_REGISTRO_CALIFICADO.toString()))
            .jsonPath("$.[*].nombreTituloOtorgado")
            .value(hasItem(DEFAULT_NOMBRE_TITULO_OTORGADO))
            .jsonPath("$.[*].numeroCreditos")
            .value(hasItem(DEFAULT_NUMERO_CREDITOS))
            .jsonPath("$.[*].duracionPrograma")
            .value(hasItem(DEFAULT_DURACION_PROGRAMA))
            .jsonPath("$.[*].presentacionPrograma")
            .value(hasItem(DEFAULT_PRESENTACION_PROGRAMA))
            .jsonPath("$.[*].mision")
            .value(hasItem(DEFAULT_MISION))
            .jsonPath("$.[*].vision")
            .value(hasItem(DEFAULT_VISION))
            .jsonPath("$.[*].perfilEstudiante")
            .value(hasItem(DEFAULT_PERFIL_ESTUDIANTE))
            .jsonPath("$.[*].perfilEgresado")
            .value(hasItem(DEFAULT_PERFIL_EGRESADO))
            .jsonPath("$.[*].perfilOcupacional")
            .value(hasItem(DEFAULT_PERFIL_OCUPACIONAL))
            .jsonPath("$.[*].urlFotoPrograma")
            .value(hasItem(DEFAULT_URL_FOTO_PROGRAMA))
            .jsonPath("$.[*].dirigidoAQuien")
            .value(hasItem(DEFAULT_DIRIGIDO_A_QUIEN))
            .jsonPath("$.[*].costoPrograma")
            .value(hasItem(sameNumber(DEFAULT_COSTO_PROGRAMA)))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProgramasWithEagerRelationshipsIsEnabled() {
        when(programaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(programaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProgramasWithEagerRelationshipsIsNotEnabled() {
        when(programaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(programaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPrograma() {
        // Initialize the database
        programaRepository.save(programa).block();

        // Get the programa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, programa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(programa.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigoSnies")
            .value(is(DEFAULT_CODIGO_SNIES.intValue()))
            .jsonPath("$.codigoRegistroCalificado")
            .value(is(DEFAULT_CODIGO_REGISTRO_CALIFICADO.intValue()))
            .jsonPath("$.fechaRegistroCalificado")
            .value(is(DEFAULT_FECHA_REGISTRO_CALIFICADO.toString()))
            .jsonPath("$.nombreTituloOtorgado")
            .value(is(DEFAULT_NOMBRE_TITULO_OTORGADO))
            .jsonPath("$.numeroCreditos")
            .value(is(DEFAULT_NUMERO_CREDITOS))
            .jsonPath("$.duracionPrograma")
            .value(is(DEFAULT_DURACION_PROGRAMA))
            .jsonPath("$.presentacionPrograma")
            .value(is(DEFAULT_PRESENTACION_PROGRAMA))
            .jsonPath("$.mision")
            .value(is(DEFAULT_MISION))
            .jsonPath("$.vision")
            .value(is(DEFAULT_VISION))
            .jsonPath("$.perfilEstudiante")
            .value(is(DEFAULT_PERFIL_ESTUDIANTE))
            .jsonPath("$.perfilEgresado")
            .value(is(DEFAULT_PERFIL_EGRESADO))
            .jsonPath("$.perfilOcupacional")
            .value(is(DEFAULT_PERFIL_OCUPACIONAL))
            .jsonPath("$.urlFotoPrograma")
            .value(is(DEFAULT_URL_FOTO_PROGRAMA))
            .jsonPath("$.dirigidoAQuien")
            .value(is(DEFAULT_DIRIGIDO_A_QUIEN))
            .jsonPath("$.costoPrograma")
            .value(is(sameNumber(DEFAULT_COSTO_PROGRAMA)))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    void getNonExistingPrograma() {
        // Get the programa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPrograma() throws Exception {
        // Initialize the database
        programaRepository.save(programa).block();

        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();

        // Update the programa
        Programa updatedPrograma = programaRepository.findById(programa.getId()).block();
        updatedPrograma
            .nombre(UPDATED_NOMBRE)
            .codigoSnies(UPDATED_CODIGO_SNIES)
            .codigoRegistroCalificado(UPDATED_CODIGO_REGISTRO_CALIFICADO)
            .fechaRegistroCalificado(UPDATED_FECHA_REGISTRO_CALIFICADO)
            .nombreTituloOtorgado(UPDATED_NOMBRE_TITULO_OTORGADO)
            .numeroCreditos(UPDATED_NUMERO_CREDITOS)
            .duracionPrograma(UPDATED_DURACION_PROGRAMA)
            .presentacionPrograma(UPDATED_PRESENTACION_PROGRAMA)
            .mision(UPDATED_MISION)
            .vision(UPDATED_VISION)
            .perfilEstudiante(UPDATED_PERFIL_ESTUDIANTE)
            .perfilEgresado(UPDATED_PERFIL_EGRESADO)
            .perfilOcupacional(UPDATED_PERFIL_OCUPACIONAL)
            .urlFotoPrograma(UPDATED_URL_FOTO_PROGRAMA)
            .dirigidoAQuien(UPDATED_DIRIGIDO_A_QUIEN)
            .costoPrograma(UPDATED_COSTO_PROGRAMA)
            .estado(UPDATED_ESTADO);
        ProgramaDTO programaDTO = programaMapper.toDto(updatedPrograma);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, programaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPrograma.getCodigoSnies()).isEqualTo(UPDATED_CODIGO_SNIES);
        assertThat(testPrograma.getCodigoRegistroCalificado()).isEqualTo(UPDATED_CODIGO_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getFechaRegistroCalificado()).isEqualTo(UPDATED_FECHA_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getNombreTituloOtorgado()).isEqualTo(UPDATED_NOMBRE_TITULO_OTORGADO);
        assertThat(testPrograma.getNumeroCreditos()).isEqualTo(UPDATED_NUMERO_CREDITOS);
        assertThat(testPrograma.getDuracionPrograma()).isEqualTo(UPDATED_DURACION_PROGRAMA);
        assertThat(testPrograma.getPresentacionPrograma()).isEqualTo(UPDATED_PRESENTACION_PROGRAMA);
        assertThat(testPrograma.getMision()).isEqualTo(UPDATED_MISION);
        assertThat(testPrograma.getVision()).isEqualTo(UPDATED_VISION);
        assertThat(testPrograma.getPerfilEstudiante()).isEqualTo(UPDATED_PERFIL_ESTUDIANTE);
        assertThat(testPrograma.getPerfilEgresado()).isEqualTo(UPDATED_PERFIL_EGRESADO);
        assertThat(testPrograma.getPerfilOcupacional()).isEqualTo(UPDATED_PERFIL_OCUPACIONAL);
        assertThat(testPrograma.getUrlFotoPrograma()).isEqualTo(UPDATED_URL_FOTO_PROGRAMA);
        assertThat(testPrograma.getDirigidoAQuien()).isEqualTo(UPDATED_DIRIGIDO_A_QUIEN);
        assertThat(testPrograma.getCostoPrograma()).isEqualByComparingTo(UPDATED_COSTO_PROGRAMA);
        assertThat(testPrograma.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void putNonExistingPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();
        programa.setId(count.incrementAndGet());

        // Create the Programa
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, programaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();
        programa.setId(count.incrementAndGet());

        // Create the Programa
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();
        programa.setId(count.incrementAndGet());

        // Create the Programa
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProgramaWithPatch() throws Exception {
        // Initialize the database
        programaRepository.save(programa).block();

        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();

        // Update the programa using partial update
        Programa partialUpdatedPrograma = new Programa();
        partialUpdatedPrograma.setId(programa.getId());

        partialUpdatedPrograma
            .fechaRegistroCalificado(UPDATED_FECHA_REGISTRO_CALIFICADO)
            .nombreTituloOtorgado(UPDATED_NOMBRE_TITULO_OTORGADO)
            .numeroCreditos(UPDATED_NUMERO_CREDITOS)
            .duracionPrograma(UPDATED_DURACION_PROGRAMA)
            .vision(UPDATED_VISION)
            .perfilEgresado(UPDATED_PERFIL_EGRESADO)
            .perfilOcupacional(UPDATED_PERFIL_OCUPACIONAL)
            .urlFotoPrograma(UPDATED_URL_FOTO_PROGRAMA)
            .dirigidoAQuien(UPDATED_DIRIGIDO_A_QUIEN)
            .costoPrograma(UPDATED_COSTO_PROGRAMA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPrograma.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPrograma))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPrograma.getCodigoSnies()).isEqualTo(DEFAULT_CODIGO_SNIES);
        assertThat(testPrograma.getCodigoRegistroCalificado()).isEqualTo(DEFAULT_CODIGO_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getFechaRegistroCalificado()).isEqualTo(UPDATED_FECHA_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getNombreTituloOtorgado()).isEqualTo(UPDATED_NOMBRE_TITULO_OTORGADO);
        assertThat(testPrograma.getNumeroCreditos()).isEqualTo(UPDATED_NUMERO_CREDITOS);
        assertThat(testPrograma.getDuracionPrograma()).isEqualTo(UPDATED_DURACION_PROGRAMA);
        assertThat(testPrograma.getPresentacionPrograma()).isEqualTo(DEFAULT_PRESENTACION_PROGRAMA);
        assertThat(testPrograma.getMision()).isEqualTo(DEFAULT_MISION);
        assertThat(testPrograma.getVision()).isEqualTo(UPDATED_VISION);
        assertThat(testPrograma.getPerfilEstudiante()).isEqualTo(DEFAULT_PERFIL_ESTUDIANTE);
        assertThat(testPrograma.getPerfilEgresado()).isEqualTo(UPDATED_PERFIL_EGRESADO);
        assertThat(testPrograma.getPerfilOcupacional()).isEqualTo(UPDATED_PERFIL_OCUPACIONAL);
        assertThat(testPrograma.getUrlFotoPrograma()).isEqualTo(UPDATED_URL_FOTO_PROGRAMA);
        assertThat(testPrograma.getDirigidoAQuien()).isEqualTo(UPDATED_DIRIGIDO_A_QUIEN);
        assertThat(testPrograma.getCostoPrograma()).isEqualByComparingTo(UPDATED_COSTO_PROGRAMA);
        assertThat(testPrograma.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void fullUpdateProgramaWithPatch() throws Exception {
        // Initialize the database
        programaRepository.save(programa).block();

        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();

        // Update the programa using partial update
        Programa partialUpdatedPrograma = new Programa();
        partialUpdatedPrograma.setId(programa.getId());

        partialUpdatedPrograma
            .nombre(UPDATED_NOMBRE)
            .codigoSnies(UPDATED_CODIGO_SNIES)
            .codigoRegistroCalificado(UPDATED_CODIGO_REGISTRO_CALIFICADO)
            .fechaRegistroCalificado(UPDATED_FECHA_REGISTRO_CALIFICADO)
            .nombreTituloOtorgado(UPDATED_NOMBRE_TITULO_OTORGADO)
            .numeroCreditos(UPDATED_NUMERO_CREDITOS)
            .duracionPrograma(UPDATED_DURACION_PROGRAMA)
            .presentacionPrograma(UPDATED_PRESENTACION_PROGRAMA)
            .mision(UPDATED_MISION)
            .vision(UPDATED_VISION)
            .perfilEstudiante(UPDATED_PERFIL_ESTUDIANTE)
            .perfilEgresado(UPDATED_PERFIL_EGRESADO)
            .perfilOcupacional(UPDATED_PERFIL_OCUPACIONAL)
            .urlFotoPrograma(UPDATED_URL_FOTO_PROGRAMA)
            .dirigidoAQuien(UPDATED_DIRIGIDO_A_QUIEN)
            .costoPrograma(UPDATED_COSTO_PROGRAMA)
            .estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPrograma.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPrograma))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPrograma.getCodigoSnies()).isEqualTo(UPDATED_CODIGO_SNIES);
        assertThat(testPrograma.getCodigoRegistroCalificado()).isEqualTo(UPDATED_CODIGO_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getFechaRegistroCalificado()).isEqualTo(UPDATED_FECHA_REGISTRO_CALIFICADO);
        assertThat(testPrograma.getNombreTituloOtorgado()).isEqualTo(UPDATED_NOMBRE_TITULO_OTORGADO);
        assertThat(testPrograma.getNumeroCreditos()).isEqualTo(UPDATED_NUMERO_CREDITOS);
        assertThat(testPrograma.getDuracionPrograma()).isEqualTo(UPDATED_DURACION_PROGRAMA);
        assertThat(testPrograma.getPresentacionPrograma()).isEqualTo(UPDATED_PRESENTACION_PROGRAMA);
        assertThat(testPrograma.getMision()).isEqualTo(UPDATED_MISION);
        assertThat(testPrograma.getVision()).isEqualTo(UPDATED_VISION);
        assertThat(testPrograma.getPerfilEstudiante()).isEqualTo(UPDATED_PERFIL_ESTUDIANTE);
        assertThat(testPrograma.getPerfilEgresado()).isEqualTo(UPDATED_PERFIL_EGRESADO);
        assertThat(testPrograma.getPerfilOcupacional()).isEqualTo(UPDATED_PERFIL_OCUPACIONAL);
        assertThat(testPrograma.getUrlFotoPrograma()).isEqualTo(UPDATED_URL_FOTO_PROGRAMA);
        assertThat(testPrograma.getDirigidoAQuien()).isEqualTo(UPDATED_DIRIGIDO_A_QUIEN);
        assertThat(testPrograma.getCostoPrograma()).isEqualByComparingTo(UPDATED_COSTO_PROGRAMA);
        assertThat(testPrograma.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void patchNonExistingPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();
        programa.setId(count.incrementAndGet());

        // Create the Programa
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, programaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();
        programa.setId(count.incrementAndGet());

        // Create the Programa
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().collectList().block().size();
        programa.setId(count.incrementAndGet());

        // Create the Programa
        ProgramaDTO programaDTO = programaMapper.toDto(programa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(programaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePrograma() {
        // Initialize the database
        programaRepository.save(programa).block();

        int databaseSizeBeforeDelete = programaRepository.findAll().collectList().block().size();

        // Delete the programa
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, programa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Programa> programaList = programaRepository.findAll().collectList().block();
        assertThat(programaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
