package co.usco.facultad.ingenieria.pagina.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma} entity.
 */
public class ArchivosProgramaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String urlName;

    @NotNull(message = "must not be null")
    private Long generationStorage;

    private String storageContentType;

    private String tipoDocumento;

    @NotNull(message = "must not be null")
    private Boolean planEstudio;

    @NotNull(message = "must not be null")
    private Boolean microDiseno;

    @NotNull(message = "must not be null")
    private String nombreArchivo;

    private ProgramaDTO programa;

    private TablaElementoCatalogoDTO tablaElementoCatalogo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public Long getGenerationStorage() {
        return generationStorage;
    }

    public void setGenerationStorage(Long generationStorage) {
        this.generationStorage = generationStorage;
    }

    public String getStorageContentType() {
        return storageContentType;
    }

    public void setStorageContentType(String storageContentType) {
        this.storageContentType = storageContentType;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Boolean getPlanEstudio() {
        return planEstudio;
    }

    public void setPlanEstudio(Boolean planEstudio) {
        this.planEstudio = planEstudio;
    }

    public Boolean getMicroDiseno() {
        return microDiseno;
    }

    public void setMicroDiseno(Boolean microDiseno) {
        this.microDiseno = microDiseno;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public ProgramaDTO getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaDTO programa) {
        this.programa = programa;
    }

    public TablaElementoCatalogoDTO getTablaElementoCatalogo() {
        return tablaElementoCatalogo;
    }

    public void setTablaElementoCatalogo(TablaElementoCatalogoDTO tablaElementoCatalogo) {
        this.tablaElementoCatalogo = tablaElementoCatalogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchivosProgramaDTO)) {
            return false;
        }

        ArchivosProgramaDTO archivosProgramaDTO = (ArchivosProgramaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, archivosProgramaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchivosProgramaDTO{" +
            "id=" + getId() +
            ", urlName='" + getUrlName() + "'" +
            ", generationStorage=" + getGenerationStorage() +
            ", storageContentType='" + getStorageContentType() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", planEstudio='" + getPlanEstudio() + "'" +
            ", microDiseno='" + getMicroDiseno() + "'" +
            ", nombreArchivo='" + getNombreArchivo() + "'" +
            ", programa=" + getPrograma() +
            ", tablaElementoCatalogo=" + getTablaElementoCatalogo() +
            "}";
    }
}
