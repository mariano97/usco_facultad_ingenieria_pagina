package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.CursoMateriaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CursoMateria} and its DTO {@link CursoMateriaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CursoMateriaMapper extends EntityMapper<CursoMateriaDTO, CursoMateria> {
    @Mapping(target = "nivelAcademico", source = "nivelAcademico", qualifiedByName = "tablaElementoCatalogoNombre")
    CursoMateriaDTO toDto(CursoMateria s);

    @Named("tablaElementoCatalogoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaElementoCatalogoDTO toDtoTablaElementoCatalogoNombre(TablaElementoCatalogo tablaElementoCatalogo);
}
