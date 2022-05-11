package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaElementoCatalogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaElementoCatalogo.class);
        TablaElementoCatalogo tablaElementoCatalogo1 = new TablaElementoCatalogo();
        tablaElementoCatalogo1.setId(1L);
        TablaElementoCatalogo tablaElementoCatalogo2 = new TablaElementoCatalogo();
        tablaElementoCatalogo2.setId(tablaElementoCatalogo1.getId());
        assertThat(tablaElementoCatalogo1).isEqualTo(tablaElementoCatalogo2);
        tablaElementoCatalogo2.setId(2L);
        assertThat(tablaElementoCatalogo1).isNotEqualTo(tablaElementoCatalogo2);
        tablaElementoCatalogo1.setId(null);
        assertThat(tablaElementoCatalogo1).isNotEqualTo(tablaElementoCatalogo2);
    }
}
