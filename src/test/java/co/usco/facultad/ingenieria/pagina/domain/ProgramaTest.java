package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgramaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Programa.class);
        Programa programa1 = new Programa();
        programa1.setId(1L);
        Programa programa2 = new Programa();
        programa2.setId(programa1.getId());
        assertThat(programa1).isEqualTo(programa2);
        programa2.setId(2L);
        assertThat(programa1).isNotEqualTo(programa2);
        programa1.setId(null);
        assertThat(programa1).isNotEqualTo(programa2);
    }
}
