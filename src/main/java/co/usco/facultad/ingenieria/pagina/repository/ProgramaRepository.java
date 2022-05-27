package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Programa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramaRepository extends ReactiveCrudRepository<Programa, Long>, ProgramaRepositoryInternal {
    Flux<Programa> findAllBy(Pageable pageable);

    @Override
    Mono<Programa> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Programa> findAllWithEagerRelationships();

    @Override
    Flux<Programa> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM programa entity WHERE entity.nivel_formacion_id = :id")
    Flux<Programa> findByNivelFormacion(Long id);

    @Query("SELECT * FROM programa entity WHERE entity.nivel_formacion_id IS NULL")
    Flux<Programa> findAllWhereNivelFormacionIsNull();

    @Query("SELECT * FROM programa entity WHERE entity.tipo_formacion_id = :id")
    Flux<Programa> findByTipoFormacion(Long id);

    @Query("SELECT * FROM programa entity WHERE entity.tipo_formacion_id IS NULL")
    Flux<Programa> findAllWhereTipoFormacionIsNull();

    @Query("SELECT * FROM programa entity WHERE entity.facultad_id = :id")
    Flux<Programa> findByFacultad(Long id);

    @Query("SELECT * FROM programa entity WHERE entity.facultad_id IS NULL")
    Flux<Programa> findAllWhereFacultadIsNull();

    @Override
    Mono<Programa> findByCodigoSnies(Long codigoSnies);

    @Query(
        "SELECT entity.* FROM programa entity JOIN rel_programa__sede joinTable ON entity.id = joinTable.sede_id WHERE joinTable.sede_id = :id"
    )
    Flux<Programa> findBySede(Long id);

    @Override
    <S extends Programa> Mono<S> save(S entity);

    @Override
    Flux<Programa> findAll();

    @Override
    Mono<Programa> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProgramaRepositoryInternal {
    <S extends Programa> Mono<S> save(S entity);

    Flux<Programa> findAllBy(Pageable pageable);

    Flux<Programa> findAll();

    Mono<Programa> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Programa> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Programa> findOneWithEagerRelationships(Long id);

    Flux<Programa> findAllWithEagerRelationships();

    Flux<Programa> findAllWithEagerRelationships(Pageable page);

    Mono<Programa> findByCodigoSnies(Long codigoSnies);

    Mono<Void> deleteById(Long id);
}
