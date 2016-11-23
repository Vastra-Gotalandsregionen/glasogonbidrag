package se.vgregion.service.glasogonbidrag.types.query.join;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;

/**
 * @author Martin Lind
 */
public class LeftJoin implements Join {
    private final String classType;
    private final String variable;

    public LeftJoin(String classType, String variable) {
        this.classType = classType;
        this.variable = variable;
    }
    @Override
    public String toJpqlString() {
        return "LEFT JOIN " + classType + " " + variable;
    }
}
