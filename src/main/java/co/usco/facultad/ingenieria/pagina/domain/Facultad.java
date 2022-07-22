package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Facultad.
 */
@Table("facultad")
public class Facultad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("mision")
    private String mision;

    @NotNull(message = "must not be null")
    @Column("vision")
    private String vision;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facultad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Facultad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMision() {
        return this.mision;
    }

    public Facultad mision(String mision) {
        this.setMision(mision);
        return this;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }

    public String getVision() {
        return this.vision;
    }

    public Facultad vision(String vision) {
        this.setVision(vision);
        return this;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facultad)) {
            return false;
        }
        return id != null && id.equals(((Facultad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facultad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", mision='" + getMision() + "'" +
            ", vision='" + getVision() + "'" +
            "}";
    }
}
