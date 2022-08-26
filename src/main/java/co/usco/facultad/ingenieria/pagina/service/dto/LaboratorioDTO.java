package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Laboratorio} entity.
 */
public class LaboratorioDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private String informacionGeneral;

    private String urlFoto;

    private Double latitud;

    private Double longitud;

    @NotNull(message = "must not be null")
    private String correoContacto;

    private String direccion;

    private TablaElementoCatalogoDTO tipoLaboratorio;

    private FacultadDTO facultad;

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

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TablaElementoCatalogoDTO getTipoLaboratorio() {
        return tipoLaboratorio;
    }

    public void setTipoLaboratorio(TablaElementoCatalogoDTO tipoLaboratorio) {
        this.tipoLaboratorio = tipoLaboratorio;
    }

    public FacultadDTO getFacultad() {
        return facultad;
    }

    public void setFacultad(FacultadDTO facultad) {
        this.facultad = facultad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LaboratorioDTO)) {
            return false;
        }

        LaboratorioDTO laboratorioDTO = (LaboratorioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, laboratorioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LaboratorioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", informacionGeneral='" + getInformacionGeneral() + "'" +
            ", urlFoto='" + getUrlFoto() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            ", correoContacto='" + getCorreoContacto() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", tipoLaboratorio=" + getTipoLaboratorio() +
            ", facultad=" + getFacultad() +
            "}";
    }
}
