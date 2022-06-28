package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Programa.
 */
@Table("programa")
public class Programa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("codigo_snies")
    private Long codigoSnies;

    @NotNull(message = "must not be null")
    @Column("codigo_registro_calificado")
    private Long codigoRegistroCalificado;

    @NotNull(message = "must not be null")
    @Column("fecha_registro_calificado")
    private Instant fechaRegistroCalificado;

    @NotNull(message = "must not be null")
    @Column("nombre_titulo_otorgado")
    private String nombreTituloOtorgado;

    @NotNull(message = "must not be null")
    @Column("numero_creditos")
    private Integer numeroCreditos;

    @Column("duracion_programa")
    private Integer duracionPrograma;

    @NotNull(message = "must not be null")
    @Column("presentacion_programa")
    private String presentacionPrograma;

    @NotNull(message = "must not be null")
    @Column("mision")
    private String mision;

    @NotNull(message = "must not be null")
    @Column("vision")
    private String vision;

    @Column("perfil_estudiante")
    private String perfilEstudiante;

    @NotNull(message = "must not be null")
    @Column("perfil_egresado")
    private String perfilEgresado;

    @Column("perfil_ocupacional")
    private String perfilOcupacional;

    @Column("url_foto_programa")
    private String urlFotoPrograma;

    @Column("dirigido_a_quien")
    private String dirigidoAQuien;

    @NotNull(message = "must not be null")
    @Column("costo_programa")
    private BigDecimal costoPrograma;

    @NotNull(message = "must not be null")
    @Column("estado")
    private Boolean estado;

    @NotNull(message = "must not be null")
    @Column("email_contacto")
    private String emailContacto;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo nivelFormacion;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo tipoFormacion;

    @Transient
    private Facultad facultad;

    @Transient
    @JsonIgnoreProperties(value = { "programas", "ciudad" }, allowSetters = true)
    private Set<Sede> sedes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "tablaElementoCatalogo", "facultad", "programas", "cursoMaterias" }, allowSetters = true)
    private Set<Profesor> profesors = new HashSet<>();

    @Column("nivel_formacion_id")
    private Long nivelFormacionId;

    @Column("tipo_formacion_id")
    private Long tipoFormacionId;

    @Column("facultad_id")
    private Long facultadId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Programa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Programa nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCodigoSnies() {
        return this.codigoSnies;
    }

    public Programa codigoSnies(Long codigoSnies) {
        this.setCodigoSnies(codigoSnies);
        return this;
    }

    public void setCodigoSnies(Long codigoSnies) {
        this.codigoSnies = codigoSnies;
    }

    public Long getCodigoRegistroCalificado() {
        return this.codigoRegistroCalificado;
    }

    public Programa codigoRegistroCalificado(Long codigoRegistroCalificado) {
        this.setCodigoRegistroCalificado(codigoRegistroCalificado);
        return this;
    }

    public void setCodigoRegistroCalificado(Long codigoRegistroCalificado) {
        this.codigoRegistroCalificado = codigoRegistroCalificado;
    }

    public Instant getFechaRegistroCalificado() {
        return this.fechaRegistroCalificado;
    }

    public Programa fechaRegistroCalificado(Instant fechaRegistroCalificado) {
        this.setFechaRegistroCalificado(fechaRegistroCalificado);
        return this;
    }

    public void setFechaRegistroCalificado(Instant fechaRegistroCalificado) {
        this.fechaRegistroCalificado = fechaRegistroCalificado;
    }

    public String getNombreTituloOtorgado() {
        return this.nombreTituloOtorgado;
    }

    public Programa nombreTituloOtorgado(String nombreTituloOtorgado) {
        this.setNombreTituloOtorgado(nombreTituloOtorgado);
        return this;
    }

    public void setNombreTituloOtorgado(String nombreTituloOtorgado) {
        this.nombreTituloOtorgado = nombreTituloOtorgado;
    }

    public Integer getNumeroCreditos() {
        return this.numeroCreditos;
    }

    public Programa numeroCreditos(Integer numeroCreditos) {
        this.setNumeroCreditos(numeroCreditos);
        return this;
    }

    public void setNumeroCreditos(Integer numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    public Integer getDuracionPrograma() {
        return this.duracionPrograma;
    }

    public Programa duracionPrograma(Integer duracionPrograma) {
        this.setDuracionPrograma(duracionPrograma);
        return this;
    }

    public void setDuracionPrograma(Integer duracionPrograma) {
        this.duracionPrograma = duracionPrograma;
    }

    public String getPresentacionPrograma() {
        return this.presentacionPrograma;
    }

    public Programa presentacionPrograma(String presentacionPrograma) {
        this.setPresentacionPrograma(presentacionPrograma);
        return this;
    }

    public void setPresentacionPrograma(String presentacionPrograma) {
        this.presentacionPrograma = presentacionPrograma;
    }

    public String getMision() {
        return this.mision;
    }

    public Programa mision(String mision) {
        this.setMision(mision);
        return this;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }

    public String getVision() {
        return this.vision;
    }

    public Programa vision(String vision) {
        this.setVision(vision);
        return this;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getPerfilEstudiante() {
        return this.perfilEstudiante;
    }

    public Programa perfilEstudiante(String perfilEstudiante) {
        this.setPerfilEstudiante(perfilEstudiante);
        return this;
    }

    public void setPerfilEstudiante(String perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public String getPerfilEgresado() {
        return this.perfilEgresado;
    }

    public Programa perfilEgresado(String perfilEgresado) {
        this.setPerfilEgresado(perfilEgresado);
        return this;
    }

    public void setPerfilEgresado(String perfilEgresado) {
        this.perfilEgresado = perfilEgresado;
    }

    public String getPerfilOcupacional() {
        return this.perfilOcupacional;
    }

    public Programa perfilOcupacional(String perfilOcupacional) {
        this.setPerfilOcupacional(perfilOcupacional);
        return this;
    }

    public void setPerfilOcupacional(String perfilOcupacional) {
        this.perfilOcupacional = perfilOcupacional;
    }

    public String getUrlFotoPrograma() {
        return this.urlFotoPrograma;
    }

    public Programa urlFotoPrograma(String urlFotoPrograma) {
        this.setUrlFotoPrograma(urlFotoPrograma);
        return this;
    }

    public void setUrlFotoPrograma(String urlFotoPrograma) {
        this.urlFotoPrograma = urlFotoPrograma;
    }

    public String getDirigidoAQuien() {
        return this.dirigidoAQuien;
    }

    public Programa dirigidoAQuien(String dirigidoAQuien) {
        this.setDirigidoAQuien(dirigidoAQuien);
        return this;
    }

    public void setDirigidoAQuien(String dirigidoAQuien) {
        this.dirigidoAQuien = dirigidoAQuien;
    }

    public BigDecimal getCostoPrograma() {
        return this.costoPrograma;
    }

    public Programa costoPrograma(BigDecimal costoPrograma) {
        this.setCostoPrograma(costoPrograma);
        return this;
    }

    public void setCostoPrograma(BigDecimal costoPrograma) {
        this.costoPrograma = costoPrograma != null ? costoPrograma.stripTrailingZeros() : null;
    }

    public Boolean getEstado() {
        return this.estado;
    }

    public Programa estado(Boolean estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getEmailContacto() {
        return this.emailContacto;
    }

    public Programa emailContacto(String emailContacto) {
        this.setEmailContacto(emailContacto);
        return this;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public TablaElementoCatalogo getNivelFormacion() {
        return this.nivelFormacion;
    }

    public void setNivelFormacion(TablaElementoCatalogo tablaElementoCatalogo) {
        this.nivelFormacion = tablaElementoCatalogo;
        this.nivelFormacionId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public Programa nivelFormacion(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setNivelFormacion(tablaElementoCatalogo);
        return this;
    }

    public TablaElementoCatalogo getTipoFormacion() {
        return this.tipoFormacion;
    }

    public void setTipoFormacion(TablaElementoCatalogo tablaElementoCatalogo) {
        this.tipoFormacion = tablaElementoCatalogo;
        this.tipoFormacionId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public Programa tipoFormacion(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setTipoFormacion(tablaElementoCatalogo);
        return this;
    }

    public Facultad getFacultad() {
        return this.facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
        this.facultadId = facultad != null ? facultad.getId() : null;
    }

    public Programa facultad(Facultad facultad) {
        this.setFacultad(facultad);
        return this;
    }

    public Set<Sede> getSedes() {
        return this.sedes;
    }

    public void setSedes(Set<Sede> sedes) {
        this.sedes = sedes;
    }

    public Programa sedes(Set<Sede> sedes) {
        this.setSedes(sedes);
        return this;
    }

    public Programa addSede(Sede sede) {
        this.sedes.add(sede);
        sede.getProgramas().add(this);
        return this;
    }

    public Programa removeSede(Sede sede) {
        this.sedes.remove(sede);
        sede.getProgramas().remove(this);
        return this;
    }

    public Set<Profesor> getProfesors() {
        return this.profesors;
    }

    public void setProfesors(Set<Profesor> profesors) {
        this.profesors = profesors;
    }

    public Programa profesors(Set<Profesor> profesors) {
        this.setProfesors(profesors);
        return this;
    }

    public Programa addProfesor(Profesor profesor) {
        this.profesors.add(profesor);
        profesor.getProgramas().add(this);
        return this;
    }

    public Programa removeProfesor(Profesor profesor) {
        this.profesors.remove(profesor);
        profesor.getProgramas().remove(this);
        return this;
    }

    public Long getNivelFormacionId() {
        return this.nivelFormacionId;
    }

    public void setNivelFormacionId(Long tablaElementoCatalogo) {
        this.nivelFormacionId = tablaElementoCatalogo;
    }

    public Long getTipoFormacionId() {
        return this.tipoFormacionId;
    }

    public void setTipoFormacionId(Long tablaElementoCatalogo) {
        this.tipoFormacionId = tablaElementoCatalogo;
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
        if (!(o instanceof Programa)) {
            return false;
        }
        return id != null && id.equals(((Programa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Programa{" +
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
            ", emailContacto='" + getEmailContacto() + "'" +
            "}";
    }
}
