package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgramaMapperTest {

    private ProgramaMapper programaMapper;

    @BeforeEach
    public void setUp() {
        programaMapper = new ProgramaMapperImpl();
    }
}
