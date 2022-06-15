package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Estados;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Estados}, with proper type conversions.
 */
@Service
public class EstadosRowMapper implements BiFunction<Row, String, Estados> {

    private final ColumnConverter converter;

    public EstadosRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Estados} stored in the database.
     */
    @Override
    public Estados apply(Row row, String prefix) {
        Estados entity = new Estados();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setPaisesId(converter.fromRow(row, prefix + "_paises_id", Long.class));
        return entity;
    }
}
