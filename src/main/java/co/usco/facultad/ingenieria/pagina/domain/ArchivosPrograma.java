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

    @Transient
    @JsonIgnoreProperties(value = { "nivelFormacion", "tipoFormacion", "facultad", "sedes", "profesors" }, allowSetters = true)
    private Programa programa;

    @Column("programa_id")
    private Long programaId;

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

    public Long getProgramaId() {
        return this.programaId;
    }

    public void setProgramaId(Long programa) {
        this.programaId = programa;
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
            "}";
    }
}
