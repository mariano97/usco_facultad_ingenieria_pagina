package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedesProgramaMapperTest {

    private RedesProgramaMapper redesProgramaMapper;

    @BeforeEach
    public void setUp() {
        redesProgramaMapper = new RedesProgramaMapperImpl();
    }
}
