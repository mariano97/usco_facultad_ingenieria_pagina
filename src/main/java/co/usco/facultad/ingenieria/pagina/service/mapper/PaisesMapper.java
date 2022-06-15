package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Paises;
import co.usco.facultad.ingenieria.pagina.service.dto.PaisesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paises} and its DTO {@link PaisesDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaisesMapper extends EntityMapper<PaisesDTO, Paises> {}
