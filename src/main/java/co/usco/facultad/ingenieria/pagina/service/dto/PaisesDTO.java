package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Paises} entity.
 */
public class PaisesDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombrePais;

    @NotNull(message = "must not be null")
    private String codigoPais;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaisesDTO)) {
            return false;
        }

        PaisesDTO paisesDTO = (PaisesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paisesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaisesDTO{" +
            "id=" + getId() +
            ", nombrePais='" + getNombrePais() + "'" +
            ", codigoPais='" + getCodigoPais() + "'" +
            "}";
    }
}
