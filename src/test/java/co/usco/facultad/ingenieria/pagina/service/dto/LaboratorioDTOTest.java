package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LaboratorioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LaboratorioDTO.class);
        LaboratorioDTO laboratorioDTO1 = new LaboratorioDTO();
        laboratorioDTO1.setId(1L);
        LaboratorioDTO laboratorioDTO2 = new LaboratorioDTO();
        assertThat(laboratorioDTO1).isNotEqualTo(laboratorioDTO2);
        laboratorioDTO2.setId(laboratorioDTO1.getId());
        assertThat(laboratorioDTO1).isEqualTo(laboratorioDTO2);
        laboratorioDTO2.setId(2L);
        assertThat(laboratorioDTO1).isNotEqualTo(laboratorioDTO2);
        laboratorioDTO1.setId(null);
        assertThat(laboratorioDTO1).isNotEqualTo(laboratorioDTO2);
    }
}
