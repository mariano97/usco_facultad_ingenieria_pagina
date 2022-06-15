package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CiudadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ciudad.class);
        Ciudad ciudad1 = new Ciudad();
        ciudad1.setId(1L);
        Ciudad ciudad2 = new Ciudad();
        ciudad2.setId(ciudad1.getId());
        assertThat(ciudad1).isEqualTo(ciudad2);
        ciudad2.setId(2L);
        assertThat(ciudad1).isNotEqualTo(ciudad2);
        ciudad1.setId(null);
        assertThat(ciudad1).isNotEqualTo(ciudad2);
    }
}
