package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaTiposCatalogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaTiposCatalogo.class);
        TablaTiposCatalogo tablaTiposCatalogo1 = new TablaTiposCatalogo();
        tablaTiposCatalogo1.setId(1L);
        TablaTiposCatalogo tablaTiposCatalogo2 = new TablaTiposCatalogo();
        tablaTiposCatalogo2.setId(tablaTiposCatalogo1.getId());
        assertThat(tablaTiposCatalogo1).isEqualTo(tablaTiposCatalogo2);
        tablaTiposCatalogo2.setId(2L);
        assertThat(tablaTiposCatalogo1).isNotEqualTo(tablaTiposCatalogo2);
        tablaTiposCatalogo1.setId(null);
        assertThat(tablaTiposCatalogo1).isNotEqualTo(tablaTiposCatalogo2);
    }
}
