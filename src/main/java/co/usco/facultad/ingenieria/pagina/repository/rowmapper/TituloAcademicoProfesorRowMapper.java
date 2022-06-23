package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.TituloAcademicoProfesor;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TituloAcademicoProfesor}, with proper type conversions.
 */
@Service
public class TituloAcademicoProfesorRowMapper implements BiFunction<Row, String, TituloAcademicoProfesor> {

    private final ColumnConverter converter;

    public TituloAcademicoProfesorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TituloAcademicoProfesor} stored in the database.
     */
    @Override
    public TituloAcademicoProfesor apply(Row row, String prefix) {
        TituloAcademicoProfesor entity = new TituloAcademicoProfesor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombreTitulo(converter.fromRow(row, prefix + "_nombre_titulo", String.class));
        entity.setNombreUniversidadTitulo(converter.fromRow(row, prefix + "_nombre_universidad_titulo", String.class));
        entity.setYearTitulo(converter.fromRow(row, prefix + "_year_titulo", Instant.class));
        entity.setTablaElementoCatalogoId(converter.fromRow(row, prefix + "_tabla_elemento_catalogo_id", Long.class));
        entity.setProfesorId(converter.fromRow(row, prefix + "_profesor_id", Long.class));
        entity.setPaisesId(converter.fromRow(row, prefix + "_paises_id", Long.class));
        return entity;
    }
}
