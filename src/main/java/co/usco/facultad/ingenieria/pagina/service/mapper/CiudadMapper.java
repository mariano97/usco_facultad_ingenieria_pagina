package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import co.usco.facultad.ingenieria.pagina.domain.Estados;
import co.usco.facultad.ingenieria.pagina.service.dto.CiudadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.EstadosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ciudad} and its DTO {@link CiudadDTO}.
 */
@Mapper(componentModel = "spring")
public interface CiudadMapper extends EntityMapper<CiudadDTO, Ciudad> {
    @Mapping(target = "estados", source = "estados", qualifiedByName = "estadosNombre")
    CiudadDTO toDto(Ciudad s);

    @Named("estadosNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EstadosDTO toDtoEstadosNombre(Estados estados);
}
