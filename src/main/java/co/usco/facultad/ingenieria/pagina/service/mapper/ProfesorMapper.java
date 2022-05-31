package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profesor} and its DTO {@link ProfesorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfesorMapper extends EntityMapper<ProfesorDTO, Profesor> {}
