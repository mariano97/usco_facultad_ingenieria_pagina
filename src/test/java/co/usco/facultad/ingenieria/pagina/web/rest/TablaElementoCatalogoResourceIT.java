package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.TablaElementoCatalogoRepository;
import co.usco.facultad.ingenieria.pagina.service.TablaElementoCatalogoService;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.TablaElementoCatalogoMapper;
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
 * Integration tests for the {@link TablaElementoCatalogoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TablaElementoCatalogoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ABREVIATURA = "AAAAAAAAAA";
    private static final String UPDATED_ABREVIATURA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ESTADO = false;
    private static final Boolean UPDATED_ESTADO = true;

    private static final String ENTITY_API_URL = "/api/tabla-elemento-catalogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TablaElementoCatalogoRepository tablaElementoCatalogoRepository;

    @Mock
    private TablaElementoCatalogoRepository tablaElementoCatalogoRepositoryMock;

    @Autowired
    private TablaElementoCatalogoMapper tablaElementoCatalogoMapper;

    @Mock
    private TablaElementoCatalogoService tablaElementoCatalogoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TablaElementoCatalogo tablaElementoCatalogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaElementoCatalogo createEntity(EntityManager em) {
        TablaElementoCatalogo tablaElementoCatalogo = new TablaElementoCatalogo()
            .nombre(DEFAULT_NOMBRE)
            .abreviatura(DEFAULT_ABREVIATURA)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        TablaTiposCatalogo tablaTiposCatalogo;
        tablaTiposCatalogo = em.insert(TablaTiposCatalogoResourceIT.createEntity(em)).block();
        tablaElementoCatalogo.setTablaTiposCatalogo(tablaTiposCatalogo);
        return tablaElementoCatalogo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TablaElementoCatalogo createUpdatedEntity(EntityManager em) {
        TablaElementoCatalogo tablaElementoCatalogo = new TablaElementoCatalogo()
            .nombre(UPDATED_NOMBRE)
            .abreviatura(UPDATED_ABREVIATURA)
            .estado(UPDATED_ESTADO);
        // Add required entity
        TablaTiposCatalogo tablaTiposCatalogo;
        tablaTiposCatalogo = em.insert(TablaTiposCatalogoResourceIT.createUpdatedEntity(em)).block();
        tablaElementoCatalogo.setTablaTiposCatalogo(tablaTiposCatalogo);
        return tablaElementoCatalogo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TablaElementoCatalogo.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        TablaTiposCatalogoResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        tablaElementoCatalogo = createEntity(em);
    }

    @Test
    void createTablaElementoCatalogo() throws Exception {
        int databaseSizeBeforeCreate = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        // Create the TablaElementoCatalogo
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeCreate + 1);
        TablaElementoCatalogo testTablaElementoCatalogo = tablaElementoCatalogoList.get(tablaElementoCatalogoList.size() - 1);
        assertThat(testTablaElementoCatalogo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTablaElementoCatalogo.getAbreviatura()).isEqualTo(DEFAULT_ABREVIATURA);
        assertThat(testTablaElementoCatalogo.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void createTablaElementoCatalogoWithExistingId() throws Exception {
        // Create the TablaElementoCatalogo with an existing ID
        tablaElementoCatalogo.setId(1L);
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        int databaseSizeBeforeCreate = tablaElementoCatalogoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        // set the field null
        tablaElementoCatalogo.setNombre(null);

        // Create the TablaElementoCatalogo, which fails.
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        // set the field null
        tablaElementoCatalogo.setEstado(null);

        // Create the TablaElementoCatalogo, which fails.
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTablaElementoCatalogos() {
        // Initialize the database
        tablaElementoCatalogoRepository.save(tablaElementoCatalogo).block();

        // Get all the tablaElementoCatalogoList
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
            .value(hasItem(tablaElementoCatalogo.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].abreviatura")
            .value(hasItem(DEFAULT_ABREVIATURA))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTablaElementoCatalogosWithEagerRelationshipsIsEnabled() {
        when(tablaElementoCatalogoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tablaElementoCatalogoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTablaElementoCatalogosWithEagerRelationshipsIsNotEnabled() {
        when(tablaElementoCatalogoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(tablaElementoCatalogoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getTablaElementoCatalogo() {
        // Initialize the database
        tablaElementoCatalogoRepository.save(tablaElementoCatalogo).block();

        // Get the tablaElementoCatalogo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tablaElementoCatalogo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tablaElementoCatalogo.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.abreviatura")
            .value(is(DEFAULT_ABREVIATURA))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    void getNonExistingTablaElementoCatalogo() {
        // Get the tablaElementoCatalogo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTablaElementoCatalogo() throws Exception {
        // Initialize the database
        tablaElementoCatalogoRepository.save(tablaElementoCatalogo).block();

        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();

        // Update the tablaElementoCatalogo
        TablaElementoCatalogo updatedTablaElementoCatalogo = tablaElementoCatalogoRepository
            .findById(tablaElementoCatalogo.getId())
            .block();
        updatedTablaElementoCatalogo.nombre(UPDATED_NOMBRE).abreviatura(UPDATED_ABREVIATURA).estado(UPDATED_ESTADO);
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(updatedTablaElementoCatalogo);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaElementoCatalogoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
        TablaElementoCatalogo testTablaElementoCatalogo = tablaElementoCatalogoList.get(tablaElementoCatalogoList.size() - 1);
        assertThat(testTablaElementoCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaElementoCatalogo.getAbreviatura()).isEqualTo(UPDATED_ABREVIATURA);
        assertThat(testTablaElementoCatalogo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void putNonExistingTablaElementoCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        tablaElementoCatalogo.setId(count.incrementAndGet());

        // Create the TablaElementoCatalogo
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tablaElementoCatalogoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTablaElementoCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        tablaElementoCatalogo.setId(count.incrementAndGet());

        // Create the TablaElementoCatalogo
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTablaElementoCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        tablaElementoCatalogo.setId(count.incrementAndGet());

        // Create the TablaElementoCatalogo
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTablaElementoCatalogoWithPatch() throws Exception {
        // Initialize the database
        tablaElementoCatalogoRepository.save(tablaElementoCatalogo).block();

        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();

        // Update the tablaElementoCatalogo using partial update
        TablaElementoCatalogo partialUpdatedTablaElementoCatalogo = new TablaElementoCatalogo();
        partialUpdatedTablaElementoCatalogo.setId(tablaElementoCatalogo.getId());

        partialUpdatedTablaElementoCatalogo.nombre(UPDATED_NOMBRE).abreviatura(UPDATED_ABREVIATURA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaElementoCatalogo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaElementoCatalogo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
        TablaElementoCatalogo testTablaElementoCatalogo = tablaElementoCatalogoList.get(tablaElementoCatalogoList.size() - 1);
        assertThat(testTablaElementoCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaElementoCatalogo.getAbreviatura()).isEqualTo(UPDATED_ABREVIATURA);
        assertThat(testTablaElementoCatalogo.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void fullUpdateTablaElementoCatalogoWithPatch() throws Exception {
        // Initialize the database
        tablaElementoCatalogoRepository.save(tablaElementoCatalogo).block();

        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();

        // Update the tablaElementoCatalogo using partial update
        TablaElementoCatalogo partialUpdatedTablaElementoCatalogo = new TablaElementoCatalogo();
        partialUpdatedTablaElementoCatalogo.setId(tablaElementoCatalogo.getId());

        partialUpdatedTablaElementoCatalogo.nombre(UPDATED_NOMBRE).abreviatura(UPDATED_ABREVIATURA).estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTablaElementoCatalogo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTablaElementoCatalogo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
        TablaElementoCatalogo testTablaElementoCatalogo = tablaElementoCatalogoList.get(tablaElementoCatalogoList.size() - 1);
        assertThat(testTablaElementoCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTablaElementoCatalogo.getAbreviatura()).isEqualTo(UPDATED_ABREVIATURA);
        assertThat(testTablaElementoCatalogo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void patchNonExistingTablaElementoCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        tablaElementoCatalogo.setId(count.incrementAndGet());

        // Create the TablaElementoCatalogo
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tablaElementoCatalogoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTablaElementoCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        tablaElementoCatalogo.setId(count.incrementAndGet());

        // Create the TablaElementoCatalogo
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTablaElementoCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = tablaElementoCatalogoRepository.findAll().collectList().block().size();
        tablaElementoCatalogo.setId(count.incrementAndGet());

        // Create the TablaElementoCatalogo
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = tablaElementoCatalogoMapper.toDto(tablaElementoCatalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tablaElementoCatalogoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TablaElementoCatalogo in the database
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTablaElementoCatalogo() {
        // Initialize the database
        tablaElementoCatalogoRepository.save(tablaElementoCatalogo).block();

        int databaseSizeBeforeDelete = tablaElementoCatalogoRepository.findAll().collectList().block().size();

        // Delete the tablaElementoCatalogo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tablaElementoCatalogo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TablaElementoCatalogo> tablaElementoCatalogoList = tablaElementoCatalogoRepository.findAll().collectList().block();
        assertThat(tablaElementoCatalogoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
