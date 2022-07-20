package co.usco.facultad.ingenieria.pagina.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.usco.facultad.ingenieria.pagina.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NoticiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Noticia.class);
        Noticia noticia1 = new Noticia();
        noticia1.setId(1L);
        Noticia noticia2 = new Noticia();
        noticia2.setId(noticia1.getId());
        assertThat(noticia1).isEqualTo(noticia2);
        noticia2.setId(2L);
        assertThat(noticia1).isNotEqualTo(noticia2);
        noticia1.setId(null);
        assertThat(noticia1).isNotEqualTo(noticia2);
    }
}
