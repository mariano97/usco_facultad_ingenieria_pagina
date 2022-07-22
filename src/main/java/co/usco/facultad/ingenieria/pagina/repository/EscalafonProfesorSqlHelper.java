package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EscalafonProfesorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("puntucacion_promedio", table, columnPrefix + "_puntucacion_promedio"));
        columns.add(Column.aliased("fecha", table, columnPrefix + "_fecha"));

        columns.add(Column.aliased("profesor_id", table, columnPrefix + "_profesor_id"));
        return columns;
    }
}
