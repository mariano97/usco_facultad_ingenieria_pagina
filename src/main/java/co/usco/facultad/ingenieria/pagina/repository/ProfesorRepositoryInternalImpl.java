package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.FacultadRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.ProfesorRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Profesor entity.
 */
@SuppressWarnings("unused")
class ProfesorRepositoryInternalImpl extends SimpleR2dbcRepository<Profesor, Long> implements ProfesorRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaElementoCatalogoRowMapper tablaelementocatalogoMapper;
    private final FacultadRowMapper facultadMapper;
    private final ProfesorRowMapper profesorMapper;

    private static final Table entityTable = Table.aliased("profesor", EntityManager.ENTITY_ALIAS);
    private static final Table tablaElementoCatalogoTable = Table.aliased("tabla_elemento_catalogo", "tablaElementoCatalogo");
    private static final Table facultadTable = Table.aliased("facultad", "facultad");

    private static final EntityManager.LinkTable cursoMateriaLink = new EntityManager.LinkTable(
        "rel_profesor__curso_materia",
        "profesor_id",
        "curso_materia_id"
    );

    public ProfesorRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaElementoCatalogoRowMapper tablaelementocatalogoMapper,
        FacultadRowMapper facultadMapper,
        ProfesorRowMapper profesorMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Profesor.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablaelementocatalogoMapper = tablaelementocatalogoMapper;
        this.facultadMapper = facultadMapper;
        this.profesorMapper = profesorMapper;
    }

    @Override
    public Flux<Profesor> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Profesor> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProfesorSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(tablaElementoCatalogoTable, "tablaElementoCatalogo"));
        columns.addAll(FacultadSqlHelper.getColumns(facultadTable, "facultad"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tablaElementoCatalogoTable)
            .on(Column.create("tabla_elemento_catalogo_id", entityTable))
            .equals(Column.create("id", tablaElementoCatalogoTable))
            .leftOuterJoin(facultadTable)
            .on(Column.create("facultad_id", entityTable))
            .equals(Column.create("id", facultadTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Profesor.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<Profesor> createQueryByPrograma(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProfesorSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(tablaElementoCatalogoTable, "tablaElementoCatalogo"));
        columns.addAll(FacultadSqlHelper.getColumns(facultadTable, "facultad"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tablaElementoCatalogoTable)
            .on(Column.create("tabla_elemento_catalogo_id", entityTable))
            .equals(Column.create("id", tablaElementoCatalogoTable))
            .leftOuterJoin(facultadTable)
            .on(Column.create("facultad_id", entityTable))
            .equals(Column.create("id", facultadTable))
            .leftOuterJoin(EntityRelationshipManager.profesorProgramaRelationManyToMany)
            .on(Column.create("id", entityTable))
            .equals(Column.create("profesor_id", EntityRelationshipManager.profesorProgramaRelationManyToMany))
            .leftOuterJoin(EntityRelationshipManager.programTable)
            .on(Column.create("programa_id", EntityRelationshipManager.profesorProgramaRelationManyToMany))
            .equals(Column.create("id", EntityRelationshipManager.programTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Profesor.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Profesor> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Profesor> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Long> countWithProgramaCodigoSnies(Long codigoSnies) {
        String condition = "";
        if (codigoSnies != null && codigoSnies > 0) {
            condition = condition.concat(" where prog.codigo_snies = ").concat(codigoSnies.toString());
        }
        return db.sql("select count(*) from (select prf.id from profesor prf " +
            "left join rel_programa__profesor prog_prf " +
            "on prf.id = prog_prf.profesor_id " +
            "left join programa prog " +
            "on prog_prf.programa_id = prog.id " +
            condition + ") src")
            .map((row, rowMetadata) -> (long) row.get(0))
            .one();
    }

    @Override
    public Flux<Profesor> findAllByProgramaCodigoSnies(Pageable pageable, Long codigoSnies) {
        Comparison whereClause = Conditions.isEqual(EntityRelationshipManager.programTable
            .column("codigo_snies"), Conditions.just(codigoSnies != null ? codigoSnies.toString() : "0"));
        return createQueryByPrograma(pageable, whereClause).all();
    }

    @Override
    public Mono<Profesor> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Profesor> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Profesor> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    @Override
    public Mono<Profesor> findOneByUserId(Long userId) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("user_id"), Conditions.just(userId.toString()));
        return createQuery(null, whereClause).one();
    }

    private Profesor process(Row row, RowMetadata metadata) {
        Profesor entity = profesorMapper.apply(row, "e");
        entity.setTablaElementoCatalogo(tablaelementocatalogoMapper.apply(row, "tablaElementoCatalogo"));
        entity.setFacultad(facultadMapper.apply(row, "facultad"));
        return entity;
    }

    @Override
    public <S extends Profesor> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Profesor> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(cursoMateriaLink, entity.getId(), entity.getCursoMaterias().stream().map(CursoMateria::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(cursoMateriaLink, entityId);
    }
}
