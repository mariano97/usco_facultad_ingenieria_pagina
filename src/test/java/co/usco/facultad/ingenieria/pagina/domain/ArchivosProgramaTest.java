package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArchivosProgramaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchivosPrograma.class);
        ArchivosPrograma archivosPrograma1 = new ArchivosPrograma();
        archivosPrograma1.setId(1L);
        ArchivosPrograma archivosPrograma2 = new ArchivosPrograma();
        archivosPrograma2.setId(archivosPrograma1.getId());
        assertThat(archivosPrograma1).isEqualTo(archivosPrograma2);
        archivosPrograma2.setId(2L);
        assertThat(archivosPrograma1).isNotEqualTo(archivosPrograma2);
        archivosPrograma1.setId(null);
        assertThat(archivosPrograma1).isNotEqualTo(archivosPrograma2);
    }
}
