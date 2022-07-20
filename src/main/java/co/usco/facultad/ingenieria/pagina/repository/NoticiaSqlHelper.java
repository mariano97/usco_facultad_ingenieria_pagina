package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class NoticiaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("titulo", table, columnPrefix + "_titulo"));
        columns.add(Column.aliased("sintesis", table, columnPrefix + "_sintesis"));
        columns.add(Column.aliased("cuerpo_descripcion", table, columnPrefix + "_cuerpo_descripcion"));
        columns.add(Column.aliased("fecha", table, columnPrefix + "_fecha"));
        columns.add(Column.aliased("url_foto", table, columnPrefix + "_url_foto"));

        columns.add(Column.aliased("facultad_id", table, columnPrefix + "_facultad_id"));
        return columns;
    }
}
