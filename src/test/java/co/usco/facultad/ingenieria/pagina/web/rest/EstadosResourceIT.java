package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Estados;
import co.usco.facultad.ingenieria.pagina.domain.Paises;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.EstadosRepository;
import co.usco.facultad.ingenieria.pagina.service.EstadosService;
import co.usco.facultad.ingenieria.pagina.service.dto.EstadosDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.EstadosMapper;
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
 * Integration tests for the {@link EstadosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EstadosResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/estados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstadosRepository estadosRepository;

    @Mock
    private EstadosRepository estadosRepositoryMock;

    @Autowired
    private EstadosMapper estadosMapper;

    @Mock
    private EstadosService estadosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Estados estados;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estados createEntity(EntityManager em) {
        Estados estados = new Estados().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO);
        // Add required entity
        Paises paises;
        paises = em.insert(PaisesResourceIT.createEntity(em)).block();
        estados.setPaises(paises);
        return estados;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estados createUpdatedEntity(EntityManager em) {
        Estados estados = new Estados().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        // Add required entity
        Paises paises;
        paises = em.insert(PaisesResourceIT.createUpdatedEntity(em)).block();
        estados.setPaises(paises);
        return estados;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Estados.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        PaisesResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        estados = createEntity(em);
    }

    @Test
    void createEstados() throws Exception {
        int databaseSizeBeforeCreate = estadosRepository.findAll().collectList().block().size();
        // Create the Estados
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeCreate + 1);
        Estados testEstados = estadosList.get(estadosList.size() - 1);
        assertThat(testEstados.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEstados.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    void createEstadosWithExistingId() throws Exception {
        // Create the Estados with an existing ID
        estados.setId(1L);
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        int databaseSizeBeforeCreate = estadosRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadosRepository.findAll().collectList().block().size();
        // set the field null
        estados.setNombre(null);

        // Create the Estados, which fails.
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadosRepository.findAll().collectList().block().size();
        // set the field null
        estados.setCodigo(null);

        // Create the Estados, which fails.
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEstados() {
        // Initialize the database
        estadosRepository.save(estados).block();

        // Get all the estadosList
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
            .value(hasItem(estados.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstadosWithEagerRelationshipsIsEnabled() {
        when(estadosServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(estadosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstadosWithEagerRelationshipsIsNotEnabled() {
        when(estadosServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(estadosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getEstados() {
        // Initialize the database
        estadosRepository.save(estados).block();

        // Get the estados
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, estados.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(estados.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO));
    }

    @Test
    void getNonExistingEstados() {
        // Get the estados
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEstados() throws Exception {
        // Initialize the database
        estadosRepository.save(estados).block();

        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();

        // Update the estados
        Estados updatedEstados = estadosRepository.findById(estados.getId()).block();
        updatedEstados.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        EstadosDTO estadosDTO = estadosMapper.toDto(updatedEstados);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, estadosDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
        Estados testEstados = estadosList.get(estadosList.size() - 1);
        assertThat(testEstados.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstados.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void putNonExistingEstados() throws Exception {
        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();
        estados.setId(count.incrementAndGet());

        // Create the Estados
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, estadosDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEstados() throws Exception {
        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();
        estados.setId(count.incrementAndGet());

        // Create the Estados
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEstados() throws Exception {
        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();
        estados.setId(count.incrementAndGet());

        // Create the Estados
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEstadosWithPatch() throws Exception {
        // Initialize the database
        estadosRepository.save(estados).block();

        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();

        // Update the estados using partial update
        Estados partialUpdatedEstados = new Estados();
        partialUpdatedEstados.setId(estados.getId());

        partialUpdatedEstados.codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEstados.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEstados))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
        Estados testEstados = estadosList.get(estadosList.size() - 1);
        assertThat(testEstados.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEstados.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void fullUpdateEstadosWithPatch() throws Exception {
        // Initialize the database
        estadosRepository.save(estados).block();

        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();

        // Update the estados using partial update
        Estados partialUpdatedEstados = new Estados();
        partialUpdatedEstados.setId(estados.getId());

        partialUpdatedEstados.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEstados.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEstados))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
        Estados testEstados = estadosList.get(estadosList.size() - 1);
        assertThat(testEstados.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstados.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void patchNonExistingEstados() throws Exception {
        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();
        estados.setId(count.incrementAndGet());

        // Create the Estados
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, estadosDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEstados() throws Exception {
        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();
        estados.setId(count.incrementAndGet());

        // Create the Estados
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEstados() throws Exception {
        int databaseSizeBeforeUpdate = estadosRepository.findAll().collectList().block().size();
        estados.setId(count.incrementAndGet());

        // Create the Estados
        EstadosDTO estadosDTO = estadosMapper.toDto(estados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(estadosDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Estados in the database
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEstados() {
        // Initialize the database
        estadosRepository.save(estados).block();

        int databaseSizeBeforeDelete = estadosRepository.findAll().collectList().block().size();

        // Delete the estados
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, estados.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Estados> estadosList = estadosRepository.findAll().collectList().block();
        assertThat(estadosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
