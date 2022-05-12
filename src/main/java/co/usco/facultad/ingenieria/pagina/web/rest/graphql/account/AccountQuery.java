package co.usco.facultad.ingenieria.pagina.web.rest.graphql.account;

import co.usco.facultad.ingenieria.pagina.repository.UserRepository;
import co.usco.facultad.ingenieria.pagina.service.UserService;
import co.usco.facultad.ingenieria.pagina.service.dto.UserDTO;
import graphql.kickstart.servlet.context.GraphQLServletContext;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountQuery implements GraphQLQueryResolver {

    private final Logger log = LoggerFactory.getLogger(AccountQuery.class);

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  UserService userService;

    public String getIsAuthenticated(DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        log.debug("Graphql request to check if the current user is authenticated");
        return context.getHttpServletRequest().getRemoteUser();
    }
}
