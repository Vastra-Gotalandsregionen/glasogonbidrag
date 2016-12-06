package se.vgregion.service.glasogonbidrag.types.lowlevel;

import se.vgregion.service.glasogonbidrag.types.OrderType;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class SupplierOrder {
    private boolean orderBySupplierName = false;
    private boolean orderByExternalServiceId = false;
    private boolean orderByCount = false;
    private boolean orderByActive = false;

    private OrderType orderType;

    /**
     * Helper method that transform a string to the correct boolean
     * value.
     *
     * @param string containing one of the booleans that should be ordered by
     * @param orderType direction of ordering.
     */
    public SupplierOrder(String string, OrderType orderType) {
        this("name".equals(string),
                "externalServiceId".equals(string),
                "count".equals(string),
                "active".equals(string),
                orderType);
    }

    public SupplierOrder(boolean orderBySupplierName,
                         boolean orderByExternalServiceId,
                         boolean orderByCount,
                         boolean orderByActive,
                         OrderType orderType) {
        this.orderBySupplierName = orderBySupplierName;
        this.orderByExternalServiceId = orderByExternalServiceId;
        this.orderByCount = orderByCount;
        this.orderByActive = orderByActive;

        this.orderType = orderType;
    }

    public boolean isOrderBySupplierName() {
        return orderBySupplierName;
    }

    public boolean isOrderByExternalServiceId() {
        return orderByExternalServiceId;
    }

    public boolean isOrderByCount() {
        return orderByCount;
    }

    public boolean isOrderByActive() {
        return orderByActive;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public boolean hasOrderBy() {
        return isOrderBySupplierName() ||
                isOrderByExternalServiceId() ||
                isOrderByCount() ||
                isOrderByActive();
    }
}
