package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Paises;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.PaisesRepository;
import co.usco.facultad.ingenieria.pagina.service.dto.PaisesDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.PaisesMapper;
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
 * Integration tests for the {@link PaisesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaisesResourceIT {

    private static final String DEFAULT_NOMBRE_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_PAIS = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_PAIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaisesRepository paisesRepository;

    @Autowired
    private PaisesMapper paisesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Paises paises;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paises createEntity(EntityManager em) {
        Paises paises = new Paises().nombrePais(DEFAULT_NOMBRE_PAIS).codigoPais(DEFAULT_CODIGO_PAIS);
        return paises;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paises createUpdatedEntity(EntityManager em) {
        Paises paises = new Paises().nombrePais(UPDATED_NOMBRE_PAIS).codigoPais(UPDATED_CODIGO_PAIS);
        return paises;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Paises.class).block();
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
        paises = createEntity(em);
    }

    @Test
    void createPaises() throws Exception {
        int databaseSizeBeforeCreate = paisesRepository.findAll().collectList().block().size();
        // Create the Paises
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeCreate + 1);
        Paises testPaises = paisesList.get(paisesList.size() - 1);
        assertThat(testPaises.getNombrePais()).isEqualTo(DEFAULT_NOMBRE_PAIS);
        assertThat(testPaises.getCodigoPais()).isEqualTo(DEFAULT_CODIGO_PAIS);
    }

    @Test
    void createPaisesWithExistingId() throws Exception {
        // Create the Paises with an existing ID
        paises.setId(1L);
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        int databaseSizeBeforeCreate = paisesRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombrePaisIsRequired() throws Exception {
        int databaseSizeBeforeTest = paisesRepository.findAll().collectList().block().size();
        // set the field null
        paises.setNombrePais(null);

        // Create the Paises, which fails.
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoPaisIsRequired() throws Exception {
        int databaseSizeBeforeTest = paisesRepository.findAll().collectList().block().size();
        // set the field null
        paises.setCodigoPais(null);

        // Create the Paises, which fails.
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPaises() {
        // Initialize the database
        paisesRepository.save(paises).block();

        // Get all the paisesList
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
            .value(hasItem(paises.getId().intValue()))
            .jsonPath("$.[*].nombrePais")
            .value(hasItem(DEFAULT_NOMBRE_PAIS))
            .jsonPath("$.[*].codigoPais")
            .value(hasItem(DEFAULT_CODIGO_PAIS));
    }

    @Test
    void getPaises() {
        // Initialize the database
        paisesRepository.save(paises).block();

        // Get the paises
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paises.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paises.getId().intValue()))
            .jsonPath("$.nombrePais")
            .value(is(DEFAULT_NOMBRE_PAIS))
            .jsonPath("$.codigoPais")
            .value(is(DEFAULT_CODIGO_PAIS));
    }

    @Test
    void getNonExistingPaises() {
        // Get the paises
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPaises() throws Exception {
        // Initialize the database
        paisesRepository.save(paises).block();

        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();

        // Update the paises
        Paises updatedPaises = paisesRepository.findById(paises.getId()).block();
        updatedPaises.nombrePais(UPDATED_NOMBRE_PAIS).codigoPais(UPDATED_CODIGO_PAIS);
        PaisesDTO paisesDTO = paisesMapper.toDto(updatedPaises);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paisesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
        Paises testPaises = paisesList.get(paisesList.size() - 1);
        assertThat(testPaises.getNombrePais()).isEqualTo(UPDATED_NOMBRE_PAIS);
        assertThat(testPaises.getCodigoPais()).isEqualTo(UPDATED_CODIGO_PAIS);
    }

    @Test
    void putNonExistingPaises() throws Exception {
        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();
        paises.setId(count.incrementAndGet());

        // Create the Paises
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paisesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaises() throws Exception {
        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();
        paises.setId(count.incrementAndGet());

        // Create the Paises
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaises() throws Exception {
        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();
        paises.setId(count.incrementAndGet());

        // Create the Paises
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaisesWithPatch() throws Exception {
        // Initialize the database
        paisesRepository.save(paises).block();

        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();

        // Update the paises using partial update
        Paises partialUpdatedPaises = new Paises();
        partialUpdatedPaises.setId(paises.getId());

        partialUpdatedPaises.nombrePais(UPDATED_NOMBRE_PAIS).codigoPais(UPDATED_CODIGO_PAIS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaises.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaises))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
        Paises testPaises = paisesList.get(paisesList.size() - 1);
        assertThat(testPaises.getNombrePais()).isEqualTo(UPDATED_NOMBRE_PAIS);
        assertThat(testPaises.getCodigoPais()).isEqualTo(UPDATED_CODIGO_PAIS);
    }

    @Test
    void fullUpdatePaisesWithPatch() throws Exception {
        // Initialize the database
        paisesRepository.save(paises).block();

        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();

        // Update the paises using partial update
        Paises partialUpdatedPaises = new Paises();
        partialUpdatedPaises.setId(paises.getId());

        partialUpdatedPaises.nombrePais(UPDATED_NOMBRE_PAIS).codigoPais(UPDATED_CODIGO_PAIS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaises.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaises))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
        Paises testPaises = paisesList.get(paisesList.size() - 1);
        assertThat(testPaises.getNombrePais()).isEqualTo(UPDATED_NOMBRE_PAIS);
        assertThat(testPaises.getCodigoPais()).isEqualTo(UPDATED_CODIGO_PAIS);
    }

    @Test
    void patchNonExistingPaises() throws Exception {
        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();
        paises.setId(count.incrementAndGet());

        // Create the Paises
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paisesDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaises() throws Exception {
        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();
        paises.setId(count.incrementAndGet());

        // Create the Paises
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaises() throws Exception {
        int databaseSizeBeforeUpdate = paisesRepository.findAll().collectList().block().size();
        paises.setId(count.incrementAndGet());

        // Create the Paises
        PaisesDTO paisesDTO = paisesMapper.toDto(paises);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paisesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paises in the database
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaises() {
        // Initialize the database
        paisesRepository.save(paises).block();

        int databaseSizeBeforeDelete = paisesRepository.findAll().collectList().block().size();

        // Delete the paises
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paises.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Paises> paisesList = paisesRepository.findAll().collectList().block();
        assertThat(paisesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
