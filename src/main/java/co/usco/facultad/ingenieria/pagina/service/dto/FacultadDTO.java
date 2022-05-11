package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Facultad} entity.
 */
public class FacultadDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacultadDTO)) {
            return false;
        }

        FacultadDTO facultadDTO = (FacultadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facultadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacultadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
