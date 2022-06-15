package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Paises.
 */
@Table("paises")
public class Paises implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre_pais")
    private String nombrePais;

    @NotNull(message = "must not be null")
    @Column("codigo_pais")
    private String codigoPais;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paises id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePais() {
        return this.nombrePais;
    }

    public Paises nombrePais(String nombrePais) {
        this.setNombrePais(nombrePais);
        return this;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getCodigoPais() {
        return this.codigoPais;
    }

    public Paises codigoPais(String codigoPais) {
        this.setCodigoPais(codigoPais);
        return this;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paises)) {
            return false;
        }
        return id != null && id.equals(((Paises) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paises{" +
            "id=" + getId() +
            ", nombrePais='" + getNombrePais() + "'" +
            ", codigoPais='" + getCodigoPais() + "'" +
            "}";
    }
}
