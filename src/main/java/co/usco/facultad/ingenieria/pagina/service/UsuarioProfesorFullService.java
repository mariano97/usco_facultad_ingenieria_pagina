package co.usco.facultad.ingenieria.pagina.service;

import co.usco.facultad.ingenieria.pagina.service.dto.AdminUserDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.UsuarioProfesorFullDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UsuarioProfesorFullService {

    public Flux<AdminUserDTO> getAllUsuariosProfesor(Pageable pageable, List<String> auths, String nameCompleteFilter);

    public Mono<UsuarioProfesorFullDTO> crearUsuarioProfesor(UsuarioProfesorFullDTO usuarioProfesorFullDTO);

    public Mono<UsuarioProfesorFullDTO> updateUsuarioProfesor(UsuarioProfesorFullDTO usuarioProfesorFullDTO);

    public Mono<UsuarioProfesorFullDTO> getUsuarioProfesorByUserLogin(String userLogin);

}
