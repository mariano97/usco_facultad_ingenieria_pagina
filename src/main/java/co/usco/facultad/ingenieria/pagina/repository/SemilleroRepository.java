package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Semillero;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Semillero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemilleroRepository extends ReactiveCrudRepository<Semillero, Long>, SemilleroRepositoryInternal {
    Flux<Semillero> findAllBy(Pageable pageable);

    @Override
    Mono<Semillero> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Semillero> findAllWithEagerRelationships();

    @Override
    Flux<Semillero> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM semillero entity WHERE entity.facultad_id = :id")
    Flux<Semillero> findByFacultad(Long id);

    @Query("SELECT * FROM semillero entity WHERE entity.facultad_id IS NULL")
    Flux<Semillero> findAllWhereFacultadIsNull();

    @Query("SELECT * FROM semillero entity WHERE entity.profesor_id = :id")
    Flux<Semillero> findByProfesor(Long id);

    @Query("SELECT * FROM semillero entity WHERE entity.profesor_id IS NULL")
    Flux<Semillero> findAllWhereProfesorIsNull();

    @Override
    <S extends Semillero> Mono<S> save(S entity);

    @Override
    Flux<Semillero> findAll();

    @Override
    Mono<Semillero> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SemilleroRepositoryInternal {
    <S extends Semillero> Mono<S> save(S entity);

    Flux<Semillero> findAllBy(Pageable pageable);

    Flux<Semillero> findAll();

    Mono<Semillero> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Semillero> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Semillero> findOneWithEagerRelationships(Long id);

    Flux<Semillero> findAllWithEagerRelationships();

    Flux<Semillero> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
