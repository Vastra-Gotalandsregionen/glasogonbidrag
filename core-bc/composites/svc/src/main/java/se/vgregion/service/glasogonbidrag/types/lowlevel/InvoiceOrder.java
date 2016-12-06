package se.vgregion.service.glasogonbidrag.types.lowlevel;

import se.vgregion.service.glasogonbidrag.types.OrderType;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class InvoiceOrder {
    private boolean orderByVerificationNumber = false;
    private boolean orderBySupplier = false;
    private boolean orderByInvoiceNumber = false;
    private boolean orderByAmount = false;
    private boolean orderByCount = false;
    private boolean orderByStatus = false;
    private boolean orderByCaseWorker = false;

    private OrderType orderType;

    public InvoiceOrder(String string, OrderType orderType) {
        this("verificationNumber".equals(string),
                "supplier".equals(string),
                "invoiceNumber".equals(string),
                "amount".equals(string),
                "count".equals(string),
                "status".equals(string),
                "caseWorker".equals(string),
                orderType);
    }

    public InvoiceOrder(boolean orderByVerificationNumber,
                        boolean orderBySupplier,
                        boolean orderByInvoiceNumber,
                        boolean orderByAmount,
                        boolean orderByCount,
                        boolean orderByStatus,
                        boolean orderByCaseWorker,
                        OrderType orderType) {
        this.orderByVerificationNumber = orderByVerificationNumber;
        this.orderBySupplier = orderBySupplier;
        this.orderByInvoiceNumber = orderByInvoiceNumber;
        this.orderByAmount = orderByAmount;
        this.orderByCount = orderByCount;
        this.orderByStatus = orderByStatus;
        this.orderByCaseWorker = orderByCaseWorker;
        this.orderType = orderType;
    }

    public boolean isOrderByVerificationNumber() {
        return orderByVerificationNumber;
    }

    public boolean isOrderBySupplier() {
        return orderBySupplier;
    }

    public boolean isOrderByInvoiceNumber() {
        return orderByInvoiceNumber;
    }

    public boolean isOrderByAmount() {
        return orderByAmount;
    }

    public boolean isOrderByCount() {
        return orderByCount;
    }

    public boolean isOrderByStatus() {
        return orderByStatus;
    }

    public boolean isOrderByCaseWorker() {
        return orderByCaseWorker;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public boolean hasOrderBy() {
        return isOrderByVerificationNumber() ||
                isOrderBySupplier() ||
                isOrderByInvoiceNumber() ||
                isOrderByAmount() ||
                isOrderByCount() ||
                isOrderByStatus() ||
                isOrderByCaseWorker();
    }
}
