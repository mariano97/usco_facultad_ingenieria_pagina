package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RedesProgramaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("url_red_social", table, columnPrefix + "_url_red_social"));

        columns.add(Column.aliased("programa_id", table, columnPrefix + "_programa_id"));
        columns.add(Column.aliased("tabla_elemento_catalogo_id", table, columnPrefix + "_tabla_elemento_catalogo_id"));
        return columns;
    }
}
