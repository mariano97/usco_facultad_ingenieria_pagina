package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Noticia;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.NoticiaRepository;
import co.usco.facultad.ingenieria.pagina.service.NoticiaService;
import co.usco.facultad.ingenieria.pagina.service.dto.NoticiaDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.NoticiaMapper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link NoticiaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NoticiaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_SINTESIS = "AAAAAAAAAA";
    private static final String UPDATED_SINTESIS = "BBBBBBBBBB";

    private static final String DEFAULT_CUERPO_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_CUERPO_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_URL_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_FOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/noticias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Mock
    private NoticiaRepository noticiaRepositoryMock;

    @Autowired
    private NoticiaMapper noticiaMapper;

    @Mock
    private NoticiaService noticiaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Noticia noticia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Noticia createEntity(EntityManager em) {
        Noticia noticia = new Noticia()
            .titulo(DEFAULT_TITULO)
            .sintesis(DEFAULT_SINTESIS)
            .cuerpoDescripcion(DEFAULT_CUERPO_DESCRIPCION)
            .fecha(DEFAULT_FECHA)
            .urlFoto(DEFAULT_URL_FOTO);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createEntity(em)).block();
        noticia.setFacultad(facultad);
        return noticia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Noticia createUpdatedEntity(EntityManager em) {
        Noticia noticia = new Noticia()
            .titulo(UPDATED_TITULO)
            .sintesis(UPDATED_SINTESIS)
            .cuerpoDescripcion(UPDATED_CUERPO_DESCRIPCION)
            .fecha(UPDATED_FECHA)
            .urlFoto(UPDATED_URL_FOTO);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createUpdatedEntity(em)).block();
        noticia.setFacultad(facultad);
        return noticia;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Noticia.class).block();
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
        noticia = createEntity(em);
    }

    @Test
    void createNoticia() throws Exception {
        int databaseSizeBeforeCreate = noticiaRepository.findAll().collectList().block().size();
        // Create the Noticia
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeCreate + 1);
        Noticia testNoticia = noticiaList.get(noticiaList.size() - 1);
        assertThat(testNoticia.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testNoticia.getSintesis()).isEqualTo(DEFAULT_SINTESIS);
        assertThat(testNoticia.getCuerpoDescripcion()).isEqualTo(DEFAULT_CUERPO_DESCRIPCION);
        assertThat(testNoticia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testNoticia.getUrlFoto()).isEqualTo(DEFAULT_URL_FOTO);
    }

    @Test
    void createNoticiaWithExistingId() throws Exception {
        // Create the Noticia with an existing ID
        noticia.setId(1L);
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        int databaseSizeBeforeCreate = noticiaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticiaRepository.findAll().collectList().block().size();
        // set the field null
        noticia.setTitulo(null);

        // Create the Noticia, which fails.
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSintesisIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticiaRepository.findAll().collectList().block().size();
        // set the field null
        noticia.setSintesis(null);

        // Create the Noticia, which fails.
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCuerpoDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticiaRepository.findAll().collectList().block().size();
        // set the field null
        noticia.setCuerpoDescripcion(null);

        // Create the Noticia, which fails.
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticiaRepository.findAll().collectList().block().size();
        // set the field null
        noticia.setFecha(null);

        // Create the Noticia, which fails.
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllNoticias() {
        // Initialize the database
        noticiaRepository.save(noticia).block();

        // Get all the noticiaList
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
            .value(hasItem(noticia.getId().intValue()))
            .jsonPath("$.[*].titulo")
            .value(hasItem(DEFAULT_TITULO))
            .jsonPath("$.[*].sintesis")
            .value(hasItem(DEFAULT_SINTESIS))
            .jsonPath("$.[*].cuerpoDescripcion")
            .value(hasItem(DEFAULT_CUERPO_DESCRIPCION))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()))
            .jsonPath("$.[*].urlFoto")
            .value(hasItem(DEFAULT_URL_FOTO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNoticiasWithEagerRelationshipsIsEnabled() {
        when(noticiaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(noticiaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNoticiasWithEagerRelationshipsIsNotEnabled() {
        when(noticiaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(noticiaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getNoticia() {
        // Initialize the database
        noticiaRepository.save(noticia).block();

        // Get the noticia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, noticia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(noticia.getId().intValue()))
            .jsonPath("$.titulo")
            .value(is(DEFAULT_TITULO))
            .jsonPath("$.sintesis")
            .value(is(DEFAULT_SINTESIS))
            .jsonPath("$.cuerpoDescripcion")
            .value(is(DEFAULT_CUERPO_DESCRIPCION))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()))
            .jsonPath("$.urlFoto")
            .value(is(DEFAULT_URL_FOTO));
    }

    @Test
    void getNonExistingNoticia() {
        // Get the noticia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewNoticia() throws Exception {
        // Initialize the database
        noticiaRepository.save(noticia).block();

        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();

        // Update the noticia
        Noticia updatedNoticia = noticiaRepository.findById(noticia.getId()).block();
        updatedNoticia
            .titulo(UPDATED_TITULO)
            .sintesis(UPDATED_SINTESIS)
            .cuerpoDescripcion(UPDATED_CUERPO_DESCRIPCION)
            .fecha(UPDATED_FECHA)
            .urlFoto(UPDATED_URL_FOTO);
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(updatedNoticia);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, noticiaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
        Noticia testNoticia = noticiaList.get(noticiaList.size() - 1);
        assertThat(testNoticia.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testNoticia.getSintesis()).isEqualTo(UPDATED_SINTESIS);
        assertThat(testNoticia.getCuerpoDescripcion()).isEqualTo(UPDATED_CUERPO_DESCRIPCION);
        assertThat(testNoticia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testNoticia.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void putNonExistingNoticia() throws Exception {
        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();
        noticia.setId(count.incrementAndGet());

        // Create the Noticia
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, noticiaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNoticia() throws Exception {
        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();
        noticia.setId(count.incrementAndGet());

        // Create the Noticia
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNoticia() throws Exception {
        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();
        noticia.setId(count.incrementAndGet());

        // Create the Noticia
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNoticiaWithPatch() throws Exception {
        // Initialize the database
        noticiaRepository.save(noticia).block();

        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();

        // Update the noticia using partial update
        Noticia partialUpdatedNoticia = new Noticia();
        partialUpdatedNoticia.setId(noticia.getId());

        partialUpdatedNoticia
            .titulo(UPDATED_TITULO)
            .sintesis(UPDATED_SINTESIS)
            .cuerpoDescripcion(UPDATED_CUERPO_DESCRIPCION)
            .urlFoto(UPDATED_URL_FOTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNoticia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNoticia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
        Noticia testNoticia = noticiaList.get(noticiaList.size() - 1);
        assertThat(testNoticia.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testNoticia.getSintesis()).isEqualTo(UPDATED_SINTESIS);
        assertThat(testNoticia.getCuerpoDescripcion()).isEqualTo(UPDATED_CUERPO_DESCRIPCION);
        assertThat(testNoticia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testNoticia.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void fullUpdateNoticiaWithPatch() throws Exception {
        // Initialize the database
        noticiaRepository.save(noticia).block();

        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();

        // Update the noticia using partial update
        Noticia partialUpdatedNoticia = new Noticia();
        partialUpdatedNoticia.setId(noticia.getId());

        partialUpdatedNoticia
            .titulo(UPDATED_TITULO)
            .sintesis(UPDATED_SINTESIS)
            .cuerpoDescripcion(UPDATED_CUERPO_DESCRIPCION)
            .fecha(UPDATED_FECHA)
            .urlFoto(UPDATED_URL_FOTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNoticia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNoticia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
        Noticia testNoticia = noticiaList.get(noticiaList.size() - 1);
        assertThat(testNoticia.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testNoticia.getSintesis()).isEqualTo(UPDATED_SINTESIS);
        assertThat(testNoticia.getCuerpoDescripcion()).isEqualTo(UPDATED_CUERPO_DESCRIPCION);
        assertThat(testNoticia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testNoticia.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void patchNonExistingNoticia() throws Exception {
        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();
        noticia.setId(count.incrementAndGet());

        // Create the Noticia
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, noticiaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNoticia() throws Exception {
        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();
        noticia.setId(count.incrementAndGet());

        // Create the Noticia
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNoticia() throws Exception {
        int databaseSizeBeforeUpdate = noticiaRepository.findAll().collectList().block().size();
        noticia.setId(count.incrementAndGet());

        // Create the Noticia
        NoticiaDTO noticiaDTO = noticiaMapper.toDto(noticia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noticiaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Noticia in the database
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNoticia() {
        // Initialize the database
        noticiaRepository.save(noticia).block();

        int databaseSizeBeforeDelete = noticiaRepository.findAll().collectList().block().size();

        // Delete the noticia
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, noticia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Noticia> noticiaList = noticiaRepository.findAll().collectList().block();
        assertThat(noticiaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
