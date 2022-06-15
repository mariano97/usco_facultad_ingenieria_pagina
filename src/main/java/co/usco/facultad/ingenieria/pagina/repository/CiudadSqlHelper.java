package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CiudadSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("latitud", table, columnPrefix + "_latitud"));
        columns.add(Column.aliased("longitud", table, columnPrefix + "_longitud"));

        columns.add(Column.aliased("estados_id", table, columnPrefix + "_estados_id"));
        return columns;
    }
}
