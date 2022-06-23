package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TituloAcademicoProfesorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TituloAcademicoProfesor.class);
        TituloAcademicoProfesor tituloAcademicoProfesor1 = new TituloAcademicoProfesor();
        tituloAcademicoProfesor1.setId(1L);
        TituloAcademicoProfesor tituloAcademicoProfesor2 = new TituloAcademicoProfesor();
        tituloAcademicoProfesor2.setId(tituloAcademicoProfesor1.getId());
        assertThat(tituloAcademicoProfesor1).isEqualTo(tituloAcademicoProfesor2);
        tituloAcademicoProfesor2.setId(2L);
        assertThat(tituloAcademicoProfesor1).isNotEqualTo(tituloAcademicoProfesor2);
        tituloAcademicoProfesor1.setId(null);
        assertThat(tituloAcademicoProfesor1).isNotEqualTo(tituloAcademicoProfesor2);
    }
}
