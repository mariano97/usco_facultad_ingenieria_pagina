package co.usco.facultad.ingenieria.pagina.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SedeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("latitud", table, columnPrefix + "_latitud"));
        columns.add(Column.aliased("longitud", table, columnPrefix + "_longitud"));
        columns.add(Column.aliased("direccion", table, columnPrefix + "_direccion"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));
        columns.add(Column.aliased("telefono_fijo", table, columnPrefix + "_telefono_fijo"));
        columns.add(Column.aliased("telefono_celular", table, columnPrefix + "_telefono_celular"));
        columns.add(Column.aliased("correo_electronico", table, columnPrefix + "_correo_electronico"));
        columns.add(Column.aliased("codigo_indicativo", table, columnPrefix + "_codigo_indicativo"));

        return columns;
    }
}
