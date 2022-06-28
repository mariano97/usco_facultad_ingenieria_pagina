package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Profesor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfesorRepository extends ReactiveCrudRepository<Profesor, Long>, ProfesorRepositoryInternal {
    Flux<Profesor> findAllBy(Pageable pageable);

    @Override
    Mono<Profesor> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Profesor> findAllWithEagerRelationships();

    @Override
    Flux<Profesor> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM profesor entity WHERE entity.tabla_elemento_catalogo_id = :id")
    Flux<Profesor> findByTablaElementoCatalogo(Long id);

    @Query("SELECT * FROM profesor entity WHERE entity.tabla_elemento_catalogo_id IS NULL")
    Flux<Profesor> findAllWhereTablaElementoCatalogoIsNull();

    @Query("SELECT * FROM profesor entity WHERE entity.facultad_id = :id")
    Flux<Profesor> findByFacultad(Long id);

    @Query("SELECT * FROM profesor entity WHERE entity.facultad_id IS NULL")
    Flux<Profesor> findAllWhereFacultadIsNull();

    @Query(
        "SELECT entity.* FROM profesor entity JOIN rel_profesor__curso_materia joinTable ON entity.id = joinTable.curso_materia_id WHERE joinTable.curso_materia_id = :id"
    )
    Flux<Profesor> findByCursoMateria(Long id);

    @Override
    <S extends Profesor> Mono<S> save(S entity);

    @Override
    Flux<Profesor> findAll();

    @Override
    Mono<Profesor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProfesorRepositoryInternal {
    <S extends Profesor> Mono<S> save(S entity);

    Flux<Profesor> findAllBy(Pageable pageable);

    Flux<Profesor> findAll();

    Mono<Profesor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Profesor> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Profesor> findOneWithEagerRelationships(Long id);

    Flux<Profesor> findAllWithEagerRelationships();

    Flux<Profesor> findAllWithEagerRelationships(Pageable page);

    Mono<Profesor> findOneByUserId(Long userId);

    Mono<Void> deleteById(Long id);
}
