package se.vgregion.service.glasogonbidrag.types.query.conditions;

import se.vgregion.service.glasogonbidrag.types.query.SqlTerm;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BetweenWhereCondition implements SqlTerm, WhereCondition {
    private final String equalsObject;
    private final String start;
    private final String stop;

    public BetweenWhereCondition(String equalsObject,
                                 String start, String stop) {
        this.equalsObject = equalsObject;

        this.start = start;
        this.stop = stop;
    }

    public String toSqlString() {
        return equalsObject + " BETWEEN " + start + " AND " + stop;
    }
}
