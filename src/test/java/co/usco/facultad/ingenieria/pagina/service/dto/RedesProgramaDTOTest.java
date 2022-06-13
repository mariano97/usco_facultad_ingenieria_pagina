package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RedesProgramaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedesProgramaDTO.class);
        RedesProgramaDTO redesProgramaDTO1 = new RedesProgramaDTO();
        redesProgramaDTO1.setId(1L);
        RedesProgramaDTO redesProgramaDTO2 = new RedesProgramaDTO();
        assertThat(redesProgramaDTO1).isNotEqualTo(redesProgramaDTO2);
        redesProgramaDTO2.setId(redesProgramaDTO1.getId());
        assertThat(redesProgramaDTO1).isEqualTo(redesProgramaDTO2);
        redesProgramaDTO2.setId(2L);
        assertThat(redesProgramaDTO1).isNotEqualTo(redesProgramaDTO2);
        redesProgramaDTO1.setId(null);
        assertThat(redesProgramaDTO1).isNotEqualTo(redesProgramaDTO2);
    }
}
