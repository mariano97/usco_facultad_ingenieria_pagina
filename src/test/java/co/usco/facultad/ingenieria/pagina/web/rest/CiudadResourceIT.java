package co.usco.facultad.ingenieria.pagina.web.rest;

import static co.usco.facultad.ingenieria.pagina.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import co.usco.facultad.ingenieria.pagina.domain.Estados;
import co.usco.facultad.ingenieria.pagina.repository.CiudadRepository;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.service.CiudadService;
import co.usco.facultad.ingenieria.pagina.service.dto.CiudadDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.CiudadMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CiudadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CiudadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUD = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUD = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUD = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUD = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/ciudads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CiudadRepository ciudadRepository;

    @Mock
    private CiudadRepository ciudadRepositoryMock;

    @Autowired
    private CiudadMapper ciudadMapper;

    @Mock
    private CiudadService ciudadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ciudad ciudad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ciudad createEntity(EntityManager em) {
        Ciudad ciudad = new Ciudad().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO).latitud(DEFAULT_LATITUD).longitud(DEFAULT_LONGITUD);
        // Add required entity
        Estados estados;
        estados = em.insert(EstadosResourceIT.createEntity(em)).block();
        ciudad.setEstados(estados);
        return ciudad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ciudad createUpdatedEntity(EntityManager em) {
        Ciudad ciudad = new Ciudad().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).latitud(UPDATED_LATITUD).longitud(UPDATED_LONGITUD);
        // Add required entity
        Estados estados;
        estados = em.insert(EstadosResourceIT.createUpdatedEntity(em)).block();
        ciudad.setEstados(estados);
        return ciudad;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ciudad.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        EstadosResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        ciudad = createEntity(em);
    }

    @Test
    void createCiudad() throws Exception {
        int databaseSizeBeforeCreate = ciudadRepository.findAll().collectList().block().size();
        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeCreate + 1);
        Ciudad testCiudad = ciudadList.get(ciudadList.size() - 1);
        assertThat(testCiudad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCiudad.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCiudad.getLatitud()).isEqualByComparingTo(DEFAULT_LATITUD);
        assertThat(testCiudad.getLongitud()).isEqualByComparingTo(DEFAULT_LONGITUD);
    }

    @Test
    void createCiudadWithExistingId() throws Exception {
        // Create the Ciudad with an existing ID
        ciudad.setId(1L);
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        int databaseSizeBeforeCreate = ciudadRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = ciudadRepository.findAll().collectList().block().size();
        // set the field null
        ciudad.setNombre(null);

        // Create the Ciudad, which fails.
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ciudadRepository.findAll().collectList().block().size();
        // set the field null
        ciudad.setCodigo(null);

        // Create the Ciudad, which fails.
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCiudads() {
        // Initialize the database
        ciudadRepository.save(ciudad).block();

        // Get all the ciudadList
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
            .value(hasItem(ciudad.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].latitud")
            .value(hasItem(sameNumber(DEFAULT_LATITUD)))
            .jsonPath("$.[*].longitud")
            .value(hasItem(sameNumber(DEFAULT_LONGITUD)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCiudadsWithEagerRelationshipsIsEnabled() {
        when(ciudadServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(ciudadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCiudadsWithEagerRelationshipsIsNotEnabled() {
        when(ciudadServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(ciudadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCiudad() {
        // Initialize the database
        ciudadRepository.save(ciudad).block();

        // Get the ciudad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ciudad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ciudad.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.latitud")
            .value(is(sameNumber(DEFAULT_LATITUD)))
            .jsonPath("$.longitud")
            .value(is(sameNumber(DEFAULT_LONGITUD)));
    }

    @Test
    void getNonExistingCiudad() {
        // Get the ciudad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCiudad() throws Exception {
        // Initialize the database
        ciudadRepository.save(ciudad).block();

        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();

        // Update the ciudad
        Ciudad updatedCiudad = ciudadRepository.findById(ciudad.getId()).block();
        updatedCiudad.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).latitud(UPDATED_LATITUD).longitud(UPDATED_LONGITUD);
        CiudadDTO ciudadDTO = ciudadMapper.toDto(updatedCiudad);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ciudadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
        Ciudad testCiudad = ciudadList.get(ciudadList.size() - 1);
        assertThat(testCiudad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCiudad.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCiudad.getLatitud()).isEqualByComparingTo(UPDATED_LATITUD);
        assertThat(testCiudad.getLongitud()).isEqualByComparingTo(UPDATED_LONGITUD);
    }

    @Test
    void putNonExistingCiudad() throws Exception {
        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();
        ciudad.setId(count.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ciudadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCiudad() throws Exception {
        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();
        ciudad.setId(count.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCiudad() throws Exception {
        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();
        ciudad.setId(count.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCiudadWithPatch() throws Exception {
        // Initialize the database
        ciudadRepository.save(ciudad).block();

        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();

        // Update the ciudad using partial update
        Ciudad partialUpdatedCiudad = new Ciudad();
        partialUpdatedCiudad.setId(ciudad.getId());

        partialUpdatedCiudad.latitud(UPDATED_LATITUD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCiudad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCiudad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
        Ciudad testCiudad = ciudadList.get(ciudadList.size() - 1);
        assertThat(testCiudad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCiudad.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCiudad.getLatitud()).isEqualByComparingTo(UPDATED_LATITUD);
        assertThat(testCiudad.getLongitud()).isEqualByComparingTo(DEFAULT_LONGITUD);
    }

    @Test
    void fullUpdateCiudadWithPatch() throws Exception {
        // Initialize the database
        ciudadRepository.save(ciudad).block();

        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();

        // Update the ciudad using partial update
        Ciudad partialUpdatedCiudad = new Ciudad();
        partialUpdatedCiudad.setId(ciudad.getId());

        partialUpdatedCiudad.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).latitud(UPDATED_LATITUD).longitud(UPDATED_LONGITUD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCiudad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCiudad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
        Ciudad testCiudad = ciudadList.get(ciudadList.size() - 1);
        assertThat(testCiudad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCiudad.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCiudad.getLatitud()).isEqualByComparingTo(UPDATED_LATITUD);
        assertThat(testCiudad.getLongitud()).isEqualByComparingTo(UPDATED_LONGITUD);
    }

    @Test
    void patchNonExistingCiudad() throws Exception {
        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();
        ciudad.setId(count.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ciudadDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCiudad() throws Exception {
        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();
        ciudad.setId(count.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCiudad() throws Exception {
        int databaseSizeBeforeUpdate = ciudadRepository.findAll().collectList().block().size();
        ciudad.setId(count.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ciudadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ciudad in the database
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCiudad() {
        // Initialize the database
        ciudadRepository.save(ciudad).block();

        int databaseSizeBeforeDelete = ciudadRepository.findAll().collectList().block().size();

        // Delete the ciudad
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ciudad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ciudad> ciudadList = ciudadRepository.findAll().collectList().block();
        assertThat(ciudadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
