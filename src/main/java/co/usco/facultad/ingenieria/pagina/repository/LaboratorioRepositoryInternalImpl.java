package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.Laboratorio;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.FacultadRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.LaboratorioRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Laboratorio entity.
 */
@SuppressWarnings("unused")
class LaboratorioRepositoryInternalImpl extends SimpleR2dbcRepository<Laboratorio, Long> implements LaboratorioRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaElementoCatalogoRowMapper tablaelementocatalogoMapper;
    private final FacultadRowMapper facultadMapper;
    private final LaboratorioRowMapper laboratorioMapper;

    private static final Table entityTable = Table.aliased("laboratorio", EntityManager.ENTITY_ALIAS);
    private static final Table tipoLaboratorioTable = Table.aliased("tabla_elemento_catalogo", "tipoLaboratorio");
    private static final Table facultadTable = Table.aliased("facultad", "facultad");

    public LaboratorioRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaElementoCatalogoRowMapper tablaelementocatalogoMapper,
        FacultadRowMapper facultadMapper,
        LaboratorioRowMapper laboratorioMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Laboratorio.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablaelementocatalogoMapper = tablaelementocatalogoMapper;
        this.facultadMapper = facultadMapper;
        this.laboratorioMapper = laboratorioMapper;
    }

    @Override
    public Flux<Laboratorio> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Laboratorio> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LaboratorioSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(tipoLaboratorioTable, "tipoLaboratorio"));
        columns.addAll(FacultadSqlHelper.getColumns(facultadTable, "facultad"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tipoLaboratorioTable)
            .on(Column.create("tipo_laboratorio_id", entityTable))
            .equals(Column.create("id", tipoLaboratorioTable))
            .leftOuterJoin(facultadTable)
            .on(Column.create("facultad_id", entityTable))
            .equals(Column.create("id", facultadTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Laboratorio.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Laboratorio> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Laboratorio> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<Laboratorio> findAllByTipoTipoLaboratorioId(Pageable pageable, Long tipoCatalogoId) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("tipo_laboratorio_id"), Conditions.just(tipoCatalogoId.toString()));
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Flux<Laboratorio> findAllByTipoTipoLaboratorioId(Long tipoCatalogoId) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("tipo_laboratorio_id"), Conditions.just(tipoCatalogoId.toString()));
        return createQuery(null, whereClause).all();
    }

    @Override
    public Mono<Laboratorio> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Laboratorio> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Laboratorio> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Laboratorio process(Row row, RowMetadata metadata) {
        Laboratorio entity = laboratorioMapper.apply(row, "e");
        entity.setTipoLaboratorio(tablaelementocatalogoMapper.apply(row, "tipoLaboratorio"));
        entity.setFacultad(facultadMapper.apply(row, "facultad"));
        return entity;
    }

    @Override
    public <S extends Laboratorio> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
