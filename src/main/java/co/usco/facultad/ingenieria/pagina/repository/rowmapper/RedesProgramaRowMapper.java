package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.RedesPrograma;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RedesPrograma}, with proper type conversions.
 */
@Service
public class RedesProgramaRowMapper implements BiFunction<Row, String, RedesPrograma> {

    private final ColumnConverter converter;

    public RedesProgramaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RedesPrograma} stored in the database.
     */
    @Override
    public RedesPrograma apply(Row row, String prefix) {
        RedesPrograma entity = new RedesPrograma();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUrlRedSocial(converter.fromRow(row, prefix + "_url_red_social", String.class));
        entity.setProgramaId(converter.fromRow(row, prefix + "_programa_id", Long.class));
        entity.setTablaElementoCatalogoId(converter.fromRow(row, prefix + "_tabla_elemento_catalogo_id", Long.class));
        return entity;
    }
}
