package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Noticia;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Noticia}, with proper type conversions.
 */
@Service
public class NoticiaRowMapper implements BiFunction<Row, String, Noticia> {

    private final ColumnConverter converter;

    public NoticiaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Noticia} stored in the database.
     */
    @Override
    public Noticia apply(Row row, String prefix) {
        Noticia entity = new Noticia();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitulo(converter.fromRow(row, prefix + "_titulo", String.class));
        entity.setSintesis(converter.fromRow(row, prefix + "_sintesis", String.class));
        entity.setCuerpoDescripcion(converter.fromRow(row, prefix + "_cuerpo_descripcion", String.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", Instant.class));
        entity.setUrlFoto(converter.fromRow(row, prefix + "_url_foto", String.class));
        entity.setFacultadId(converter.fromRow(row, prefix + "_facultad_id", Long.class));
        return entity;
    }
}
