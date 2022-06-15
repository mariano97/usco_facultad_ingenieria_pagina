package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.Estados;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.EstadosRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.PaisesRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Estados entity.
 */
@SuppressWarnings("unused")
class EstadosRepositoryInternalImpl extends SimpleR2dbcRepository<Estados, Long> implements EstadosRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaisesRowMapper paisesMapper;
    private final EstadosRowMapper estadosMapper;

    private static final Table entityTable = Table.aliased("estados", EntityManager.ENTITY_ALIAS);
    private static final Table paisesTable = Table.aliased("paises", "paises");

    public EstadosRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaisesRowMapper paisesMapper,
        EstadosRowMapper estadosMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Estados.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paisesMapper = paisesMapper;
        this.estadosMapper = estadosMapper;
    }

    @Override
    public Flux<Estados> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Estados> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EstadosSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaisesSqlHelper.getColumns(paisesTable, "paises"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(paisesTable)
            .on(Column.create("paises_id", entityTable))
            .equals(Column.create("id", paisesTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Estados.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Estados> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Estados> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Estados> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Estados> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Estados> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Estados process(Row row, RowMetadata metadata) {
        Estados entity = estadosMapper.apply(row, "e");
        entity.setPaises(paisesMapper.apply(row, "paises"));
        return entity;
    }

    @Override
    public <S extends Estados> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
