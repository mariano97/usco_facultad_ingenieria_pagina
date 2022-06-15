package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaisesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaisesDTO.class);
        PaisesDTO paisesDTO1 = new PaisesDTO();
        paisesDTO1.setId(1L);
        PaisesDTO paisesDTO2 = new PaisesDTO();
        assertThat(paisesDTO1).isNotEqualTo(paisesDTO2);
        paisesDTO2.setId(paisesDTO1.getId());
        assertThat(paisesDTO1).isEqualTo(paisesDTO2);
        paisesDTO2.setId(2L);
        assertThat(paisesDTO1).isNotEqualTo(paisesDTO2);
        paisesDTO1.setId(null);
        assertThat(paisesDTO1).isNotEqualTo(paisesDTO2);
    }
}
