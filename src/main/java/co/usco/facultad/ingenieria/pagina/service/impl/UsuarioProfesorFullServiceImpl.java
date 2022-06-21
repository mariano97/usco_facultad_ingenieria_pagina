package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.service.UserService;
import co.usco.facultad.ingenieria.pagina.service.UsuarioProfesorFullService;
import co.usco.facultad.ingenieria.pagina.service.dto.UsuarioProfesorFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
@Transactional
public class UsuarioProfesorFullServiceImpl implements UsuarioProfesorFullService {

    private final Logger log = LoggerFactory.getLogger(UsuarioProfesorFullServiceImpl.class);

    private final UserService userService;

    public UsuarioProfesorFullServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Flux<UsuarioProfesorFullDTO> getAllUsuariosProfesor(Pageable pageable) {

        return null;
    }
}
