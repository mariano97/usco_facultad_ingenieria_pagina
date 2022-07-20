package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SemilleroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemilleroDTO.class);
        SemilleroDTO semilleroDTO1 = new SemilleroDTO();
        semilleroDTO1.setId(1L);
        SemilleroDTO semilleroDTO2 = new SemilleroDTO();
        assertThat(semilleroDTO1).isNotEqualTo(semilleroDTO2);
        semilleroDTO2.setId(semilleroDTO1.getId());
        assertThat(semilleroDTO1).isEqualTo(semilleroDTO2);
        semilleroDTO2.setId(2L);
        assertThat(semilleroDTO1).isNotEqualTo(semilleroDTO2);
        semilleroDTO1.setId(null);
        assertThat(semilleroDTO1).isNotEqualTo(semilleroDTO2);
    }
}
