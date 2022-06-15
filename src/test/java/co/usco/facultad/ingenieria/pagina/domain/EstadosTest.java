package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estados.class);
        Estados estados1 = new Estados();
        estados1.setId(1L);
        Estados estados2 = new Estados();
        estados2.setId(estados1.getId());
        assertThat(estados1).isEqualTo(estados2);
        estados2.setId(2L);
        assertThat(estados1).isNotEqualTo(estados2);
        estados1.setId(null);
        assertThat(estados1).isNotEqualTo(estados2);
    }
}
