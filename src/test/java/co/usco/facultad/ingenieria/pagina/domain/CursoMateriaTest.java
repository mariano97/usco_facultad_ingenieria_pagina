package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CursoMateriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CursoMateria.class);
        CursoMateria cursoMateria1 = new CursoMateria();
        cursoMateria1.setId(1L);
        CursoMateria cursoMateria2 = new CursoMateria();
        cursoMateria2.setId(cursoMateria1.getId());
        assertThat(cursoMateria1).isEqualTo(cursoMateria2);
        cursoMateria2.setId(2L);
        assertThat(cursoMateria1).isNotEqualTo(cursoMateria2);
        cursoMateria1.setId(null);
        assertThat(cursoMateria1).isNotEqualTo(cursoMateria2);
    }
}
