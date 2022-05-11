package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TablaTiposCatalogo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TablaTiposCatalogoRepository
    extends ReactiveCrudRepository<TablaTiposCatalogo, Long>, TablaTiposCatalogoRepositoryInternal {
    Flux<TablaTiposCatalogo> findAllBy(Pageable pageable);

    @Override
    <S extends TablaTiposCatalogo> Mono<S> save(S entity);

    @Override
    Flux<TablaTiposCatalogo> findAll();

    @Override
    Mono<TablaTiposCatalogo> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TablaTiposCatalogoRepositoryInternal {
    <S extends TablaTiposCatalogo> Mono<S> save(S entity);

    Flux<TablaTiposCatalogo> findAllBy(Pageable pageable);

    Flux<TablaTiposCatalogo> findAll();

    Mono<TablaTiposCatalogo> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TablaTiposCatalogo> findAllBy(Pageable pageable, Criteria criteria);

}
