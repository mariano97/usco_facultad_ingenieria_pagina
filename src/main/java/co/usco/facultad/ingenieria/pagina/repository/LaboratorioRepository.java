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
    Mono<Laboratorio> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Laboratorio> findAllWithEagerRelationships();

    @Override
    Flux<Laboratorio> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM laboratorio entity WHERE entity.tipo_laboratorio_id = :id")
    Flux<Laboratorio> findByTipoLaboratorio(Long id);

    @Query("SELECT * FROM laboratorio entity WHERE entity.tipo_laboratorio_id IS NULL")
    Flux<Laboratorio> findAllWhereTipoLaboratorioIsNull();

    @Query("SELECT * FROM laboratorio entity WHERE entity.facultad_id = :id")
    Flux<Laboratorio> findByFacultad(Long id);

    @Query("SELECT * FROM laboratorio entity WHERE entity.facultad_id IS NULL")
    Flux<Laboratorio> findAllWhereFacultadIsNull();

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

    Flux<Laboratorio> findAllByTipoTipoLaboratorioId(Pageable pageable, Long tipoCatalogoId);

    Flux<Laboratorio> findAllByTipoTipoLaboratorioId(Long tipoCatalogoId);

    Mono<Laboratorio> findOneWithEagerRelationships(Long id);

    Flux<Laboratorio> findAllWithEagerRelationships();

    Flux<Laboratorio> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
