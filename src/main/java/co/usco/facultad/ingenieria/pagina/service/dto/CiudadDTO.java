package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Ciudad} entity.
 */
public class CiudadDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private String codigo;

    private BigDecimal latitud;

    private BigDecimal longitud;

    private EstadosDTO estados;

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

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public EstadosDTO getEstados() {
        return estados;
    }

    public void setEstados(EstadosDTO estados) {
        this.estados = estados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CiudadDTO)) {
            return false;
        }

        CiudadDTO ciudadDTO = (CiudadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ciudadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CiudadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            ", estados=" + getEstados() +
            "}";
    }
}
