package se.vgregion.service.glasogonbidrag.types.query.conditions;

import se.vgregion.service.glasogonbidrag.types.query.JpqlTerm;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class EqualsWhereCondition implements JpqlTerm, WhereCondition {
    private final String leftHandSide;
    private final String rightHandSide;

    public EqualsWhereCondition(String leftHandSide, String rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public String toJpqlString() {
        return leftHandSide + " = " + rightHandSide;
    }
}
