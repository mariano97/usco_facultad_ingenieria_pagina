package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaTiposCatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TablaTiposCatalogo} and its DTO {@link TablaTiposCatalogoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TablaTiposCatalogoMapper extends EntityMapper<TablaTiposCatalogoDTO, TablaTiposCatalogo> {}
