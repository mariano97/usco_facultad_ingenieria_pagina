package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Laboratorio;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.LaboratorioDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Laboratorio} and its DTO {@link LaboratorioDTO}.
 */
@Mapper(componentModel = "spring")
public interface LaboratorioMapper extends EntityMapper<LaboratorioDTO, Laboratorio> {
    @Mapping(target = "tipoLaboratorio", source = "tipoLaboratorio", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "facultad", source = "facultad", qualifiedByName = "facultadNombre")
    LaboratorioDTO toDto(Laboratorio s);

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
