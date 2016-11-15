package se.vgregion.service.glasogonbidrag.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class LowLevelSortOrder {
    private final String orderBy;
    private final String defaultOrderBy;
    private final OrderType orderType;
    private final Map<String, Object> filters;
    private final Map<String, String> orderMap;
    private final Map<String, String> filtersMap;

    public LowLevelSortOrder(String orderBy,
                             String defaultOrderBy,
                             OrderType orderType,
                             Map<String, String> orderMap) {
        this(orderBy, defaultOrderBy, orderType, new HashMap<String, Object>(),
                orderMap, new HashMap<String, String>());
    }

    public LowLevelSortOrder(String orderBy,
                             String defaultOrderBy,
                             OrderType orderType,
                             Map<String, Object> filters,
                             Map<String, String> orderMap,
                             Map<String, String> filtersMap) {
        this.orderBy = orderBy;
        this.defaultOrderBy = defaultOrderBy;
        this.orderType = orderType;
        this.filters = filters;
        this.orderMap = orderMap;
        this.filtersMap = filtersMap;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getDefaultOrderBy() {
        return defaultOrderBy;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public Map<String, String> getOrderMap() {
        return orderMap;
    }

    public Map<String, String> getFiltersMap() {
        return filtersMap;
    }

    @Override
    public String toString() {
        String orderByValue = defaultOrderBy;
        if (orderBy != null) {
            orderByValue = orderMap.get(orderBy);
        }

        return String.format("%s %s", orderByValue, orderType);
    }

    public String getFilterString() {
        StringBuilder builder = new StringBuilder();

        Iterator<String> iterator = filters.keySet().iterator();
        if (iterator.hasNext()) {
            getFilterStringValue(builder, iterator);
        }
        while (iterator.hasNext()) {
            builder.append(" AND ");
            getFilterStringValue(builder, iterator);
        }

        return builder.toString();
    }

    private void getFilterStringValue(StringBuilder builder,
                                      Iterator<String> iterator) {
        String s = iterator.next();
        String value = filters.get(s).toString();

        if (filtersMap.containsKey(value)) {
            value = filtersMap.get(value);
        }

        String key = orderMap.get(s);
        builder.append(key)
                .append(" LIKE '%").append(value).append("%'");
    }
}
