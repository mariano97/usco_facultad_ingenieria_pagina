package co.usco.facultad.ingenieria.pagina.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CursoMateriaMapperTest {

    private CursoMateriaMapper cursoMateriaMapper;

    @BeforeEach
    public void setUp() {
        cursoMateriaMapper = new CursoMateriaMapperImpl();
    }
}
