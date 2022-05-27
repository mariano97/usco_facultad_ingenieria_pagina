package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.domain.Sede;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.FacultadRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.ProgramaRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.TablaElementoCatalogoRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
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
 * Spring Data SQL reactive custom repository implementation for the Programa entity.
 */
@SuppressWarnings("unused")
class ProgramaRepositoryInternalImpl extends SimpleR2dbcRepository<Programa, Long> implements ProgramaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaElementoCatalogoRowMapper tablaelementocatalogoMapper;
    private final FacultadRowMapper facultadMapper;
    private final ProgramaRowMapper programaMapper;

    private static final Table entityTable = Table.aliased("programa", EntityManager.ENTITY_ALIAS);
    private static final Table nivelFormacionTable = Table.aliased("tabla_elemento_catalogo", "nivelFormacion");
    private static final Table tipoFormacionTable = Table.aliased("tabla_elemento_catalogo", "tipoFormacion");
    private static final Table facultadTable = Table.aliased("facultad", "facultad");

    private static final EntityManager.LinkTable sedeLink = new EntityManager.LinkTable("rel_programa__sede", "programa_id", "sede_id");

    public ProgramaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaElementoCatalogoRowMapper tablaelementocatalogoMapper,
        FacultadRowMapper facultadMapper,
        ProgramaRowMapper programaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Programa.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablaelementocatalogoMapper = tablaelementocatalogoMapper;
        this.facultadMapper = facultadMapper;
        this.programaMapper = programaMapper;
    }

    @Override
    public Flux<Programa> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Programa> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProgramaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(nivelFormacionTable, "nivelFormacion"));
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(tipoFormacionTable, "tipoFormacion"));
        columns.addAll(FacultadSqlHelper.getColumns(facultadTable, "facultad"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(nivelFormacionTable)
            .on(Column.create("nivel_formacion_id", entityTable))
            .equals(Column.create("id", nivelFormacionTable))
            .leftOuterJoin(tipoFormacionTable)
            .on(Column.create("tipo_formacion_id", entityTable))
            .equals(Column.create("id", tipoFormacionTable))
            .leftOuterJoin(facultadTable)
            .on(Column.create("facultad_id", entityTable))
            .equals(Column.create("id", facultadTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Programa.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Programa> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Programa> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Programa> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Programa> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Programa> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    @Override
    public Mono<Programa> findByCodigoSnies(Long codigoSnies) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("codigo_snies"), Conditions.just(codigoSnies.toString()));
        return createQuery(null, whereClause).one();
    }

    private Programa process(Row row, RowMetadata metadata) {
        Programa entity = programaMapper.apply(row, "e");
        entity.setNivelFormacion(tablaelementocatalogoMapper.apply(row, "nivelFormacion"));
        entity.setTipoFormacion(tablaelementocatalogoMapper.apply(row, "tipoFormacion"));
        entity.setFacultad(facultadMapper.apply(row, "facultad"));
        return entity;
    }

    @Override
    public <S extends Programa> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Programa> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager.updateLinkTable(sedeLink, entity.getId(), entity.getSedes().stream().map(Sede::getId)).then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(sedeLink, entityId);
    }
}
