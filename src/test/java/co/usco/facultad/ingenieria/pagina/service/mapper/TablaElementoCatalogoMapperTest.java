package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TablaElementoCatalogoMapperTest {

    private TablaElementoCatalogoMapper tablaElementoCatalogoMapper;

    @BeforeEach
    public void setUp() {
        tablaElementoCatalogoMapper = new TablaElementoCatalogoMapperImpl();
    }
}
