package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Estados;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Estados entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadosRepository extends ReactiveCrudRepository<Estados, Long>, EstadosRepositoryInternal {
    Flux<Estados> findAllBy(Pageable pageable);

    @Override
    Mono<Estados> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Estados> findAllWithEagerRelationships();

    @Override
    Flux<Estados> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM estados entity WHERE entity.paises_id = :id")
    Flux<Estados> findByPaises(Long id);

    @Query("SELECT * FROM estados entity WHERE entity.paises_id IS NULL")
    Flux<Estados> findAllWherePaisesIsNull();

    @Override
    <S extends Estados> Mono<S> save(S entity);

    @Override
    Flux<Estados> findAll();

    @Override
    Mono<Estados> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EstadosRepositoryInternal {
    <S extends Estados> Mono<S> save(S entity);

    Flux<Estados> findAllBy(Pageable pageable);

    Flux<Estados> findAll();

    Mono<Estados> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Estados> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Estados> findOneWithEagerRelationships(Long id);

    Flux<Estados> findAllWithEagerRelationships();

    Flux<Estados> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
