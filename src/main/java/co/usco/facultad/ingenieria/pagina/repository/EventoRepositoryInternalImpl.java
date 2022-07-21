package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.Evento;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.EventoRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.FacultadRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
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
 * Spring Data SQL reactive custom repository implementation for the Evento entity.
 */
@SuppressWarnings("unused")
class EventoRepositoryInternalImpl extends SimpleR2dbcRepository<Evento, Long> implements EventoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FacultadRowMapper facultadMapper;
    private final EventoRowMapper eventoMapper;

    private static final Table entityTable = Table.aliased("evento", EntityManager.ENTITY_ALIAS);
    private static final Table facultadTable = Table.aliased("facultad", "facultad");

    public EventoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FacultadRowMapper facultadMapper,
        EventoRowMapper eventoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Evento.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.facultadMapper = facultadMapper;
        this.eventoMapper = eventoMapper;
    }

    @Override
    public Flux<Evento> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Evento> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EventoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FacultadSqlHelper.getColumns(facultadTable, "facultad"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(facultadTable)
            .on(Column.create("facultad_id", entityTable))
            .equals(Column.create("id", facultadTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Evento.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Evento> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Evento> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Evento> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Evento> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Evento> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    @Override
    public Flux<Evento> findAllFechaMayorQue(Pageable pageable, Instant fechaInicial) {
        Comparison whereClause = Conditions.isGreaterOrEqualTo(entityTable.column("fecha"), Conditions.just("'".concat(fechaInicial.toString()).concat("'")));
        return createQuery(pageable, whereClause).all();
    }

    private Evento process(Row row, RowMetadata metadata) {
        Evento entity = eventoMapper.apply(row, "e");
        entity.setFacultad(facultadMapper.apply(row, "facultad"));
        return entity;
    }

    @Override
    public <S extends Evento> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
