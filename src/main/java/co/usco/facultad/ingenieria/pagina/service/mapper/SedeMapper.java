package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import co.usco.facultad.ingenieria.pagina.domain.Sede;
import co.usco.facultad.ingenieria.pagina.service.dto.CiudadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.SedeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sede} and its DTO {@link SedeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SedeMapper extends EntityMapper<SedeDTO, Sede> {
    @Mapping(target = "ciudad", source = "ciudad", qualifiedByName = "ciudadNombre")
    SedeDTO toDto(Sede s);

    @Named("ciudadNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CiudadDTO toDtoCiudadNombre(Ciudad ciudad);
}
