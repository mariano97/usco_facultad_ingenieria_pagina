package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Programa} and its DTO {@link ProgramaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgramaMapper extends EntityMapper<ProgramaDTO, Programa> {
    @Mapping(target = "nivelFormacion", source = "nivelFormacion", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "tipoFormacion", source = "tipoFormacion", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "facultad", source = "facultad", qualifiedByName = "facultadNombre")
    ProgramaDTO toDto(Programa s);

    @Named("tablaElementoCatalogoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaElementoCatalogoDTO toDtoTablaElementoCatalogoNombre(TablaElementoCatalogo tablaElementoCatalogo);

    @Named("facultadNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    FacultadDTO toDtoFacultadNombre(Facultad facultad);
}
