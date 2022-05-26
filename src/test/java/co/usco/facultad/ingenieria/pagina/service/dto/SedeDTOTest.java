package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SedeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SedeDTO.class);
        SedeDTO sedeDTO1 = new SedeDTO();
        sedeDTO1.setId(1L);
        SedeDTO sedeDTO2 = new SedeDTO();
        assertThat(sedeDTO1).isNotEqualTo(sedeDTO2);
        sedeDTO2.setId(sedeDTO1.getId());
        assertThat(sedeDTO1).isEqualTo(sedeDTO2);
        sedeDTO2.setId(2L);
        assertThat(sedeDTO1).isNotEqualTo(sedeDTO2);
        sedeDTO1.setId(null);
        assertThat(sedeDTO1).isNotEqualTo(sedeDTO2);
    }
}
