package se.vgregion.service.glasogonbidrag.types.query;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.Join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AggregationQueryBuilder {
    private Class<?> type;
    private List<String> select;
    private From from;
    private List<String> groupBy;
    private List<String> aggregations;
    private List<WhereCondition> conditions;
    private List<Join> joins;

    public AggregationQueryBuilder() {
    }

    public AggregationQueryBuilder type(Class<?> type) {
        this.type = type;
        return this;
    }

    public AggregationQueryBuilder select() {
        this.select = new ArrayList<>();
        return this;
    }

    public AggregationQueryBuilder select(String... select) {
        this.select = Arrays.asList(select);
        return this;
    }

    public AggregationQueryBuilder from(From from) {
        this.from = from;
        return this;
    }

    public AggregationQueryBuilder groupBy(String... groupBy) {
        this.groupBy = Arrays.asList(groupBy);
        return this;
    }

    public AggregationQueryBuilder agg(String... aggregations) {
        this.aggregations = Arrays.asList(aggregations);
        return this;
    }

    public AggregationQueryBuilder where(
            WhereCondition... conditions) {
        this.conditions = Arrays.asList(conditions);
        return this;
    }

    public AggregationQueryBuilder join(Join... joins) {
        this.joins = Arrays.asList(joins);
        return this;
    }

    public AggregationQuery build() {
        return new AggregationQuery(
                select, aggregations, from, joins, conditions, groupBy, type);
    }
}
