package se.vgregion.service.glasogonbidrag.types.query.conditions;

import se.vgregion.service.glasogonbidrag.types.query.SqlTerm;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class EqualsWhereCondition implements SqlTerm, WhereCondition {
    private final String leftHandSide;
    private final String rightHandSide;

    public EqualsWhereCondition(String leftHandSide, String rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public String toSqlString() {
        return leftHandSide + " = " + rightHandSide;
    }
}
