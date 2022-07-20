package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Noticia.
 */
@Table("noticia")
public class Noticia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("titulo")
    private String titulo;

    @NotNull(message = "must not be null")
    @Column("sintesis")
    private String sintesis;

    @NotNull(message = "must not be null")
    @Column("cuerpo_descripcion")
    private String cuerpoDescripcion;

    @NotNull(message = "must not be null")
    @Column("fecha")
    private Instant fecha;

    @Column("url_foto")
    private String urlFoto;

    @Transient
    private Facultad facultad;

    @Column("facultad_id")
    private Long facultadId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Noticia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Noticia titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSintesis() {
        return this.sintesis;
    }

    public Noticia sintesis(String sintesis) {
        this.setSintesis(sintesis);
        return this;
    }

    public void setSintesis(String sintesis) {
        this.sintesis = sintesis;
    }

    public String getCuerpoDescripcion() {
        return this.cuerpoDescripcion;
    }

    public Noticia cuerpoDescripcion(String cuerpoDescripcion) {
        this.setCuerpoDescripcion(cuerpoDescripcion);
        return this;
    }

    public void setCuerpoDescripcion(String cuerpoDescripcion) {
        this.cuerpoDescripcion = cuerpoDescripcion;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Noticia fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getUrlFoto() {
        return this.urlFoto;
    }

    public Noticia urlFoto(String urlFoto) {
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

    public Noticia facultad(Facultad facultad) {
        this.setFacultad(facultad);
        return this;
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
        if (!(o instanceof Noticia)) {
            return false;
        }
        return id != null && id.equals(((Noticia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Noticia{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", sintesis='" + getSintesis() + "'" +
            ", cuerpoDescripcion='" + getCuerpoDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", urlFoto='" + getUrlFoto() + "'" +
            "}";
    }
}
