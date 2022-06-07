package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ArchivosProgramaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("url_name", table, columnPrefix + "_url_name"));
        columns.add(Column.aliased("generation_storage", table, columnPrefix + "_generation_storage"));
        columns.add(Column.aliased("storage_content_type", table, columnPrefix + "_storage_content_type"));
        columns.add(Column.aliased("tipo_documento", table, columnPrefix + "_tipo_documento"));
        columns.add(Column.aliased("plan_estudio", table, columnPrefix + "_plan_estudio"));

        columns.add(Column.aliased("programa_id", table, columnPrefix + "_programa_id"));
        columns.add(Column.aliased("tabla_elemento_catalogo_id", table, columnPrefix + "_tabla_elemento_catalogo_id"));
        return columns;
    }
}