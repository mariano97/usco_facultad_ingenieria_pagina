package co.usco.facultad.ingenieria.pagina.web.rest.graphql.account.user;


import co.usco.facultad.ingenieria.pagina.security.jwt.TokenProvider;
import co.usco.facultad.ingenieria.pagina.service.UserService;
import co.usco.facultad.ingenieria.pagina.service.dto.UserDTO;
import co.usco.facultad.ingenieria.pagina.web.rest.graphql.account.AccountQuery;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserQuery implements GraphQLQueryResolver {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private  UserService userService;

    public Mono<UserDTO> getAccount() {
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .defaultIfEmpty(null);
            // .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }
}
