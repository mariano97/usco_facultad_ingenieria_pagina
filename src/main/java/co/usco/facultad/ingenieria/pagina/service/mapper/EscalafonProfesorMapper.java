package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.service.dto.EscalafonProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EscalafonProfesor} and its DTO {@link EscalafonProfesorDTO}.
 */
@Mapper(componentModel = "spring")
public interface EscalafonProfesorMapper extends EntityMapper<EscalafonProfesorDTO, EscalafonProfesor> {
    @Mapping(target = "profesor", source = "profesor", qualifiedByName = "profesorId")
    EscalafonProfesorDTO toDto(EscalafonProfesor s);

    @Named("profesorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfesorDTO toDtoProfesorId(Profesor profesor);
}
