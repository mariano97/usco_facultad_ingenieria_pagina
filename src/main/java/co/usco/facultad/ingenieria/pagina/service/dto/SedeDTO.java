package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Sede} entity.
 */
public class SedeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private Double latitud;

    @NotNull(message = "must not be null")
    private Double longitud;

    @NotNull(message = "must not be null")
    private String direccion;

    @NotNull(message = "must not be null")
    private Boolean estado;

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

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SedeDTO)) {
            return false;
        }

        SedeDTO sedeDTO = (SedeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sedeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SedeDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            ", direccion='" + getDireccion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
