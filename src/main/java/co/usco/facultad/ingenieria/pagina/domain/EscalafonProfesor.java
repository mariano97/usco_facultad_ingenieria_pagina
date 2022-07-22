package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EscalafonProfesor.
 */
@Table("escalafon_profesor")
public class EscalafonProfesor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("puntucacion_promedio")
    private Double puntucacionPromedio;

    @NotNull(message = "must not be null")
    @Column("fecha")
    private Instant fecha;

    @Transient
    @JsonIgnoreProperties(value = { "tablaElementoCatalogo", "facultad", "programas", "cursoMaterias" }, allowSetters = true)
    private Profesor profesor;

    @Column("profesor_id")
    private Long profesorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EscalafonProfesor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPuntucacionPromedio() {
        return this.puntucacionPromedio;
    }

    public EscalafonProfesor puntucacionPromedio(Double puntucacionPromedio) {
        this.setPuntucacionPromedio(puntucacionPromedio);
        return this;
    }

    public void setPuntucacionPromedio(Double puntucacionPromedio) {
        this.puntucacionPromedio = puntucacionPromedio;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public EscalafonProfesor fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Profesor getProfesor() {
        return this.profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        this.profesorId = profesor != null ? profesor.getId() : null;
    }

    public EscalafonProfesor profesor(Profesor profesor) {
        this.setProfesor(profesor);
        return this;
    }

    public Long getProfesorId() {
        return this.profesorId;
    }

    public void setProfesorId(Long profesor) {
        this.profesorId = profesor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EscalafonProfesor)) {
            return false;
        }
        return id != null && id.equals(((EscalafonProfesor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EscalafonProfesor{" +
            "id=" + getId() +
            ", puntucacionPromedio=" + getPuntucacionPromedio() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
