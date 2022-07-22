package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor} entity.
 */
public class EscalafonProfesorDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Double puntucacionPromedio;

    @NotNull(message = "must not be null")
    private Instant fecha;

    private ProfesorDTO profesor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPuntucacionPromedio() {
        return puntucacionPromedio;
    }

    public void setPuntucacionPromedio(Double puntucacionPromedio) {
        this.puntucacionPromedio = puntucacionPromedio;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
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
        if (!(o instanceof EscalafonProfesorDTO)) {
            return false;
        }

        EscalafonProfesorDTO escalafonProfesorDTO = (EscalafonProfesorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, escalafonProfesorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EscalafonProfesorDTO{" +
            "id=" + getId() +
            ", puntucacionPromedio=" + getPuntucacionPromedio() +
            ", fecha='" + getFecha() + "'" +
            ", profesor=" + getProfesor() +
            "}";
    }
}
