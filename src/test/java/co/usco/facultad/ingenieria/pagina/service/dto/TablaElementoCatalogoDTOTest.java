package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaElementoCatalogoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaElementoCatalogoDTO.class);
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO1 = new TablaElementoCatalogoDTO();
        tablaElementoCatalogoDTO1.setId(1L);
        TablaElementoCatalogoDTO tablaElementoCatalogoDTO2 = new TablaElementoCatalogoDTO();
        assertThat(tablaElementoCatalogoDTO1).isNotEqualTo(tablaElementoCatalogoDTO2);
        tablaElementoCatalogoDTO2.setId(tablaElementoCatalogoDTO1.getId());
        assertThat(tablaElementoCatalogoDTO1).isEqualTo(tablaElementoCatalogoDTO2);
        tablaElementoCatalogoDTO2.setId(2L);
        assertThat(tablaElementoCatalogoDTO1).isNotEqualTo(tablaElementoCatalogoDTO2);
        tablaElementoCatalogoDTO1.setId(null);
        assertThat(tablaElementoCatalogoDTO1).isNotEqualTo(tablaElementoCatalogoDTO2);
    }
}
