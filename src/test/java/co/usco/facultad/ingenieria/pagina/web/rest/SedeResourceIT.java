package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import co.usco.facultad.ingenieria.pagina.domain.Sede;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.SedeRepository;
import co.usco.facultad.ingenieria.pagina.service.SedeService;
import co.usco.facultad.ingenieria.pagina.service.dto.SedeDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.SedeMapper;
import java.time.Duration;
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
 * Integration tests for the {@link SedeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SedeResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUD = 1D;
    private static final Double UPDATED_LATITUD = 2D;

    private static final Double DEFAULT_LONGITUD = 1D;
    private static final Double UPDATED_LONGITUD = 2D;

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ESTADO = false;
    private static final Boolean UPDATED_ESTADO = true;

    private static final String DEFAULT_TELEFONO_FIJO = "AAAAAAA";
    private static final String UPDATED_TELEFONO_FIJO = "BBBBBBB";

    private static final String DEFAULT_TELEFONO_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO_ELECTRONICO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO_ELECTRONICO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_INDICATIVO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_INDICATIVO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sedes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SedeRepository sedeRepository;

    @Mock
    private SedeRepository sedeRepositoryMock;

    @Autowired
    private SedeMapper sedeMapper;

    @Mock
    private SedeService sedeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Sede sede;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sede createEntity(EntityManager em) {
        Sede sede = new Sede()
            .nombre(DEFAULT_NOMBRE)
            .latitud(DEFAULT_LATITUD)
            .longitud(DEFAULT_LONGITUD)
            .direccion(DEFAULT_DIRECCION)
            .estado(DEFAULT_ESTADO)
            .telefonoFijo(DEFAULT_TELEFONO_FIJO)
            .telefonoCelular(DEFAULT_TELEFONO_CELULAR)
            .correoElectronico(DEFAULT_CORREO_ELECTRONICO)
            .codigoIndicativo(DEFAULT_CODIGO_INDICATIVO);
        // Add required entity
        Ciudad ciudad;
        ciudad = em.insert(CiudadResourceIT.createEntity(em)).block();
        sede.setCiudad(ciudad);
        return sede;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sede createUpdatedEntity(EntityManager em) {
        Sede sede = new Sede()
            .nombre(UPDATED_NOMBRE)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .direccion(UPDATED_DIRECCION)
            .estado(UPDATED_ESTADO)
            .telefonoFijo(UPDATED_TELEFONO_FIJO)
            .telefonoCelular(UPDATED_TELEFONO_CELULAR)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO)
            .codigoIndicativo(UPDATED_CODIGO_INDICATIVO);
        // Add required entity
        Ciudad ciudad;
        ciudad = em.insert(CiudadResourceIT.createUpdatedEntity(em)).block();
        sede.setCiudad(ciudad);
        return sede;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Sede.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CiudadResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        sede = createEntity(em);
    }

    @Test
    void createSede() throws Exception {
        int databaseSizeBeforeCreate = sedeRepository.findAll().collectList().block().size();
        // Create the Sede
        SedeDTO sedeDTO = sedeMapper.toDto(sede);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate + 1);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSede.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testSede.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testSede.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testSede.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testSede.getTelefonoFijo()).isEqualTo(DEFAULT_TELEFONO_FIJO);
        assertThat(testSede.getTelefonoCelular()).isEqualTo(DEFAULT_TELEFONO_CELULAR);
        assertThat(testSede.getCorreoElectronico()).isEqualTo(DEFAULT_CORREO_ELECTRONICO);
        assertThat(testSede.getCodigoIndicativo()).isEqualTo(DEFAULT_CODIGO_INDICATIVO);
    }

    @Test
    void createSedeWithExistingId() throws Exception {
        // Create the Sede with an existing ID
        sede.setId(1L);
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        int databaseSizeBeforeCreate = sedeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().collectList().block().size();
        // set the field null
        sede.setNombre(null);

        // Create the Sede, which fails.
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLatitudIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().collectList().block().size();
        // set the field null
        sede.setLatitud(null);

        // Create the Sede, which fails.
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLongitudIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().collectList().block().size();
        // set the field null
        sede.setLongitud(null);

        // Create the Sede, which fails.
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().collectList().block().size();
        // set the field null
        sede.setDireccion(null);

        // Create the Sede, which fails.
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().collectList().block().size();
        // set the field null
        sede.setEstado(null);

        // Create the Sede, which fails.
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCorreoElectronicoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().collectList().block().size();
        // set the field null
        sede.setCorreoElectronico(null);

        // Create the Sede, which fails.
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIndicativoIsRequired() throws Exception {
        int databaseSizeBeforeTest = sedeRepository.findAll().collectList().block().size();
        // set the field null
        sede.setCodigoIndicativo(null);

        // Create the Sede, which fails.
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSedes() {
        // Initialize the database
        sedeRepository.save(sede).block();

        // Get all the sedeList
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
            .value(hasItem(sede.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].latitud")
            .value(hasItem(DEFAULT_LATITUD.doubleValue()))
            .jsonPath("$.[*].longitud")
            .value(hasItem(DEFAULT_LONGITUD.doubleValue()))
            .jsonPath("$.[*].direccion")
            .value(hasItem(DEFAULT_DIRECCION))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.booleanValue()))
            .jsonPath("$.[*].telefonoFijo")
            .value(hasItem(DEFAULT_TELEFONO_FIJO))
            .jsonPath("$.[*].telefonoCelular")
            .value(hasItem(DEFAULT_TELEFONO_CELULAR))
            .jsonPath("$.[*].correoElectronico")
            .value(hasItem(DEFAULT_CORREO_ELECTRONICO))
            .jsonPath("$.[*].codigoIndicativo")
            .value(hasItem(DEFAULT_CODIGO_INDICATIVO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSedesWithEagerRelationshipsIsEnabled() {
        when(sedeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sedeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSedesWithEagerRelationshipsIsNotEnabled() {
        when(sedeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sedeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSede() {
        // Initialize the database
        sedeRepository.save(sede).block();

        // Get the sede
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sede.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sede.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.latitud")
            .value(is(DEFAULT_LATITUD.doubleValue()))
            .jsonPath("$.longitud")
            .value(is(DEFAULT_LONGITUD.doubleValue()))
            .jsonPath("$.direccion")
            .value(is(DEFAULT_DIRECCION))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.booleanValue()))
            .jsonPath("$.telefonoFijo")
            .value(is(DEFAULT_TELEFONO_FIJO))
            .jsonPath("$.telefonoCelular")
            .value(is(DEFAULT_TELEFONO_CELULAR))
            .jsonPath("$.correoElectronico")
            .value(is(DEFAULT_CORREO_ELECTRONICO))
            .jsonPath("$.codigoIndicativo")
            .value(is(DEFAULT_CODIGO_INDICATIVO));
    }

    @Test
    void getNonExistingSede() {
        // Get the sede
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSede() throws Exception {
        // Initialize the database
        sedeRepository.save(sede).block();

        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();

        // Update the sede
        Sede updatedSede = sedeRepository.findById(sede.getId()).block();
        updatedSede
            .nombre(UPDATED_NOMBRE)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .direccion(UPDATED_DIRECCION)
            .estado(UPDATED_ESTADO)
            .telefonoFijo(UPDATED_TELEFONO_FIJO)
            .telefonoCelular(UPDATED_TELEFONO_CELULAR)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO)
            .codigoIndicativo(UPDATED_CODIGO_INDICATIVO);
        SedeDTO sedeDTO = sedeMapper.toDto(updatedSede);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sedeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSede.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testSede.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testSede.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSede.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testSede.getTelefonoFijo()).isEqualTo(UPDATED_TELEFONO_FIJO);
        assertThat(testSede.getTelefonoCelular()).isEqualTo(UPDATED_TELEFONO_CELULAR);
        assertThat(testSede.getCorreoElectronico()).isEqualTo(UPDATED_CORREO_ELECTRONICO);
        assertThat(testSede.getCodigoIndicativo()).isEqualTo(UPDATED_CODIGO_INDICATIVO);
    }

    @Test
    void putNonExistingSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();
        sede.setId(count.incrementAndGet());

        // Create the Sede
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sedeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();
        sede.setId(count.incrementAndGet());

        // Create the Sede
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();
        sede.setId(count.incrementAndGet());

        // Create the Sede
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSedeWithPatch() throws Exception {
        // Initialize the database
        sedeRepository.save(sede).block();

        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();

        // Update the sede using partial update
        Sede partialUpdatedSede = new Sede();
        partialUpdatedSede.setId(sede.getId());

        partialUpdatedSede
            .nombre(UPDATED_NOMBRE)
            .longitud(UPDATED_LONGITUD)
            .direccion(UPDATED_DIRECCION)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO)
            .codigoIndicativo(UPDATED_CODIGO_INDICATIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSede.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSede))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSede.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testSede.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testSede.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSede.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testSede.getTelefonoFijo()).isEqualTo(DEFAULT_TELEFONO_FIJO);
        assertThat(testSede.getTelefonoCelular()).isEqualTo(DEFAULT_TELEFONO_CELULAR);
        assertThat(testSede.getCorreoElectronico()).isEqualTo(UPDATED_CORREO_ELECTRONICO);
        assertThat(testSede.getCodigoIndicativo()).isEqualTo(UPDATED_CODIGO_INDICATIVO);
    }

    @Test
    void fullUpdateSedeWithPatch() throws Exception {
        // Initialize the database
        sedeRepository.save(sede).block();

        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();

        // Update the sede using partial update
        Sede partialUpdatedSede = new Sede();
        partialUpdatedSede.setId(sede.getId());

        partialUpdatedSede
            .nombre(UPDATED_NOMBRE)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .direccion(UPDATED_DIRECCION)
            .estado(UPDATED_ESTADO)
            .telefonoFijo(UPDATED_TELEFONO_FIJO)
            .telefonoCelular(UPDATED_TELEFONO_CELULAR)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO)
            .codigoIndicativo(UPDATED_CODIGO_INDICATIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSede.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSede))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSede.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testSede.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testSede.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testSede.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testSede.getTelefonoFijo()).isEqualTo(UPDATED_TELEFONO_FIJO);
        assertThat(testSede.getTelefonoCelular()).isEqualTo(UPDATED_TELEFONO_CELULAR);
        assertThat(testSede.getCorreoElectronico()).isEqualTo(UPDATED_CORREO_ELECTRONICO);
        assertThat(testSede.getCodigoIndicativo()).isEqualTo(UPDATED_CODIGO_INDICATIVO);
    }

    @Test
    void patchNonExistingSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();
        sede.setId(count.incrementAndGet());

        // Create the Sede
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sedeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();
        sede.setId(count.incrementAndGet());

        // Create the Sede
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().collectList().block().size();
        sede.setId(count.incrementAndGet());

        // Create the Sede
        SedeDTO sedeDTO = sedeMapper.toDto(sede);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sedeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSede() {
        // Initialize the database
        sedeRepository.save(sede).block();

        int databaseSizeBeforeDelete = sedeRepository.findAll().collectList().block().size();

        // Delete the sede
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sede.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Sede> sedeList = sedeRepository.findAll().collectList().block();
        assertThat(sedeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
