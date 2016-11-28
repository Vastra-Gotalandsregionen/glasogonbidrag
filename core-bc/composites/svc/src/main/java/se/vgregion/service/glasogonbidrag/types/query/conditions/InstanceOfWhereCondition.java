package se.vgregion.service.glasogonbidrag.types.query.conditions;

import se.vgregion.service.glasogonbidrag.types.query.SqlTerm;

/**
 * @author Martin Lind
 */
public class InstanceOfWhereCondition implements SqlTerm, WhereCondition {
    private final String variable;
    private final Class<?> instance;

    public InstanceOfWhereCondition(String variable, Class<?> instance) {
        this.variable = variable;
        this.instance = instance;
    }

    @Override
    public String toSqlString() {
        return "TYPE(" + variable + ") = " + instance.getCanonicalName();
    }
}
