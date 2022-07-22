package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.EscalafonProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.dto.EscalafonProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.EscalafonProfesorMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link EscalafonProfesorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EscalafonProfesorResourceIT {

    private static final Double DEFAULT_PUNTUCACION_PROMEDIO = 1D;
    private static final Double UPDATED_PUNTUCACION_PROMEDIO = 2D;

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/escalafon-profesors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EscalafonProfesorRepository escalafonProfesorRepository;

    @Autowired
    private EscalafonProfesorMapper escalafonProfesorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EscalafonProfesor escalafonProfesor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EscalafonProfesor createEntity(EntityManager em) {
        EscalafonProfesor escalafonProfesor = new EscalafonProfesor()
            .puntucacionPromedio(DEFAULT_PUNTUCACION_PROMEDIO)
            .fecha(DEFAULT_FECHA);
        // Add required entity
        Profesor profesor;
        profesor = em.insert(ProfesorResourceIT.createEntity(em)).block();
        escalafonProfesor.setProfesor(profesor);
        return escalafonProfesor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EscalafonProfesor createUpdatedEntity(EntityManager em) {
        EscalafonProfesor escalafonProfesor = new EscalafonProfesor()
            .puntucacionPromedio(UPDATED_PUNTUCACION_PROMEDIO)
            .fecha(UPDATED_FECHA);
        // Add required entity
        Profesor profesor;
        profesor = em.insert(ProfesorResourceIT.createUpdatedEntity(em)).block();
        escalafonProfesor.setProfesor(profesor);
        return escalafonProfesor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EscalafonProfesor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ProfesorResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        escalafonProfesor = createEntity(em);
    }

    @Test
    void createEscalafonProfesor() throws Exception {
        int databaseSizeBeforeCreate = escalafonProfesorRepository.findAll().collectList().block().size();
        // Create the EscalafonProfesor
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeCreate + 1);
        EscalafonProfesor testEscalafonProfesor = escalafonProfesorList.get(escalafonProfesorList.size() - 1);
        assertThat(testEscalafonProfesor.getPuntucacionPromedio()).isEqualTo(DEFAULT_PUNTUCACION_PROMEDIO);
        assertThat(testEscalafonProfesor.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    void createEscalafonProfesorWithExistingId() throws Exception {
        // Create the EscalafonProfesor with an existing ID
        escalafonProfesor.setId(1L);
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        int databaseSizeBeforeCreate = escalafonProfesorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPuntucacionPromedioIsRequired() throws Exception {
        int databaseSizeBeforeTest = escalafonProfesorRepository.findAll().collectList().block().size();
        // set the field null
        escalafonProfesor.setPuntucacionPromedio(null);

        // Create the EscalafonProfesor, which fails.
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = escalafonProfesorRepository.findAll().collectList().block().size();
        // set the field null
        escalafonProfesor.setFecha(null);

        // Create the EscalafonProfesor, which fails.
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEscalafonProfesors() {
        // Initialize the database
        escalafonProfesorRepository.save(escalafonProfesor).block();

        // Get all the escalafonProfesorList
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
            .value(hasItem(escalafonProfesor.getId().intValue()))
            .jsonPath("$.[*].puntucacionPromedio")
            .value(hasItem(DEFAULT_PUNTUCACION_PROMEDIO.doubleValue()))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()));
    }

    @Test
    void getEscalafonProfesor() {
        // Initialize the database
        escalafonProfesorRepository.save(escalafonProfesor).block();

        // Get the escalafonProfesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, escalafonProfesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(escalafonProfesor.getId().intValue()))
            .jsonPath("$.puntucacionPromedio")
            .value(is(DEFAULT_PUNTUCACION_PROMEDIO.doubleValue()))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()));
    }

    @Test
    void getNonExistingEscalafonProfesor() {
        // Get the escalafonProfesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEscalafonProfesor() throws Exception {
        // Initialize the database
        escalafonProfesorRepository.save(escalafonProfesor).block();

        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();

        // Update the escalafonProfesor
        EscalafonProfesor updatedEscalafonProfesor = escalafonProfesorRepository.findById(escalafonProfesor.getId()).block();
        updatedEscalafonProfesor.puntucacionPromedio(UPDATED_PUNTUCACION_PROMEDIO).fecha(UPDATED_FECHA);
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(updatedEscalafonProfesor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, escalafonProfesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
        EscalafonProfesor testEscalafonProfesor = escalafonProfesorList.get(escalafonProfesorList.size() - 1);
        assertThat(testEscalafonProfesor.getPuntucacionPromedio()).isEqualTo(UPDATED_PUNTUCACION_PROMEDIO);
        assertThat(testEscalafonProfesor.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    void putNonExistingEscalafonProfesor() throws Exception {
        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();
        escalafonProfesor.setId(count.incrementAndGet());

        // Create the EscalafonProfesor
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, escalafonProfesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEscalafonProfesor() throws Exception {
        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();
        escalafonProfesor.setId(count.incrementAndGet());

        // Create the EscalafonProfesor
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEscalafonProfesor() throws Exception {
        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();
        escalafonProfesor.setId(count.incrementAndGet());

        // Create the EscalafonProfesor
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEscalafonProfesorWithPatch() throws Exception {
        // Initialize the database
        escalafonProfesorRepository.save(escalafonProfesor).block();

        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();

        // Update the escalafonProfesor using partial update
        EscalafonProfesor partialUpdatedEscalafonProfesor = new EscalafonProfesor();
        partialUpdatedEscalafonProfesor.setId(escalafonProfesor.getId());

        partialUpdatedEscalafonProfesor.puntucacionPromedio(UPDATED_PUNTUCACION_PROMEDIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEscalafonProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEscalafonProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
        EscalafonProfesor testEscalafonProfesor = escalafonProfesorList.get(escalafonProfesorList.size() - 1);
        assertThat(testEscalafonProfesor.getPuntucacionPromedio()).isEqualTo(UPDATED_PUNTUCACION_PROMEDIO);
        assertThat(testEscalafonProfesor.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    void fullUpdateEscalafonProfesorWithPatch() throws Exception {
        // Initialize the database
        escalafonProfesorRepository.save(escalafonProfesor).block();

        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();

        // Update the escalafonProfesor using partial update
        EscalafonProfesor partialUpdatedEscalafonProfesor = new EscalafonProfesor();
        partialUpdatedEscalafonProfesor.setId(escalafonProfesor.getId());

        partialUpdatedEscalafonProfesor.puntucacionPromedio(UPDATED_PUNTUCACION_PROMEDIO).fecha(UPDATED_FECHA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEscalafonProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEscalafonProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
        EscalafonProfesor testEscalafonProfesor = escalafonProfesorList.get(escalafonProfesorList.size() - 1);
        assertThat(testEscalafonProfesor.getPuntucacionPromedio()).isEqualTo(UPDATED_PUNTUCACION_PROMEDIO);
        assertThat(testEscalafonProfesor.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    void patchNonExistingEscalafonProfesor() throws Exception {
        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();
        escalafonProfesor.setId(count.incrementAndGet());

        // Create the EscalafonProfesor
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, escalafonProfesorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEscalafonProfesor() throws Exception {
        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();
        escalafonProfesor.setId(count.incrementAndGet());

        // Create the EscalafonProfesor
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEscalafonProfesor() throws Exception {
        int databaseSizeBeforeUpdate = escalafonProfesorRepository.findAll().collectList().block().size();
        escalafonProfesor.setId(count.incrementAndGet());

        // Create the EscalafonProfesor
        EscalafonProfesorDTO escalafonProfesorDTO = escalafonProfesorMapper.toDto(escalafonProfesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(escalafonProfesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EscalafonProfesor in the database
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEscalafonProfesor() {
        // Initialize the database
        escalafonProfesorRepository.save(escalafonProfesor).block();

        int databaseSizeBeforeDelete = escalafonProfesorRepository.findAll().collectList().block().size();

        // Delete the escalafonProfesor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, escalafonProfesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<EscalafonProfesor> escalafonProfesorList = escalafonProfesorRepository.findAll().collectList().block();
        assertThat(escalafonProfesorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
