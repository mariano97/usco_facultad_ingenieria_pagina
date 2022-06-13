package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.RedesPrograma;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.RedesProgramaRepository;
import co.usco.facultad.ingenieria.pagina.service.RedesProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.RedesProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.RedesProgramaMapper;
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
 * Integration tests for the {@link RedesProgramaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RedesProgramaResourceIT {

    private static final String DEFAULT_URL_RED_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_URL_RED_SOCIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/redes-programas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RedesProgramaRepository redesProgramaRepository;

    @Mock
    private RedesProgramaRepository redesProgramaRepositoryMock;

    @Autowired
    private RedesProgramaMapper redesProgramaMapper;

    @Mock
    private RedesProgramaService redesProgramaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private RedesPrograma redesPrograma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RedesPrograma createEntity(EntityManager em) {
        RedesPrograma redesPrograma = new RedesPrograma().urlRedSocial(DEFAULT_URL_RED_SOCIAL);
        // Add required entity
        Programa programa;
        programa = em.insert(ProgramaResourceIT.createEntity(em)).block();
        redesPrograma.setPrograma(programa);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createEntity(em)).block();
        redesPrograma.setTablaElementoCatalogo(tablaElementoCatalogo);
        return redesPrograma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RedesPrograma createUpdatedEntity(EntityManager em) {
        RedesPrograma redesPrograma = new RedesPrograma().urlRedSocial(UPDATED_URL_RED_SOCIAL);
        // Add required entity
        Programa programa;
        programa = em.insert(ProgramaResourceIT.createUpdatedEntity(em)).block();
        redesPrograma.setPrograma(programa);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createUpdatedEntity(em)).block();
        redesPrograma.setTablaElementoCatalogo(tablaElementoCatalogo);
        return redesPrograma;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RedesPrograma.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ProgramaResourceIT.deleteEntities(em);
        TablaElementoCatalogoResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        redesPrograma = createEntity(em);
    }

    @Test
    void createRedesPrograma() throws Exception {
        int databaseSizeBeforeCreate = redesProgramaRepository.findAll().collectList().block().size();
        // Create the RedesPrograma
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeCreate + 1);
        RedesPrograma testRedesPrograma = redesProgramaList.get(redesProgramaList.size() - 1);
        assertThat(testRedesPrograma.getUrlRedSocial()).isEqualTo(DEFAULT_URL_RED_SOCIAL);
    }

    @Test
    void createRedesProgramaWithExistingId() throws Exception {
        // Create the RedesPrograma with an existing ID
        redesPrograma.setId(1L);
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        int databaseSizeBeforeCreate = redesProgramaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUrlRedSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = redesProgramaRepository.findAll().collectList().block().size();
        // set the field null
        redesPrograma.setUrlRedSocial(null);

        // Create the RedesPrograma, which fails.
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRedesProgramas() {
        // Initialize the database
        redesProgramaRepository.save(redesPrograma).block();

        // Get all the redesProgramaList
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
            .value(hasItem(redesPrograma.getId().intValue()))
            .jsonPath("$.[*].urlRedSocial")
            .value(hasItem(DEFAULT_URL_RED_SOCIAL));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRedesProgramasWithEagerRelationshipsIsEnabled() {
        when(redesProgramaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(redesProgramaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRedesProgramasWithEagerRelationshipsIsNotEnabled() {
        when(redesProgramaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(redesProgramaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getRedesPrograma() {
        // Initialize the database
        redesProgramaRepository.save(redesPrograma).block();

        // Get the redesPrograma
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, redesPrograma.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(redesPrograma.getId().intValue()))
            .jsonPath("$.urlRedSocial")
            .value(is(DEFAULT_URL_RED_SOCIAL));
    }

    @Test
    void getNonExistingRedesPrograma() {
        // Get the redesPrograma
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRedesPrograma() throws Exception {
        // Initialize the database
        redesProgramaRepository.save(redesPrograma).block();

        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();

        // Update the redesPrograma
        RedesPrograma updatedRedesPrograma = redesProgramaRepository.findById(redesPrograma.getId()).block();
        updatedRedesPrograma.urlRedSocial(UPDATED_URL_RED_SOCIAL);
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(updatedRedesPrograma);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, redesProgramaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
        RedesPrograma testRedesPrograma = redesProgramaList.get(redesProgramaList.size() - 1);
        assertThat(testRedesPrograma.getUrlRedSocial()).isEqualTo(UPDATED_URL_RED_SOCIAL);
    }

    @Test
    void putNonExistingRedesPrograma() throws Exception {
        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();
        redesPrograma.setId(count.incrementAndGet());

        // Create the RedesPrograma
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, redesProgramaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRedesPrograma() throws Exception {
        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();
        redesPrograma.setId(count.incrementAndGet());

        // Create the RedesPrograma
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRedesPrograma() throws Exception {
        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();
        redesPrograma.setId(count.incrementAndGet());

        // Create the RedesPrograma
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRedesProgramaWithPatch() throws Exception {
        // Initialize the database
        redesProgramaRepository.save(redesPrograma).block();

        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();

        // Update the redesPrograma using partial update
        RedesPrograma partialUpdatedRedesPrograma = new RedesPrograma();
        partialUpdatedRedesPrograma.setId(redesPrograma.getId());

        partialUpdatedRedesPrograma.urlRedSocial(UPDATED_URL_RED_SOCIAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRedesPrograma.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRedesPrograma))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
        RedesPrograma testRedesPrograma = redesProgramaList.get(redesProgramaList.size() - 1);
        assertThat(testRedesPrograma.getUrlRedSocial()).isEqualTo(UPDATED_URL_RED_SOCIAL);
    }

    @Test
    void fullUpdateRedesProgramaWithPatch() throws Exception {
        // Initialize the database
        redesProgramaRepository.save(redesPrograma).block();

        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();

        // Update the redesPrograma using partial update
        RedesPrograma partialUpdatedRedesPrograma = new RedesPrograma();
        partialUpdatedRedesPrograma.setId(redesPrograma.getId());

        partialUpdatedRedesPrograma.urlRedSocial(UPDATED_URL_RED_SOCIAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRedesPrograma.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRedesPrograma))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
        RedesPrograma testRedesPrograma = redesProgramaList.get(redesProgramaList.size() - 1);
        assertThat(testRedesPrograma.getUrlRedSocial()).isEqualTo(UPDATED_URL_RED_SOCIAL);
    }

    @Test
    void patchNonExistingRedesPrograma() throws Exception {
        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();
        redesPrograma.setId(count.incrementAndGet());

        // Create the RedesPrograma
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, redesProgramaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRedesPrograma() throws Exception {
        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();
        redesPrograma.setId(count.incrementAndGet());

        // Create the RedesPrograma
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRedesPrograma() throws Exception {
        int databaseSizeBeforeUpdate = redesProgramaRepository.findAll().collectList().block().size();
        redesPrograma.setId(count.incrementAndGet());

        // Create the RedesPrograma
        RedesProgramaDTO redesProgramaDTO = redesProgramaMapper.toDto(redesPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(redesProgramaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RedesPrograma in the database
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRedesPrograma() {
        // Initialize the database
        redesProgramaRepository.save(redesPrograma).block();

        int databaseSizeBeforeDelete = redesProgramaRepository.findAll().collectList().block().size();

        // Delete the redesPrograma
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, redesPrograma.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RedesPrograma> redesProgramaList = redesProgramaRepository.findAll().collectList().block();
        assertThat(redesProgramaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
