package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.domain.Semillero;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.SemilleroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Semillero} and its DTO {@link SemilleroDTO}.
 */
@Mapper(componentModel = "spring")
public interface SemilleroMapper extends EntityMapper<SemilleroDTO, Semillero> {
    @Mapping(target = "facultad", source = "facultad", qualifiedByName = "facultadNombre")
    @Mapping(target = "profesor", source = "profesor", qualifiedByName = "profesorId")
    SemilleroDTO toDto(Semillero s);

    @Named("facultadNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    FacultadDTO toDtoFacultadNombre(Facultad facultad);

    @Named("profesorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfesorDTO toDtoProfesorId(Profesor profesor);
}
