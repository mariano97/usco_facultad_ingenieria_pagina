package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Laboratorio.
 */
@Table("laboratorio")
public class Laboratorio implements Serializable {

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

    @Column("latitud")
    private Double latitud;

    @Column("longitud")
    private Double longitud;

    @NotNull(message = "must not be null")
    @Column("correo_contacto")
    private String correoContacto;

    @Column("direccion")
    private String direccion;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo tipoLaboratorio;

    @Transient
    private Facultad facultad;

    @Column("tipo_laboratorio_id")
    private Long tipoLaboratorioId;

    @Column("facultad_id")
    private Long facultadId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Laboratorio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Laboratorio nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInformacionGeneral() {
        return this.informacionGeneral;
    }

    public Laboratorio informacionGeneral(String informacionGeneral) {
        this.setInformacionGeneral(informacionGeneral);
        return this;
    }

    public void setInformacionGeneral(String informacionGeneral) {
        this.informacionGeneral = informacionGeneral;
    }

    public String getUrlFoto() {
        return this.urlFoto;
    }

    public Laboratorio urlFoto(String urlFoto) {
        this.setUrlFoto(urlFoto);
        return this;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Double getLatitud() {
        return this.latitud;
    }

    public Laboratorio latitud(Double latitud) {
        this.setLatitud(latitud);
        return this;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return this.longitud;
    }

    public Laboratorio longitud(Double longitud) {
        this.setLongitud(longitud);
        return this;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getCorreoContacto() {
        return this.correoContacto;
    }

    public Laboratorio correoContacto(String correoContacto) {
        this.setCorreoContacto(correoContacto);
        return this;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Laboratorio direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TablaElementoCatalogo getTipoLaboratorio() {
        return this.tipoLaboratorio;
    }

    public void setTipoLaboratorio(TablaElementoCatalogo tablaElementoCatalogo) {
        this.tipoLaboratorio = tablaElementoCatalogo;
        this.tipoLaboratorioId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public Laboratorio tipoLaboratorio(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setTipoLaboratorio(tablaElementoCatalogo);
        return this;
    }

    public Facultad getFacultad() {
        return this.facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
        this.facultadId = facultad != null ? facultad.getId() : null;
    }

    public Laboratorio facultad(Facultad facultad) {
        this.setFacultad(facultad);
        return this;
    }

    public Long getTipoLaboratorioId() {
        return this.tipoLaboratorioId;
    }

    public void setTipoLaboratorioId(Long tablaElementoCatalogo) {
        this.tipoLaboratorioId = tablaElementoCatalogo;
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
        if (!(o instanceof Laboratorio)) {
            return false;
        }
        return id != null && id.equals(((Laboratorio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Laboratorio{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", informacionGeneral='" + getInformacionGeneral() + "'" +
            ", urlFoto='" + getUrlFoto() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            ", correoContacto='" + getCorreoContacto() + "'" +
            ", direccion='" + getDireccion() + "'" +
            "}";
    }
}
