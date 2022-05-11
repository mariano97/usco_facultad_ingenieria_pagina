package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.Programa} entity.
 */
public class ProgramaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private Long codigoSnies;

    @NotNull(message = "must not be null")
    private Long codigoRegistroCalificado;

    @NotNull(message = "must not be null")
    private Instant fechaRegistroCalificado;

    @NotNull(message = "must not be null")
    private String nombreTituloOtorgado;

    @NotNull(message = "must not be null")
    private Integer numeroCreditos;

    private Integer duracionPrograma;

    @NotNull(message = "must not be null")
    private String presentacionPrograma;

    @NotNull(message = "must not be null")
    private String mision;

    @NotNull(message = "must not be null")
    private String vision;

    private String perfilEstudiante;

    @NotNull(message = "must not be null")
    private String perfilEgresado;

    private String perfilOcupacional;

    private String urlFotoPrograma;

    private String dirigidoAQuien;

    @NotNull(message = "must not be null")
    private BigDecimal costoPrograma;

    @NotNull(message = "must not be null")
    private Boolean estado;

    private TablaElementoCatalogoDTO nivelFormacion;

    private TablaElementoCatalogoDTO tipoFormacion;

    private FacultadDTO facultad;

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

    public Long getCodigoSnies() {
        return codigoSnies;
    }

    public void setCodigoSnies(Long codigoSnies) {
        this.codigoSnies = codigoSnies;
    }

    public Long getCodigoRegistroCalificado() {
        return codigoRegistroCalificado;
    }

    public void setCodigoRegistroCalificado(Long codigoRegistroCalificado) {
        this.codigoRegistroCalificado = codigoRegistroCalificado;
    }

    public Instant getFechaRegistroCalificado() {
        return fechaRegistroCalificado;
    }

    public void setFechaRegistroCalificado(Instant fechaRegistroCalificado) {
        this.fechaRegistroCalificado = fechaRegistroCalificado;
    }

    public String getNombreTituloOtorgado() {
        return nombreTituloOtorgado;
    }

    public void setNombreTituloOtorgado(String nombreTituloOtorgado) {
        this.nombreTituloOtorgado = nombreTituloOtorgado;
    }

    public Integer getNumeroCreditos() {
        return numeroCreditos;
    }

    public void setNumeroCreditos(Integer numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    public Integer getDuracionPrograma() {
        return duracionPrograma;
    }

    public void setDuracionPrograma(Integer duracionPrograma) {
        this.duracionPrograma = duracionPrograma;
    }

    public String getPresentacionPrograma() {
        return presentacionPrograma;
    }

    public void setPresentacionPrograma(String presentacionPrograma) {
        this.presentacionPrograma = presentacionPrograma;
    }

    public String getMision() {
        return mision;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(String perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public String getPerfilEgresado() {
        return perfilEgresado;
    }

    public void setPerfilEgresado(String perfilEgresado) {
        this.perfilEgresado = perfilEgresado;
    }

    public String getPerfilOcupacional() {
        return perfilOcupacional;
    }

    public void setPerfilOcupacional(String perfilOcupacional) {
        this.perfilOcupacional = perfilOcupacional;
    }

    public String getUrlFotoPrograma() {
        return urlFotoPrograma;
    }

    public void setUrlFotoPrograma(String urlFotoPrograma) {
        this.urlFotoPrograma = urlFotoPrograma;
    }

    public String getDirigidoAQuien() {
        return dirigidoAQuien;
    }

    public void setDirigidoAQuien(String dirigidoAQuien) {
        this.dirigidoAQuien = dirigidoAQuien;
    }

    public BigDecimal getCostoPrograma() {
        return costoPrograma;
    }

    public void setCostoPrograma(BigDecimal costoPrograma) {
        this.costoPrograma = costoPrograma;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public TablaElementoCatalogoDTO getNivelFormacion() {
        return nivelFormacion;
    }

    public void setNivelFormacion(TablaElementoCatalogoDTO nivelFormacion) {
        this.nivelFormacion = nivelFormacion;
    }

    public TablaElementoCatalogoDTO getTipoFormacion() {
        return tipoFormacion;
    }

    public void setTipoFormacion(TablaElementoCatalogoDTO tipoFormacion) {
        this.tipoFormacion = tipoFormacion;
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
        if (!(o instanceof ProgramaDTO)) {
            return false;
        }

        ProgramaDTO programaDTO = (ProgramaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, programaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigoSnies=" + getCodigoSnies() +
            ", codigoRegistroCalificado=" + getCodigoRegistroCalificado() +
            ", fechaRegistroCalificado='" + getFechaRegistroCalificado() + "'" +
            ", nombreTituloOtorgado='" + getNombreTituloOtorgado() + "'" +
            ", numeroCreditos=" + getNumeroCreditos() +
            ", duracionPrograma=" + getDuracionPrograma() +
            ", presentacionPrograma='" + getPresentacionPrograma() + "'" +
            ", mision='" + getMision() + "'" +
            ", vision='" + getVision() + "'" +
            ", perfilEstudiante='" + getPerfilEstudiante() + "'" +
            ", perfilEgresado='" + getPerfilEgresado() + "'" +
            ", perfilOcupacional='" + getPerfilOcupacional() + "'" +
            ", urlFotoPrograma='" + getUrlFotoPrograma() + "'" +
            ", dirigidoAQuien='" + getDirigidoAQuien() + "'" +
            ", costoPrograma=" + getCostoPrograma() +
            ", estado='" + getEstado() + "'" +
            ", nivelFormacion=" + getNivelFormacion() +
            ", tipoFormacion=" + getTipoFormacion() +
            ", facultad=" + getFacultad() +
            "}";
    }
}
