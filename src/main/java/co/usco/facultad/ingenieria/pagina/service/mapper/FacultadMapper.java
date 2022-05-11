package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facultad} and its DTO {@link FacultadDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacultadMapper extends EntityMapper<FacultadDTO, Facultad> {}
