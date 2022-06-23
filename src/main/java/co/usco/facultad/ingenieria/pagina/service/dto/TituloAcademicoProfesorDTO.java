package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor} entity.
 */
public class TituloAcademicoProfesorDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombreTitulo;

    @NotNull(message = "must not be null")
    private String nombreUniversidadTitulo;

    @NotNull(message = "must not be null")
    private Instant yearTitulo;

    private TablaElementoCatalogoDTO tablaElementoCatalogo;

    private ProfesorDTO profesor;

    private PaisesDTO paises;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTitulo() {
        return nombreTitulo;
    }

    public void setNombreTitulo(String nombreTitulo) {
        this.nombreTitulo = nombreTitulo;
    }

    public String getNombreUniversidadTitulo() {
        return nombreUniversidadTitulo;
    }

    public void setNombreUniversidadTitulo(String nombreUniversidadTitulo) {
        this.nombreUniversidadTitulo = nombreUniversidadTitulo;
    }

    public Instant getYearTitulo() {
        return yearTitulo;
    }

    public void setYearTitulo(Instant yearTitulo) {
        this.yearTitulo = yearTitulo;
    }

    public TablaElementoCatalogoDTO getTablaElementoCatalogo() {
        return tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogoDTO tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
    }

    public ProfesorDTO getProfesor() {
        return profesor;
    }

    public void setProfesor(ProfesorDTO profesor) {
        this.profesor = profesor;
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
        if (!(o instanceof TituloAcademicoProfesorDTO)) {
            return false;
        }

        TituloAcademicoProfesorDTO tituloAcademicoProfesorDTO = (TituloAcademicoProfesorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tituloAcademicoProfesorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TituloAcademicoProfesorDTO{" +
            "id=" + getId() +
            ", nombreTitulo='" + getNombreTitulo() + "'" +
            ", nombreUniversidadTitulo='" + getNombreUniversidadTitulo() + "'" +
            ", yearTitulo='" + getYearTitulo() + "'" +
            ", tablaElementoCatalogo=" + getTablaElementoCatalogo() +
            ", profesor=" + getProfesor() +
            ", paises=" + getPaises() +
            "}";
    }
}
