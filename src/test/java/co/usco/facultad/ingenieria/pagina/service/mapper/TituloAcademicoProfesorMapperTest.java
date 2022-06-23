package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TituloAcademicoProfesorMapperTest {

    private TituloAcademicoProfesorMapper tituloAcademicoProfesorMapper;

    @BeforeEach
    public void setUp() {
        tituloAcademicoProfesorMapper = new TituloAcademicoProfesorMapperImpl();
    }
}
