package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.TablaElementoCatalogoRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.TablaTiposCatalogoRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the TablaElementoCatalogo entity.
 */
@SuppressWarnings("unused")
class TablaElementoCatalogoRepositoryInternalImpl
    extends SimpleR2dbcRepository<TablaElementoCatalogo, Long>
    implements TablaElementoCatalogoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaTiposCatalogoRowMapper tablatiposcatalogoMapper;
    private final TablaElementoCatalogoRowMapper tablaelementocatalogoMapper;

    private static final Table entityTable = Table.aliased("tabla_elemento_catalogo", EntityManager.ENTITY_ALIAS);
    private static final Table tablaTiposCatalogoTable = Table.aliased("tabla_tipos_catalogo", "tablaTiposCatalogo");

    public TablaElementoCatalogoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaTiposCatalogoRowMapper tablatiposcatalogoMapper,
        TablaElementoCatalogoRowMapper tablaelementocatalogoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TablaElementoCatalogo.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablatiposcatalogoMapper = tablatiposcatalogoMapper;
        this.tablaelementocatalogoMapper = tablaelementocatalogoMapper;
    }

    @Override
    public Flux<TablaElementoCatalogo> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TablaElementoCatalogo> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TablaElementoCatalogoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaTiposCatalogoSqlHelper.getColumns(tablaTiposCatalogoTable, "tablaTiposCatalogo"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tablaTiposCatalogoTable)
            .on(Column.create("tabla_tipos_catalogo_id", entityTable))
            .equals(Column.create("id", tablaTiposCatalogoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TablaElementoCatalogo.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TablaElementoCatalogo> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TablaElementoCatalogo> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<TablaElementoCatalogo> findAllByAbreviatura(String abreviatura) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("abreviatura"), Conditions.just("'".concat(abreviatura).concat("'")));
        return createQuery(null, whereClause).all();
    }

    @Override
    public Mono<TablaElementoCatalogo> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<TablaElementoCatalogo> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<TablaElementoCatalogo> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private TablaElementoCatalogo process(Row row, RowMetadata metadata) {
        TablaElementoCatalogo entity = tablaelementocatalogoMapper.apply(row, "e");
        entity.setTablaTiposCatalogo(tablatiposcatalogoMapper.apply(row, "tablaTiposCatalogo"));
        return entity;
    }

    @Override
    public <S extends TablaElementoCatalogo> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
