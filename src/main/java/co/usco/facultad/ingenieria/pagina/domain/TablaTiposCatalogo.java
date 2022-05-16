package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TablaTiposCatalogo.
 */
@Table("tabla_tipos_catalogo")
public class TablaTiposCatalogo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("estado")
    private Boolean estado;

    @NotNull(message = "must not be null")
    @Column("key_identificador")
    private String keyIdentificador;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TablaTiposCatalogo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TablaTiposCatalogo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return this.estado;
    }

    public TablaTiposCatalogo estado(Boolean estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getKeyIdentificador() {
        return this.keyIdentificador;
    }

    public TablaTiposCatalogo keyIdentificador(String keyIdentificador) {
        this.setKeyIdentificador(keyIdentificador);
        return this;
    }

    public void setKeyIdentificador(String keyIdentificador) {
        this.keyIdentificador = keyIdentificador;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TablaTiposCatalogo)) {
            return false;
        }
        return id != null && id.equals(((TablaTiposCatalogo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TablaTiposCatalogo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", estado='" + getEstado() + "'" +
            ", keyIdentificador='" + getKeyIdentificador() + "'" +
            "}";
    }
}
