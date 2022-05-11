package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TablaElementoCatalogoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("abreviatura", table, columnPrefix + "_abreviatura"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));

        columns.add(Column.aliased("tabla_tipos_catalogo_id", table, columnPrefix + "_tabla_tipos_catalogo_id"));
        return columns;
    }
}
