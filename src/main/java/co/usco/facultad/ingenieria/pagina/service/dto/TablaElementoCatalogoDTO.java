package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo} entity.
 */
public class TablaElementoCatalogoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    private String abreviatura;

    @NotNull(message = "must not be null")
    private Boolean estado;

    private TablaTiposCatalogoDTO tablaTiposCatalogo;

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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public TablaTiposCatalogoDTO getTablaTiposCatalogo() {
        return tablaTiposCatalogo;
    }

    public void setTablaTiposCatalogo(TablaTiposCatalogoDTO tablaTiposCatalogo) {
        this.tablaTiposCatalogo = tablaTiposCatalogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TablaElementoCatalogoDTO)) {
            return false;
        }

        TablaElementoCatalogoDTO tablaElementoCatalogoDTO = (TablaElementoCatalogoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tablaElementoCatalogoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TablaElementoCatalogoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", abreviatura='" + getAbreviatura() + "'" +
            ", estado='" + getEstado() + "'" +
            ", tablaTiposCatalogo=" + getTablaTiposCatalogo() +
            "}";
    }
}
