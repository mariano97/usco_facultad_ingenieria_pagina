package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Laboratorio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Laboratorio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LaboratorioRepository extends ReactiveCrudRepository<Laboratorio, Long>, LaboratorioRepositoryInternal {
    Flux<Laboratorio> findAllBy(Pageable pageable);

    @Override
    <S extends Laboratorio> Mono<S> save(S entity);

    @Override
    Flux<Laboratorio> findAll();

    @Override
    Mono<Laboratorio> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LaboratorioRepositoryInternal {
    <S extends Laboratorio> Mono<S> save(S entity);

    Flux<Laboratorio> findAllBy(Pageable pageable);

    Flux<Laboratorio> findAll();

    Mono<Laboratorio> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Laboratorio> findAllBy(Pageable pageable, Criteria criteria);

}
