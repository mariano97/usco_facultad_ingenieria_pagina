package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Sede;
import co.usco.facultad.ingenieria.pagina.service.dto.SedeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sede} and its DTO {@link SedeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SedeMapper extends EntityMapper<SedeDTO, Sede> {}
