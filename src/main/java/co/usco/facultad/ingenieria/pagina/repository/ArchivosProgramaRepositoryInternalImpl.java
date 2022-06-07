package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.ArchivosProgramaRowMapper;
import co.usco.facultad.ingenieria.pagina.repository.rowmapper.ProgramaRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the ArchivosPrograma entity.
 */
@SuppressWarnings("unused")
class ArchivosProgramaRepositoryInternalImpl
    extends SimpleR2dbcRepository<ArchivosPrograma, Long>
    implements ArchivosProgramaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProgramaRowMapper programaMapper;
    private final ArchivosProgramaRowMapper archivosprogramaMapper;

    private static final Table entityTable = Table.aliased("archivos_programa", EntityManager.ENTITY_ALIAS);
    private static final Table programaTable = Table.aliased("programa", "programa");

    public ArchivosProgramaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProgramaRowMapper programaMapper,
        ArchivosProgramaRowMapper archivosprogramaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ArchivosPrograma.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.programaMapper = programaMapper;
        this.archivosprogramaMapper = archivosprogramaMapper;
    }

    @Override
    public Flux<ArchivosPrograma> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ArchivosPrograma> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ArchivosProgramaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProgramaSqlHelper.getColumns(programaTable, "programa"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(programaTable)
            .on(Column.create("programa_id", entityTable))
            .equals(Column.create("id", programaTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ArchivosPrograma.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ArchivosPrograma> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ArchivosPrograma> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ArchivosPrograma> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<ArchivosPrograma> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<ArchivosPrograma> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private ArchivosPrograma process(Row row, RowMetadata metadata) {
        ArchivosPrograma entity = archivosprogramaMapper.apply(row, "e");
        entity.setPrograma(programaMapper.apply(row, "programa"));
        return entity;
    }

    @Override
    public <S extends ArchivosPrograma> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
