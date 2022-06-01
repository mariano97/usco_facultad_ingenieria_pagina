package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.Sede;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.SedeDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Programa} and its DTO {@link ProgramaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgramaMapper extends EntityMapper<ProgramaDTO, Programa> {
    @Mapping(target = "nivelFormacion", source = "nivelFormacion", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "tipoFormacion", source = "tipoFormacion", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "facultad", source = "facultad", qualifiedByName = "facultadNombre")
    @Mapping(target = "sedes", source = "sedes", qualifiedByName = "sedeNombreSet")
    @Mapping(target = "profesors", source = "profesors", qualifiedByName = "profesorIdSet")
    ProgramaDTO toDto(Programa s);

    @Mapping(target = "removeSede", ignore = true)
    @Mapping(target = "removeProfesor", ignore = true)
    Programa toEntity(ProgramaDTO programaDTO);

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

    @Named("sedeNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    SedeDTO toDtoSedeNombre(Sede sede);

    @Named("sedeNombreSet")
    default Set<SedeDTO> toDtoSedeNombreSet(Set<Sede> sede) {
        return sede.stream().map(this::toDtoSedeNombre).collect(Collectors.toSet());
    }

    @Named("profesorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfesorDTO toDtoProfesorId(Profesor profesor);

    @Named("profesorIdSet")
    default Set<ProfesorDTO> toDtoProfesorIdSet(Set<Profesor> profesor) {
        return profesor.stream().map(this::toDtoProfesorId).collect(Collectors.toSet());
    }
}
