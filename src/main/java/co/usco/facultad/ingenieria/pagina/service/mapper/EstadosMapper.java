package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Estados;
import co.usco.facultad.ingenieria.pagina.domain.Paises;
import co.usco.facultad.ingenieria.pagina.service.dto.EstadosDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.PaisesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Estados} and its DTO {@link EstadosDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstadosMapper extends EntityMapper<EstadosDTO, Estados> {
    @Mapping(target = "paises", source = "paises", qualifiedByName = "paisesNombrePais")
    EstadosDTO toDto(Estados s);

    @Named("paisesNombrePais")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombrePais", source = "nombrePais")
    PaisesDTO toDtoPaisesNombrePais(Paises paises);
}
