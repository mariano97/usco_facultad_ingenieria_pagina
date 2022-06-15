package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Ciudad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CiudadRepository extends ReactiveCrudRepository<Ciudad, Long>, CiudadRepositoryInternal {
    Flux<Ciudad> findAllBy(Pageable pageable);

    @Override
    Mono<Ciudad> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Ciudad> findAllWithEagerRelationships();

    @Override
    Flux<Ciudad> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM ciudad entity WHERE entity.estados_id = :id")
    Flux<Ciudad> findByEstados(Long id);

    @Query("SELECT * FROM ciudad entity WHERE entity.estados_id IS NULL")
    Flux<Ciudad> findAllWhereEstadosIsNull();

    @Override
    <S extends Ciudad> Mono<S> save(S entity);

    @Override
    Flux<Ciudad> findAll();

    @Override
    Mono<Ciudad> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CiudadRepositoryInternal {
    <S extends Ciudad> Mono<S> save(S entity);

    Flux<Ciudad> findAllBy(Pageable pageable);

    Flux<Ciudad> findAll();

    Mono<Ciudad> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Ciudad> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Ciudad> findOneWithEagerRelationships(Long id);

    Flux<Ciudad> findAllWithEagerRelationships();

    Flux<Ciudad> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
