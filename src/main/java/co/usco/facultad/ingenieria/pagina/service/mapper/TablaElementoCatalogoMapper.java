package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaTiposCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TablaElementoCatalogo} and its DTO {@link TablaElementoCatalogoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TablaElementoCatalogoMapper extends EntityMapper<TablaElementoCatalogoDTO, TablaElementoCatalogo> {
    @Mapping(target = "tablaTiposCatalogo", source = "tablaTiposCatalogo", qualifiedByName = "tablaTiposCatalogoNombre")
    TablaElementoCatalogoDTO toDto(TablaElementoCatalogo s);

    @Named("tablaTiposCatalogoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaTiposCatalogoDTO toDtoTablaTiposCatalogoNombre(TablaTiposCatalogo tablaTiposCatalogo);
}
