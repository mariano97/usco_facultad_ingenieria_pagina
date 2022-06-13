package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.RedesPrograma} entity.
 */
public class RedesProgramaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String urlRedSocial;

    private ProgramaDTO programa;

    private TablaElementoCatalogoDTO tablaElementoCatalogo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlRedSocial() {
        return urlRedSocial;
    }

    public void setUrlRedSocial(String urlRedSocial) {
        this.urlRedSocial = urlRedSocial;
    }

    public ProgramaDTO getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaDTO programa) {
        this.programa = programa;
    }

    public TablaElementoCatalogoDTO getTablaElementoCatalogo() {
        return tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogoDTO tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RedesProgramaDTO)) {
            return false;
        }

        RedesProgramaDTO redesProgramaDTO = (RedesProgramaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, redesProgramaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RedesProgramaDTO{" +
            "id=" + getId() +
            ", urlRedSocial='" + getUrlRedSocial() + "'" +
            ", programa=" + getPrograma() +
            ", tablaElementoCatalogo=" + getTablaElementoCatalogo() +
            "}";
    }
}
