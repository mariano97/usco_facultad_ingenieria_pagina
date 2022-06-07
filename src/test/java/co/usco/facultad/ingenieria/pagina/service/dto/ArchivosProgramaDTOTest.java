package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArchivosProgramaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchivosProgramaDTO.class);
        ArchivosProgramaDTO archivosProgramaDTO1 = new ArchivosProgramaDTO();
        archivosProgramaDTO1.setId(1L);
        ArchivosProgramaDTO archivosProgramaDTO2 = new ArchivosProgramaDTO();
        assertThat(archivosProgramaDTO1).isNotEqualTo(archivosProgramaDTO2);
        archivosProgramaDTO2.setId(archivosProgramaDTO1.getId());
        assertThat(archivosProgramaDTO1).isEqualTo(archivosProgramaDTO2);
        archivosProgramaDTO2.setId(2L);
        assertThat(archivosProgramaDTO1).isNotEqualTo(archivosProgramaDTO2);
        archivosProgramaDTO1.setId(null);
        assertThat(archivosProgramaDTO1).isNotEqualTo(archivosProgramaDTO2);
    }
}
