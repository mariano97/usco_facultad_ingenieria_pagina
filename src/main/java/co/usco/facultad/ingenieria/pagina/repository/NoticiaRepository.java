package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Noticia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Spring Data SQL reactive repository for the Noticia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoticiaRepository extends ReactiveCrudRepository<Noticia, Long>, NoticiaRepositoryInternal {
    Flux<Noticia> findAllBy(Pageable pageable);

    @Override
    Mono<Noticia> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Noticia> findAllWithEagerRelationships();

    @Override
    Flux<Noticia> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM noticia entity WHERE entity.facultad_id = :id")
    Flux<Noticia> findByFacultad(Long id);

    @Query("SELECT * FROM noticia entity WHERE entity.facultad_id IS NULL")
    Flux<Noticia> findAllWhereFacultadIsNull();

    @Override
    <S extends Noticia> Mono<S> save(S entity);

    @Override
    Flux<Noticia> findAll();

    @Override
    Mono<Noticia> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface NoticiaRepositoryInternal {
    <S extends Noticia> Mono<S> save(S entity);

    Flux<Noticia> findAllBy(Pageable pageable);

    Flux<Noticia> findAll();

    Mono<Noticia> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Noticia> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Noticia> findOneWithEagerRelationships(Long id);

    Flux<Noticia> findAllWithEagerRelationships();

    Flux<Noticia> findAllWithEagerRelationships(Pageable page);

    Flux<Noticia> findAllFechaMayorQue(Pageable pageable, Instant fechaInicial);

    Mono<Void> deleteById(Long id);
}
