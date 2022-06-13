package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.RedesPrograma;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.ProgramaRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.RedesProgramaRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the RedesPrograma entity.
 */
@SuppressWarnings("unused")
class RedesProgramaRepositoryInternalImpl extends SimpleR2dbcRepository<RedesPrograma, Long> implements RedesProgramaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProgramaRowMapper programaMapper;
    private final TablaElementoCatalogoRowMapper tablaelementocatalogoMapper;
    private final RedesProgramaRowMapper redesprogramaMapper;

    private static final Table entityTable = Table.aliased("redes_programa", EntityManager.ENTITY_ALIAS);
    private static final Table programaTable = Table.aliased("programa", "programa");
    private static final Table tablaElementoCatalogoTable = Table.aliased("tabla_elemento_catalogo", "tablaElementoCatalogo");

    public RedesProgramaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProgramaRowMapper programaMapper,
        TablaElementoCatalogoRowMapper tablaelementocatalogoMapper,
        RedesProgramaRowMapper redesprogramaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RedesPrograma.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.programaMapper = programaMapper;
        this.tablaelementocatalogoMapper = tablaelementocatalogoMapper;
        this.redesprogramaMapper = redesprogramaMapper;
    }

    @Override
    public Flux<RedesPrograma> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RedesPrograma> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RedesProgramaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProgramaSqlHelper.getColumns(programaTable, "programa"));
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(tablaElementoCatalogoTable, "tablaElementoCatalogo"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(programaTable)
            .on(Column.create("programa_id", entityTable))
            .equals(Column.create("id", programaTable))
            .leftOuterJoin(tablaElementoCatalogoTable)
            .on(Column.create("tabla_elemento_catalogo_id", entityTable))
            .equals(Column.create("id", tablaElementoCatalogoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RedesPrograma.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<RedesPrograma> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RedesPrograma> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<RedesPrograma> findAllByProgramaId(Long programaId) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("programa_id"), Conditions.just(programaId.toString()));
        return createQuery(null, whereClause).all();
    }

    @Override
    public Mono<RedesPrograma> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<RedesPrograma> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<RedesPrograma> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private RedesPrograma process(Row row, RowMetadata metadata) {
        RedesPrograma entity = redesprogramaMapper.apply(row, "e");
        entity.setPrograma(programaMapper.apply(row, "programa"));
        entity.setTablaElementoCatalogo(tablaelementocatalogoMapper.apply(row, "tablaElementoCatalogo"));
        return entity;
    }

    @Override
    public <S extends RedesPrograma> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
