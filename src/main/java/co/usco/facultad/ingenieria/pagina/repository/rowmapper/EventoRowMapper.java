package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Evento;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Evento}, with proper type conversions.
 */
@Service
public class EventoRowMapper implements BiFunction<Row, String, Evento> {

    private final ColumnConverter converter;

    public EventoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Evento} stored in the database.
     */
    @Override
    public Evento apply(Row row, String prefix) {
        Evento entity = new Evento();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitulo(converter.fromRow(row, prefix + "_titulo", String.class));
        entity.setCuerpo(converter.fromRow(row, prefix + "_cuerpo", String.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", Instant.class));
        entity.setHora(converter.fromRow(row, prefix + "_hora", Instant.class));
        entity.setLugar(converter.fromRow(row, prefix + "_lugar", String.class));
        entity.setUrlFoto(converter.fromRow(row, prefix + "_url_foto", String.class));
        entity.setFacultadId(converter.fromRow(row, prefix + "_facultad_id", Long.class));
        return entity;
    }
}
