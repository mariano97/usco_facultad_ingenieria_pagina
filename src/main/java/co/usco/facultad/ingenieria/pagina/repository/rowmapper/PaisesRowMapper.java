package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Paises;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Paises}, with proper type conversions.
 */
@Service
public class PaisesRowMapper implements BiFunction<Row, String, Paises> {

    private final ColumnConverter converter;

    public PaisesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Paises} stored in the database.
     */
    @Override
    public Paises apply(Row row, String prefix) {
        Paises entity = new Paises();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombrePais(converter.fromRow(row, prefix + "_nombre_pais", String.class));
        entity.setCodigoPais(converter.fromRow(row, prefix + "_codigo_pais", String.class));
        return entity;
    }
}
