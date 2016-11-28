package se.vgregion.service.glasogonbidrag.types.query;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.Join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AggregationSqlQueryBuilder {
    private List<String> select;
    private From from;
    private List<String> groupBy;
    private List<String> aggregations;
    private List<WhereCondition> conditions;
    private List<Join> joins;

    public AggregationSqlQueryBuilder() {
        conditions = new ArrayList<>();
    }

    public AggregationSqlQueryBuilder select() {
        this.select = new ArrayList<>();
        return this;
    }

    public AggregationSqlQueryBuilder select(String... select) {
        this.select = Arrays.asList(select);
        return this;
    }

    public AggregationSqlQueryBuilder from(From from) {
        this.from = from;
        return this;
    }

    public AggregationSqlQueryBuilder groupBy(String... groupBy) {
        this.groupBy = Arrays.asList(groupBy);
        return this;
    }

    public AggregationSqlQueryBuilder agg(String... aggregations) {
        this.aggregations = Arrays.asList(aggregations);
        return this;
    }

    public AggregationSqlQueryBuilder where(WhereCondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public AggregationSqlQueryBuilder join(Join... joins) {
        this.joins = Arrays.asList(joins);
        return this;
    }

    public AggregationSqlQuery build() {
        return new AggregationSqlQuery(
                select, aggregations, from, joins, conditions, groupBy);
    }
}
