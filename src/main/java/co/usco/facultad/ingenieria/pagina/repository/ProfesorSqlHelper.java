package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProfesorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("segundo_nombre", table, columnPrefix + "_segundo_nombre"));
        columns.add(Column.aliased("segundo_apellido", table, columnPrefix + "_segundo_apellido"));
        columns.add(Column.aliased("email_alternativo", table, columnPrefix + "_email_alternativo"));
        columns.add(Column.aliased("activo", table, columnPrefix + "_activo"));
        columns.add(Column.aliased("perfil", table, columnPrefix + "_perfil"));
        columns.add(Column.aliased("telefono_celular", table, columnPrefix + "_telefono_celular"));
        columns.add(Column.aliased("oficina", table, columnPrefix + "_oficina"));
        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));

        columns.add(Column.aliased("tabla_elemento_catalogo_id", table, columnPrefix + "_tabla_elemento_catalogo_id"));
        columns.add(Column.aliased("facultad_id", table, columnPrefix + "_facultad_id"));
        return columns;
    }
}
