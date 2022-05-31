package co.usco.facultad.ingenieria.pagina.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import co.usco.facultad.ingenieria.pagina.IntegrationTest;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.repository.EntityManager;
import co.usco.facultad.ingenieria.pagina.repository.ProfesorRepository;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.mapper.ProfesorMapper;
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
 * Integration tests for the {@link ProfesorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProfesorResourceIT {

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ALTERNATIVO = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ALTERNATIVO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String DEFAULT_PERFIL = "AAAAAAAAAA";
    private static final String UPDATED_PERFIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_OFICINA = "AAAAAAAAAA";
    private static final String UPDATED_OFICINA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profesors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ProfesorMapper profesorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Profesor profesor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profesor createEntity(EntityManager em) {
        Profesor profesor = new Profesor()
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .emailAlternativo(DEFAULT_EMAIL_ALTERNATIVO)
            .activo(DEFAULT_ACTIVO)
            .perfil(DEFAULT_PERFIL)
            .telefonoCelular(DEFAULT_TELEFONO_CELULAR)
            .oficina(DEFAULT_OFICINA);
        return profesor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profesor createUpdatedEntity(EntityManager em) {
        Profesor profesor = new Profesor()
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .emailAlternativo(UPDATED_EMAIL_ALTERNATIVO)
            .activo(UPDATED_ACTIVO)
            .perfil(UPDATED_PERFIL)
            .telefonoCelular(UPDATED_TELEFONO_CELULAR)
            .oficina(UPDATED_OFICINA);
        return profesor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Profesor.class).block();
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
        profesor = createEntity(em);
    }

    @Test
    void createProfesor() throws Exception {
        int databaseSizeBeforeCreate = profesorRepository.findAll().collectList().block().size();
        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeCreate + 1);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testProfesor.getEmailAlternativo()).isEqualTo(DEFAULT_EMAIL_ALTERNATIVO);
        assertThat(testProfesor.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testProfesor.getPerfil()).isEqualTo(DEFAULT_PERFIL);
        assertThat(testProfesor.getTelefonoCelular()).isEqualTo(DEFAULT_TELEFONO_CELULAR);
        assertThat(testProfesor.getOficina()).isEqualTo(DEFAULT_OFICINA);
    }

    @Test
    void createProfesorWithExistingId() throws Exception {
        // Create the Profesor with an existing ID
        profesor.setId(1L);
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        int databaseSizeBeforeCreate = profesorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkActivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = profesorRepository.findAll().collectList().block().size();
        // set the field null
        profesor.setActivo(null);

        // Create the Profesor, which fails.
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPerfilIsRequired() throws Exception {
        int databaseSizeBeforeTest = profesorRepository.findAll().collectList().block().size();
        // set the field null
        profesor.setPerfil(null);

        // Create the Profesor, which fails.
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProfesors() {
        // Initialize the database
        profesorRepository.save(profesor).block();

        // Get all the profesorList
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
            .value(hasItem(profesor.getId().intValue()))
            .jsonPath("$.[*].segundoNombre")
            .value(hasItem(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.[*].segundoApellido")
            .value(hasItem(DEFAULT_SEGUNDO_APELLIDO))
            .jsonPath("$.[*].emailAlternativo")
            .value(hasItem(DEFAULT_EMAIL_ALTERNATIVO))
            .jsonPath("$.[*].activo")
            .value(hasItem(DEFAULT_ACTIVO.booleanValue()))
            .jsonPath("$.[*].perfil")
            .value(hasItem(DEFAULT_PERFIL))
            .jsonPath("$.[*].telefonoCelular")
            .value(hasItem(DEFAULT_TELEFONO_CELULAR))
            .jsonPath("$.[*].oficina")
            .value(hasItem(DEFAULT_OFICINA));
    }

    @Test
    void getProfesor() {
        // Initialize the database
        profesorRepository.save(profesor).block();

        // Get the profesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, profesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(profesor.getId().intValue()))
            .jsonPath("$.segundoNombre")
            .value(is(DEFAULT_SEGUNDO_NOMBRE))
            .jsonPath("$.segundoApellido")
            .value(is(DEFAULT_SEGUNDO_APELLIDO))
            .jsonPath("$.emailAlternativo")
            .value(is(DEFAULT_EMAIL_ALTERNATIVO))
            .jsonPath("$.activo")
            .value(is(DEFAULT_ACTIVO.booleanValue()))
            .jsonPath("$.perfil")
            .value(is(DEFAULT_PERFIL))
            .jsonPath("$.telefonoCelular")
            .value(is(DEFAULT_TELEFONO_CELULAR))
            .jsonPath("$.oficina")
            .value(is(DEFAULT_OFICINA));
    }

    @Test
    void getNonExistingProfesor() {
        // Get the profesor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProfesor() throws Exception {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();

        // Update the profesor
        Profesor updatedProfesor = profesorRepository.findById(profesor.getId()).block();
        updatedProfesor
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .emailAlternativo(UPDATED_EMAIL_ALTERNATIVO)
            .activo(UPDATED_ACTIVO)
            .perfil(UPDATED_PERFIL)
            .telefonoCelular(UPDATED_TELEFONO_CELULAR)
            .oficina(UPDATED_OFICINA);
        ProfesorDTO profesorDTO = profesorMapper.toDto(updatedProfesor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testProfesor.getEmailAlternativo()).isEqualTo(UPDATED_EMAIL_ALTERNATIVO);
        assertThat(testProfesor.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testProfesor.getPerfil()).isEqualTo(UPDATED_PERFIL);
        assertThat(testProfesor.getTelefonoCelular()).isEqualTo(UPDATED_TELEFONO_CELULAR);
        assertThat(testProfesor.getOficina()).isEqualTo(UPDATED_OFICINA);
    }

    @Test
    void putNonExistingProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profesorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProfesorWithPatch() throws Exception {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();

        // Update the profesor using partial update
        Profesor partialUpdatedProfesor = new Profesor();
        partialUpdatedProfesor.setId(profesor.getId());

        partialUpdatedProfesor
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .emailAlternativo(UPDATED_EMAIL_ALTERNATIVO)
            .activo(UPDATED_ACTIVO)
            .telefonoCelular(UPDATED_TELEFONO_CELULAR)
            .oficina(UPDATED_OFICINA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testProfesor.getEmailAlternativo()).isEqualTo(UPDATED_EMAIL_ALTERNATIVO);
        assertThat(testProfesor.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testProfesor.getPerfil()).isEqualTo(DEFAULT_PERFIL);
        assertThat(testProfesor.getTelefonoCelular()).isEqualTo(UPDATED_TELEFONO_CELULAR);
        assertThat(testProfesor.getOficina()).isEqualTo(UPDATED_OFICINA);
    }

    @Test
    void fullUpdateProfesorWithPatch() throws Exception {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();

        // Update the profesor using partial update
        Profesor partialUpdatedProfesor = new Profesor();
        partialUpdatedProfesor.setId(profesor.getId());

        partialUpdatedProfesor
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .emailAlternativo(UPDATED_EMAIL_ALTERNATIVO)
            .activo(UPDATED_ACTIVO)
            .perfil(UPDATED_PERFIL)
            .telefonoCelular(UPDATED_TELEFONO_CELULAR)
            .oficina(UPDATED_OFICINA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfesor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
        Profesor testProfesor = profesorList.get(profesorList.size() - 1);
        assertThat(testProfesor.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testProfesor.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testProfesor.getEmailAlternativo()).isEqualTo(UPDATED_EMAIL_ALTERNATIVO);
        assertThat(testProfesor.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testProfesor.getPerfil()).isEqualTo(UPDATED_PERFIL);
        assertThat(testProfesor.getTelefonoCelular()).isEqualTo(UPDATED_TELEFONO_CELULAR);
        assertThat(testProfesor.getOficina()).isEqualTo(UPDATED_OFICINA);
    }

    @Test
    void patchNonExistingProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, profesorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProfesor() throws Exception {
        int databaseSizeBeforeUpdate = profesorRepository.findAll().collectList().block().size();
        profesor.setId(count.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profesorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profesor in the database
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProfesor() {
        // Initialize the database
        profesorRepository.save(profesor).block();

        int databaseSizeBeforeDelete = profesorRepository.findAll().collectList().block().size();

        // Delete the profesor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, profesor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Profesor> profesorList = profesorRepository.findAll().collectList().block();
        assertThat(profesorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
