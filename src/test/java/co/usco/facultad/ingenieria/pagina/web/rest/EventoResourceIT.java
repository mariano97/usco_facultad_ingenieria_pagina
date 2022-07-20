package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Evento;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.EventoRepository;
import co.usco.facultad.ingenieria.pagina.service.EventoService;
import co.usco.facultad.ingenieria.pagina.service.dto.EventoDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.EventoMapper;
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
 * Integration tests for the {@link EventoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EventoResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_CUERPO = "AAAAAAAAAA";
    private static final String UPDATED_CUERPO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LUGAR = "AAAAAAAAAA";
    private static final String UPDATED_LUGAR = "BBBBBBBBBB";

    private static final String DEFAULT_URL_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_FOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/eventos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventoRepository eventoRepository;

    @Mock
    private EventoRepository eventoRepositoryMock;

    @Autowired
    private EventoMapper eventoMapper;

    @Mock
    private EventoService eventoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Evento evento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createEntity(EntityManager em) {
        Evento evento = new Evento()
            .titulo(DEFAULT_TITULO)
            .cuerpo(DEFAULT_CUERPO)
            .fecha(DEFAULT_FECHA)
            .hora(DEFAULT_HORA)
            .lugar(DEFAULT_LUGAR)
            .urlFoto(DEFAULT_URL_FOTO);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createEntity(em)).block();
        evento.setFacultad(facultad);
        return evento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createUpdatedEntity(EntityManager em) {
        Evento evento = new Evento()
            .titulo(UPDATED_TITULO)
            .cuerpo(UPDATED_CUERPO)
            .fecha(UPDATED_FECHA)
            .hora(UPDATED_HORA)
            .lugar(UPDATED_LUGAR)
            .urlFoto(UPDATED_URL_FOTO);
        // Add required entity
        Facultad facultad;
        facultad = em.insert(FacultadResourceIT.createUpdatedEntity(em)).block();
        evento.setFacultad(facultad);
        return evento;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Evento.class).block();
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
        evento = createEntity(em);
    }

    @Test
    void createEvento() throws Exception {
        int databaseSizeBeforeCreate = eventoRepository.findAll().collectList().block().size();
        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate + 1);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testEvento.getCuerpo()).isEqualTo(DEFAULT_CUERPO);
        assertThat(testEvento.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testEvento.getHora()).isEqualTo(DEFAULT_HORA);
        assertThat(testEvento.getLugar()).isEqualTo(DEFAULT_LUGAR);
        assertThat(testEvento.getUrlFoto()).isEqualTo(DEFAULT_URL_FOTO);
    }

    @Test
    void createEventoWithExistingId() throws Exception {
        // Create the Evento with an existing ID
        evento.setId(1L);
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        int databaseSizeBeforeCreate = eventoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().collectList().block().size();
        // set the field null
        evento.setTitulo(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCuerpoIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().collectList().block().size();
        // set the field null
        evento.setCuerpo(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().collectList().block().size();
        // set the field null
        evento.setFecha(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHoraIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().collectList().block().size();
        // set the field null
        evento.setHora(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLugarIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().collectList().block().size();
        // set the field null
        evento.setLugar(null);

        // Create the Evento, which fails.
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEventos() {
        // Initialize the database
        eventoRepository.save(evento).block();

        // Get all the eventoList
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
            .value(hasItem(evento.getId().intValue()))
            .jsonPath("$.[*].titulo")
            .value(hasItem(DEFAULT_TITULO))
            .jsonPath("$.[*].cuerpo")
            .value(hasItem(DEFAULT_CUERPO))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()))
            .jsonPath("$.[*].hora")
            .value(hasItem(DEFAULT_HORA.toString()))
            .jsonPath("$.[*].lugar")
            .value(hasItem(DEFAULT_LUGAR))
            .jsonPath("$.[*].urlFoto")
            .value(hasItem(DEFAULT_URL_FOTO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventosWithEagerRelationshipsIsEnabled() {
        when(eventoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(eventoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventosWithEagerRelationshipsIsNotEnabled() {
        when(eventoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(eventoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getEvento() {
        // Initialize the database
        eventoRepository.save(evento).block();

        // Get the evento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, evento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(evento.getId().intValue()))
            .jsonPath("$.titulo")
            .value(is(DEFAULT_TITULO))
            .jsonPath("$.cuerpo")
            .value(is(DEFAULT_CUERPO))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()))
            .jsonPath("$.hora")
            .value(is(DEFAULT_HORA.toString()))
            .jsonPath("$.lugar")
            .value(is(DEFAULT_LUGAR))
            .jsonPath("$.urlFoto")
            .value(is(DEFAULT_URL_FOTO));
    }

    @Test
    void getNonExistingEvento() {
        // Get the evento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEvento() throws Exception {
        // Initialize the database
        eventoRepository.save(evento).block();

        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();

        // Update the evento
        Evento updatedEvento = eventoRepository.findById(evento.getId()).block();
        updatedEvento
            .titulo(UPDATED_TITULO)
            .cuerpo(UPDATED_CUERPO)
            .fecha(UPDATED_FECHA)
            .hora(UPDATED_HORA)
            .lugar(UPDATED_LUGAR)
            .urlFoto(UPDATED_URL_FOTO);
        EventoDTO eventoDTO = eventoMapper.toDto(updatedEvento);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, eventoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testEvento.getCuerpo()).isEqualTo(UPDATED_CUERPO);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getHora()).isEqualTo(UPDATED_HORA);
        assertThat(testEvento.getLugar()).isEqualTo(UPDATED_LUGAR);
        assertThat(testEvento.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void putNonExistingEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();
        evento.setId(count.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, eventoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();
        evento.setId(count.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();
        evento.setId(count.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEventoWithPatch() throws Exception {
        // Initialize the database
        eventoRepository.save(evento).block();

        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();

        // Update the evento using partial update
        Evento partialUpdatedEvento = new Evento();
        partialUpdatedEvento.setId(evento.getId());

        partialUpdatedEvento.cuerpo(UPDATED_CUERPO).fecha(UPDATED_FECHA).urlFoto(UPDATED_URL_FOTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEvento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEvento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testEvento.getCuerpo()).isEqualTo(UPDATED_CUERPO);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getHora()).isEqualTo(DEFAULT_HORA);
        assertThat(testEvento.getLugar()).isEqualTo(DEFAULT_LUGAR);
        assertThat(testEvento.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void fullUpdateEventoWithPatch() throws Exception {
        // Initialize the database
        eventoRepository.save(evento).block();

        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();

        // Update the evento using partial update
        Evento partialUpdatedEvento = new Evento();
        partialUpdatedEvento.setId(evento.getId());

        partialUpdatedEvento
            .titulo(UPDATED_TITULO)
            .cuerpo(UPDATED_CUERPO)
            .fecha(UPDATED_FECHA)
            .hora(UPDATED_HORA)
            .lugar(UPDATED_LUGAR)
            .urlFoto(UPDATED_URL_FOTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEvento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEvento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testEvento.getCuerpo()).isEqualTo(UPDATED_CUERPO);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getHora()).isEqualTo(UPDATED_HORA);
        assertThat(testEvento.getLugar()).isEqualTo(UPDATED_LUGAR);
        assertThat(testEvento.getUrlFoto()).isEqualTo(UPDATED_URL_FOTO);
    }

    @Test
    void patchNonExistingEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();
        evento.setId(count.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, eventoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();
        evento.setId(count.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().collectList().block().size();
        evento.setId(count.incrementAndGet());

        // Create the Evento
        EventoDTO eventoDTO = eventoMapper.toDto(evento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEvento() {
        // Initialize the database
        eventoRepository.save(evento).block();

        int databaseSizeBeforeDelete = eventoRepository.findAll().collectList().block().size();

        // Delete the evento
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, evento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Evento> eventoList = eventoRepository.findAll().collectList().block();
        assertThat(eventoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
