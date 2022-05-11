package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Facultad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacultadRepository extends ReactiveCrudRepository<Facultad, Long>, FacultadRepositoryInternal {
    Flux<Facultad> findAllBy(Pageable pageable);

    @Override
    <S extends Facultad> Mono<S> save(S entity);

    @Override
    Flux<Facultad> findAll();

    @Override
    Mono<Facultad> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FacultadRepositoryInternal {
    <S extends Facultad> Mono<S> save(S entity);

    Flux<Facultad> findAllBy(Pageable pageable);

    Flux<Facultad> findAll();

    Mono<Facultad> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Facultad> findAllBy(Pageable pageable, Criteria criteria);

}
