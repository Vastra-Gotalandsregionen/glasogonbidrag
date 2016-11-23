package se.vgregion.service.glasogonbidrag.types.query.conditions;

import se.vgregion.service.glasogonbidrag.types.query.JpqlTerm;

/**
 * @author Martin Lind
 */
public class InstanceOfWhereCondition implements JpqlTerm, WhereCondition {
    private final String variable;
    private final Class<?> instance;

    public InstanceOfWhereCondition(String variable, Class<?> instance) {
        this.variable = variable;
        this.instance = instance;
    }

    @Override
    public String toJpqlString() {
        return "TYPE(" + variable + ") = " + instance.getSimpleName();
    }
}
