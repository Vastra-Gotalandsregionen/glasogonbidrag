package se.vgregion.service.glasogonbidrag.types.query.join;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class LeftSqlJoin implements Join {
    private final String table;
    private final String variable;
    private final WhereCondition condition;

    public LeftSqlJoin(String table, String variable, WhereCondition condition) {
        this.table = table;
        this.variable = variable;
        this.condition = condition;
    }

    public String getTable() {
        return table;
    }

    public String getVariable() {
        return variable;
    }

    public WhereCondition getCondition() {
        return condition;
    }

    @Override
    public String toJpqlString() {
        return "LEFT JOIN " + table + " " + variable + " " +
               "ON " + condition.toJpqlString();
    }
}
