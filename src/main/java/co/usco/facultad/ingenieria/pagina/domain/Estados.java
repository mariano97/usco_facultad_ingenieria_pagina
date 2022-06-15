package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Estados.
 */
@Table("estados")
public class Estados implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("codigo")
    private String codigo;

    @Transient
    private Paises paises;

    @Column("paises_id")
    private Long paisesId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estados id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Estados nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Estados codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Paises getPaises() {
        return this.paises;
    }

    public void setPaises(Paises paises) {
        this.paises = paises;
        this.paisesId = paises != null ? paises.getId() : null;
    }

    public Estados paises(Paises paises) {
        this.setPaises(paises);
        return this;
    }

    public Long getPaisesId() {
        return this.paisesId;
    }

    public void setPaisesId(Long paises) {
        this.paisesId = paises;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estados)) {
            return false;
        }
        return id != null && id.equals(((Estados) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estados{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
