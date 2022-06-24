package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CursoMateriaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CursoMateriaDTO.class);
        CursoMateriaDTO cursoMateriaDTO1 = new CursoMateriaDTO();
        cursoMateriaDTO1.setId(1L);
        CursoMateriaDTO cursoMateriaDTO2 = new CursoMateriaDTO();
        assertThat(cursoMateriaDTO1).isNotEqualTo(cursoMateriaDTO2);
        cursoMateriaDTO2.setId(cursoMateriaDTO1.getId());
        assertThat(cursoMateriaDTO1).isEqualTo(cursoMateriaDTO2);
        cursoMateriaDTO2.setId(2L);
        assertThat(cursoMateriaDTO1).isNotEqualTo(cursoMateriaDTO2);
        cursoMateriaDTO1.setId(null);
        assertThat(cursoMateriaDTO1).isNotEqualTo(cursoMateriaDTO2);
    }
}
