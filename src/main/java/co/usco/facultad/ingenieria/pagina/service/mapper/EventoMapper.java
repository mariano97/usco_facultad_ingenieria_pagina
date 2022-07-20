package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Evento;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.service.dto.EventoDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evento} and its DTO {@link EventoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventoMapper extends EntityMapper<EventoDTO, Evento> {
    @Mapping(target = "facultad", source = "facultad", qualifiedByName = "facultadNombre")
    EventoDTO toDto(Evento s);

    @Named("facultadNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    FacultadDTO toDtoFacultadNombre(Facultad facultad);
}
