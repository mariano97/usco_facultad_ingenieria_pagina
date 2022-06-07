package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.ArchivosProgramaRepository;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.service.ArchivosProgramaService;
import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.ArchivosProgramaMapper;
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
 * Integration tests for the {@link ArchivosProgramaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ArchivosProgramaResourceIT {

    private static final String DEFAULT_URL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_URL_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_GENERATION_STORAGE = 1L;
    private static final Long UPDATED_GENERATION_STORAGE = 2L;

    private static final String DEFAULT_STORAGE_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_DOCUMENTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PLAN_ESTUDIO = false;
    private static final Boolean UPDATED_PLAN_ESTUDIO = true;

    private static final String ENTITY_API_URL = "/api/archivos-programas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArchivosProgramaRepository archivosProgramaRepository;

    @Mock
    private ArchivosProgramaRepository archivosProgramaRepositoryMock;

    @Autowired
    private ArchivosProgramaMapper archivosProgramaMapper;

    @Mock
    private ArchivosProgramaService archivosProgramaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ArchivosPrograma archivosPrograma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchivosPrograma createEntity(EntityManager em) {
        ArchivosPrograma archivosPrograma = new ArchivosPrograma()
            .urlName(DEFAULT_URL_NAME)
            .generationStorage(DEFAULT_GENERATION_STORAGE)
            .storageContentType(DEFAULT_STORAGE_CONTENT_TYPE)
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .planEstudio(DEFAULT_PLAN_ESTUDIO);
        // Add required entity
        Programa programa;
        programa = em.insert(ProgramaResourceIT.createEntity(em)).block();
        archivosPrograma.setPrograma(programa);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createEntity(em)).block();
        archivosPrograma.setTablaElementoCatalogo(tablaElementoCatalogo);
        return archivosPrograma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchivosPrograma createUpdatedEntity(EntityManager em) {
        ArchivosPrograma archivosPrograma = new ArchivosPrograma()
            .urlName(UPDATED_URL_NAME)
            .generationStorage(UPDATED_GENERATION_STORAGE)
            .storageContentType(UPDATED_STORAGE_CONTENT_TYPE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .planEstudio(UPDATED_PLAN_ESTUDIO);
        // Add required entity
        Programa programa;
        programa = em.insert(ProgramaResourceIT.createUpdatedEntity(em)).block();
        archivosPrograma.setPrograma(programa);
        // Add required entity
        TablaElementoCatalogo tablaElementoCatalogo;
        tablaElementoCatalogo = em.insert(TablaElementoCatalogoResourceIT.createUpdatedEntity(em)).block();
        archivosPrograma.setTablaElementoCatalogo(tablaElementoCatalogo);
        return archivosPrograma;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ArchivosPrograma.class).block();
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
        archivosPrograma = createEntity(em);
    }

    @Test
    void createArchivosPrograma() throws Exception {
        int databaseSizeBeforeCreate = archivosProgramaRepository.findAll().collectList().block().size();
        // Create the ArchivosPrograma
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeCreate + 1);
        ArchivosPrograma testArchivosPrograma = archivosProgramaList.get(archivosProgramaList.size() - 1);
        assertThat(testArchivosPrograma.getUrlName()).isEqualTo(DEFAULT_URL_NAME);
        assertThat(testArchivosPrograma.getGenerationStorage()).isEqualTo(DEFAULT_GENERATION_STORAGE);
        assertThat(testArchivosPrograma.getStorageContentType()).isEqualTo(DEFAULT_STORAGE_CONTENT_TYPE);
        assertThat(testArchivosPrograma.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testArchivosPrograma.getPlanEstudio()).isEqualTo(DEFAULT_PLAN_ESTUDIO);
    }

    @Test
    void createArchivosProgramaWithExistingId() throws Exception {
        // Create the ArchivosPrograma with an existing ID
        archivosPrograma.setId(1L);
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        int databaseSizeBeforeCreate = archivosProgramaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUrlNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = archivosProgramaRepository.findAll().collectList().block().size();
        // set the field null
        archivosPrograma.setUrlName(null);

        // Create the ArchivosPrograma, which fails.
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGenerationStorageIsRequired() throws Exception {
        int databaseSizeBeforeTest = archivosProgramaRepository.findAll().collectList().block().size();
        // set the field null
        archivosPrograma.setGenerationStorage(null);

        // Create the ArchivosPrograma, which fails.
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPlanEstudioIsRequired() throws Exception {
        int databaseSizeBeforeTest = archivosProgramaRepository.findAll().collectList().block().size();
        // set the field null
        archivosPrograma.setPlanEstudio(null);

        // Create the ArchivosPrograma, which fails.
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllArchivosProgramas() {
        // Initialize the database
        archivosProgramaRepository.save(archivosPrograma).block();

        // Get all the archivosProgramaList
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
            .value(hasItem(archivosPrograma.getId().intValue()))
            .jsonPath("$.[*].urlName")
            .value(hasItem(DEFAULT_URL_NAME))
            .jsonPath("$.[*].generationStorage")
            .value(hasItem(DEFAULT_GENERATION_STORAGE.intValue()))
            .jsonPath("$.[*].storageContentType")
            .value(hasItem(DEFAULT_STORAGE_CONTENT_TYPE))
            .jsonPath("$.[*].tipoDocumento")
            .value(hasItem(DEFAULT_TIPO_DOCUMENTO))
            .jsonPath("$.[*].planEstudio")
            .value(hasItem(DEFAULT_PLAN_ESTUDIO.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArchivosProgramasWithEagerRelationshipsIsEnabled() {
        when(archivosProgramaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(archivosProgramaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArchivosProgramasWithEagerRelationshipsIsNotEnabled() {
        when(archivosProgramaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(archivosProgramaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getArchivosPrograma() {
        // Initialize the database
        archivosProgramaRepository.save(archivosPrograma).block();

        // Get the archivosPrograma
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, archivosPrograma.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(archivosPrograma.getId().intValue()))
            .jsonPath("$.urlName")
            .value(is(DEFAULT_URL_NAME))
            .jsonPath("$.generationStorage")
            .value(is(DEFAULT_GENERATION_STORAGE.intValue()))
            .jsonPath("$.storageContentType")
            .value(is(DEFAULT_STORAGE_CONTENT_TYPE))
            .jsonPath("$.tipoDocumento")
            .value(is(DEFAULT_TIPO_DOCUMENTO))
            .jsonPath("$.planEstudio")
            .value(is(DEFAULT_PLAN_ESTUDIO.booleanValue()));
    }

    @Test
    void getNonExistingArchivosPrograma() {
        // Get the archivosPrograma
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewArchivosPrograma() throws Exception {
        // Initialize the database
        archivosProgramaRepository.save(archivosPrograma).block();

        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();

        // Update the archivosPrograma
        ArchivosPrograma updatedArchivosPrograma = archivosProgramaRepository.findById(archivosPrograma.getId()).block();
        updatedArchivosPrograma
            .urlName(UPDATED_URL_NAME)
            .generationStorage(UPDATED_GENERATION_STORAGE)
            .storageContentType(UPDATED_STORAGE_CONTENT_TYPE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .planEstudio(UPDATED_PLAN_ESTUDIO);
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(updatedArchivosPrograma);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, archivosProgramaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
        ArchivosPrograma testArchivosPrograma = archivosProgramaList.get(archivosProgramaList.size() - 1);
        assertThat(testArchivosPrograma.getUrlName()).isEqualTo(UPDATED_URL_NAME);
        assertThat(testArchivosPrograma.getGenerationStorage()).isEqualTo(UPDATED_GENERATION_STORAGE);
        assertThat(testArchivosPrograma.getStorageContentType()).isEqualTo(UPDATED_STORAGE_CONTENT_TYPE);
        assertThat(testArchivosPrograma.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testArchivosPrograma.getPlanEstudio()).isEqualTo(UPDATED_PLAN_ESTUDIO);
    }

    @Test
    void putNonExistingArchivosPrograma() throws Exception {
        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();
        archivosPrograma.setId(count.incrementAndGet());

        // Create the ArchivosPrograma
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, archivosProgramaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchArchivosPrograma() throws Exception {
        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();
        archivosPrograma.setId(count.incrementAndGet());

        // Create the ArchivosPrograma
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamArchivosPrograma() throws Exception {
        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();
        archivosPrograma.setId(count.incrementAndGet());

        // Create the ArchivosPrograma
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateArchivosProgramaWithPatch() throws Exception {
        // Initialize the database
        archivosProgramaRepository.save(archivosPrograma).block();

        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();

        // Update the archivosPrograma using partial update
        ArchivosPrograma partialUpdatedArchivosPrograma = new ArchivosPrograma();
        partialUpdatedArchivosPrograma.setId(archivosPrograma.getId());

        partialUpdatedArchivosPrograma
            .urlName(UPDATED_URL_NAME)
            .generationStorage(UPDATED_GENERATION_STORAGE)
            .storageContentType(UPDATED_STORAGE_CONTENT_TYPE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .planEstudio(UPDATED_PLAN_ESTUDIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArchivosPrograma.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArchivosPrograma))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
        ArchivosPrograma testArchivosPrograma = archivosProgramaList.get(archivosProgramaList.size() - 1);
        assertThat(testArchivosPrograma.getUrlName()).isEqualTo(UPDATED_URL_NAME);
        assertThat(testArchivosPrograma.getGenerationStorage()).isEqualTo(UPDATED_GENERATION_STORAGE);
        assertThat(testArchivosPrograma.getStorageContentType()).isEqualTo(UPDATED_STORAGE_CONTENT_TYPE);
        assertThat(testArchivosPrograma.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testArchivosPrograma.getPlanEstudio()).isEqualTo(UPDATED_PLAN_ESTUDIO);
    }

    @Test
    void fullUpdateArchivosProgramaWithPatch() throws Exception {
        // Initialize the database
        archivosProgramaRepository.save(archivosPrograma).block();

        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();

        // Update the archivosPrograma using partial update
        ArchivosPrograma partialUpdatedArchivosPrograma = new ArchivosPrograma();
        partialUpdatedArchivosPrograma.setId(archivosPrograma.getId());

        partialUpdatedArchivosPrograma
            .urlName(UPDATED_URL_NAME)
            .generationStorage(UPDATED_GENERATION_STORAGE)
            .storageContentType(UPDATED_STORAGE_CONTENT_TYPE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .planEstudio(UPDATED_PLAN_ESTUDIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedArchivosPrograma.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedArchivosPrograma))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
        ArchivosPrograma testArchivosPrograma = archivosProgramaList.get(archivosProgramaList.size() - 1);
        assertThat(testArchivosPrograma.getUrlName()).isEqualTo(UPDATED_URL_NAME);
        assertThat(testArchivosPrograma.getGenerationStorage()).isEqualTo(UPDATED_GENERATION_STORAGE);
        assertThat(testArchivosPrograma.getStorageContentType()).isEqualTo(UPDATED_STORAGE_CONTENT_TYPE);
        assertThat(testArchivosPrograma.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testArchivosPrograma.getPlanEstudio()).isEqualTo(UPDATED_PLAN_ESTUDIO);
    }

    @Test
    void patchNonExistingArchivosPrograma() throws Exception {
        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();
        archivosPrograma.setId(count.incrementAndGet());

        // Create the ArchivosPrograma
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, archivosProgramaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchArchivosPrograma() throws Exception {
        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();
        archivosPrograma.setId(count.incrementAndGet());

        // Create the ArchivosPrograma
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamArchivosPrograma() throws Exception {
        int databaseSizeBeforeUpdate = archivosProgramaRepository.findAll().collectList().block().size();
        archivosPrograma.setId(count.incrementAndGet());

        // Create the ArchivosPrograma
        ArchivosProgramaDTO archivosProgramaDTO = archivosProgramaMapper.toDto(archivosPrograma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(archivosProgramaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ArchivosPrograma in the database
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteArchivosPrograma() {
        // Initialize the database
        archivosProgramaRepository.save(archivosPrograma).block();

        int databaseSizeBeforeDelete = archivosProgramaRepository.findAll().collectList().block().size();

        // Delete the archivosPrograma
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, archivosPrograma.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ArchivosPrograma> archivosProgramaList = archivosProgramaRepository.findAll().collectList().block();
        assertThat(archivosProgramaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
