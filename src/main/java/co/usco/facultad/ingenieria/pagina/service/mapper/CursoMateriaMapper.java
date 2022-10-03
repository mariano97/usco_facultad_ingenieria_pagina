package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.service.dto.CursoMateriaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProgramaDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CursoMateria} and its DTO {@link CursoMateriaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CursoMateriaMapper extends EntityMapper<CursoMateriaDTO, CursoMateria> {
    @Mapping(target = "nivelAcademico", source = "nivelAcademico", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "programas", source = "programas", qualifiedByName = "programaNombreSet")
    CursoMateriaDTO toDto(CursoMateria s);

    @Mapping(target = "removePrograma", ignore = true)
    CursoMateria toEntity(CursoMateriaDTO cursoMateriaDTO);

    @Named("tablaElementoCatalogoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaElementoCatalogoDTO toDtoTablaElementoCatalogoNombre(TablaElementoCatalogo tablaElementoCatalogo);

    @Named("programaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProgramaDTO toDtoProgramaNombre(Programa programa);

    @Named("programaNombreSet")
    default Set<ProgramaDTO> toDtoProgramaNombreSet(Set<Programa> programa) {
        return programa.stream().map(this::toDtoProgramaNombre).collect(Collectors.toSet());
    }
}
