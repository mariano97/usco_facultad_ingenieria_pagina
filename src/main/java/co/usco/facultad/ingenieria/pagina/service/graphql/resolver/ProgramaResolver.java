package co.usco.facultad.ingenieria.pagina.service.graphql.resolver;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.repository.ProgramaRepository;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class ProgramaResolver implements GraphQLQueryResolver {

    private final Logger log = LoggerFactory.getLogger(ProgramaResolver.class);

    private ProgramaRepository programaRepository;

    @Autowired
    public ProgramaResolver(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    public Mono<Programa> programa(Long id) {
        log.debug("Dentro de prgrama consulta id: {}", id);
        return programaRepository.findById(id);
    }
}
