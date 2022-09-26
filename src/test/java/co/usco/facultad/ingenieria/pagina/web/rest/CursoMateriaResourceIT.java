package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.CursoMateriaRepository;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.service.CursoMateriaService;
import co.usco.facultad.ingenieria.pagina.service.dto.CursoMateriaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.CursoMateriaMapper;
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
 * Integration tests for the {@link CursoMateriaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CursoMateriaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Long DEFAULT_NUMERO_CREDITOS = 1L;
    private static final Long UPDATED_NUMERO_CREDITOS = 2L;

    private static final Long DEFAULT_SEMESTRE_IMPARTIDA = 1L;
    private static final Long UPDATED_SEMESTRE_IMPARTIDA = 2L;

    private static final String ENTITY_API_URL = "/api/curso-materias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CursoMateriaRepository cursoMateriaRepository;

    @Mock
    private CursoMateriaRepository cursoMateriaRepositoryMock;

    @Autowired
    private CursoMateriaMapper cursoMateriaMapper;

    @Mock
    private CursoMateriaService cursoMateriaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CursoMateria cursoMateria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CursoMateria createEntity(EntityManager em) {
        CursoMateria cursoMateria = new CursoMateria()
            .nombre(DEFAULT_NOMBRE)
            .numeroCreditos(DEFAULT_NUMERO_CREDITOS)
            .semestreImpartida(DEFAULT_SEMESTRE_IMPARTIDA);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createEntity(em)).block();
        cursoMateria.setNivelAcademico(tablaElementoCatalogo);
        return cursoMateria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CursoMateria createUpdatedEntity(EntityManager em) {
        CursoMateria cursoMateria = new CursoMateria()
            .nombre(UPDATED_NOMBRE)
            .numeroCreditos(UPDATED_NUMERO_CREDITOS)
            .semestreImpartida(UPDATED_SEMESTRE_IMPARTIDA);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createUpdatedEntity(em)).block();
        cursoMateria.setNivelAcademico(tablaElementoCatalogo);
        return cursoMateria;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_curso_materia__programa").block();
            em.deleteAll(CursoMateria.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TablaElementoCatalogoResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        cursoMateria = createEntity(em);
    }

    @Test
    void createCursoMateria() throws Exception {
        int databaseSizeBeforeCreate = cursoMateriaRepository.findAll().collectList().block().size();
        // Create the CursoMateria
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeCreate + 1);
        CursoMateria testCursoMateria = cursoMateriaList.get(cursoMateriaList.size() - 1);
        assertThat(testCursoMateria.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCursoMateria.getNumeroCreditos()).isEqualTo(DEFAULT_NUMERO_CREDITOS);
        assertThat(testCursoMateria.getSemestreImpartida()).isEqualTo(DEFAULT_SEMESTRE_IMPARTIDA);
    }

    @Test
    void createCursoMateriaWithExistingId() throws Exception {
        // Create the CursoMateria with an existing ID
        cursoMateria.setId(1L);
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        int databaseSizeBeforeCreate = cursoMateriaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursoMateriaRepository.findAll().collectList().block().size();
        // set the field null
        cursoMateria.setNombre(null);

        // Create the CursoMateria, which fails.
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumeroCreditosIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursoMateriaRepository.findAll().collectList().block().size();
        // set the field null
        cursoMateria.setNumeroCreditos(null);

        // Create the CursoMateria, which fails.
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSemestreImpartidaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursoMateriaRepository.findAll().collectList().block().size();
        // set the field null
        cursoMateria.setSemestreImpartida(null);

        // Create the CursoMateria, which fails.
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCursoMaterias() {
        // Initialize the database
        cursoMateriaRepository.save(cursoMateria).block();

        // Get all the cursoMateriaList
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
            .value(hasItem(cursoMateria.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].numeroCreditos")
            .value(hasItem(DEFAULT_NUMERO_CREDITOS.intValue()))
            .jsonPath("$.[*].semestreImpartida")
            .value(hasItem(DEFAULT_SEMESTRE_IMPARTIDA.intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCursoMateriasWithEagerRelationshipsIsEnabled() {
        when(cursoMateriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(cursoMateriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCursoMateriasWithEagerRelationshipsIsNotEnabled() {
        when(cursoMateriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(cursoMateriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCursoMateria() {
        // Initialize the database
        cursoMateriaRepository.save(cursoMateria).block();

        // Get the cursoMateria
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cursoMateria.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cursoMateria.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.numeroCreditos")
            .value(is(DEFAULT_NUMERO_CREDITOS.intValue()))
            .jsonPath("$.semestreImpartida")
            .value(is(DEFAULT_SEMESTRE_IMPARTIDA.intValue()));
    }

    @Test
    void getNonExistingCursoMateria() {
        // Get the cursoMateria
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCursoMateria() throws Exception {
        // Initialize the database
        cursoMateriaRepository.save(cursoMateria).block();

        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();

        // Update the cursoMateria
        CursoMateria updatedCursoMateria = cursoMateriaRepository.findById(cursoMateria.getId()).block();
        updatedCursoMateria.nombre(UPDATED_NOMBRE).numeroCreditos(UPDATED_NUMERO_CREDITOS).semestreImpartida(UPDATED_SEMESTRE_IMPARTIDA);
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(updatedCursoMateria);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cursoMateriaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
        CursoMateria testCursoMateria = cursoMateriaList.get(cursoMateriaList.size() - 1);
        assertThat(testCursoMateria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCursoMateria.getNumeroCreditos()).isEqualTo(UPDATED_NUMERO_CREDITOS);
        assertThat(testCursoMateria.getSemestreImpartida()).isEqualTo(UPDATED_SEMESTRE_IMPARTIDA);
    }

    @Test
    void putNonExistingCursoMateria() throws Exception {
        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();
        cursoMateria.setId(count.incrementAndGet());

        // Create the CursoMateria
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cursoMateriaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCursoMateria() throws Exception {
        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();
        cursoMateria.setId(count.incrementAndGet());

        // Create the CursoMateria
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCursoMateria() throws Exception {
        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();
        cursoMateria.setId(count.incrementAndGet());

        // Create the CursoMateria
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCursoMateriaWithPatch() throws Exception {
        // Initialize the database
        cursoMateriaRepository.save(cursoMateria).block();

        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();

        // Update the cursoMateria using partial update
        CursoMateria partialUpdatedCursoMateria = new CursoMateria();
        partialUpdatedCursoMateria.setId(cursoMateria.getId());

        partialUpdatedCursoMateria.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCursoMateria.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCursoMateria))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
        CursoMateria testCursoMateria = cursoMateriaList.get(cursoMateriaList.size() - 1);
        assertThat(testCursoMateria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCursoMateria.getNumeroCreditos()).isEqualTo(DEFAULT_NUMERO_CREDITOS);
        assertThat(testCursoMateria.getSemestreImpartida()).isEqualTo(DEFAULT_SEMESTRE_IMPARTIDA);
    }

    @Test
    void fullUpdateCursoMateriaWithPatch() throws Exception {
        // Initialize the database
        cursoMateriaRepository.save(cursoMateria).block();

        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();

        // Update the cursoMateria using partial update
        CursoMateria partialUpdatedCursoMateria = new CursoMateria();
        partialUpdatedCursoMateria.setId(cursoMateria.getId());

        partialUpdatedCursoMateria
            .nombre(UPDATED_NOMBRE)
            .numeroCreditos(UPDATED_NUMERO_CREDITOS)
            .semestreImpartida(UPDATED_SEMESTRE_IMPARTIDA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCursoMateria.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCursoMateria))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
        CursoMateria testCursoMateria = cursoMateriaList.get(cursoMateriaList.size() - 1);
        assertThat(testCursoMateria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCursoMateria.getNumeroCreditos()).isEqualTo(UPDATED_NUMERO_CREDITOS);
        assertThat(testCursoMateria.getSemestreImpartida()).isEqualTo(UPDATED_SEMESTRE_IMPARTIDA);
    }

    @Test
    void patchNonExistingCursoMateria() throws Exception {
        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();
        cursoMateria.setId(count.incrementAndGet());

        // Create the CursoMateria
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cursoMateriaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCursoMateria() throws Exception {
        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();
        cursoMateria.setId(count.incrementAndGet());

        // Create the CursoMateria
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCursoMateria() throws Exception {
        int databaseSizeBeforeUpdate = cursoMateriaRepository.findAll().collectList().block().size();
        cursoMateria.setId(count.incrementAndGet());

        // Create the CursoMateria
        CursoMateriaDTO cursoMateriaDTO = cursoMateriaMapper.toDto(cursoMateria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cursoMateriaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CursoMateria in the database
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCursoMateria() {
        // Initialize the database
        cursoMateriaRepository.save(cursoMateria).block();

        int databaseSizeBeforeDelete = cursoMateriaRepository.findAll().collectList().block().size();

        // Delete the cursoMateria
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cursoMateria.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CursoMateria> cursoMateriaList = cursoMateriaRepository.findAll().collectList().block();
        assertThat(cursoMateriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
