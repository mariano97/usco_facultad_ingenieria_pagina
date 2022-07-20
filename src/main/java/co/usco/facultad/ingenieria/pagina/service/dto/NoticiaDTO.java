package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Noticia} entity.
 */
public class NoticiaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String titulo;

    @NotNull(message = "must not be null")
    private String sintesis;

    @NotNull(message = "must not be null")
    private String cuerpoDescripcion;

    @NotNull(message = "must not be null")
    private Instant fecha;

    private String urlFoto;

    private FacultadDTO facultad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSintesis() {
        return sintesis;
    }

    public void setSintesis(String sintesis) {
        this.sintesis = sintesis;
    }

    public String getCuerpoDescripcion() {
        return cuerpoDescripcion;
    }

    public void setCuerpoDescripcion(String cuerpoDescripcion) {
        this.cuerpoDescripcion = cuerpoDescripcion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public FacultadDTO getFacultad() {
        return facultad;
    }

    public void setFacultad(FacultadDTO facultad) {
        this.facultad = facultad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoticiaDTO)) {
            return false;
        }

        NoticiaDTO noticiaDTO = (NoticiaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, noticiaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoticiaDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", sintesis='" + getSintesis() + "'" +
            ", cuerpoDescripcion='" + getCuerpoDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", urlFoto='" + getUrlFoto() + "'" +
            ", facultad=" + getFacultad() +
            "}";
    }
}
