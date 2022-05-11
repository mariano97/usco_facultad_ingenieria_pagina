package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProgramaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("codigo_snies", table, columnPrefix + "_codigo_snies"));
        columns.add(Column.aliased("codigo_registro_calificado", table, columnPrefix + "_codigo_registro_calificado"));
        columns.add(Column.aliased("fecha_registro_calificado", table, columnPrefix + "_fecha_registro_calificado"));
        columns.add(Column.aliased("nombre_titulo_otorgado", table, columnPrefix + "_nombre_titulo_otorgado"));
        columns.add(Column.aliased("numero_creditos", table, columnPrefix + "_numero_creditos"));
        columns.add(Column.aliased("duracion_programa", table, columnPrefix + "_duracion_programa"));
        columns.add(Column.aliased("presentacion_programa", table, columnPrefix + "_presentacion_programa"));
        columns.add(Column.aliased("mision", table, columnPrefix + "_mision"));
        columns.add(Column.aliased("vision", table, columnPrefix + "_vision"));
        columns.add(Column.aliased("perfil_estudiante", table, columnPrefix + "_perfil_estudiante"));
        columns.add(Column.aliased("perfil_egresado", table, columnPrefix + "_perfil_egresado"));
        columns.add(Column.aliased("perfil_ocupacional", table, columnPrefix + "_perfil_ocupacional"));
        columns.add(Column.aliased("url_foto_programa", table, columnPrefix + "_url_foto_programa"));
        columns.add(Column.aliased("dirigido_a_quien", table, columnPrefix + "_dirigido_a_quien"));
        columns.add(Column.aliased("costo_programa", table, columnPrefix + "_costo_programa"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));

        columns.add(Column.aliased("nivel_formacion_id", table, columnPrefix + "_nivel_formacion_id"));
        columns.add(Column.aliased("tipo_formacion_id", table, columnPrefix + "_tipo_formacion_id"));
        columns.add(Column.aliased("facultad_id", table, columnPrefix + "_facultad_id"));
        return columns;
    }
}
