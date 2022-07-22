package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.EscalafonProfesor;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EscalafonProfesor}, with proper type conversions.
 */
@Service
public class EscalafonProfesorRowMapper implements BiFunction<Row, String, EscalafonProfesor> {

    private final ColumnConverter converter;

    public EscalafonProfesorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EscalafonProfesor} stored in the database.
     */
    @Override
    public EscalafonProfesor apply(Row row, String prefix) {
        EscalafonProfesor entity = new EscalafonProfesor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPuntucacionPromedio(converter.fromRow(row, prefix + "_puntucacion_promedio", Double.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", Instant.class));
        entity.setProfesorId(converter.fromRow(row, prefix + "_profesor_id", Long.class));
        return entity;
    }
}
