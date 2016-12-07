package se.vgregion.service.glasogonbidrag.types.filter;

import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryFilter {
    private String number = null;
    private String fullName = null;

    public BeneficiaryFilter(Map<String, Object> filters) {
        if (filters.containsKey("number")) {
            number = (String) filters.get("number");
        }

        if (filters.containsKey("fullName")) {
            fullName = (String) filters.get("fullName");
        }
    }

    public String getNumber() {
        return number;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean hasNumberFilter() {
        return number != null;
    }

    public boolean hasFullNameFilter() {
        return fullName != null;
    }

    public boolean hasFilters() {
        return hasNumberFilter() || hasFullNameFilter();
    }
}
