package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Noticia;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.NoticiaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Noticia} and its DTO {@link NoticiaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoticiaMapper extends EntityMapper<NoticiaDTO, Noticia> {
    @Mapping(target = "facultad", source = "facultad", qualifiedByName = "facultadNombre")
    NoticiaDTO toDto(Noticia s);

    @Named("facultadNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    FacultadDTO toDtoFacultadNombre(Facultad facultad);
}
