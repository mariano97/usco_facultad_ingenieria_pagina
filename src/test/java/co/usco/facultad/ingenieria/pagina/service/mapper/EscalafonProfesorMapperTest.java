package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EscalafonProfesorMapperTest {

    private EscalafonProfesorMapper escalafonProfesorMapper;

    @BeforeEach
    public void setUp() {
        escalafonProfesorMapper = new EscalafonProfesorMapperImpl();
    }
}
