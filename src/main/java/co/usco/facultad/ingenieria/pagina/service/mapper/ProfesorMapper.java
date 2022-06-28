package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.CursoMateriaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.FacultadDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profesor} and its DTO {@link ProfesorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfesorMapper extends EntityMapper<ProfesorDTO, Profesor> {
    @Mapping(target = "tablaElementoCatalogo", source = "tablaElementoCatalogo", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "facultad", source = "facultad", qualifiedByName = "facultadNombre")
    @Mapping(target = "cursoMaterias", source = "cursoMaterias", qualifiedByName = "cursoMateriaIdSet")
    ProfesorDTO toDto(Profesor s);

    @Mapping(target = "removeCursoMateria", ignore = true)
    Profesor toEntity(ProfesorDTO profesorDTO);

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

    @Named("cursoMateriaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CursoMateriaDTO toDtoCursoMateriaId(CursoMateria cursoMateria);

    @Named("cursoMateriaIdSet")
    default Set<CursoMateriaDTO> toDtoCursoMateriaIdSet(Set<CursoMateria> cursoMateria) {
        return cursoMateria.stream().map(this::toDtoCursoMateriaId).collect(Collectors.toSet());
    }
}
