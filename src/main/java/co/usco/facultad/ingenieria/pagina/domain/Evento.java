package co.usco.facultad.ingenieria.pagina.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Evento.
 */
@Table("evento")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("titulo")
    private String titulo;

    @NotNull(message = "must not be null")
    @Column("cuerpo")
    private String cuerpo;

    @NotNull(message = "must not be null")
    @Column("fecha")
    private Instant fecha;

    @NotNull(message = "must not be null")
    @Column("hora")
    private Instant hora;

    @NotNull(message = "must not be null")
    @Column("lugar")
    private String lugar;

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

    public Evento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Evento titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return this.cuerpo;
    }

    public Evento cuerpo(String cuerpo) {
        this.setCuerpo(cuerpo);
        return this;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Evento fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Instant getHora() {
        return this.hora;
    }

    public Evento hora(Instant hora) {
        this.setHora(hora);
        return this;
    }

    public void setHora(Instant hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return this.lugar;
    }

    public Evento lugar(String lugar) {
        this.setLugar(lugar);
        return this;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getUrlFoto() {
        return this.urlFoto;
    }

    public Evento urlFoto(String urlFoto) {
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

    public Evento facultad(Facultad facultad) {
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
        if (!(o instanceof Evento)) {
            return false;
        }
        return id != null && id.equals(((Evento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evento{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", cuerpo='" + getCuerpo() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", hora='" + getHora() + "'" +
            ", lugar='" + getLugar() + "'" +
            ", urlFoto='" + getUrlFoto() + "'" +
            "}";
    }
}
