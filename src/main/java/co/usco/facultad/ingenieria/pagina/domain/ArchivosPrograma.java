package co.usco.facultad.ingenieria.pagina.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ArchivosPrograma.
 */
@Table("archivos_programa")
public class ArchivosPrograma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("url_name")
    private String urlName;

    @NotNull(message = "must not be null")
    @Column("generation_storage")
    private Long generationStorage;

    @Column("storage_content_type")
    private String storageContentType;

    @Column("tipo_documento")
    private String tipoDocumento;

    @NotNull(message = "must not be null")
    @Column("plan_estudio")
    private Boolean planEstudio;

    @NotNull(message = "must not be null")
    @Column("micro_diseno")
    private Boolean microDiseno;

    @NotNull(message = "must not be null")
    @Column("nombre_archivo")
    private String nombreArchivo;

    @Transient
    @JsonIgnoreProperties(value = { "nivelFormacion", "tipoFormacion", "facultad", "sedes", "profesors" }, allowSetters = true)
    private Programa programa;

    @Transient
    @JsonIgnoreProperties(value = { "tablaTiposCatalogo" }, allowSetters = true)
    private TablaElementoCatalogo tablaElementoCatalogo;

    @Column("programa_id")
    private Long programaId;

    @Column("tabla_elemento_catalogo_id")
    private Long tablaElementoCatalogoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArchivosPrograma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlName() {
        return this.urlName;
    }

    public ArchivosPrograma urlName(String urlName) {
        this.setUrlName(urlName);
        return this;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public Long getGenerationStorage() {
        return this.generationStorage;
    }

    public ArchivosPrograma generationStorage(Long generationStorage) {
        this.setGenerationStorage(generationStorage);
        return this;
    }

    public void setGenerationStorage(Long generationStorage) {
        this.generationStorage = generationStorage;
    }

    public String getStorageContentType() {
        return this.storageContentType;
    }

    public ArchivosPrograma storageContentType(String storageContentType) {
        this.setStorageContentType(storageContentType);
        return this;
    }

    public void setStorageContentType(String storageContentType) {
        this.storageContentType = storageContentType;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public ArchivosPrograma tipoDocumento(String tipoDocumento) {
        this.setTipoDocumento(tipoDocumento);
        return this;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Boolean getPlanEstudio() {
        return this.planEstudio;
    }

    public ArchivosPrograma planEstudio(Boolean planEstudio) {
        this.setPlanEstudio(planEstudio);
        return this;
    }

    public void setPlanEstudio(Boolean planEstudio) {
        this.planEstudio = planEstudio;
    }

    public Boolean getMicroDiseno() {
        return this.microDiseno;
    }

    public ArchivosPrograma microDiseno(Boolean microDiseno) {
        this.setMicroDiseno(microDiseno);
        return this;
    }

    public void setMicroDiseno(Boolean microDiseno) {
        this.microDiseno = microDiseno;
    }

    public String getNombreArchivo() {
        return this.nombreArchivo;
    }

    public ArchivosPrograma nombreArchivo(String nombreArchivo) {
        this.setNombreArchivo(nombreArchivo);
        return this;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Programa getPrograma() {
        return this.programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
        this.programaId = programa != null ? programa.getId() : null;
    }

    public ArchivosPrograma programa(Programa programa) {
        this.setPrograma(programa);
        return this;
    }

    public TablaElementoCatalogo getTablaElementoCatalogo() {
        return this.tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
        this.tablaElementoCatalogoId = tablaElementoCatalogo != null ? tablaElementoCatalogo.getId() : null;
    }

    public ArchivosPrograma tablaElementoCatalogo(TablaElementoCatalogo tablaElementoCatalogo) {
        this.setTablaElementoCatalogo(tablaElementoCatalogo);
        return this;
    }

    public Long getProgramaId() {
        return this.programaId;
    }

    public void setProgramaId(Long programa) {
        this.programaId = programa;
    }

    public Long getTablaElementoCatalogoId() {
        return this.tablaElementoCatalogoId;
    }

    public void setTablaElementoCatalogoId(Long tablaElementoCatalogo) {
        this.tablaElementoCatalogoId = tablaElementoCatalogo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchivosPrograma)) {
            return false;
        }
        return id != null && id.equals(((ArchivosPrograma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchivosPrograma{" +
            "id=" + getId() +
            ", urlName='" + getUrlName() + "'" +
            ", generationStorage=" + getGenerationStorage() +
            ", storageContentType='" + getStorageContentType() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", planEstudio='" + getPlanEstudio() + "'" +
            ", microDiseno='" + getMicroDiseno() + "'" +
            ", nombreArchivo='" + getNombreArchivo() + "'" +
            "}";
    }
}
