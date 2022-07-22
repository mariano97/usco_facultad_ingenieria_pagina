package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the EscalafonProfesor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EscalafonProfesorRepository extends ReactiveCrudRepository<EscalafonProfesor, Long>, EscalafonProfesorRepositoryInternal {
    Flux<EscalafonProfesor> findAllBy(Pageable pageable);

    @Query("SELECT * FROM escalafon_profesor entity WHERE entity.profesor_id = :id")
    Flux<EscalafonProfesor> findByProfesor(Long id);

    @Query("SELECT * FROM escalafon_profesor entity WHERE entity.profesor_id IS NULL")
    Flux<EscalafonProfesor> findAllWhereProfesorIsNull();

    @Override
    <S extends EscalafonProfesor> Mono<S> save(S entity);

    @Override
    Flux<EscalafonProfesor> findAll();

    @Override
    Mono<EscalafonProfesor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EscalafonProfesorRepositoryInternal {
    <S extends EscalafonProfesor> Mono<S> save(S entity);

    Flux<EscalafonProfesor> findAllBy(Pageable pageable);

    Flux<EscalafonProfesor> findAll();

    Mono<EscalafonProfesor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EscalafonProfesor> findAllBy(Pageable pageable, Criteria criteria);

}
