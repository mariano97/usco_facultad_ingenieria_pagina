package co.usco.facultad.ingenieria.pagina.repository;

import org.springframework.data.relational.core.sql.Table;

public class EntityRelationshipManager {

    public static final Table programaSedeRelationManyToMany = Table.aliased("rel_programa__sede", "relProgramaSede");
    public static final Table profesorCursoMateriaRelationManyToMany = Table.aliased("rel_profesor__curso_materia", "relProfesorCursoMateria");
    public static final Table profesorProgramaRelationManyToMany = Table.aliased("rel_programa__profesor", "relProgramaProfesor");


    public static final Table programTable = Table.aliased("programa", "p");

}
