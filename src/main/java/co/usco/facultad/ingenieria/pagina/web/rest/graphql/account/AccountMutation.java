package co.usco.facultad.ingenieria.pagina.web.rest.graphql.account;

import co.usco.facultad.ingenieria.pagina.domain.User;
import co.usco.facultad.ingenieria.pagina.security.jwt.JWTFilter;
import co.usco.facultad.ingenieria.pagina.security.jwt.TokenProvider;
import co.usco.facultad.ingenieria.pagina.service.UserService;
import co.usco.facultad.ingenieria.pagina.service.dto.UserDTO;
import co.usco.facultad.ingenieria.pagina.web.rest.graphql.account.response.Token;
import co.usco.facultad.ingenieria.pagina.web.rest.AccountResource;
import co.usco.facultad.ingenieria.pagina.web.rest.UserJWTController;
import co.usco.facultad.ingenieria.pagina.web.rest.errors.InvalidPasswordException;
import co.usco.facultad.ingenieria.pagina.web.rest.vm.LoginVM;
import co.usco.facultad.ingenieria.pagina.web.rest.vm.ManagedUserVM;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Component
public class AccountMutation implements GraphQLMutationResolver {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Autowired
    private TokenProvider tokenProvider;

    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private UserService userService;

    public UserDTO register(@Valid ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        Mono<User> user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        return new UserDTO(user.block());
        //mailService.sendActivationEmail(user);
    }

    public Token authenticate(LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe()) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            Token token = new Token();
            token.setToken(jwt);
            return token;
        }catch (AuthenticationException e){
            throw new AuthenticationException(e.getMessage()) {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
