package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.FacultadRepository;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.FacultadMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link FacultadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FacultadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_MISION = "AAAAAAAAAA";
    private static final String UPDATED_MISION = "BBBBBBBBBB";

    private static final String DEFAULT_VISION = "AAAAAAAAAA";
    private static final String UPDATED_VISION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facultads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacultadRepository facultadRepository;

    @Autowired
    private FacultadMapper facultadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Facultad facultad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facultad createEntity(EntityManager em) {
        Facultad facultad = new Facultad().nombre(DEFAULT_NOMBRE).mision(DEFAULT_MISION).vision(DEFAULT_VISION);
        return facultad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facultad createUpdatedEntity(EntityManager em) {
        Facultad facultad = new Facultad().nombre(UPDATED_NOMBRE).mision(UPDATED_MISION).vision(UPDATED_VISION);
        return facultad;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Facultad.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        facultad = createEntity(em);
    }

    @Test
    void createFacultad() throws Exception {
        int databaseSizeBeforeCreate = facultadRepository.findAll().collectList().block().size();
        // Create the Facultad
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeCreate + 1);
        Facultad testFacultad = facultadList.get(facultadList.size() - 1);
        assertThat(testFacultad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFacultad.getMision()).isEqualTo(DEFAULT_MISION);
        assertThat(testFacultad.getVision()).isEqualTo(DEFAULT_VISION);
    }

    @Test
    void createFacultadWithExistingId() throws Exception {
        // Create the Facultad with an existing ID
        facultad.setId(1L);
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        int databaseSizeBeforeCreate = facultadRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = facultadRepository.findAll().collectList().block().size();
        // set the field null
        facultad.setNombre(null);

        // Create the Facultad, which fails.
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = facultadRepository.findAll().collectList().block().size();
        // set the field null
        facultad.setMision(null);

        // Create the Facultad, which fails.
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = facultadRepository.findAll().collectList().block().size();
        // set the field null
        facultad.setVision(null);

        // Create the Facultad, which fails.
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFacultads() {
        // Initialize the database
        facultadRepository.save(facultad).block();

        // Get all the facultadList
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
            .value(hasItem(facultad.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].mision")
            .value(hasItem(DEFAULT_MISION))
            .jsonPath("$.[*].vision")
            .value(hasItem(DEFAULT_VISION));
    }

    @Test
    void getFacultad() {
        // Initialize the database
        facultadRepository.save(facultad).block();

        // Get the facultad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, facultad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(facultad.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.mision")
            .value(is(DEFAULT_MISION))
            .jsonPath("$.vision")
            .value(is(DEFAULT_VISION));
    }

    @Test
    void getNonExistingFacultad() {
        // Get the facultad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFacultad() throws Exception {
        // Initialize the database
        facultadRepository.save(facultad).block();

        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();

        // Update the facultad
        Facultad updatedFacultad = facultadRepository.findById(facultad.getId()).block();
        updatedFacultad.nombre(UPDATED_NOMBRE).mision(UPDATED_MISION).vision(UPDATED_VISION);
        FacultadDTO facultadDTO = facultadMapper.toDto(updatedFacultad);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, facultadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
        Facultad testFacultad = facultadList.get(facultadList.size() - 1);
        assertThat(testFacultad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFacultad.getMision()).isEqualTo(UPDATED_MISION);
        assertThat(testFacultad.getVision()).isEqualTo(UPDATED_VISION);
    }

    @Test
    void putNonExistingFacultad() throws Exception {
        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();
        facultad.setId(count.incrementAndGet());

        // Create the Facultad
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, facultadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFacultad() throws Exception {
        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();
        facultad.setId(count.incrementAndGet());

        // Create the Facultad
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFacultad() throws Exception {
        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();
        facultad.setId(count.incrementAndGet());

        // Create the Facultad
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFacultadWithPatch() throws Exception {
        // Initialize the database
        facultadRepository.save(facultad).block();

        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();

        // Update the facultad using partial update
        Facultad partialUpdatedFacultad = new Facultad();
        partialUpdatedFacultad.setId(facultad.getId());

        partialUpdatedFacultad.nombre(UPDATED_NOMBRE).vision(UPDATED_VISION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFacultad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFacultad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
        Facultad testFacultad = facultadList.get(facultadList.size() - 1);
        assertThat(testFacultad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFacultad.getMision()).isEqualTo(DEFAULT_MISION);
        assertThat(testFacultad.getVision()).isEqualTo(UPDATED_VISION);
    }

    @Test
    void fullUpdateFacultadWithPatch() throws Exception {
        // Initialize the database
        facultadRepository.save(facultad).block();

        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();

        // Update the facultad using partial update
        Facultad partialUpdatedFacultad = new Facultad();
        partialUpdatedFacultad.setId(facultad.getId());

        partialUpdatedFacultad.nombre(UPDATED_NOMBRE).mision(UPDATED_MISION).vision(UPDATED_VISION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFacultad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFacultad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
        Facultad testFacultad = facultadList.get(facultadList.size() - 1);
        assertThat(testFacultad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFacultad.getMision()).isEqualTo(UPDATED_MISION);
        assertThat(testFacultad.getVision()).isEqualTo(UPDATED_VISION);
    }

    @Test
    void patchNonExistingFacultad() throws Exception {
        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();
        facultad.setId(count.incrementAndGet());

        // Create the Facultad
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, facultadDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFacultad() throws Exception {
        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();
        facultad.setId(count.incrementAndGet());

        // Create the Facultad
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFacultad() throws Exception {
        int databaseSizeBeforeUpdate = facultadRepository.findAll().collectList().block().size();
        facultad.setId(count.incrementAndGet());

        // Create the Facultad
        FacultadDTO facultadDTO = facultadMapper.toDto(facultad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(facultadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Facultad in the database
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFacultad() {
        // Initialize the database
        facultadRepository.save(facultad).block();

        int databaseSizeBeforeDelete = facultadRepository.findAll().collectList().block().size();

        // Delete the facultad
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, facultad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Facultad> facultadList = facultadRepository.findAll().collectList().block();
        assertThat(facultadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
