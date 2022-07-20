package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Semillero;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Semillero}, with proper type conversions.
 */
@Service
public class SemilleroRowMapper implements BiFunction<Row, String, Semillero> {

    private final ColumnConverter converter;

    public SemilleroRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Semillero} stored in the database.
     */
    @Override
    public Semillero apply(Row row, String prefix) {
        Semillero entity = new Semillero();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setInformacionGeneral(converter.fromRow(row, prefix + "_informacion_general", String.class));
        entity.setUrlFoto(converter.fromRow(row, prefix + "_url_foto", String.class));
        entity.setFacultadId(converter.fromRow(row, prefix + "_facultad_id", Long.class));
        entity.setProfesorId(converter.fromRow(row, prefix + "_profesor_id", Long.class));
        return entity;
    }
}
