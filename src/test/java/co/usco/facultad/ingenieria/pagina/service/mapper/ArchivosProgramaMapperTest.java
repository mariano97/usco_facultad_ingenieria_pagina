package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArchivosProgramaMapperTest {

    private ArchivosProgramaMapper archivosProgramaMapper;

    @BeforeEach
    public void setUp() {
        archivosProgramaMapper = new ArchivosProgramaMapperImpl();
    }
}
