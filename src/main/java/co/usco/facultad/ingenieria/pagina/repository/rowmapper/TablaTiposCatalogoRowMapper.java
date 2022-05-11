package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.TablaTiposCatalogo;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TablaTiposCatalogo}, with proper type conversions.
 */
@Service
public class TablaTiposCatalogoRowMapper implements BiFunction<Row, String, TablaTiposCatalogo> {

    private final ColumnConverter converter;

    public TablaTiposCatalogoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TablaTiposCatalogo} stored in the database.
     */
    @Override
    public TablaTiposCatalogo apply(Row row, String prefix) {
        TablaTiposCatalogo entity = new TablaTiposCatalogo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", Boolean.class));
        return entity;
    }
}
