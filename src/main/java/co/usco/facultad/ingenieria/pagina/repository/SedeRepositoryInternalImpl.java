package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.Sede;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.CiudadRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.SedeRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Sede entity.
 */
@SuppressWarnings("unused")
class SedeRepositoryInternalImpl extends SimpleR2dbcRepository<Sede, Long> implements SedeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CiudadRowMapper ciudadMapper;
    private final SedeRowMapper sedeMapper;

    private static final Table entityTable = Table.aliased("sede", EntityManager.ENTITY_ALIAS);
    private static final Table ciudadTable = Table.aliased("ciudad", "ciudad");

    public SedeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CiudadRowMapper ciudadMapper,
        SedeRowMapper sedeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Sede.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.ciudadMapper = ciudadMapper;
        this.sedeMapper = sedeMapper;
    }

    @Override
    public Flux<Sede> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Sede> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SedeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CiudadSqlHelper.getColumns(ciudadTable, "ciudad"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(ciudadTable)
            .on(Column.create("ciudad_id", entityTable))
            .equals(Column.create("id", ciudadTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Sede.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Sede> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Sede> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Sede> fundByCodigoIndicativo(String codigoIdicativo) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("codigo_indicativo"), Conditions.just("'".concat(codigoIdicativo.toString()).concat("'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Sede> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Sede> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Sede> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Sede process(Row row, RowMetadata metadata) {
        Sede entity = sedeMapper.apply(row, "e");
        entity.setCiudad(ciudadMapper.apply(row, "ciudad"));
        return entity;
    }

    @Override
    public <S extends Sede> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
