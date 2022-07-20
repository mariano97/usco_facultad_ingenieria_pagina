package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Semillero.
 */
@Table("semillero")
public class Semillero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("informacion_general")
    private String informacionGeneral;

    @Column("url_foto")
    private String urlFoto;

    @Transient
    private Facultad facultad;

    @Transient
    @JsonIgnoreProperties(value = { "tablaElementoCatalogo", "facultad", "programas", "cursoMaterias" }, allowSetters = true)
    private Profesor profesor;

    @Column("facultad_id")
    private Long facultadId;

    @Column("profesor_id")
    private Long profesorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Semillero id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Semillero nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInformacionGeneral() {
        return this.informacionGeneral;
    }

    public Semillero informacionGeneral(String informacionGeneral) {
        this.setInformacionGeneral(informacionGeneral);
        return this;
    }

    public void setInformacionGeneral(String informacionGeneral) {
        this.informacionGeneral = informacionGeneral;
    }

    public String getUrlFoto() {
        return this.urlFoto;
    }

    public Semillero urlFoto(String urlFoto) {
        this.setUrlFoto(urlFoto);
        return this;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Facultad getFacultad() {
        return this.facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
        this.facultadId = facultad != null ? facultad.getId() : null;
    }

    public Semillero facultad(Facultad facultad) {
        this.setFacultad(facultad);
        return this;
    }

    public Profesor getProfesor() {
        return this.profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        this.profesorId = profesor != null ? profesor.getId() : null;
    }

    public Semillero profesor(Profesor profesor) {
        this.setProfesor(profesor);
        return this;
    }

    public Long getFacultadId() {
        return this.facultadId;
    }

    public void setFacultadId(Long facultad) {
        this.facultadId = facultad;
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
        if (!(o instanceof Semillero)) {
            return false;
        }
        return id != null && id.equals(((Semillero) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semillero{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", informacionGeneral='" + getInformacionGeneral() + "'" +
            ", urlFoto='" + getUrlFoto() + "'" +
            "}";
    }
}
