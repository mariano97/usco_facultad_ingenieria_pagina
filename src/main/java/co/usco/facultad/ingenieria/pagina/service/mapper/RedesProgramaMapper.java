package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.RedesPrograma;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.RedesProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RedesPrograma} and its DTO {@link RedesProgramaDTO}.
 */
@Mapper(componentModel = "spring")
public interface RedesProgramaMapper extends EntityMapper<RedesProgramaDTO, RedesPrograma> {
    @Mapping(target = "programa", source = "programa", qualifiedByName = "programaNombre")
    @Mapping(target = "tablaElementoCatalogo", source = "tablaElementoCatalogo", qualifiedByName = "tablaElementoCatalogoNombre")
    RedesProgramaDTO toDto(RedesPrograma s);

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
