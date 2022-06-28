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
 * A TituloAcademicoProfesor.
 */
@Table("titulo_academico_profesor")
public class TituloAcademicoProfesor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre_titulo")
    private String nombreTitulo;

    @NotNull(message = "must not be null")
    @Column("nombre_universidad_titulo")
    private String nombreUniversidadTitulo;

    @NotNull(message = "must not be null")
    @Column("year_titulo")
    private Instant yearTitulo;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo tablaElementoCatalogo;

    @Transient
    @JsonIgnoreProperties(value = { "tablaElementoCatalogo", "facultad", "programas", "cursoMaterias" }, allowSetters = true)
    private Profesor profesor;

    @Transient
    private Paises paises;

    @Column("tabla_elemento_catalogo_id")
    private Long tablaElementoCatalogoId;

    @Column("profesor_id")
    private Long profesorId;

    @Column("paises_id")
    private Long paisesId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TituloAcademicoProfesor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTitulo() {
        return this.nombreTitulo;
    }

    public TituloAcademicoProfesor nombreTitulo(String nombreTitulo) {
        this.setNombreTitulo(nombreTitulo);
        return this;
    }

    public void setNombreTitulo(String nombreTitulo) {
        this.nombreTitulo = nombreTitulo;
    }

    public String getNombreUniversidadTitulo() {
        return this.nombreUniversidadTitulo;
    }

    public TituloAcademicoProfesor nombreUniversidadTitulo(String nombreUniversidadTitulo) {
        this.setNombreUniversidadTitulo(nombreUniversidadTitulo);
        return this;
    }

    public void setNombreUniversidadTitulo(String nombreUniversidadTitulo) {
        this.nombreUniversidadTitulo = nombreUniversidadTitulo;
    }

    public Instant getYearTitulo() {
        return this.yearTitulo;
    }

    public TituloAcademicoProfesor yearTitulo(Instant yearTitulo) {
        this.setYearTitulo(yearTitulo);
        return this;
    }

    public void setYearTitulo(Instant yearTitulo) {
        this.yearTitulo = yearTitulo;
    }

    public TablaElementoCatalogo getTablaElementoCatalogo() {
        return this.tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
        this.tablaElementoCatalogoId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public TituloAcademicoProfesor tablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setTablaElementoCatalogo(tablaElementoCatalogo);
        return this;
    }

    public Profesor getProfesor() {
        return this.profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        this.profesorId = profesor != null ? profesor.getId() : null;
    }

    public TituloAcademicoProfesor profesor(Profesor profesor) {
        this.setProfesor(profesor);
        return this;
    }

    public Paises getPaises() {
        return this.paises;
    }

    public void setPaises(Paises paises) {
        this.paises = paises;
        this.paisesId = paises != null ? paises.getId() : null;
    }

    public TituloAcademicoProfesor paises(Paises paises) {
        this.setPaises(paises);
        return this;
    }

    public Long getTablaElementoCatalogoId() {
        return this.tablaElementoCatalogoId;
    }

    public void setTablaElementoCatalogoId(Long tablaElementoCatalogo) {
        this.tablaElementoCatalogoId = tablaElementoCatalogo;
    }

    public Long getProfesorId() {
        return this.profesorId;
    }

    public void setProfesorId(Long profesor) {
        this.profesorId = profesor;
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
        if (!(o instanceof TituloAcademicoProfesor)) {
            return false;
        }
        return id != null && id.equals(((TituloAcademicoProfesor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TituloAcademicoProfesor{" +
            "id=" + getId() +
            ", nombreTitulo='" + getNombreTitulo() + "'" +
            ", nombreUniversidadTitulo='" + getNombreUniversidadTitulo() + "'" +
            ", yearTitulo='" + getYearTitulo() + "'" +
            "}";
    }
}
