package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RedesPrograma.
 */
@Table("redes_programa")
public class RedesPrograma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("url_red_social")
    private String urlRedSocial;

    @Transient
    @JsonIgnoreProperties(
        value = { "nivelFormacion", "tipoFormacion", "facultad", "sedes", "profesors", "cursoMaterias" },
        allowSetters = true
    )
    private Programa programa;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo tablaElementoCatalogo;

    @Column("programa_id")
    private Long programaId;

    @Column("tabla_elemento_catalogo_id")
    private Long tablaElementoCatalogoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RedesPrograma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlRedSocial() {
        return this.urlRedSocial;
    }

    public RedesPrograma urlRedSocial(String urlRedSocial) {
        this.setUrlRedSocial(urlRedSocial);
        return this;
    }

    public void setUrlRedSocial(String urlRedSocial) {
        this.urlRedSocial = urlRedSocial;
    }

    public Programa getPrograma() {
        return this.programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
        this.programaId = programa != null ? programa.getId() : null;
    }

    public RedesPrograma programa(Programa programa) {
        this.setPrograma(programa);
        return this;
    }

    public TablaElementoCatalogo getTablaElementoCatalogo() {
        return this.tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
        this.tablaElementoCatalogoId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public RedesPrograma tablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setTablaElementoCatalogo(tablaElementoCatalogo);
        return this;
    }

    public Long getProgramaId() {
        return this.programaId;
    }

    public void setProgramaId(Long programa) {
        this.programaId = programa;
    }

    public Long getTablaElementoCatalogoId() {
        return this.tablaElementoCatalogoId;
    }

    public void setTablaElementoCatalogoId(Long tablaElementoCatalogo) {
        this.tablaElementoCatalogoId = tablaElementoCatalogo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RedesPrograma)) {
            return false;
        }
        return id != null && id.equals(((RedesPrograma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RedesPrograma{" +
            "id=" + getId() +
            ", urlRedSocial='" + getUrlRedSocial() + "'" +
            "}";
    }
}
