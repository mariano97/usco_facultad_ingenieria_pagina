package co.usco.facultad.ingenieria.pagina.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacultadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacultadDTO.class);
        FacultadDTO facultadDTO1 = new FacultadDTO();
        facultadDTO1.setId(1L);
        FacultadDTO facultadDTO2 = new FacultadDTO();
        assertThat(facultadDTO1).isNotEqualTo(facultadDTO2);
        facultadDTO2.setId(facultadDTO1.getId());
        assertThat(facultadDTO1).isEqualTo(facultadDTO2);
        facultadDTO2.setId(2L);
        assertThat(facultadDTO1).isNotEqualTo(facultadDTO2);
        facultadDTO1.setId(null);
        assertThat(facultadDTO1).isNotEqualTo(facultadDTO2);
    }
}
