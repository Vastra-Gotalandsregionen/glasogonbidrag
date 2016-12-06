package se.vgregion.service.glasogonbidrag.types.lowlevel;

import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class SupplierFilter {
    private String supplierName = null;
    private String externalServiceId = null;
    private Boolean active = null;

    public SupplierFilter(Map<String, Object> filters) {
        if (filters.containsKey("name")) {
            supplierName = (String) filters.get("name");
        }

        if (filters.containsKey("externalServiceId")) {
            externalServiceId = (String) filters.get("externalServiceId");
        }

        if (filters.containsKey("active")) {
            String activeString = (String) filters.get("active");
            active = "yes".equals(activeString) || !"no".equals(activeString);
        }
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getExternalServiceId() {
        return externalServiceId;
    }

    public Boolean getActive() {
        return active;
    }

    public boolean hasSupplierName() {
        return supplierName != null;
    }

    public boolean hasExternalServiceId() {
        return externalServiceId != null;
    }

    public boolean hasActive() {
        return active != null;
    }

    public boolean hasFilters() {
        return hasSupplierName()
                || hasExternalServiceId()
                || hasActive();
    }
}
