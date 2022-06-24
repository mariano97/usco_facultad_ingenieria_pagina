package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.CursoMateria;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CursoMateria}, with proper type conversions.
 */
@Service
public class CursoMateriaRowMapper implements BiFunction<Row, String, CursoMateria> {

    private final ColumnConverter converter;

    public CursoMateriaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CursoMateria} stored in the database.
     */
    @Override
    public CursoMateria apply(Row row, String prefix) {
        CursoMateria entity = new CursoMateria();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setNumeroCreditos(converter.fromRow(row, prefix + "_numero_creditos", Long.class));
        entity.setSemestreImpartida(converter.fromRow(row, prefix + "_semestre_impartida", Long.class));
        entity.setNivelAcademicoId(converter.fromRow(row, prefix + "_nivel_academico_id", Long.class));
        return entity;
    }
}
