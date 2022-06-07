package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.ArchivosProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArchivosPrograma} and its DTO {@link ArchivosProgramaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArchivosProgramaMapper extends EntityMapper<ArchivosProgramaDTO, ArchivosPrograma> {
    @Mapping(target = "programa", source = "programa", qualifiedByName = "programaNombre")
    @Mapping(target = "tablaElementoCatalogo", source = "tablaElementoCatalogo", qualifiedByName = "tablaElementoCatalogoNombre")
    ArchivosProgramaDTO toDto(ArchivosPrograma s);

    @Named("programaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProgramaDTO toDtoProgramaNombre(Programa programa);

    @Named("tablaElementoCatalogoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaElementoCatalogoDTO toDtoTablaElementoCatalogoNombre(TablaElementoCatalogo tablaElementoCatalogo);
}
