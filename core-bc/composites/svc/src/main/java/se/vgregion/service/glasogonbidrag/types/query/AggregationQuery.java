package se.vgregion.service.glasogonbidrag.types.query;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.Join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AggregationQuery extends Query {
    private final List<String> select;
    private final List<String> aggregationFunctions;
    private final List<String> groupBy;
    private final Class<?> type;

    public AggregationQuery(List<String> select,
                            List<String> aggregationFunctions,
                            From from,
                            List<Join> joins,
                            List<WhereCondition> conditions,
                            List<String> groupBy,
                            Class<?> type) {
        super(from, joins, conditions);
        this.select = select;
        this.aggregationFunctions = aggregationFunctions;
        this.groupBy = groupBy;
        this.type = type;
    }

    public String toJpqlString() {
        List<String> selectList = new ArrayList<>();
        selectList.addAll(groupBy);
        selectList.addAll(aggregationFunctions);
        selectList.addAll(select);

        String selectString = joinStrings(selectList, ", ");
        if (type != null) {
            selectString = "new " + type.getCanonicalName()
                    + "(" + selectString + ")";
        }

        return "SELECT " + selectString + " " +
               "FROM " + getFrom() + " " +
                         getJoins() + " " +
               "WHERE " + getConditions() + " " +
               "GROUP BY " + joinStrings(groupBy, ", ");
    }
}
