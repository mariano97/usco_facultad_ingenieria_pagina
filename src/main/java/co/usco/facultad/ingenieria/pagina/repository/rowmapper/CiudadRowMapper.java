package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Ciudad;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Ciudad}, with proper type conversions.
 */
@Service
public class CiudadRowMapper implements BiFunction<Row, String, Ciudad> {

    private final ColumnConverter converter;

    public CiudadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ciudad} stored in the database.
     */
    @Override
    public Ciudad apply(Row row, String prefix) {
        Ciudad entity = new Ciudad();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setLatitud(converter.fromRow(row, prefix + "_latitud", BigDecimal.class));
        entity.setLongitud(converter.fromRow(row, prefix + "_longitud", BigDecimal.class));
        entity.setEstadosId(converter.fromRow(row, prefix + "_estados_id", Long.class));
        return entity;
    }
}
