package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SemilleroSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("informacion_general", table, columnPrefix + "_informacion_general"));
        columns.add(Column.aliased("url_foto", table, columnPrefix + "_url_foto"));

        columns.add(Column.aliased("facultad_id", table, columnPrefix + "_facultad_id"));
        columns.add(Column.aliased("profesor_id", table, columnPrefix + "_profesor_id"));
        return columns;
    }
}
