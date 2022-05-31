package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Profesor} entity.
 */
public class ProfesorDTO implements Serializable {

    private Long id;

    private String segundoNombre;

    private String segundoApellido;

    private String emailAlternativo;

    @NotNull(message = "must not be null")
    private Boolean activo;

    @NotNull(message = "must not be null")
    private String perfil;

    private String telefonoCelular;

    private String oficina;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
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
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", segundoApellido='" + getSegundoApellido() + "'" +
            ", emailAlternativo='" + getEmailAlternativo() + "'" +
            ", activo='" + getActivo() + "'" +
            ", perfil='" + getPerfil() + "'" +
            ", telefonoCelular='" + getTelefonoCelular() + "'" +
            ", oficina='" + getOficina() + "'" +
            "}";
    }
}
