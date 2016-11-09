package se.vgregion.service.glasogonbidrag.types;

import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class LowLevelSortOrder {
    private final String orderBy;
    private final String defaultOrderBy;
    private final OrderType orderType;
    private final Map<String, String> orderMap;

    public LowLevelSortOrder(String orderBy,
                             String defaultOrderBy,
                             OrderType orderType,
                             Map<String, String> orderMap) {
        this.orderBy = orderBy;
        this.defaultOrderBy = defaultOrderBy;
        this.orderType = orderType;
        this.orderMap = orderMap;
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

    public Map<String, String> getOrderMap() {
        return orderMap;
    }

    @Override
    public String toString() {
        String orderByValue = defaultOrderBy;
        if (orderBy != null) {
            orderByValue = orderMap.get(orderBy);
        }

        return String.format("%s %s", orderByValue, orderType);
    }
}
