package se.vgregion.service.glasogonbidrag.types.query;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class QueryBuilderFactory {
    public static AggregationSqlQueryBuilder simpleQuery() {
        return new AggregationSqlQueryBuilder();
    }


}
