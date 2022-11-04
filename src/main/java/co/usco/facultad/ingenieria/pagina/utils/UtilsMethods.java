package co.usco.facultad.ingenieria.pagina.utils;

import co.usco.facultad.ingenieria.pagina.service.dto.UsuarioProfesorFullDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UtilsMethods {

    private boolean existeUsuarioProfesorIntoList(List<UsuarioProfesorFullDTO> usuariosProfesoresFullDTOList, UsuarioProfesorFullDTO usuarioProfesorFullDTO) {
        return usuariosProfesoresFullDTOList.stream()
            .filter(usuario -> usuario.getAdminUserDTO().getId().longValue() == usuarioProfesorFullDTO.getAdminUserDTO().getId().longValue())
            .findFirst()
            .isPresent();
    }

    private boolean addUsuarioProfesorToList(List<UsuarioProfesorFullDTO> usuariosProfesoresFullDTOList, UsuarioProfesorFullDTO usuarioProfesorFullDTO) {
        boolean resultado = false;
        if (!existeUsuarioProfesorIntoList(usuariosProfesoresFullDTOList, usuarioProfesorFullDTO)) {
            usuariosProfesoresFullDTOList.add(usuarioProfesorFullDTO);
            resultado = true;
            /* System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("Dentro de metodo addUsuarioProfesorToList2");
            System.out.println(usuariosProfesoresFullDTOList.toString());
            System.out.println(usuariosProfesoresFullDTOList.size());
            System.out.println("");
            System.out.println("");
            System.out.println(""); */
        }
        return resultado;
    }

    public List<UsuarioProfesorFullDTO> cleanListUsuariosProfesores(List<UsuarioProfesorFullDTO> usuarioProfesorFullDTOList) {
        Set<UsuarioProfesorFullDTO> setUsuarioProfesoresTemp = new HashSet<>(usuarioProfesorFullDTOList);
        usuarioProfesorFullDTOList.clear();
        usuarioProfesorFullDTOList.addAll(setUsuarioProfesoresTemp);
        return usuarioProfesorFullDTOList;
    }

    public Flux<UsuarioProfesorFullDTO> cleanFluxUsuariosProfesores(Flux<UsuarioProfesorFullDTO> usuarioProfesorFlux) {
        return usuarioProfesorFlux.collectList().flatMapMany(usuarioProfesorFullDTOS -> {
            List<UsuarioProfesorFullDTO> usuarioProfesorFullDTOList = cleanListUsuariosProfesores(usuarioProfesorFullDTOS);
            return Flux.fromIterable(usuarioProfesorFullDTOList);
        });

    }

}
