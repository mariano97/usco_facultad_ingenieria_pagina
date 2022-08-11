package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Laboratorio;
import co.usco.facultad.ingenieria.pagina.service.dto.LaboratorioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Laboratorio} and its DTO {@link LaboratorioDTO}.
 */
@Mapper(componentModel = "spring")
public interface LaboratorioMapper extends EntityMapper<LaboratorioDTO, Laboratorio> {}
