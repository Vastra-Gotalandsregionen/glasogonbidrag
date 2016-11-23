package se.vgregion.service.glasogonbidrag.types.query;

import se.vgregion.service.glasogonbidrag.types.query.conditions.WhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.Join;

import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public abstract class Query implements JpqlTerm {
    private final From from;
    private final List<Join> joins;
    private final List<WhereCondition> conditions;

    public Query(From from,
                 List<Join> joins,
                 List<WhereCondition> conditions) {
        this.from = from;
        this.joins = joins;
        this.conditions = conditions;
    }

    protected String getFrom() {
        return from.toJpqlString();
    }

    protected String getJoins() {
        return joinTerms(joins, " ");
    }

    protected String getConditions() {
        return joinTerms(conditions, " AND ");
    }

    protected <T extends JpqlTerm> String joinTerms(List<T> join,
                                                    String separator) {
        StringBuilder builder = new StringBuilder();

        Iterator<T> iterator = join.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next().toJpqlString());
        }
        while (iterator.hasNext()) {
            builder.append(separator).append(iterator.next().toJpqlString());
        }

        return builder.toString();
    }

    protected <T> String joinStrings(List<T> join, String separator) {
        StringBuilder builder = new StringBuilder();

        Iterator<T> iterator = join.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next().toString());
        }
        while (iterator.hasNext()) {
            builder.append(separator).append(iterator.next().toString());
        }

        return builder.toString();
    }
}
