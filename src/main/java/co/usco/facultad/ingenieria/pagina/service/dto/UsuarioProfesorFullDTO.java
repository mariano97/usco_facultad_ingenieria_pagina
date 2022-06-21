package co.usco.facultad.ingenieria.pagina.service.dto;

public class UsuarioProfesorFullDTO {

    private AdminUserDTO adminUserDTO;
    private ProfesorDTO profesorDTO;

    public AdminUserDTO getAdminUserDTO() {
        return adminUserDTO;
    }

    public void setAdminUserDTO(AdminUserDTO adminUserDTO) {
        this.adminUserDTO = adminUserDTO;
    }

    public ProfesorDTO getProfesorDTO() {
        return profesorDTO;
    }

    public void setProfesorDTO(ProfesorDTO profesorDTO) {
        this.profesorDTO = profesorDTO;
    }

    @Override
    public String toString() {
        return "UsuarioProfesorFullDTO{" +
            "adminUserDTO=" + adminUserDTO +
            ", profesorDTO=" + profesorDTO +
            '}';
    }
}
