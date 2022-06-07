package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.ArchivosPrograma;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ArchivosPrograma}, with proper type conversions.
 */
@Service
public class ArchivosProgramaRowMapper implements BiFunction<Row, String, ArchivosPrograma> {

    private final ColumnConverter converter;

    public ArchivosProgramaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ArchivosPrograma} stored in the database.
     */
    @Override
    public ArchivosPrograma apply(Row row, String prefix) {
        ArchivosPrograma entity = new ArchivosPrograma();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUrlName(converter.fromRow(row, prefix + "_url_name", String.class));
        entity.setGenerationStorage(converter.fromRow(row, prefix + "_generation_storage", Long.class));
        entity.setStorageContentType(converter.fromRow(row, prefix + "_storage_content_type", String.class));
        entity.setTipoDocumento(converter.fromRow(row, prefix + "_tipo_documento", String.class));
        entity.setPlanEstudio(converter.fromRow(row, prefix + "_plan_estudio", Boolean.class));
        entity.setProgramaId(converter.fromRow(row, prefix + "_programa_id", Long.class));
        return entity;
    }
}
