package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EscalafonProfesorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EscalafonProfesorDTO.class);
        EscalafonProfesorDTO escalafonProfesorDTO1 = new EscalafonProfesorDTO();
        escalafonProfesorDTO1.setId(1L);
        EscalafonProfesorDTO escalafonProfesorDTO2 = new EscalafonProfesorDTO();
        assertThat(escalafonProfesorDTO1).isNotEqualTo(escalafonProfesorDTO2);
        escalafonProfesorDTO2.setId(escalafonProfesorDTO1.getId());
        assertThat(escalafonProfesorDTO1).isEqualTo(escalafonProfesorDTO2);
        escalafonProfesorDTO2.setId(2L);
        assertThat(escalafonProfesorDTO1).isNotEqualTo(escalafonProfesorDTO2);
        escalafonProfesorDTO1.setId(null);
        assertThat(escalafonProfesorDTO1).isNotEqualTo(escalafonProfesorDTO2);
    }
}
