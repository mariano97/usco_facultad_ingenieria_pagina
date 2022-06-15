package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.CiudadRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.EstadosRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
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
 * Spring Data SQL reactive custom repository implementation for the Ciudad entity.
 */
@SuppressWarnings("unused")
class CiudadRepositoryInternalImpl extends SimpleR2dbcRepository<Ciudad, Long> implements CiudadRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EstadosRowMapper estadosMapper;
    private final CiudadRowMapper ciudadMapper;

    private static final Table entityTable = Table.aliased("ciudad", EntityManager.ENTITY_ALIAS);
    private static final Table estadosTable = Table.aliased("estados", "estados");

    public CiudadRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EstadosRowMapper estadosMapper,
        CiudadRowMapper ciudadMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Ciudad.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.estadosMapper = estadosMapper;
        this.ciudadMapper = ciudadMapper;
    }

    @Override
    public Flux<Ciudad> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Ciudad> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CiudadSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EstadosSqlHelper.getColumns(estadosTable, "estados"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(estadosTable)
            .on(Column.create("estados_id", entityTable))
            .equals(Column.create("id", estadosTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Ciudad.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Ciudad> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Ciudad> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Ciudad> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Ciudad> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Ciudad> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Ciudad process(Row row, RowMetadata metadata) {
        Ciudad entity = ciudadMapper.apply(row, "e");
        entity.setEstados(estadosMapper.apply(row, "estados"));
        return entity;
    }

    @Override
    public <S extends Ciudad> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
