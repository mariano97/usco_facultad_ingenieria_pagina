package co.usco.facultad.ingenieria.pagina.service.impl;

import co.usco.facultad.ingenieria.pagina.service.ProfesorService;
import co.usco.facultad.ingenieria.pagina.service.UserService;
import co.usco.facultad.ingenieria.pagina.service.UsuarioProfesorFullService;
import co.usco.facultad.ingenieria.pagina.service.dto.AdminUserDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.UsuarioProfesorFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UsuarioProfesorFullServiceImpl implements UsuarioProfesorFullService {

    private final Logger log = LoggerFactory.getLogger(UsuarioProfesorFullServiceImpl.class);

    private final UserService userService;

    private final ProfesorService profesorService;

    public UsuarioProfesorFullServiceImpl(UserService userService, ProfesorService profesorService) {
        this.userService = userService;
        this.profesorService = profesorService;
    }

    @Override
    public Mono<Long> countTotalProfesor() {
        return profesorService.countAll();
    }

    @Override
    public Mono<Long> countProfesorByProgramaCodigoSnies(Long codigoSnies) {
        return profesorService.countProfesorByProgramaCodigoSnies(codigoSnies);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UsuarioProfesorFullDTO> getAllUsuariosProfesorByProgramaCodigoSnies(Pageable pageable, Long codigoSnies) {
        List<UsuarioProfesorFullDTO> usuarioProfesorFullDTOList = new ArrayList<>();
        return profesorService.findAllByProgramaCodigoSnies(pageable, codigoSnies)
            .flatMap(profesorDTO -> userService.getOneById(profesorDTO.getUserId())
                .map(adminUserDTO -> usuarioProfesorFullDTOList.add(new UsuarioProfesorFullDTO(adminUserDTO, profesorDTO))))
            .flatMap(addedResult ->
                Flux.fromIterable(usuarioProfesorFullDTOList));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UsuarioProfesorFullDTO> getAllUsuarioProfesor(Pageable pageable) {
        List<UsuarioProfesorFullDTO> usuarioProfesorFullDTOList = new ArrayList<>();
        return profesorService.findAll(pageable)
            .flatMap(profesorDTO -> userService.getOneById(profesorDTO.getUserId())
                .map(adminUserDTO -> usuarioProfesorFullDTOList.add(new UsuarioProfesorFullDTO(adminUserDTO, profesorDTO))))
            .flatMap(addedResult ->
                Flux.fromIterable(usuarioProfesorFullDTOList));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminUserDTO> getAllUsuariosProfesor(Pageable pageable, List<String> auths, String nameCompleteFilter) {
        return userService.getAllWithAuthoritiesAndSpecicatedAuthorities(pageable, auths, nameCompleteFilter);

    }

    @Override
    @Transactional
    public Mono<UsuarioProfesorFullDTO> crearUsuarioProfesor(UsuarioProfesorFullDTO usuarioProfesorFullDTO) {
        String password = generatePassword();
        return userService.createUserProfesor(usuarioProfesorFullDTO.getAdminUserDTO(), password)
            .flatMap(adminUserDTO -> {
                usuarioProfesorFullDTO.getProfesorDTO().setUserId(adminUserDTO.getId());
                return profesorService.save(usuarioProfesorFullDTO.getProfesorDTO())
                    .map(profesorDTO -> new UsuarioProfesorFullDTO(adminUserDTO, profesorDTO))
                    .flatMap(Mono::just);
            });
    }

    @Override
    @Transactional
    public Mono<UsuarioProfesorFullDTO> updateUsuarioProfesor(UsuarioProfesorFullDTO usuarioProfesorFullDTO) {
        return userService.updateUserProfesor(usuarioProfesorFullDTO.getAdminUserDTO())
            .flatMap(adminUserDTO -> {
                usuarioProfesorFullDTO.getProfesorDTO().setUserId(adminUserDTO.getId());
                return profesorService.update(usuarioProfesorFullDTO.getProfesorDTO())
                    .map(profesorDTO -> new UsuarioProfesorFullDTO(adminUserDTO, profesorDTO))
                    .flatMap(Mono::just);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UsuarioProfesorFullDTO> getUsuarioProfesorByUserLogin(String userLogin) {
        return userService.getUserWithAuthoritiesByLogin(userLogin).map(AdminUserDTO::new)
            .flatMap(adminUserDTO ->
                profesorService.findOneByUserId(adminUserDTO.getId())
                    .map(profesorDTO -> new UsuarioProfesorFullDTO(adminUserDTO, profesorDTO))
                    .flatMap(Mono::just)
            );
    }

    private String generatePassword() {
        return "usco_ingenieria";
    }
}
