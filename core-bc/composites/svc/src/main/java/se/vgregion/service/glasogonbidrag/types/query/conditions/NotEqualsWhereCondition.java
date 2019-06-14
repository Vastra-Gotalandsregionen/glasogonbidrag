package se.vgregion.service.glasogonbidrag.types.query.conditions;

import se.vgregion.service.glasogonbidrag.types.query.SqlTerm;

public class NotEqualsWhereCondition implements SqlTerm, WhereCondition {
    private final String leftHandSide;
    private final String rightHandSide;

    public NotEqualsWhereCondition(String leftHandSide, String rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public String toSqlString() {
        return leftHandSide + " != " + rightHandSide;
    }
}
