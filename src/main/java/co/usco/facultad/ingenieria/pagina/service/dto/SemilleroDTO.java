package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Semillero} entity.
 */
public class SemilleroDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private String informacionGeneral;

    private String urlFoto;

    private FacultadDTO facultad;

    private ProfesorDTO profesor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInformacionGeneral() {
        return informacionGeneral;
    }

    public void setInformacionGeneral(String informacionGeneral) {
        this.informacionGeneral = informacionGeneral;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public FacultadDTO getFacultad() {
        return facultad;
    }

    public void setFacultad(FacultadDTO facultad) {
        this.facultad = facultad;
    }

    public ProfesorDTO getProfesor() {
        return profesor;
    }

    public void setProfesor(ProfesorDTO profesor) {
        this.profesor = profesor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SemilleroDTO)) {
            return false;
        }

        SemilleroDTO semilleroDTO = (SemilleroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, semilleroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemilleroDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", informacionGeneral='" + getInformacionGeneral() + "'" +
            ", urlFoto='" + getUrlFoto() + "'" +
            ", facultad=" + getFacultad() +
            ", profesor=" + getProfesor() +
            "}";
    }
}
