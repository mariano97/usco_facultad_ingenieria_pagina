package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TablaElementoCatalogo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TablaElementoCatalogoRepository
    extends ReactiveCrudRepository<TablaElementoCatalogo, Long>, TablaElementoCatalogoRepositoryInternal {
    Flux<TablaElementoCatalogo> findAllBy(Pageable pageable);

    @Override
    Mono<TablaElementoCatalogo> findOneWithEagerRelationships(Long id);

    @Override
    Flux<TablaElementoCatalogo> findAllWithEagerRelationships();

    @Override
    Flux<TablaElementoCatalogo> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM tabla_elemento_catalogo entity WHERE entity.tabla_tipos_catalogo_id = :id")
    Flux<TablaElementoCatalogo> findByTablaTiposCatalogo(Long id);

    @Query("SELECT * FROM tabla_elemento_catalogo entity WHERE entity.tabla_tipos_catalogo_id IS NULL")
    Flux<TablaElementoCatalogo> findAllWhereTablaTiposCatalogoIsNull();

    @Override
    <S extends TablaElementoCatalogo> Mono<S> save(S entity);

    @Override
    Flux<TablaElementoCatalogo> findAll();

    @Override
    Mono<TablaElementoCatalogo> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TablaElementoCatalogoRepositoryInternal {
    <S extends TablaElementoCatalogo> Mono<S> save(S entity);

    Flux<TablaElementoCatalogo> findAllBy(Pageable pageable);

    Flux<TablaElementoCatalogo> findAll();

    Mono<TablaElementoCatalogo> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TablaElementoCatalogo> findAllBy(Pageable pageable, Criteria criteria);

    Mono<TablaElementoCatalogo> findOneWithEagerRelationships(Long id);

    Flux<TablaElementoCatalogo> findAllWithEagerRelationships();

    Flux<TablaElementoCatalogo> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
