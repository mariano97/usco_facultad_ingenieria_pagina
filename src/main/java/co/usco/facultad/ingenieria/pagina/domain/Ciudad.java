package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Ciudad.
 */
@Table("ciudad")
public class Ciudad implements Serializable {

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

    @Column("latitud")
    private BigDecimal latitud;

    @Column("longitud")
    private BigDecimal longitud;

    @Transient
    @JsonIgnoreProperties(value = { "paises" }, allowSetters = true)
    private Estados estados;

    @Column("estados_id")
    private Long estadosId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ciudad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Ciudad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Ciudad codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getLatitud() {
        return this.latitud;
    }

    public Ciudad latitud(BigDecimal latitud) {
        this.setLatitud(latitud);
        return this;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud != null ? latitud.stripTrailingZeros() : null;
    }

    public BigDecimal getLongitud() {
        return this.longitud;
    }

    public Ciudad longitud(BigDecimal longitud) {
        this.setLongitud(longitud);
        return this;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud != null ? longitud.stripTrailingZeros() : null;
    }

    public Estados getEstados() {
        return this.estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
        this.estadosId = estados != null ? estados.getId() : null;
    }

    public Ciudad estados(Estados estados) {
        this.setEstados(estados);
        return this;
    }

    public Long getEstadosId() {
        return this.estadosId;
    }

    public void setEstadosId(Long estados) {
        this.estadosId = estados;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ciudad)) {
            return false;
        }
        return id != null && id.equals(((Ciudad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ciudad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            "}";
    }
}
