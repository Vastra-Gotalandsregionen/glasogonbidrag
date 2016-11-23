package se.vgregion.service.glasogonbidrag.types.query;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.Join;

import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BasicQuery extends Query {
    private final List<String> select;

    public BasicQuery(List<String> select,
                      From from,
                      List<Join> joins,
                      List<WhereCondition> conditions) {
        super(from, joins, conditions);
        this.select = select;
    }

    @Override
    public String toJpqlString() {
        return null;
    }
}
