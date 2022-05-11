package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.TablaTiposCatalogoRepository;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaTiposCatalogoDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.TablaTiposCatalogoMapper;
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
 * Integration tests for the {@link TablaTiposCatalogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TablaTiposCatalogoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ESTADO = false;
    private static final Boolean UPDATED_ESTADO = true;

    private static final String ENTITY_API_URL = "/api/tabla-tipos-catalogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TablaTiposCatalogoRepository tablaTiposCatalogoRepository;

    @Autowired
    private TablaTiposCatalogoMapper tablaTiposCatalogoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TablaTiposCatalogo tablaTiposCatalogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaTiposCatalogo createEntity(EntityManager em) {
        TablaTiposCatalogo tablaTiposCatalogo = new TablaTiposCatalogo().nombre(DEFAULT_NOMBRE).estado(DEFAULT_ESTADO);
        return tablaTiposCatalogo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaTiposCatalogo createUpdatedEntity(EntityManager em) {
        TablaTiposCatalogo tablaTiposCatalogo = new TablaTiposCatalogo().nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);
        return tablaTiposCatalogo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TablaTiposCatalogo.class).block();
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
        tablaTiposCatalogo = createEntity(em);
    }

    @Test
    void createTablaTiposCatalogo() throws Exception {
        int databaseSizeBeforeCreate = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        // Create the TablaTiposCatalogo
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeCreate + 1);
        TablaTiposCatalogo testTablaTiposCatalogo = tablaTiposCatalogoList.get(tablaTiposCatalogoList.size() - 1);
        assertThat(testTablaTiposCatalogo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTablaTiposCatalogo.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void createTablaTiposCatalogoWithExistingId() throws Exception {
        // Create the TablaTiposCatalogo with an existing ID
        tablaTiposCatalogo.setId(1L);
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        int databaseSizeBeforeCreate = tablaTiposCatalogoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        // set the field null
        tablaTiposCatalogo.setNombre(null);

        // Create the TablaTiposCatalogo, which fails.
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        // set the field null
        tablaTiposCatalogo.setEstado(null);

        // Create the TablaTiposCatalogo, which fails.
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTablaTiposCatalogos() {
        // Initialize the database
        tablaTiposCatalogoRepository.save(tablaTiposCatalogo).block();

        // Get all the tablaTiposCatalogoList
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
            .value(hasItem(tablaTiposCatalogo.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    void getTablaTiposCatalogo() {
        // Initialize the database
        tablaTiposCatalogoRepository.save(tablaTiposCatalogo).block();

        // Get the tablaTiposCatalogo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tablaTiposCatalogo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tablaTiposCatalogo.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    void getNonExistingTablaTiposCatalogo() {
        // Get the tablaTiposCatalogo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTablaTiposCatalogo() throws Exception {
        // Initialize the database
        tablaTiposCatalogoRepository.save(tablaTiposCatalogo).block();

        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();

        // Update the tablaTiposCatalogo
        TablaTiposCatalogo updatedTablaTiposCatalogo = tablaTiposCatalogoRepository.findById(tablaTiposCatalogo.getId()).block();
        updatedTablaTiposCatalogo.nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(updatedTablaTiposCatalogo);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaTiposCatalogoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
        TablaTiposCatalogo testTablaTiposCatalogo = tablaTiposCatalogoList.get(tablaTiposCatalogoList.size() - 1);
        assertThat(testTablaTiposCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaTiposCatalogo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void putNonExistingTablaTiposCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        tablaTiposCatalogo.setId(count.incrementAndGet());

        // Create the TablaTiposCatalogo
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaTiposCatalogoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTablaTiposCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        tablaTiposCatalogo.setId(count.incrementAndGet());

        // Create the TablaTiposCatalogo
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTablaTiposCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        tablaTiposCatalogo.setId(count.incrementAndGet());

        // Create the TablaTiposCatalogo
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTablaTiposCatalogoWithPatch() throws Exception {
        // Initialize the database
        tablaTiposCatalogoRepository.save(tablaTiposCatalogo).block();

        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();

        // Update the tablaTiposCatalogo using partial update
        TablaTiposCatalogo partialUpdatedTablaTiposCatalogo = new TablaTiposCatalogo();
        partialUpdatedTablaTiposCatalogo.setId(tablaTiposCatalogo.getId());

        partialUpdatedTablaTiposCatalogo.estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaTiposCatalogo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaTiposCatalogo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
        TablaTiposCatalogo testTablaTiposCatalogo = tablaTiposCatalogoList.get(tablaTiposCatalogoList.size() - 1);
        assertThat(testTablaTiposCatalogo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTablaTiposCatalogo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void fullUpdateTablaTiposCatalogoWithPatch() throws Exception {
        // Initialize the database
        tablaTiposCatalogoRepository.save(tablaTiposCatalogo).block();

        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();

        // Update the tablaTiposCatalogo using partial update
        TablaTiposCatalogo partialUpdatedTablaTiposCatalogo = new TablaTiposCatalogo();
        partialUpdatedTablaTiposCatalogo.setId(tablaTiposCatalogo.getId());

        partialUpdatedTablaTiposCatalogo.nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaTiposCatalogo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaTiposCatalogo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
        TablaTiposCatalogo testTablaTiposCatalogo = tablaTiposCatalogoList.get(tablaTiposCatalogoList.size() - 1);
        assertThat(testTablaTiposCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaTiposCatalogo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void patchNonExistingTablaTiposCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        tablaTiposCatalogo.setId(count.incrementAndGet());

        // Create the TablaTiposCatalogo
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tablaTiposCatalogoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTablaTiposCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        tablaTiposCatalogo.setId(count.incrementAndGet());

        // Create the TablaTiposCatalogo
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTablaTiposCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaTiposCatalogoRepository.findAll().collectList().block().size();
        tablaTiposCatalogo.setId(count.incrementAndGet());

        // Create the TablaTiposCatalogo
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = tablaTiposCatalogoMapper.toDto(tablaTiposCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaTiposCatalogoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaTiposCatalogo in the database
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTablaTiposCatalogo() {
        // Initialize the database
        tablaTiposCatalogoRepository.save(tablaTiposCatalogo).block();

        int databaseSizeBeforeDelete = tablaTiposCatalogoRepository.findAll().collectList().block().size();

        // Delete the tablaTiposCatalogo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tablaTiposCatalogo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TablaTiposCatalogo> tablaTiposCatalogoList = tablaTiposCatalogoRepository.findAll().collectList().block();
        assertThat(tablaTiposCatalogoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
