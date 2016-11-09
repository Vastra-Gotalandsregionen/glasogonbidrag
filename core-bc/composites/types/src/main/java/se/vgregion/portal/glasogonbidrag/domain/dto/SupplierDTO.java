package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class SupplierDTO {
    private final long id;
    private final String externalServiceId;
    private final long count;
    private final boolean active;
    private final Supplier supplier;

    public SupplierDTO(long id, String externalServiceId, long count, boolean active) {
        this.id = id;
        this.externalServiceId = externalServiceId;
        this.count = count;
        this.active = active;
        this.supplier = null;
    }

    public SupplierDTO(Supplier supplier) {
        this.supplier = supplier;
        this.id = supplier.getId();
        this.externalServiceId = supplier.getExternalServiceId();
        this.count = supplier.getInvoices().size();
        this.active = supplier.isActive();
    }

    public long getId() {
        return id;
    }

    public String getExternalServiceId() {
        return externalServiceId;
    }

    public long getCount() {
        return count;
    }

    public boolean isActive() {
        return active;
    }

    public Supplier getSupplier() {
        return supplier;
    }
}
