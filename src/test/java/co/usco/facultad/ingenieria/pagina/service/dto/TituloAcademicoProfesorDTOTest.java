package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TituloAcademicoProfesorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TituloAcademicoProfesorDTO.class);
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO1 = new TituloAcademicoProfesorDTO();
        tituloAcademicoProfesorDTO1.setId(1L);
        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO2 = new TituloAcademicoProfesorDTO();
        assertThat(tituloAcademicoProfesorDTO1).isNotEqualTo(tituloAcademicoProfesorDTO2);
        tituloAcademicoProfesorDTO2.setId(tituloAcademicoProfesorDTO1.getId());
        assertThat(tituloAcademicoProfesorDTO1).isEqualTo(tituloAcademicoProfesorDTO2);
        tituloAcademicoProfesorDTO2.setId(2L);
        assertThat(tituloAcademicoProfesorDTO1).isNotEqualTo(tituloAcademicoProfesorDTO2);
        tituloAcademicoProfesorDTO1.setId(null);
        assertThat(tituloAcademicoProfesorDTO1).isNotEqualTo(tituloAcademicoProfesorDTO2);
    }
}
