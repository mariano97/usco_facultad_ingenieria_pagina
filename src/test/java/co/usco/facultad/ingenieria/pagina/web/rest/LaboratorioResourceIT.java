package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Laboratorio;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.LaboratorioRepository;
import co.usco.facultad.ingenieria.pagina.service.LaboratorioService;
import co.usco.facultad.ingenieria.pagina.service.dto.LaboratorioDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.LaboratorioMapper;
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
 * Integration tests for the {@link LaboratorioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LaboratorioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_INFORMACION_GENERAL = "AAAAAAAAAA";
    private static final String UPDATED_INFORMACION_GENERAL = "BBBBBBBBBB";

    private static final String DEFAULT_URL_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_FOTO = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUD = 1D;
    private static final Double UPDATED_LATITUD = 2D;

    private static final Double DEFAULT_LONGITUD = 1D;
    private static final Double UPDATED_LONGITUD = 2D;

    private static final String DEFAULT_CORREO_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/laboratorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private LaboratorioRepository laboratorioRepositoryMock;

    @Autowired
    private LaboratorioMapper laboratorioMapper;

    @Mock
    private LaboratorioService laboratorioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Laboratorio laboratorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laboratorio createEntity(EntityManager em) {
        Laboratorio laboratorio = new Laboratorio()
            .nombre(DEFAULT_NOMBRE)
            .informacionGeneral(DEFAULT_INFORMACION_GENERAL)
            .urlFoto(DEFAULT_URL_FOTO)
            .latitud(DEFAULT_LATITUD)
            .longitud(DEFAULT_LONGITUD)
            .correoContacto(DEFAULT_CORREO_CONTACTO)
            .direccion(DEFAULT_DIRECCION);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createEntity(em)).block();
        laboratorio.setTipoLaboratorio(tablaElementoCatalogo);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createEntity(em)).block();
        laboratorio.setFacultad(facultad);
        return laboratorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laboratorio createUpdatedEntity(EntityManager em) {
        Laboratorio laboratorio = new Laboratorio()
            .nombre(UPDATED_NOMBRE)
            .informacionGeneral(UPDATED_INFORMACION_GENERAL)
            .urlFoto(UPDATED_URL_FOTO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .correoContacto(UPDATED_CORREO_CONTACTO)
            .direccion(UPDATED_DIRECCION);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createUpdatedEntity(em)).block();
        laboratorio.setTipoLaboratorio(tablaElementoCatalogo);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createUpdatedEntity(em)).block();
        laboratorio.setFacultad(facultad);
        return laboratorio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Laboratorio.class).block();
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
        laboratorio = createEntity(em);
    }

    @Test
    void createLaboratorio() throws Exception {
        int databaseSizeBeforeCreate = laboratorioRepository.findAll().collectList().block().size();
        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeCreate + 1);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testLaboratorio.getInformacionGeneral()).isEqualTo(DEFAULT_INFORMACION_GENERAL);
        assertThat(testLaboratorio.getUrlFoto()).isEqualTo(DEFAULT_URL_FOTO);
        assertThat(testLaboratorio.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testLaboratorio.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testLaboratorio.getCorreoContacto()).isEqualTo(DEFAULT_CORREO_CONTACTO);
        assertThat(testLaboratorio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    void createLaboratorioWithExistingId() throws Exception {
        // Create the Laboratorio with an existing ID
        laboratorio.setId(1L);
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        int databaseSizeBeforeCreate = laboratorioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratorioRepository.findAll().collectList().block().size();
        // set the field null
        laboratorio.setNombre(null);

        // Create the Laboratorio, which fails.
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkInformacionGeneralIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratorioRepository.findAll().collectList().block().size();
        // set the field null
        laboratorio.setInformacionGeneral(null);

        // Create the Laboratorio, which fails.
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCorreoContactoIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratorioRepository.findAll().collectList().block().size();
        // set the field null
        laboratorio.setCorreoContacto(null);

        // Create the Laboratorio, which fails.
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLaboratorios() {
        // Initialize the database
        laboratorioRepository.save(laboratorio).block();

        // Get all the laboratorioList
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
            .value(hasItem(laboratorio.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].informacionGeneral")
            .value(hasItem(DEFAULT_INFORMACION_GENERAL))
            .jsonPath("$.[*].urlFoto")
            .value(hasItem(DEFAULT_URL_FOTO))
            .jsonPath("$.[*].latitud")
            .value(hasItem(DEFAULT_LATITUD.doubleValue()))
            .jsonPath("$.[*].longitud")
            .value(hasItem(DEFAULT_LONGITUD.doubleValue()))
            .jsonPath("$.[*].correoContacto")
            .value(hasItem(DEFAULT_CORREO_CONTACTO))
            .jsonPath("$.[*].direccion")
            .value(hasItem(DEFAULT_DIRECCION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLaboratoriosWithEagerRelationshipsIsEnabled() {
        when(laboratorioServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(laboratorioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLaboratoriosWithEagerRelationshipsIsNotEnabled() {
        when(laboratorioServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(laboratorioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getLaboratorio() {
        // Initialize the database
        laboratorioRepository.save(laboratorio).block();

        // Get the laboratorio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, laboratorio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(laboratorio.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.informacionGeneral")
            .value(is(DEFAULT_INFORMACION_GENERAL))
            .jsonPath("$.urlFoto")
            .value(is(DEFAULT_URL_FOTO))
            .jsonPath("$.latitud")
            .value(is(DEFAULT_LATITUD.doubleValue()))
            .jsonPath("$.longitud")
            .value(is(DEFAULT_LONGITUD.doubleValue()))
            .jsonPath("$.correoContacto")
            .value(is(DEFAULT_CORREO_CONTACTO))
            .jsonPath("$.direccion")
            .value(is(DEFAULT_DIRECCION));
    }

    @Test
    void getNonExistingLaboratorio() {
        // Get the laboratorio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewLaboratorio() throws Exception {
        // Initialize the database
        laboratorioRepository.save(laboratorio).block();

        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();

        // Update the laboratorio
        Laboratorio updatedLaboratorio = laboratorioRepository.findById(laboratorio.getId()).block();
        updatedLaboratorio
            .nombre(UPDATED_NOMBRE)
            .informacionGeneral(UPDATED_INFORMACION_GENERAL)
            .urlFoto(UPDATED_URL_FOTO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .correoContacto(UPDATED_CORREO_CONTACTO)
            .direccion(UPDATED_DIRECCION);
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(updatedLaboratorio);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, laboratorioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLaboratorio.getInformacionGeneral()).isEqualTo(UPDATED_INFORMACION_GENERAL);
        assertThat(testLaboratorio.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
        assertThat(testLaboratorio.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testLaboratorio.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testLaboratorio.getCorreoContacto()).isEqualTo(UPDATED_CORREO_CONTACTO);
        assertThat(testLaboratorio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    void putNonExistingLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, laboratorioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLaboratorioWithPatch() throws Exception {
        // Initialize the database
        laboratorioRepository.save(laboratorio).block();

        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();

        // Update the laboratorio using partial update
        Laboratorio partialUpdatedLaboratorio = new Laboratorio();
        partialUpdatedLaboratorio.setId(laboratorio.getId());

        partialUpdatedLaboratorio
            .nombre(UPDATED_NOMBRE)
            .informacionGeneral(UPDATED_INFORMACION_GENERAL)
            .urlFoto(UPDATED_URL_FOTO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .correoContacto(UPDATED_CORREO_CONTACTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLaboratorio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLaboratorio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLaboratorio.getInformacionGeneral()).isEqualTo(UPDATED_INFORMACION_GENERAL);
        assertThat(testLaboratorio.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
        assertThat(testLaboratorio.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testLaboratorio.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testLaboratorio.getCorreoContacto()).isEqualTo(UPDATED_CORREO_CONTACTO);
        assertThat(testLaboratorio.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    void fullUpdateLaboratorioWithPatch() throws Exception {
        // Initialize the database
        laboratorioRepository.save(laboratorio).block();

        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();

        // Update the laboratorio using partial update
        Laboratorio partialUpdatedLaboratorio = new Laboratorio();
        partialUpdatedLaboratorio.setId(laboratorio.getId());

        partialUpdatedLaboratorio
            .nombre(UPDATED_NOMBRE)
            .informacionGeneral(UPDATED_INFORMACION_GENERAL)
            .urlFoto(UPDATED_URL_FOTO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .correoContacto(UPDATED_CORREO_CONTACTO)
            .direccion(UPDATED_DIRECCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLaboratorio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLaboratorio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
        Laboratorio testLaboratorio = laboratorioList.get(laboratorioList.size() - 1);
        assertThat(testLaboratorio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLaboratorio.getInformacionGeneral()).isEqualTo(UPDATED_INFORMACION_GENERAL);
        assertThat(testLaboratorio.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
        assertThat(testLaboratorio.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testLaboratorio.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testLaboratorio.getCorreoContacto()).isEqualTo(UPDATED_CORREO_CONTACTO);
        assertThat(testLaboratorio.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    void patchNonExistingLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, laboratorioDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLaboratorio() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioRepository.findAll().collectList().block().size();
        laboratorio.setId(count.incrementAndGet());

        // Create the Laboratorio
        LaboratorioDTO laboratorioDTO = laboratorioMapper.toDto(laboratorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(laboratorioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Laboratorio in the database
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLaboratorio() {
        // Initialize the database
        laboratorioRepository.save(laboratorio).block();

        int databaseSizeBeforeDelete = laboratorioRepository.findAll().collectList().block().size();

        // Delete the laboratorio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, laboratorio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll().collectList().block();
        assertThat(laboratorioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
