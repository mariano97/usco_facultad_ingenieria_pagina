package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaTiposCatalogoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaTiposCatalogoDTO.class);
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO1 = new TablaTiposCatalogoDTO();
        tablaTiposCatalogoDTO1.setId(1L);
        TablaTiposCatalogoDTO tablaTiposCatalogoDTO2 = new TablaTiposCatalogoDTO();
        assertThat(tablaTiposCatalogoDTO1).isNotEqualTo(tablaTiposCatalogoDTO2);
        tablaTiposCatalogoDTO2.setId(tablaTiposCatalogoDTO1.getId());
        assertThat(tablaTiposCatalogoDTO1).isEqualTo(tablaTiposCatalogoDTO2);
        tablaTiposCatalogoDTO2.setId(2L);
        assertThat(tablaTiposCatalogoDTO1).isNotEqualTo(tablaTiposCatalogoDTO2);
        tablaTiposCatalogoDTO1.setId(null);
        assertThat(tablaTiposCatalogoDTO1).isNotEqualTo(tablaTiposCatalogoDTO2);
    }
}
