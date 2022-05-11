package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TablaElementoCatalogo.
 */
@Table("tabla_elemento_catalogo")
public class TablaElementoCatalogo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @Column("abreviatura")
    private String abreviatura;

    @NotNull(message = "must not be null")
    @Column("estado")
    private Boolean estado;

    @Transient
    private TablaTiposCatalogo tablaTiposCatalogo;

    @Column("tabla_tipos_catalogo_id")
    private Long tablaTiposCatalogoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TablaElementoCatalogo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TablaElementoCatalogo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return this.abreviatura;
    }

    public TablaElementoCatalogo abreviatura(String abreviatura) {
        this.setAbreviatura(abreviatura);
        return this;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getEstado() {
        return this.estado;
    }

    public TablaElementoCatalogo estado(Boolean estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public TablaTiposCatalogo getTablaTiposCatalogo() {
        return this.tablaTiposCatalogo;
    }

    public void setTablaTiposCatalogo(TablaTiposCatalogo tablaTiposCatalogo) {
        this.tablaTiposCatalogo = tablaTiposCatalogo;
        this.tablaTiposCatalogoId = tablaTiposCatalogo != null ? tablaTiposCatalogo.getId() : null;
    }

    public TablaElementoCatalogo tablaTiposCatalogo(TablaTiposCatalogo tablaTiposCatalogo) {
        this.setTablaTiposCatalogo(tablaTiposCatalogo);
        return this;
    }

    public Long getTablaTiposCatalogoId() {
        return this.tablaTiposCatalogoId;
    }

    public void setTablaTiposCatalogoId(Long tablaTiposCatalogo) {
        this.tablaTiposCatalogoId = tablaTiposCatalogo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TablaElementoCatalogo)) {
            return false;
        }
        return id != null && id.equals(((TablaElementoCatalogo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TablaElementoCatalogo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", abreviatura='" + getAbreviatura() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
