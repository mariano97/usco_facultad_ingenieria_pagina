package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaisesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paises.class);
        Paises paises1 = new Paises();
        paises1.setId(1L);
        Paises paises2 = new Paises();
        paises2.setId(paises1.getId());
        assertThat(paises1).isEqualTo(paises2);
        paises2.setId(2L);
        assertThat(paises1).isNotEqualTo(paises2);
        paises1.setId(null);
        assertThat(paises1).isNotEqualTo(paises2);
    }
}
