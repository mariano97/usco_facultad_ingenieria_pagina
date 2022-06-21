package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.UsuarioProfesorFullDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface UsuarioProfesorFullService {

    public Flux<UsuarioProfesorFullDTO> getAllUsuariosProfesor(Pageable pageable);

}
