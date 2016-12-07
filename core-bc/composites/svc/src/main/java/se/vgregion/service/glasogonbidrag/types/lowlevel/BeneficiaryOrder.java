package se.vgregion.service.glasogonbidrag.types.lowlevel;

import se.vgregion.service.glasogonbidrag.types.OrderType;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryOrder {
    private boolean orderByNumber = false;
    private boolean orderByFullName = false;
    private boolean orderByCount = false;

    private OrderType orderType;

    public BeneficiaryOrder(String string, OrderType orderType) {
        this("number".equals(string),
                "fullName".equals(string),
                "count".equals(string),
                orderType);
    }

    public BeneficiaryOrder(boolean orderByNumber,
                            boolean orderByFullName,
                            boolean orderByCount,
                            OrderType orderType) {
        this.orderByNumber = orderByNumber;
        this.orderByFullName = orderByFullName;
        this.orderByCount = orderByCount;
        this.orderType = orderType;
    }

    public boolean isOrderByNumber() {
        return orderByNumber;
    }

    public boolean isOrderByFullName() {
        return orderByFullName;
    }

    public boolean isOrderByCount() {
        return orderByCount;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public boolean hasOrderBy() {
        return isOrderByNumber() ||
                isOrderByFullName() ||
                isOrderByCount();
    }
}
