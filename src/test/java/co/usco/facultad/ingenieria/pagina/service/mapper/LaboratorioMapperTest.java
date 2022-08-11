package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LaboratorioMapperTest {

    private LaboratorioMapper laboratorioMapper;

    @BeforeEach
    public void setUp() {
        laboratorioMapper = new LaboratorioMapperImpl();
    }
}
