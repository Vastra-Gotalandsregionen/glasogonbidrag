package se.vgregion.service.glasogonbidrag.types.query;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.Join;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BasicSqlQuery extends SqlQuery {
    private final List<String> select;

    public BasicSqlQuery(List<String> select,
                         From from,
                         List<Join> joins,
                         List<WhereCondition> conditions) {
        super(from, joins, conditions);
        this.select = select;
    }

    @Override
    public String toSqlString() {
        return null;
    }
}
