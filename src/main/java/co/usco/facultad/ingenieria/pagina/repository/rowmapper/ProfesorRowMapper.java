package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Profesor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Profesor}, with proper type conversions.
 */
@Service
public class ProfesorRowMapper implements BiFunction<Row, String, Profesor> {

    private final ColumnConverter converter;

    public ProfesorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Profesor} stored in the database.
     */
    @Override
    public Profesor apply(Row row, String prefix) {
        Profesor entity = new Profesor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSegundoNombre(converter.fromRow(row, prefix + "_segundo_nombre", String.class));
        entity.setSegundoApellido(converter.fromRow(row, prefix + "_segundo_apellido", String.class));
        entity.setEmailAlternativo(converter.fromRow(row, prefix + "_email_alternativo", String.class));
        entity.setActivo(converter.fromRow(row, prefix + "_activo", Boolean.class));
        entity.setPerfil(converter.fromRow(row, prefix + "_perfil", String.class));
        entity.setTelefonoCelular(converter.fromRow(row, prefix + "_telefono_celular", String.class));
        entity.setOficina(converter.fromRow(row, prefix + "_oficina", String.class));
        return entity;
    }
}
