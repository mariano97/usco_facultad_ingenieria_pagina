package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TituloAcademicoProfesor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TituloAcademicoProfesorRepository
    extends ReactiveCrudRepository<TituloAcademicoProfesor, Long>, TituloAcademicoProfesorRepositoryInternal {
    Flux<TituloAcademicoProfesor> findAllBy(Pageable pageable);

    @Override
    Mono<TituloAcademicoProfesor> findOneWithEagerRelationships(Long id);

    @Override
    Flux<TituloAcademicoProfesor> findAllWithEagerRelationships();

    @Override
    Flux<TituloAcademicoProfesor> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM titulo_academico_profesor entity WHERE entity.tabla_elemento_catalogo_id = :id")
    Flux<TituloAcademicoProfesor> findByTablaElementoCatalogo(Long id);

    @Query("SELECT * FROM titulo_academico_profesor entity WHERE entity.tabla_elemento_catalogo_id IS NULL")
    Flux<TituloAcademicoProfesor> findAllWhereTablaElementoCatalogoIsNull();

    @Query("SELECT * FROM titulo_academico_profesor entity WHERE entity.profesor_id = :id")
    Flux<TituloAcademicoProfesor> findByProfesor(Long id);

    @Query("SELECT * FROM titulo_academico_profesor entity WHERE entity.profesor_id IS NULL")
    Flux<TituloAcademicoProfesor> findAllWhereProfesorIsNull();

    @Query("SELECT * FROM titulo_academico_profesor entity WHERE entity.paises_id = :id")
    Flux<TituloAcademicoProfesor> findByPaises(Long id);

    @Query("SELECT * FROM titulo_academico_profesor entity WHERE entity.paises_id IS NULL")
    Flux<TituloAcademicoProfesor> findAllWherePaisesIsNull();

    @Override
    <S extends TituloAcademicoProfesor> Mono<S> save(S entity);

    @Override
    Flux<TituloAcademicoProfesor> findAll();

    @Override
    Mono<TituloAcademicoProfesor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TituloAcademicoProfesorRepositoryInternal {
    <S extends TituloAcademicoProfesor> Mono<S> save(S entity);

    Flux<TituloAcademicoProfesor> findAllBy(Pageable pageable);

    Flux<TituloAcademicoProfesor> findAll();

    Mono<TituloAcademicoProfesor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TituloAcademicoProfesor> findAllBy(Pageable pageable, Criteria criteria);

    Mono<TituloAcademicoProfesor> findOneWithEagerRelationships(Long id);

    Flux<TituloAcademicoProfesor> findAllWithEagerRelationships();

    Flux<TituloAcademicoProfesor> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
