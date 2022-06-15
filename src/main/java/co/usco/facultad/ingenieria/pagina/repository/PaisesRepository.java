package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Paises;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Paises entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaisesRepository extends ReactiveCrudRepository<Paises, Long>, PaisesRepositoryInternal {
    Flux<Paises> findAllBy(Pageable pageable);

    @Override
    <S extends Paises> Mono<S> save(S entity);

    @Override
    Flux<Paises> findAll();

    @Override
    Mono<Paises> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PaisesRepositoryInternal {
    <S extends Paises> Mono<S> save(S entity);

    Flux<Paises> findAllBy(Pageable pageable);

    Flux<Paises> findAll();

    Mono<Paises> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Paises> findAllBy(Pageable pageable, Criteria criteria);

}
