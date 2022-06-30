package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Profesor} entity.
 */
public class ProfesorDTO implements Serializable {

    private Long id;

    private String emailAlternativo;

    @NotNull(message = "must not be null")
    private Boolean activo;

    @NotNull(message = "must not be null")
    private String perfil;

    private String telefonoCelular;

    private String oficina;

    @NotNull(message = "must not be null")
    private Long userId;

    private String urlFotoProfesor;

    private TablaElementoCatalogoDTO tablaElementoCatalogo;

    private FacultadDTO facultad;

    private Set<CursoMateriaDTO> cursoMaterias = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAlternativo() {
        return emailAlternativo;
    }

    public void setEmailAlternativo(String emailAlternativo) {
        this.emailAlternativo = emailAlternativo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUrlFotoProfesor() {
        return urlFotoProfesor;
    }

    public void setUrlFotoProfesor(String urlFotoProfesor) {
        this.urlFotoProfesor = urlFotoProfesor;
    }

    public TablaElementoCatalogoDTO getTablaElementoCatalogo() {
        return tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogoDTO tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
    }

    public FacultadDTO getFacultad() {
        return facultad;
    }

    public void setFacultad(FacultadDTO facultad) {
        this.facultad = facultad;
    }

    public Set<CursoMateriaDTO> getCursoMaterias() {
        return cursoMaterias;
    }

    public void setCursoMaterias(Set<CursoMateriaDTO> cursoMaterias) {
        this.cursoMaterias = cursoMaterias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfesorDTO)) {
            return false;
        }

        ProfesorDTO profesorDTO = (ProfesorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profesorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfesorDTO{" +
            "id=" + getId() +
            ", emailAlternativo='" + getEmailAlternativo() + "'" +
            ", activo='" + getActivo() + "'" +
            ", perfil='" + getPerfil() + "'" +
            ", telefonoCelular='" + getTelefonoCelular() + "'" +
            ", oficina='" + getOficina() + "'" +
            ", userId=" + getUserId() +
            ", urlFotoProfesor='" + getUrlFotoProfesor() + "'" +
            ", tablaElementoCatalogo=" + getTablaElementoCatalogo() +
            ", facultad=" + getFacultad() +
            ", cursoMaterias=" + getCursoMaterias() +
            "}";
    }
}
