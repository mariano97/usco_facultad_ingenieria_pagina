package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Laboratorio;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Laboratorio}, with proper type conversions.
 */
@Service
public class LaboratorioRowMapper implements BiFunction<Row, String, Laboratorio> {

    private final ColumnConverter converter;

    public LaboratorioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Laboratorio} stored in the database.
     */
    @Override
    public Laboratorio apply(Row row, String prefix) {
        Laboratorio entity = new Laboratorio();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setInformacionGeneral(converter.fromRow(row, prefix + "_informacion_general", String.class));
        entity.setUrlFoto(converter.fromRow(row, prefix + "_url_foto", String.class));
        entity.setLatitud(converter.fromRow(row, prefix + "_latitud", Double.class));
        entity.setLongitud(converter.fromRow(row, prefix + "_longitud", Double.class));
        entity.setCorreoContacto(converter.fromRow(row, prefix + "_correo_contacto", String.class));
        entity.setDireccion(converter.fromRow(row, prefix + "_direccion", String.class));
        return entity;
    }
}
