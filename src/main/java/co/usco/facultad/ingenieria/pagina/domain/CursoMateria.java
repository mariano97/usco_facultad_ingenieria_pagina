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
 * A CursoMateria.
 */
@Table("curso_materia")
public class CursoMateria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("numero_creditos")
    private Long numeroCreditos;

    @NotNull(message = "must not be null")
    @Column("semestre_impartida")
    private Long semestreImpartida;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo nivelAcademico;

    @Transient
    @JsonIgnoreProperties(value = { "tablaElementoCatalogo", "facultad", "programas", "cursoMaterias" }, allowSetters = true)
    private Set<Profesor> profesors = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "nivelFormacion", "tipoFormacion", "facultad", "sedes", "profesors", "cursoMaterias" },
        allowSetters = true
    )
    private Set<Programa> programas = new HashSet<>();

    @Column("nivel_academico_id")
    private Long nivelAcademicoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CursoMateria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public CursoMateria nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getNumeroCreditos() {
        return this.numeroCreditos;
    }

    public CursoMateria numeroCreditos(Long numeroCreditos) {
        this.setNumeroCreditos(numeroCreditos);
        return this;
    }

    public void setNumeroCreditos(Long numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    public Long getSemestreImpartida() {
        return this.semestreImpartida;
    }

    public CursoMateria semestreImpartida(Long semestreImpartida) {
        this.setSemestreImpartida(semestreImpartida);
        return this;
    }

    public void setSemestreImpartida(Long semestreImpartida) {
        this.semestreImpartida = semestreImpartida;
    }

    public TablaElementoCatalogo getNivelAcademico() {
        return this.nivelAcademico;
    }

    public void setNivelAcademico(TablaElementoCatalogo tablaElementoCatalogo) {
        this.nivelAcademico = tablaElementoCatalogo;
        this.nivelAcademicoId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public CursoMateria nivelAcademico(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setNivelAcademico(tablaElementoCatalogo);
        return this;
    }

    public Set<Profesor> getProfesors() {
        return this.profesors;
    }

    public void setProfesors(Set<Profesor> profesors) {
        if (this.profesors != null) {
            this.profesors.forEach(i -> i.removeCursoMateria(this));
        }
        if (profesors != null) {
            profesors.forEach(i -> i.addCursoMateria(this));
        }
        this.profesors = profesors;
    }

    public CursoMateria profesors(Set<Profesor> profesors) {
        this.setProfesors(profesors);
        return this;
    }

    public CursoMateria addProfesor(Profesor profesor) {
        this.profesors.add(profesor);
        profesor.getCursoMaterias().add(this);
        return this;
    }

    public CursoMateria removeProfesor(Profesor profesor) {
        this.profesors.remove(profesor);
        profesor.getCursoMaterias().remove(this);
        return this;
    }

    public Set<Programa> getProgramas() {
        return this.programas;
    }

    public void setProgramas(Set<Programa> programas) {
        this.programas = programas;
    }

    public CursoMateria programas(Set<Programa> programas) {
        this.setProgramas(programas);
        return this;
    }

    public CursoMateria addPrograma(Programa programa) {
        this.programas.add(programa);
        programa.getCursoMaterias().add(this);
        return this;
    }

    public CursoMateria removePrograma(Programa programa) {
        this.programas.remove(programa);
        programa.getCursoMaterias().remove(this);
        return this;
    }

    public Long getNivelAcademicoId() {
        return this.nivelAcademicoId;
    }

    public void setNivelAcademicoId(Long tablaElementoCatalogo) {
        this.nivelAcademicoId = tablaElementoCatalogo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CursoMateria)) {
            return false;
        }
        return id != null && id.equals(((CursoMateria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursoMateria{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", numeroCreditos=" + getNumeroCreditos() +
            ", semestreImpartida=" + getSemestreImpartida() +
            "}";
    }
}
