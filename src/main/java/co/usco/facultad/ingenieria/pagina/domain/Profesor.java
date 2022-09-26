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
 * A Profesor.
 */
@Table("profesor")
public class Profesor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

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

    @NotNull(message = "must not be null")
    @Column("user_id")
    private Long userId;

    @Column("url_foto_profesor")
    private String urlFotoProfesor;

    @Column("titulo_academico")
    private String tituloAcademico;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo tablaElementoCatalogo;

    @Transient
    private Facultad facultad;

    @Transient
    @JsonIgnoreProperties(
        value = { "nivelFormacion", "tipoFormacion", "facultad", "sedes", "profesors", "cursoMaterias" },
        allowSetters = true
    )
    private Set<Programa> programas = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "nivelAcademico", "profesors", "programas" }, allowSetters = true)
    private Set<CursoMateria> cursoMaterias = new HashSet<>();

    @Column("tabla_elemento_catalogo_id")
    private Long tablaElementoCatalogoId;

    @Column("facultad_id")
    private Long facultadId;

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

    public Long getUserId() {
        return this.userId;
    }

    public Profesor userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUrlFotoProfesor() {
        return this.urlFotoProfesor;
    }

    public Profesor urlFotoProfesor(String urlFotoProfesor) {
        this.setUrlFotoProfesor(urlFotoProfesor);
        return this;
    }

    public void setUrlFotoProfesor(String urlFotoProfesor) {
        this.urlFotoProfesor = urlFotoProfesor;
    }

    public String getTituloAcademico() {
        return this.tituloAcademico;
    }

    public Profesor tituloAcademico(String tituloAcademico) {
        this.setTituloAcademico(tituloAcademico);
        return this;
    }

    public void setTituloAcademico(String tituloAcademico) {
        this.tituloAcademico = tituloAcademico;
    }

    public TablaElementoCatalogo getTablaElementoCatalogo() {
        return this.tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
        this.tablaElementoCatalogoId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public Profesor tablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setTablaElementoCatalogo(tablaElementoCatalogo);
        return this;
    }

    public Facultad getFacultad() {
        return this.facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
        this.facultadId = facultad != null ? facultad.getId() : null;
    }

    public Profesor facultad(Facultad facultad) {
        this.setFacultad(facultad);
        return this;
    }

    public Set<Programa> getProgramas() {
        return this.programas;
    }

    public void setProgramas(Set<Programa> programas) {
        if (this.programas != null) {
            this.programas.forEach(i -> i.removeProfesor(this));
        }
        if (programas != null) {
            programas.forEach(i -> i.addProfesor(this));
        }
        this.programas = programas;
    }

    public Profesor programas(Set<Programa> programas) {
        this.setProgramas(programas);
        return this;
    }

    public Profesor addPrograma(Programa programa) {
        this.programas.add(programa);
        programa.getProfesors().add(this);
        return this;
    }

    public Profesor removePrograma(Programa programa) {
        this.programas.remove(programa);
        programa.getProfesors().remove(this);
        return this;
    }

    public Set<CursoMateria> getCursoMaterias() {
        return this.cursoMaterias;
    }

    public void setCursoMaterias(Set<CursoMateria> cursoMaterias) {
        this.cursoMaterias = cursoMaterias;
    }

    public Profesor cursoMaterias(Set<CursoMateria> cursoMaterias) {
        this.setCursoMaterias(cursoMaterias);
        return this;
    }

    public Profesor addCursoMateria(CursoMateria cursoMateria) {
        this.cursoMaterias.add(cursoMateria);
        cursoMateria.getProfesors().add(this);
        return this;
    }

    public Profesor removeCursoMateria(CursoMateria cursoMateria) {
        this.cursoMaterias.remove(cursoMateria);
        cursoMateria.getProfesors().remove(this);
        return this;
    }

    public Long getTablaElementoCatalogoId() {
        return this.tablaElementoCatalogoId;
    }

    public void setTablaElementoCatalogoId(Long tablaElementoCatalogo) {
        this.tablaElementoCatalogoId = tablaElementoCatalogo;
    }

    public Long getFacultadId() {
        return this.facultadId;
    }

    public void setFacultadId(Long facultad) {
        this.facultadId = facultad;
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
            ", emailAlternativo='" + getEmailAlternativo() + "'" +
            ", activo='" + getActivo() + "'" +
            ", perfil='" + getPerfil() + "'" +
            ", telefonoCelular='" + getTelefonoCelular() + "'" +
            ", oficina='" + getOficina() + "'" +
            ", userId=" + getUserId() +
            ", urlFotoProfesor='" + getUrlFotoProfesor() + "'" +
            ", tituloAcademico='" + getTituloAcademico() + "'" +
            "}";
    }
}
