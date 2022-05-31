package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Profesor.
 */
@Table("profesor")
public class Profesor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("segundo_nombre")
    private String segundoNombre;

    @Column("segundo_apellido")
    private String segundoApellido;

    @Column("email_alternativo")
    private String emailAlternativo;

    @NotNull(message = "must not be null")
    @Column("activo")
    private Boolean activo;

    @NotNull(message = "must not be null")
    @Column("perfil")
    private String perfil;

    @Column("telefono_celular")
    private String telefonoCelular;

    @Column("oficina")
    private String oficina;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profesor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSegundoNombre() {
        return this.segundoNombre;
    }

    public Profesor segundoNombre(String segundoNombre) {
        this.setSegundoNombre(segundoNombre);
        return this;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getSegundoApellido() {
        return this.segundoApellido;
    }

    public Profesor segundoApellido(String segundoApellido) {
        this.setSegundoApellido(segundoApellido);
        return this;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getEmailAlternativo() {
        return this.emailAlternativo;
    }

    public Profesor emailAlternativo(String emailAlternativo) {
        this.setEmailAlternativo(emailAlternativo);
        return this;
    }

    public void setEmailAlternativo(String emailAlternativo) {
        this.emailAlternativo = emailAlternativo;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Profesor activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getPerfil() {
        return this.perfil;
    }

    public Profesor perfil(String perfil) {
        this.setPerfil(perfil);
        return this;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getTelefonoCelular() {
        return this.telefonoCelular;
    }

    public Profesor telefonoCelular(String telefonoCelular) {
        this.setTelefonoCelular(telefonoCelular);
        return this;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getOficina() {
        return this.oficina;
    }

    public Profesor oficina(String oficina) {
        this.setOficina(oficina);
        return this;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profesor)) {
            return false;
        }
        return id != null && id.equals(((Profesor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profesor{" +
            "id=" + getId() +
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", segundoApellido='" + getSegundoApellido() + "'" +
            ", emailAlternativo='" + getEmailAlternativo() + "'" +
            ", activo='" + getActivo() + "'" +
            ", perfil='" + getPerfil() + "'" +
            ", telefonoCelular='" + getTelefonoCelular() + "'" +
            ", oficina='" + getOficina() + "'" +
            "}";
    }
}
