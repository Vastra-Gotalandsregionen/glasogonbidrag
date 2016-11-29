package se.vgregion.service.glasogonbidrag.types.query;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.Join;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AggregationSqlQuery extends SqlQuery {
    private final List<String> select;
    private final List<String> aggregationFunctions;
    private final List<String> groupBy;

    public AggregationSqlQuery(List<String> select,
                               List<String> aggregationFunctions,
                               From from,
                               List<Join> joins,
                               List<WhereCondition> conditions,
                               List<String> groupBy) {
        super(from, joins, conditions);
        this.select = select;
        this.aggregationFunctions = aggregationFunctions;
        this.groupBy = groupBy;
    }

    public String toSqlString() {
        List<String> selectList = new ArrayList<>();

        String groupByString = String.format("(%s) AS group_data",
                joinStrings(groupBy, "||"));

        selectList.add(groupByString);
        selectList.addAll(aggregationFunctions);
        selectList.addAll(select);

        String selectString = joinStrings(selectList, ", ");

        return "SELECT " + selectString + " " +
               "FROM " + getFrom() + " " +
                         getJoins() + " " +
               "WHERE " + getConditions() + " " +
               "GROUP BY " + joinStrings(groupBy, ", ");
    }
}
