package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CursoMateriaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("numero_creditos", table, columnPrefix + "_numero_creditos"));
        columns.add(Column.aliased("semestre_impartida", table, columnPrefix + "_semestre_impartida"));

        columns.add(Column.aliased("nivel_academico_id", table, columnPrefix + "_nivel_academico_id"));
        return columns;
    }
}
