package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadosDTO.class);
        EstadosDTO estadosDTO1 = new EstadosDTO();
        estadosDTO1.setId(1L);
        EstadosDTO estadosDTO2 = new EstadosDTO();
        assertThat(estadosDTO1).isNotEqualTo(estadosDTO2);
        estadosDTO2.setId(estadosDTO1.getId());
        assertThat(estadosDTO1).isEqualTo(estadosDTO2);
        estadosDTO2.setId(2L);
        assertThat(estadosDTO1).isNotEqualTo(estadosDTO2);
        estadosDTO1.setId(null);
        assertThat(estadosDTO1).isNotEqualTo(estadosDTO2);
    }
}
