package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.CursoMateria} entity.
 */
public class CursoMateriaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private Long numeroCreditos;

    @NotNull(message = "must not be null")
    private Long semestreImpartida;

    private TablaElementoCatalogoDTO nivelAcademico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getNumeroCreditos() {
        return numeroCreditos;
    }

    public void setNumeroCreditos(Long numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    public Long getSemestreImpartida() {
        return semestreImpartida;
    }

    public void setSemestreImpartida(Long semestreImpartida) {
        this.semestreImpartida = semestreImpartida;
    }

    public TablaElementoCatalogoDTO getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(TablaElementoCatalogoDTO nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CursoMateriaDTO)) {
            return false;
        }

        CursoMateriaDTO cursoMateriaDTO = (CursoMateriaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cursoMateriaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursoMateriaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", numeroCreditos=" + getNumeroCreditos() +
            ", semestreImpartida=" + getSemestreImpartida() +
            ", nivelAcademico=" + getNivelAcademico() +
            "}";
    }
}
