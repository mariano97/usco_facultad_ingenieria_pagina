package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArchivosPrograma} and its DTO {@link ArchivosProgramaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArchivosProgramaMapper extends EntityMapper<ArchivosProgramaDTO, ArchivosPrograma> {
    @Mapping(target = "programa", source = "programa", qualifiedByName = "programaNombre")
    ArchivosProgramaDTO toDto(ArchivosPrograma s);

    @Named("programaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProgramaDTO toDtoProgramaNombre(Programa programa);
}
