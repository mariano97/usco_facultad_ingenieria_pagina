package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RedesProgramaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedesPrograma.class);
        RedesPrograma redesPrograma1 = new RedesPrograma();
        redesPrograma1.setId(1L);
        RedesPrograma redesPrograma2 = new RedesPrograma();
        redesPrograma2.setId(redesPrograma1.getId());
        assertThat(redesPrograma1).isEqualTo(redesPrograma2);
        redesPrograma2.setId(2L);
        assertThat(redesPrograma1).isNotEqualTo(redesPrograma2);
        redesPrograma1.setId(null);
        assertThat(redesPrograma1).isNotEqualTo(redesPrograma2);
    }
}
