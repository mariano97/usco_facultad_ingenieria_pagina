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

    @Size(max = 7)
    private String telefonoFijo;

    @Size(max = 10)
    private String telefonoCelular;

    @NotNull(message = "must not be null")
    private String correoElectronico;

    @NotNull(message = "must not be null")
    private String codigoIndicativo;

    private CiudadDTO ciudad;

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

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCodigoIndicativo() {
        return codigoIndicativo;
    }

    public void setCodigoIndicativo(String codigoIndicativo) {
        this.codigoIndicativo = codigoIndicativo;
    }

    public CiudadDTO getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadDTO ciudad) {
        this.ciudad = ciudad;
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
            ", telefonoFijo='" + getTelefonoFijo() + "'" +
            ", telefonoCelular='" + getTelefonoCelular() + "'" +
            ", correoElectronico='" + getCorreoElectronico() + "'" +
            ", codigoIndicativo='" + getCodigoIndicativo() + "'" +
            ", ciudad=" + getCiudad() +
            "}";
    }
}
