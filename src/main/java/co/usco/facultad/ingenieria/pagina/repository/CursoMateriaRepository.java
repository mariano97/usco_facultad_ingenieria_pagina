package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CursoMateria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CursoMateriaRepository extends ReactiveCrudRepository<CursoMateria, Long>, CursoMateriaRepositoryInternal {
    Flux<CursoMateria> findAllBy(Pageable pageable);

    @Override
    Mono<CursoMateria> findOneWithEagerRelationships(Long id);

    @Override
    Flux<CursoMateria> findAllWithEagerRelationships();

    @Override
    Flux<CursoMateria> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM curso_materia entity WHERE entity.nivel_academico_id = :id")
    Flux<CursoMateria> findByNivelAcademico(Long id);

    @Query("SELECT * FROM curso_materia entity WHERE entity.nivel_academico_id IS NULL")
    Flux<CursoMateria> findAllWhereNivelAcademicoIsNull();

    @Query(
        "SELECT entity.* FROM curso_materia entity JOIN rel_curso_materia__programa joinTable ON entity.id = joinTable.programa_id WHERE joinTable.programa_id = :id"
    )
    Flux<CursoMateria> findByPrograma(Long id);

    @Override
    <S extends CursoMateria> Mono<S> save(S entity);

    @Override
    Flux<CursoMateria> findAll();

    @Override
    Mono<CursoMateria> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Query("DELETE FROM rel_profesor__curso_materia cursoMateria WHERE cursoMateria.curso_materia_id = :materiaId")
    Mono<Void> deleteProfesorCursoMateriaByMateriaId(Long materiaId);
}

interface CursoMateriaRepositoryInternal {
    <S extends CursoMateria> Mono<S> save(S entity);

    Flux<CursoMateria> findAllBy(Pageable pageable);

    Flux<CursoMateria> findAll();

    Mono<CursoMateria> findById(Long id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CursoMateria> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Long> countWithProgramaId(Long programaId);

    Flux<CursoMateria> findByAllByPrograma(Pageable pageable, Long programaId);

    Flux<CursoMateria> findByAllByPrograma(Long programaId);

    Flux<CursoMateria> findAllByProfesorRelation(Long programaId);

    Mono<CursoMateria> findOneWithEagerRelationships(Long id);

    Flux<CursoMateria> findAllWithEagerRelationships();

    Flux<CursoMateria> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
