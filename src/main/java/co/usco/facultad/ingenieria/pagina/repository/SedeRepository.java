package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Sede;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Sede entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SedeRepository extends ReactiveCrudRepository<Sede, Long>, SedeRepositoryInternal {
    Flux<Sede> findAllBy(Pageable pageable);

    @Override
    Mono<Sede> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Sede> findAllWithEagerRelationships();

    @Override
    Flux<Sede> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM sede entity WHERE entity.ciudad_id = :id")
    Flux<Sede> findByCiudad(Long id);

    @Query("SELECT * FROM sede entity WHERE entity.ciudad_id IS NULL")
    Flux<Sede> findAllWhereCiudadIsNull();

    @Override
    <S extends Sede> Mono<S> save(S entity);

    @Override
    Flux<Sede> findAll();

    @Override
    Mono<Sede> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SedeRepositoryInternal {
    <S extends Sede> Mono<S> save(S entity);

    Flux<Sede> findAllBy(Pageable pageable);

    Flux<Sede> findAll();

    Mono<Sede> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Sede> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Sede> fundByCodigoIndicativo(String codigoIdicativo);

    Mono<Sede> findOneWithEagerRelationships(Long id);

    Flux<Sede> findAllWithEagerRelationships();

    Flux<Sede> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
