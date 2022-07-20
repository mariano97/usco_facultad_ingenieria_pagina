package co.usco.facultad.ingenieria.pagina.web.rest;

import co.usco.facultad.ingenieria.pagina.service.ProfesorService;
import co.usco.facultad.ingenieria.pagina.service.UserService;
import co.usco.facultad.ingenieria.pagina.service.UsuarioProfesorFullService;
import co.usco.facultad.ingenieria.pagina.service.dto.AdminUserDTO;
import co.usco.facultad.ingenieria.pagina.service.dto.UsuarioProfesorFullDTO;
import co.usco.facultad.ingenieria.pagina.utils.UtilsMethods;
import co.usco.facultad.ingenieria.pagina.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioProfesorFullResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioProfesorFullResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String ENTITY_NAME = "programa";

    private final UsuarioProfesorFullService usuarioProfesorFullService;

    private final UserService userService;

    private final ProfesorService profesorService;

    private final UtilsMethods utilsMethods;

    public UsuarioProfesorFullResource(UsuarioProfesorFullService usuarioProfesorFullService, UserService userService, ProfesorService profesorService, UtilsMethods utilsMethods) {
        this.usuarioProfesorFullService = usuarioProfesorFullService;
        this.userService = userService;
        this.profesorService = profesorService;
        this.utilsMethods = utilsMethods;
    }

    @PostMapping(value = {
        "/usuario-profesor-full",
        "/open/usuario-profesor-full"
    })
    public Mono<ResponseEntity<UsuarioProfesorFullDTO>> createUsuarioProfesor(
        @RequestBody UsuarioProfesorFullDTO usuarioProfesorFullDTO
    ) {
        if (usuarioProfesorFullDTO.getAdminUserDTO().getId() != null || usuarioProfesorFullDTO.getProfesorDTO().getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
        }
        return usuarioProfesorFullService.crearUsuarioProfesor(usuarioProfesorFullDTO)
            .map(usuarioProfesorFullDTOTemp -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/admin/users/" + usuarioProfesorFullDTOTemp.getAdminUserDTO().getLogin()))
                        .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", usuarioProfesorFullDTOTemp.getAdminUserDTO().getLogin()))
                        .body(usuarioProfesorFullDTOTemp);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @PutMapping(value = {
        "/usuario-profesor-full",
        "/open/usuario-profesor-full"
    })
    public Mono<ResponseEntity<UsuarioProfesorFullDTO>> updateUsuarioProfesor(@RequestBody UsuarioProfesorFullDTO usuarioProfesorFullDTO) {
        if (usuarioProfesorFullDTO.getAdminUserDTO().getId() == null || usuarioProfesorFullDTO.getProfesorDTO().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        log.debug("Usuario actualizar profesor: {}", usuarioProfesorFullDTO.toString());
        return usuarioProfesorFullService.updateUsuarioProfesor(usuarioProfesorFullDTO)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(result ->
                ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getAdminUserDTO().getId().toString()))
                    .body(result)
            );
    }

    @GetMapping(value = {
        "/usuario-profesor-full",
        "/open/usuario-profesor-full"
    })
    public Mono<ResponseEntity<Flux<AdminUserDTO>>> getAllUsuariosProfesor(
        @org.springdoc.api.annotations.ParameterObject ServerHttpRequest request,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "auths", required = false) List<String> auths,
        @RequestParam(value = "nameCompleteFilter", required = false) String nameCompleteFilter
    ) {
        return userService
            .countWithSpecicatedAuthorities(auths, nameCompleteFilter)
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(usuarioProfesorFullService.getAllUsuariosProfesor(pageable, auths, nameCompleteFilter)));

    }

    @GetMapping(value = {
        "/usuario-profesor-full/usuario-by-user-id",
        "/open/usuario-profesor-full/usuario-by-user-id"
    })
    public Mono<ResponseEntity<AdminUserDTO>> getUsuarioByUserId(@RequestParam("userId") Long userId) {
        return userService.getOneById(userId)
            .map(adminUserDTO -> ResponseEntity.ok().body(adminUserDTO));
    }

    @GetMapping(value = {
        "/usuario-profesor-full/name-filtering",
        "/open/usuario-profesor-full/name-filtering",
    })
    public Mono<ResponseEntity<Flux<UsuarioProfesorFullDTO>>> getAllUsuariosProfesorNameCompleteFiltering(
        @org.springdoc.api.annotations.ParameterObject ServerHttpRequest request,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "auths", required = false) List<String> auths,
        @RequestParam(value = "nameCompleteFilter", required = false) String nameCompleteFilter
    ) {
        return userService
            .countWithSpecicatedAuthorities(auths, nameCompleteFilter)
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(usuarioProfesorFullService
                .getAllUsuariosProfesores(pageable, auths, nameCompleteFilter).collectList()
                .flatMapMany(usuarioProfesorFullDTOS -> {
                    List<UsuarioProfesorFullDTO> usuarioProfesorFullDTOListTemp = utilsMethods.cleanListUsuariosProfesores(usuarioProfesorFullDTOS);
                    return Flux.fromIterable(usuarioProfesorFullDTOListTemp);
                })));
    }

    @GetMapping(value = {
        "/usuario-profesor-full/all/by-programaga-codigo-snies",
        "/open/usuario-profesor-full/all/by-programaga-codigo-snies",
    })
    public Mono<ResponseEntity<List<UsuarioProfesorFullDTO>>> getAllUsuarioFullProfesor(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(value = "codigo_snies", required = false) Long codigoSnies
    ) {
        return usuarioProfesorFullService
            .countProfesorByProgramaCodigoSnies(codigoSnies)
            .zipWith(usuarioProfesorFullService.getAllUsuariosProfesorByProgramaCodigoSnies(pageable, codigoSnies).collectList())
            .map(countWithEntities -> {
                List<UsuarioProfesorFullDTO> usuarioProfesoresList = utilsMethods.cleanListUsuariosProfesores(countWithEntities.getT2());
                return ResponseEntity.ok()
                    .headers(PaginationUtil.generatePaginationHttpHeaders(
                        UriComponentsBuilder.fromHttpRequest(request),
                        new PageImpl<>(usuarioProfesoresList, pageable, countWithEntities.getT1())
                    ))
                    .body(usuarioProfesoresList);
            });
    }

    @GetMapping(value = {
        "/usuario-profesor-full/all",
        "/open/usuario-profesor-full/all",
    })
    public Mono<ResponseEntity<List<UsuarioProfesorFullDTO>>> getAllUsuariosProfesor(@org.springdoc.api.annotations.ParameterObject Pageable pageable,
                                                                                     ServerHttpRequest request) {
        return usuarioProfesorFullService
            .countTotalProfesor()
            .zipWith(usuarioProfesorFullService.getAllUsuarioProfesor(pageable).collectList())
            .map(countWithEntities -> {
                List<UsuarioProfesorFullDTO> usuarioProfesoresList = utilsMethods.cleanListUsuariosProfesores(countWithEntities.getT2());
                return ResponseEntity.ok()
                    .headers(PaginationUtil.generatePaginationHttpHeaders(
                        UriComponentsBuilder.fromHttpRequest(request),
                        new PageImpl<>(usuarioProfesoresList, pageable, countWithEntities.getT1())
                    ))
                    .body(usuarioProfesoresList);
            });

    }

    @GetMapping(value = {
        "/usuario-profesor-full/by-user-login",
        "/open/usuario-profesor-full/by-user-login"
    })
    public Mono<ResponseEntity<UsuarioProfesorFullDTO>> getUsuarioProfesorByUserLogin(@RequestParam(value = "userLogin", required = false) String userLogin) {
        if (userLogin == null || userLogin.isBlank()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return usuarioProfesorFullService
            .getUsuarioProfesorByUserLogin(userLogin)
            .map(usuarioProfesorFullDTO ->
                ResponseEntity.ok()
                    .body(usuarioProfesorFullDTO));
    }
}
