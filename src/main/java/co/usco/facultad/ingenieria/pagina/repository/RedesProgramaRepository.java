package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.RedesPrograma;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the RedesPrograma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RedesProgramaRepository extends ReactiveCrudRepository<RedesPrograma, Long>, RedesProgramaRepositoryInternal {
    Flux<RedesPrograma> findAllBy(Pageable pageable);

    @Override
    Mono<RedesPrograma> findOneWithEagerRelationships(Long id);

    @Override
    Flux<RedesPrograma> findAllWithEagerRelationships();

    @Override
    Flux<RedesPrograma> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM redes_programa entity WHERE entity.programa_id = :id")
    Flux<RedesPrograma> findByPrograma(Long id);

    @Query("SELECT * FROM redes_programa entity WHERE entity.programa_id IS NULL")
    Flux<RedesPrograma> findAllWhereProgramaIsNull();

    @Query("SELECT * FROM redes_programa entity WHERE entity.tabla_elemento_catalogo_id = :id")
    Flux<RedesPrograma> findByTablaElementoCatalogo(Long id);

    @Query("SELECT * FROM redes_programa entity WHERE entity.tabla_elemento_catalogo_id IS NULL")
    Flux<RedesPrograma> findAllWhereTablaElementoCatalogoIsNull();

    @Override
    <S extends RedesPrograma> Mono<S> save(S entity);

    @Override
    Flux<RedesPrograma> findAll();

    @Override
    Mono<RedesPrograma> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface RedesProgramaRepositoryInternal {
    <S extends RedesPrograma> Mono<S> save(S entity);

    Flux<RedesPrograma> findAllBy(Pageable pageable);

    Flux<RedesPrograma> findAll();

    Mono<RedesPrograma> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RedesPrograma> findAllBy(Pageable pageable, Criteria criteria);

    Flux<RedesPrograma> findAllByProgramaId(Long programaId);

    Mono<RedesPrograma> findOneWithEagerRelationships(Long id);

    Flux<RedesPrograma> findAllWithEagerRelationships();

    Flux<RedesPrograma> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
