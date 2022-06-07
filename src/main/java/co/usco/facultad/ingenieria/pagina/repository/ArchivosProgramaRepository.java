package co.usco.facultad.ingenieria.pagina.repository;

import co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the ArchivosPrograma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArchivosProgramaRepository extends ReactiveCrudRepository<ArchivosPrograma, Long>, ArchivosProgramaRepositoryInternal {
    Flux<ArchivosPrograma> findAllBy(Pageable pageable);

    @Override
    Mono<ArchivosPrograma> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ArchivosPrograma> findAllWithEagerRelationships();

    @Override
    Flux<ArchivosPrograma> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM archivos_programa entity WHERE entity.programa_id = :id")
    Flux<ArchivosPrograma> findByPrograma(Long id);

    @Query("SELECT * FROM archivos_programa entity WHERE entity.programa_id IS NULL")
    Flux<ArchivosPrograma> findAllWhereProgramaIsNull();

    @Override
    <S extends ArchivosPrograma> Mono<S> save(S entity);

    @Override
    Flux<ArchivosPrograma> findAll();

    @Override
    Mono<ArchivosPrograma> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ArchivosProgramaRepositoryInternal {
    <S extends ArchivosPrograma> Mono<S> save(S entity);

    Flux<ArchivosPrograma> findAllBy(Pageable pageable);

    Flux<ArchivosPrograma> findAll();

    Mono<ArchivosPrograma> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ArchivosPrograma> findAllBy(Pageable pageable, Criteria criteria);

    Mono<ArchivosPrograma> findOneWithEagerRelationships(Long id);

    Flux<ArchivosPrograma> findAllWithEagerRelationships();

    Flux<ArchivosPrograma> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
