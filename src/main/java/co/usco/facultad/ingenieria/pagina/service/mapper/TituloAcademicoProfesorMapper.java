package co.usco.facultad.ingenieria.pagina.service.mapper;

import co.usco.facultad.ingenieria.pagina.domain.Paises;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor;
import co.usco.facultad.ingenieria.pagina.service.dto.PaisesDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.ProfesorDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TablaElementoCatalogoDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.TituloAcademicoProfesorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TituloAcademicoProfesor} and its DTO {@link TituloAcademicoProfesorDTO}.
 */
@Mapper(componentModel = "spring")
public interface TituloAcademicoProfesorMapper extends EntityMapper<TituloAcademicoProfesorDTO, TituloAcademicoProfesor> {
    @Mapping(target = "tablaElementoCatalogo", source = "tablaElementoCatalogo", qualifiedByName = "tablaElementoCatalogoNombre")
    @Mapping(target = "profesor", source = "profesor", qualifiedByName = "profesorId")
    @Mapping(target = "paises", source = "paises", qualifiedByName = "paisesNombrePais")
    TituloAcademicoProfesorDTO toDto(TituloAcademicoProfesor s);

    @Named("tablaElementoCatalogoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaElementoCatalogoDTO toDtoTablaElementoCatalogoNombre(TablaElementoCatalogo tablaElementoCatalogo);

    @Named("profesorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfesorDTO toDtoProfesorId(Profesor profesor);

    @Named("paisesNombrePais")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombrePais", source = "nombrePais")
    PaisesDTO toDtoPaisesNombrePais(Paises paises);
}
