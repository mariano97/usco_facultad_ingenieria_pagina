package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.Semillero;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.FacultadRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.ProfesorRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.SemilleroRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Semillero entity.
 */
@SuppressWarnings("unused")
class SemilleroRepositoryInternalImpl extends SimpleR2dbcRepository<Semillero, Long> implements SemilleroRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FacultadRowMapper facultadMapper;
    private final ProfesorRowMapper profesorMapper;
    private final SemilleroRowMapper semilleroMapper;

    private static final Table entityTable = Table.aliased("semillero", EntityManager.ENTITY_ALIAS);
    private static final Table facultadTable = Table.aliased("facultad", "facultad");
    private static final Table profesorTable = Table.aliased("profesor", "profesor");

    public SemilleroRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FacultadRowMapper facultadMapper,
        ProfesorRowMapper profesorMapper,
        SemilleroRowMapper semilleroMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Semillero.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.facultadMapper = facultadMapper;
        this.profesorMapper = profesorMapper;
        this.semilleroMapper = semilleroMapper;
    }

    @Override
    public Flux<Semillero> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Semillero> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SemilleroSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FacultadSqlHelper.getColumns(facultadTable, "facultad"));
        columns.addAll(ProfesorSqlHelper.getColumns(profesorTable, "profesor"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(facultadTable)
            .on(Column.create("facultad_id", entityTable))
            .equals(Column.create("id", facultadTable))
            .leftOuterJoin(profesorTable)
            .on(Column.create("profesor_id", entityTable))
            .equals(Column.create("id", profesorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Semillero.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Semillero> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Semillero> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Semillero> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Semillero> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Semillero> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Semillero process(Row row, RowMetadata metadata) {
        Semillero entity = semilleroMapper.apply(row, "e");
        entity.setFacultad(facultadMapper.apply(row, "facultad"));
        entity.setProfesor(profesorMapper.apply(row, "profesor"));
        return entity;
    }

    @Override
    public <S extends Semillero> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
