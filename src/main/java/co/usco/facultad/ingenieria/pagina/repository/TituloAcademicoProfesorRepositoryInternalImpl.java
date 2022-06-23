package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.PaisesRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.ProfesorRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.TablaElementoCatalogoRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.TituloAcademicoProfesorRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the TituloAcademicoProfesor entity.
 */
@SuppressWarnings("unused")
class TituloAcademicoProfesorRepositoryInternalImpl
    extends SimpleR2dbcRepository<TituloAcademicoProfesor, Long>
    implements TituloAcademicoProfesorRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TablaElementoCatalogoRowMapper tablaelementocatalogoMapper;
    private final ProfesorRowMapper profesorMapper;
    private final PaisesRowMapper paisesMapper;
    private final TituloAcademicoProfesorRowMapper tituloacademicoprofesorMapper;

    private static final Table entityTable = Table.aliased("titulo_academico_profesor", EntityManager.ENTITY_ALIAS);
    private static final Table tablaElementoCatalogoTable = Table.aliased("tabla_elemento_catalogo", "tablaElementoCatalogo");
    private static final Table profesorTable = Table.aliased("profesor", "profesor");
    private static final Table paisesTable = Table.aliased("paises", "paises");

    public TituloAcademicoProfesorRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TablaElementoCatalogoRowMapper tablaelementocatalogoMapper,
        ProfesorRowMapper profesorMapper,
        PaisesRowMapper paisesMapper,
        TituloAcademicoProfesorRowMapper tituloacademicoprofesorMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(TituloAcademicoProfesor.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tablaelementocatalogoMapper = tablaelementocatalogoMapper;
        this.profesorMapper = profesorMapper;
        this.paisesMapper = paisesMapper;
        this.tituloacademicoprofesorMapper = tituloacademicoprofesorMapper;
    }

    @Override
    public Flux<TituloAcademicoProfesor> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TituloAcademicoProfesor> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TituloAcademicoProfesorSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TablaElementoCatalogoSqlHelper.getColumns(tablaElementoCatalogoTable, "tablaElementoCatalogo"));
        columns.addAll(ProfesorSqlHelper.getColumns(profesorTable, "profesor"));
        columns.addAll(PaisesSqlHelper.getColumns(paisesTable, "paises"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tablaElementoCatalogoTable)
            .on(Column.create("tabla_elemento_catalogo_id", entityTable))
            .equals(Column.create("id", tablaElementoCatalogoTable))
            .leftOuterJoin(profesorTable)
            .on(Column.create("profesor_id", entityTable))
            .equals(Column.create("id", profesorTable))
            .leftOuterJoin(paisesTable)
            .on(Column.create("paises_id", entityTable))
            .equals(Column.create("id", paisesTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TituloAcademicoProfesor.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TituloAcademicoProfesor> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TituloAcademicoProfesor> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<TituloAcademicoProfesor> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<TituloAcademicoProfesor> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<TituloAcademicoProfesor> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private TituloAcademicoProfesor process(Row row, RowMetadata metadata) {
        TituloAcademicoProfesor entity = tituloacademicoprofesorMapper.apply(row, "e");
        entity.setTablaElementoCatalogo(tablaelementocatalogoMapper.apply(row, "tablaElementoCatalogo"));
        entity.setProfesor(profesorMapper.apply(row, "profesor"));
        entity.setPaises(paisesMapper.apply(row, "paises"));
        return entity;
    }

    @Override
    public <S extends TituloAcademicoProfesor> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
