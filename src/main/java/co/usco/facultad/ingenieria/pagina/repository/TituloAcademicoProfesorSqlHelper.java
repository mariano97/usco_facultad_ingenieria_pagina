package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TituloAcademicoProfesorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre_titulo", table, columnPrefix + "_nombre_titulo"));
        columns.add(Column.aliased("nombre_universidad_titulo", table, columnPrefix + "_nombre_universidad_titulo"));
        columns.add(Column.aliased("year_titulo", table, columnPrefix + "_year_titulo"));

        columns.add(Column.aliased("tabla_elemento_catalogo_id", table, columnPrefix + "_tabla_elemento_catalogo_id"));
        columns.add(Column.aliased("profesor_id", table, columnPrefix + "_profesor_id"));
        columns.add(Column.aliased("paises_id", table, columnPrefix + "_paises_id"));
        return columns;
    }
}
