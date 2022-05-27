package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Sede;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Sede}, with proper type conversions.
 */
@Service
public class SedeRowMapper implements BiFunction<Row, String, Sede> {

    private final ColumnConverter converter;

    public SedeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Sede} stored in the database.
     */
    @Override
    public Sede apply(Row row, String prefix) {
        Sede entity = new Sede();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setLatitud(converter.fromRow(row, prefix + "_latitud", Double.class));
        entity.setLongitud(converter.fromRow(row, prefix + "_longitud", Double.class));
        entity.setDireccion(converter.fromRow(row, prefix + "_direccion", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", Boolean.class));
        entity.setTelefonoFijo(converter.fromRow(row, prefix + "_telefono_fijo", String.class));
        entity.setTelefonoCelular(converter.fromRow(row, prefix + "_telefono_celular", String.class));
        entity.setCorreoElectronico(converter.fromRow(row, prefix + "_correo_electronico", String.class));
        return entity;
    }
}
