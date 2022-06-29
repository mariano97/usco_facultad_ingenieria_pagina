package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.CursoMateriaRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.TablaElementoCatalogoRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the CursoMateria entity.
 */
@SuppressWarnings("unused")
class CursoMateriaRepositoryInternalImpl extends SimpleR2dbcRepository<CursoMateria, Long> implements CursoMateriaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaElementoCatalogoRowMapper tablaelementocatalogoMapper;
    private final CursoMateriaRowMapper cursomateriaMapper;

    private static final Table entityTable = Table.aliased("curso_materia", EntityManager.ENTITY_ALIAS);
    private static final Table nivelAcademicoTable = Table.aliased("tabla_elemento_catalogo", "nivelAcademico");

    public CursoMateriaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaElementoCatalogoRowMapper tablaelementocatalogoMapper,
        CursoMateriaRowMapper cursomateriaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CursoMateria.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablaelementocatalogoMapper = tablaelementocatalogoMapper;
        this.cursomateriaMapper = cursomateriaMapper;
    }

    @Override
    public Flux<CursoMateria> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CursoMateria> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CursoMateriaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(nivelAcademicoTable, "nivelAcademico"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nivelAcademicoTable)
            .on(Column.create("nivel_academico_id", entityTable))
            .equals(Column.create("id", nivelAcademicoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CursoMateria.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<CursoMateria> createQueryRelationManyToManyProfesor(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CursoMateriaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(nivelAcademicoTable, "nivelAcademico"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nivelAcademicoTable)
            .on(Column.create("nivel_academico_id", entityTable))
            .equals(Column.create("id", nivelAcademicoTable))
            .leftOuterJoin(EntityRelationshipManager.profesorCursoMateriaRelationManyToMany)
            .on(Column.create("id", entityTable))
            .equals(Column.create("curso_materia_id", EntityRelationshipManager.profesorCursoMateriaRelationManyToMany));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CursoMateria.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CursoMateria> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CursoMateria> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<CursoMateria> findAllByProfesorRelation(Long programaId) {
        Comparison whereClause = Conditions.isEqual(EntityRelationshipManager.profesorCursoMateriaRelationManyToMany.column("profesor_id"), Conditions.just(programaId.toString()));
        return createQueryRelationManyToManyProfesor(null, whereClause).all();
    }

    @Override
    public Mono<CursoMateria> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<CursoMateria> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<CursoMateria> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private CursoMateria process(Row row, RowMetadata metadata) {
        CursoMateria entity = cursomateriaMapper.apply(row, "e");
        entity.setNivelAcademico(tablaelementocatalogoMapper.apply(row, "nivelAcademico"));
        return entity;
    }

    @Override
    public <S extends CursoMateria> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
