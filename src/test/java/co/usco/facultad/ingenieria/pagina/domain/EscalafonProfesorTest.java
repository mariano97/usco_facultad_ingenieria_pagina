package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EscalafonProfesorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EscalafonProfesor.class);
        EscalafonProfesor escalafonProfesor1 = new EscalafonProfesor();
        escalafonProfesor1.setId(1L);
        EscalafonProfesor escalafonProfesor2 = new EscalafonProfesor();
        escalafonProfesor2.setId(escalafonProfesor1.getId());
        assertThat(escalafonProfesor1).isEqualTo(escalafonProfesor2);
        escalafonProfesor2.setId(2L);
        assertThat(escalafonProfesor1).isNotEqualTo(escalafonProfesor2);
        escalafonProfesor1.setId(null);
        assertThat(escalafonProfesor1).isNotEqualTo(escalafonProfesor2);
    }
}
