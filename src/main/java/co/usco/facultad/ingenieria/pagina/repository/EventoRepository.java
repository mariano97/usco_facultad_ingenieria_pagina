package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Evento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Spring Data SQL reactive repository for the Evento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventoRepository extends ReactiveCrudRepository<Evento, Long>, EventoRepositoryInternal {
    Flux<Evento> findAllBy(Pageable pageable);

    @Override
    Mono<Evento> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Evento> findAllWithEagerRelationships();

    @Override
    Flux<Evento> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM evento entity WHERE entity.facultad_id = :id")
    Flux<Evento> findByFacultad(Long id);

    @Query("SELECT * FROM evento entity WHERE entity.facultad_id IS NULL")
    Flux<Evento> findAllWhereFacultadIsNull();

    @Override
    <S extends Evento> Mono<S> save(S entity);

    @Override
    Flux<Evento> findAll();

    @Override
    Mono<Evento> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EventoRepositoryInternal {
    <S extends Evento> Mono<S> save(S entity);

    Flux<Evento> findAllBy(Pageable pageable);

    Flux<Evento> findAll();

    Mono<Evento> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Evento> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Evento> findOneWithEagerRelationships(Long id);

    Flux<Evento> findAllWithEagerRelationships();

    Flux<Evento> findAllWithEagerRelationships(Pageable page);

    Flux<Evento> findAllFechaMayorQue(Pageable pageable, Instant fechaInicial);

    Mono<Void> deleteById(Long id);
}
