package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.TablaElementoCatalogo;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TablaElementoCatalogo}, with proper type conversions.
 */
@Service
public class TablaElementoCatalogoRowMapper implements BiFunction<Row, String, TablaElementoCatalogo> {

    private final ColumnConverter converter;

    public TablaElementoCatalogoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TablaElementoCatalogo} stored in the database.
     */
    @Override
    public TablaElementoCatalogo apply(Row row, String prefix) {
        TablaElementoCatalogo entity = new TablaElementoCatalogo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setAbreviatura(converter.fromRow(row, prefix + "_abreviatura", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", Boolean.class));
        entity.setTablaTiposCatalogoId(converter.fromRow(row, prefix + "_tabla_tipos_catalogo_id", Long.class));
        return entity;
    }
}
