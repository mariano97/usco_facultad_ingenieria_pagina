package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Facultad;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Facultad}, with proper type conversions.
 */
@Service
public class FacultadRowMapper implements BiFunction<Row, String, Facultad> {

    private final ColumnConverter converter;

    public FacultadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Facultad} stored in the database.
     */
    @Override
    public Facultad apply(Row row, String prefix) {
        Facultad entity = new Facultad();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        return entity;
    }
}
