package co.usco.facultad.ingenieria.pagina.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import co.usco.facultad.ingenieria.pagina.domain.Authority;
import co.usco.facultad.ingenieria.pagina.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import co.usco.facultad.ingenieria.pagina.security.AuthoritiesConstants;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long>, UserRepositoryInternal {
    Mono<User> findOneByActivationKey(String activationKey);

    Flux<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDateTime dateTime);

    Mono<User> findOneByResetKey(String resetKey);

    Mono<User> findOneByEmailIgnoreCase(String email);

    Mono<User> findOneByLogin(String login);

    Mono<User> findById(Long id);

    Flux<User> findAllByIdNotNull(Pageable pageable);

    Flux<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    Mono<Long> count();

    @Query("INSERT INTO jhi_user_authority VALUES(:userId, :authority)")
    Mono<Void> saveUserAuthority(Long userId, String authority);

    @Query("DELETE FROM jhi_user_authority")
    Mono<Void> deleteAllUserAuthorities();

    @Query("DELETE FROM jhi_user_authority WHERE user_id = :userId")
    Mono<Void> deleteUserAuthorities(Long userId);
}

interface DeleteExtended<T> {
    Mono<Void> delete(T user);
}

interface UserRepositoryInternal extends DeleteExtended<User> {
    Mono<User> findOneWithAuthoritiesByLogin(String login);

    Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Flux<User> findAllWithAuthorities(Pageable pageable);

    Flux<User> findAllWithAuthoritiesAndSpecicatedAuthorities(Pageable pageable, List<String> auth, String nameCompleteFilter);

    Mono<Long> countWithSpecicatedAuthorities(List<String> authorities, String nameCompleteFilter);
}

class UserRepositoryInternalImpl implements UserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final R2dbcConverter r2dbcConverter;

    public UserRepositoryInternalImpl(DatabaseClient db, R2dbcEntityTemplate r2dbcEntityTemplate, R2dbcConverter r2dbcConverter) {
        this.db = db;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.r2dbcConverter = r2dbcConverter;
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByLogin(String login) {
        return findOneWithAuthoritiesBy("login", login);
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return findOneWithAuthoritiesBy("email", email.toLowerCase());
    }

    @Override
    public Flux<User> findAllWithAuthorities(Pageable pageable) {
        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("id");
        String direction = String.valueOf(
            pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(Sort.DEFAULT_DIRECTION)
        );
        long page = pageable.getPageNumber();
        long size = pageable.getPageSize();

        return db
            .sql("SELECT * FROM jhi_user u LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id")
            .map((row, metadata) ->
                Tuples.of(r2dbcConverter.read(User.class, row, metadata), Optional.ofNullable(row.get("authority_name", String.class)))
            )
            .all()
            .groupBy(t -> t.getT1().getLogin())
            .flatMap(l -> l.collectList().map(t -> updateUserWithAuthorities(t.get(0).getT1(), t)))
            .sort(
                Sort.Direction.fromString(direction) == Sort.DEFAULT_DIRECTION
                    ? new BeanComparator<>(property)
                    : new BeanComparator<>(property).reversed()
            )
            .skip(page * size)
            .take(size);
    }

    @Override
    public Flux<User> findAllWithAuthoritiesAndSpecicatedAuthorities(Pageable pageable, List<String> auths, String nameCompleteFilter) {
        return findAllWithAuthorities(pageable).collectList()
            .map(users -> {
                if (auths != null && auths.size() > 0) {
                    return users.stream().filter(user -> user.getAuthorities().stream()
                        .anyMatch(authority -> auths.contains(authority.getName()))).collect(Collectors.toList());
                } else {
                    return users;
                }
            })
            .map(users -> users.stream().map(user -> {
                if (user.getNameComplete() == null) {
                    user.setNameComplete("");
                }
                return user;
            }).collect(Collectors.toList()))
            .map(users -> {
                if (nameCompleteFilter != null && !nameCompleteFilter.isBlank()) {
                    return users.stream().filter(user -> user.getNameComplete().toLowerCase().trim().contains(nameCompleteFilter.toLowerCase().trim()))
                        .collect(Collectors.toList());
                } else {
                    return users;
                }
            })
            .map(users -> users.stream().filter(Objects::nonNull).collect(Collectors.toList()))
            .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Long> countWithSpecicatedAuthorities(List<String> authorities, String nameCompleteFilter) {
        String conditionAuth = " ";
        if (authorities != null && authorities.size() > 0) {
            conditionAuth = conditionAuth.concat("where ");
            conditionAuth = conditionAuth.concat(" (");
            for (int i = 0; i<authorities.size(); i++) {
                conditionAuth = conditionAuth.concat("ua.authority_name like '%" + authorities.get(i) + "%'");
                if ((i + 1) <authorities.size()) {
                    conditionAuth = conditionAuth.concat(" OR ");
                }
            }
            conditionAuth = conditionAuth.concat(") ");
        }
        if (nameCompleteFilter != null && !nameCompleteFilter.isBlank()) {
            if (conditionAuth.contains("where")) {
                conditionAuth = conditionAuth.concat(" AND ");
            } else {
                conditionAuth = conditionAuth.concat("where ");
            }
            conditionAuth = conditionAuth.concat("u.name_complete like '%" + nameCompleteFilter.toLowerCase().trim() + "%'");
        }
        return db
            .sql("select count(*) from (select u.id from jhi_user u " +
                "LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id " +
                conditionAuth +
                " group by u.id) src")
            .map((row, rowMetadata) -> (long) row.get(0))
            .one();
    }

    @Override
    public Mono<Void> delete(User user) {
        return db
            .sql("DELETE FROM jhi_user_authority WHERE user_id = :userId")
            .bind("userId", user.getId())
            .then()
            .then(r2dbcEntityTemplate.delete(User.class).matching(query(where("id").is(user.getId()))).all().then());
    }

    private Mono<User> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
        return db
            .sql("SELECT * FROM jhi_user u LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id WHERE u." + fieldName + " = :" + fieldName)
            .bind(fieldName, fieldValue)
            .map((row, metadata) ->
                Tuples.of(r2dbcConverter.read(User.class, row, metadata), Optional.ofNullable(row.get("authority_name", String.class)))
            )
            .all()
            .collectList()
            .filter(l -> !l.isEmpty())
            .map(l -> updateUserWithAuthorities(l.get(0).getT1(), l));
    }

    private User updateUserWithAuthorities(User user, List<Tuple2<User, Optional<String>>> tuples) {
        user.setAuthorities(
            tuples
                .stream()
                .filter(t -> t.getT2().isPresent())
                .map(t -> {
                    Authority authority = new Authority();
                    authority.setName(t.getT2().get());
                    return authority;
                })
                .collect(Collectors.toSet())
        );

        return user;
    }
}
