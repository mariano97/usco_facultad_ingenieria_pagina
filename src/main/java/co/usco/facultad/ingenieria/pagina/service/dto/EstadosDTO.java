package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Estados} entity.
 */
public class EstadosDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private String codigo;

    private PaisesDTO paises;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public PaisesDTO getPaises() {
        return paises;
    }

    public void setPaises(PaisesDTO paises) {
        this.paises = paises;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EstadosDTO)) {
            return false;
        }

        EstadosDTO estadosDTO = (EstadosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, estadosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstadosDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", paises=" + getPaises() +
            "}";
    }
}
