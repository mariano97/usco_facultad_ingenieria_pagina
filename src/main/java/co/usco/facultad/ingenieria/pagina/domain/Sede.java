package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Sede.
 */
@Table("sede")
public class Sede implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("latitud")
    private Double latitud;

    @NotNull(message = "must not be null")
    @Column("longitud")
    private Double longitud;

    @NotNull(message = "must not be null")
    @Column("direccion")
    private String direccion;

    @NotNull(message = "must not be null")
    @Column("estado")
    private Boolean estado;

    @Size(max = 7)
    @Column("telefono_fijo")
    private String telefonoFijo;

    @Size(max = 10)
    @Column("telefono_celular")
    private String telefonoCelular;

    @NotNull(message = "must not be null")
    @Column("correo_electronico")
    private String correoElectronico;

    @NotNull(message = "must not be null")
    @Column("codigo_indicativo")
    private String codigoIndicativo;

    @Transient
    @JsonIgnoreProperties(
        value = { "nivelFormacion", "tipoFormacion", "facultad", "sedes", "profesors", "cursoMaterias" },
        allowSetters = true
    )
    private Set<Programa> programas = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "estados" }, allowSetters = true)
    private Ciudad ciudad;

    @Column("ciudad_id")
    private Long ciudadId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sede id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Sede nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getLatitud() {
        return this.latitud;
    }

    public Sede latitud(Double latitud) {
        this.setLatitud(latitud);
        return this;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return this.longitud;
    }

    public Sede longitud(Double longitud) {
        this.setLongitud(longitud);
        return this;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Sede direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEstado() {
        return this.estado;
    }

    public Sede estado(Boolean estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTelefonoFijo() {
        return this.telefonoFijo;
    }

    public Sede telefonoFijo(String telefonoFijo) {
        this.setTelefonoFijo(telefonoFijo);
        return this;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoCelular() {
        return this.telefonoCelular;
    }

    public Sede telefonoCelular(String telefonoCelular) {
        this.setTelefonoCelular(telefonoCelular);
        return this;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getCorreoElectronico() {
        return this.correoElectronico;
    }

    public Sede correoElectronico(String correoElectronico) {
        this.setCorreoElectronico(correoElectronico);
        return this;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCodigoIndicativo() {
        return this.codigoIndicativo;
    }

    public Sede codigoIndicativo(String codigoIndicativo) {
        this.setCodigoIndicativo(codigoIndicativo);
        return this;
    }

    public void setCodigoIndicativo(String codigoIndicativo) {
        this.codigoIndicativo = codigoIndicativo;
    }

    public Set<Programa> getProgramas() {
        return this.programas;
    }

    public void setProgramas(Set<Programa> programas) {
        if (this.programas != null) {
            this.programas.forEach(i -> i.removeSede(this));
        }
        if (programas != null) {
            programas.forEach(i -> i.addSede(this));
        }
        this.programas = programas;
    }

    public Sede programas(Set<Programa> programas) {
        this.setProgramas(programas);
        return this;
    }

    public Sede addPrograma(Programa programa) {
        this.programas.add(programa);
        programa.getSedes().add(this);
        return this;
    }

    public Sede removePrograma(Programa programa) {
        this.programas.remove(programa);
        programa.getSedes().remove(this);
        return this;
    }

    public Ciudad getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
        this.ciudadId = ciudad != null ? ciudad.getId() : null;
    }

    public Sede ciudad(Ciudad ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public Long getCiudadId() {
        return this.ciudadId;
    }

    public void setCiudadId(Long ciudad) {
        this.ciudadId = ciudad;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sede)) {
            return false;
        }
        return id != null && id.equals(((Sede) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sede{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            ", direccion='" + getDireccion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", telefonoFijo='" + getTelefonoFijo() + "'" +
            ", telefonoCelular='" + getTelefonoCelular() + "'" +
            ", correoElectronico='" + getCorreoElectronico() + "'" +
            ", codigoIndicativo='" + getCodigoIndicativo() + "'" +
            "}";
    }
}
