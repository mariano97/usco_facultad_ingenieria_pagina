package co.usco.facultad.ingenieria.pagina.service.graphql.resolver;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import co.usco.facultad.ingenieria.pagina.repository.ProgramaRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class Query implements GraphQLQueryResolver {

    private ProgramaRepository programaRepository;

    @Autowired
    public Query(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    public Flux<Programa> findAllProgramas() {
        return programaRepository.findAll();
    }
}
