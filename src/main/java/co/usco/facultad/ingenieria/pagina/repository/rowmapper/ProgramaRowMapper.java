package co.usco.facultad.ingenieria.pagina.repository.rowmapper;

import co.usco.facultad.ingenieria.pagina.domain.Programa;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Programa}, with proper type conversions.
 */
@Service
public class ProgramaRowMapper implements BiFunction<Row, String, Programa> {

    private final ColumnConverter converter;

    public ProgramaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Programa} stored in the database.
     */
    @Override
    public Programa apply(Row row, String prefix) {
        Programa entity = new Programa();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCodigoSnies(converter.fromRow(row, prefix + "_codigo_snies", Long.class));
        entity.setCodigoRegistroCalificado(converter.fromRow(row, prefix + "_codigo_registro_calificado", Long.class));
        entity.setFechaRegistroCalificado(converter.fromRow(row, prefix + "_fecha_registro_calificado", Instant.class));
        entity.setNombreTituloOtorgado(converter.fromRow(row, prefix + "_nombre_titulo_otorgado", String.class));
        entity.setNumeroCreditos(converter.fromRow(row, prefix + "_numero_creditos", Integer.class));
        entity.setDuracionPrograma(converter.fromRow(row, prefix + "_duracion_programa", Integer.class));
        entity.setPresentacionPrograma(converter.fromRow(row, prefix + "_presentacion_programa", String.class));
        entity.setMision(converter.fromRow(row, prefix + "_mision", String.class));
        entity.setVision(converter.fromRow(row, prefix + "_vision", String.class));
        entity.setPerfilEstudiante(converter.fromRow(row, prefix + "_perfil_estudiante", String.class));
        entity.setPerfilEgresado(converter.fromRow(row, prefix + "_perfil_egresado", String.class));
        entity.setPerfilOcupacional(converter.fromRow(row, prefix + "_perfil_ocupacional", String.class));
        entity.setUrlFotoPrograma(converter.fromRow(row, prefix + "_url_foto_programa", String.class));
        entity.setDirigidoAQuien(converter.fromRow(row, prefix + "_dirigido_a_quien", String.class));
        entity.setCostoPrograma(converter.fromRow(row, prefix + "_costo_programa", BigDecimal.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", Boolean.class));
        entity.setEmailContacto(converter.fromRow(row, prefix + "_email_contacto", String.class));
        entity.setNivelFormacionId(converter.fromRow(row, prefix + "_nivel_formacion_id", Long.class));
        entity.setTipoFormacionId(converter.fromRow(row, prefix + "_tipo_formacion_id", Long.class));
        entity.setFacultadId(converter.fromRow(row, prefix + "_facultad_id", Long.class));
        return entity;
    }
}
