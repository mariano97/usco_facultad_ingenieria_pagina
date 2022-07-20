package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Semillero;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.SemilleroRepository;
import co.usco.facultad.ingenieria.pagina.service.SemilleroService;
import co.usco.facultad.ingenieria.pagina.service.dto.SemilleroDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.SemilleroMapper;
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
 * Integration tests for the {@link SemilleroResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SemilleroResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_INFORMACION_GENERAL = "AAAAAAAAAA";
    private static final String UPDATED_INFORMACION_GENERAL = "BBBBBBBBBB";

    private static final String DEFAULT_URL_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_FOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/semilleros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SemilleroRepository semilleroRepository;

    @Mock
    private SemilleroRepository semilleroRepositoryMock;

    @Autowired
    private SemilleroMapper semilleroMapper;

    @Mock
    private SemilleroService semilleroServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Semillero semillero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semillero createEntity(EntityManager em) {
        Semillero semillero = new Semillero()
            .nombre(DEFAULT_NOMBRE)
            .informacionGeneral(DEFAULT_INFORMACION_GENERAL)
            .urlFoto(DEFAULT_URL_FOTO);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createEntity(em)).block();
        semillero.setFacultad(facultad);
        return semillero;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semillero createUpdatedEntity(EntityManager em) {
        Semillero semillero = new Semillero()
            .nombre(UPDATED_NOMBRE)
            .informacionGeneral(UPDATED_INFORMACION_GENERAL)
            .urlFoto(UPDATED_URL_FOTO);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createUpdatedEntity(em)).block();
        semillero.setFacultad(facultad);
        return semillero;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Semillero.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        FacultadResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        semillero = createEntity(em);
    }

    @Test
    void createSemillero() throws Exception {
        int databaseSizeBeforeCreate = semilleroRepository.findAll().collectList().block().size();
        // Create the Semillero
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeCreate + 1);
        Semillero testSemillero = semilleroList.get(semilleroList.size() - 1);
        assertThat(testSemillero.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSemillero.getInformacionGeneral()).isEqualTo(DEFAULT_INFORMACION_GENERAL);
        assertThat(testSemillero.getUrlFoto()).isEqualTo(DEFAULT_URL_FOTO);
    }

    @Test
    void createSemilleroWithExistingId() throws Exception {
        // Create the Semillero with an existing ID
        semillero.setId(1L);
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        int databaseSizeBeforeCreate = semilleroRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = semilleroRepository.findAll().collectList().block().size();
        // set the field null
        semillero.setNombre(null);

        // Create the Semillero, which fails.
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkInformacionGeneralIsRequired() throws Exception {
        int databaseSizeBeforeTest = semilleroRepository.findAll().collectList().block().size();
        // set the field null
        semillero.setInformacionGeneral(null);

        // Create the Semillero, which fails.
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSemilleros() {
        // Initialize the database
        semilleroRepository.save(semillero).block();

        // Get all the semilleroList
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
            .value(hasItem(semillero.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].informacionGeneral")
            .value(hasItem(DEFAULT_INFORMACION_GENERAL))
            .jsonPath("$.[*].urlFoto")
            .value(hasItem(DEFAULT_URL_FOTO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSemillerosWithEagerRelationshipsIsEnabled() {
        when(semilleroServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(semilleroServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSemillerosWithEagerRelationshipsIsNotEnabled() {
        when(semilleroServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(semilleroServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSemillero() {
        // Initialize the database
        semilleroRepository.save(semillero).block();

        // Get the semillero
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, semillero.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(semillero.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.informacionGeneral")
            .value(is(DEFAULT_INFORMACION_GENERAL))
            .jsonPath("$.urlFoto")
            .value(is(DEFAULT_URL_FOTO));
    }

    @Test
    void getNonExistingSemillero() {
        // Get the semillero
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSemillero() throws Exception {
        // Initialize the database
        semilleroRepository.save(semillero).block();

        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();

        // Update the semillero
        Semillero updatedSemillero = semilleroRepository.findById(semillero.getId()).block();
        updatedSemillero.nombre(UPDATED_NOMBRE).informacionGeneral(UPDATED_INFORMACION_GENERAL).urlFoto(UPDATED_URL_FOTO);
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(updatedSemillero);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, semilleroDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
        Semillero testSemillero = semilleroList.get(semilleroList.size() - 1);
        assertThat(testSemillero.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSemillero.getInformacionGeneral()).isEqualTo(UPDATED_INFORMACION_GENERAL);
        assertThat(testSemillero.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void putNonExistingSemillero() throws Exception {
        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();
        semillero.setId(count.incrementAndGet());

        // Create the Semillero
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, semilleroDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSemillero() throws Exception {
        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();
        semillero.setId(count.incrementAndGet());

        // Create the Semillero
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSemillero() throws Exception {
        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();
        semillero.setId(count.incrementAndGet());

        // Create the Semillero
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSemilleroWithPatch() throws Exception {
        // Initialize the database
        semilleroRepository.save(semillero).block();

        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();

        // Update the semillero using partial update
        Semillero partialUpdatedSemillero = new Semillero();
        partialUpdatedSemillero.setId(semillero.getId());

        partialUpdatedSemillero.nombre(UPDATED_NOMBRE).informacionGeneral(UPDATED_INFORMACION_GENERAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSemillero.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSemillero))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
        Semillero testSemillero = semilleroList.get(semilleroList.size() - 1);
        assertThat(testSemillero.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSemillero.getInformacionGeneral()).isEqualTo(UPDATED_INFORMACION_GENERAL);
        assertThat(testSemillero.getUrlFoto()).isEqualTo(DEFAULT_URL_FOTO);
    }

    @Test
    void fullUpdateSemilleroWithPatch() throws Exception {
        // Initialize the database
        semilleroRepository.save(semillero).block();

        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();

        // Update the semillero using partial update
        Semillero partialUpdatedSemillero = new Semillero();
        partialUpdatedSemillero.setId(semillero.getId());

        partialUpdatedSemillero.nombre(UPDATED_NOMBRE).informacionGeneral(UPDATED_INFORMACION_GENERAL).urlFoto(UPDATED_URL_FOTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSemillero.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSemillero))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
        Semillero testSemillero = semilleroList.get(semilleroList.size() - 1);
        assertThat(testSemillero.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSemillero.getInformacionGeneral()).isEqualTo(UPDATED_INFORMACION_GENERAL);
        assertThat(testSemillero.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void patchNonExistingSemillero() throws Exception {
        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();
        semillero.setId(count.incrementAndGet());

        // Create the Semillero
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, semilleroDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSemillero() throws Exception {
        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();
        semillero.setId(count.incrementAndGet());

        // Create the Semillero
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSemillero() throws Exception {
        int databaseSizeBeforeUpdate = semilleroRepository.findAll().collectList().block().size();
        semillero.setId(count.incrementAndGet());

        // Create the Semillero
        SemilleroDTO semilleroDTO = semilleroMapper.toDto(semillero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(semilleroDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Semillero in the database
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSemillero() {
        // Initialize the database
        semilleroRepository.save(semillero).block();

        int databaseSizeBeforeDelete = semilleroRepository.findAll().collectList().block().size();

        // Delete the semillero
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, semillero.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Semillero> semilleroList = semilleroRepository.findAll().collectList().block();
        assertThat(semilleroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
