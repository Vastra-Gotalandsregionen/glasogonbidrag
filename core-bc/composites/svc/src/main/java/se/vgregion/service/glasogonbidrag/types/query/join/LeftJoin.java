package se.vgregion.service.glasogonbidrag.types.query.join;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;

/**
 * @author Martin Lind
 */
public class LeftJoin implements Join {
    private final String table;
    private final String variable;
    private final WhereCondition condition;

    public LeftJoin(String table, String variable, WhereCondition condition) {
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
    public String toSqlString() {
        return "LEFT JOIN " + table + " " + variable + " " +
                "ON " + condition.toSqlString();
    }
}
