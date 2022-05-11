package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo} entity.
 */
public class TablaTiposCatalogoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

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
        if (!(o instanceof TablaTiposCatalogoDTO)) {
            return false;
        }

        TablaTiposCatalogoDTO tablaTiposCatalogoDTO = (TablaTiposCatalogoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tablaTiposCatalogoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TablaTiposCatalogoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
