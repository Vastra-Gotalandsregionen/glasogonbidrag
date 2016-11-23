package se.vgregion.portal.glasogonbidrag.domain.dto;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StatisticReportDTO {
    private final String county;
    private final String municipality;
    private final long count;
    private final long amount;

    public StatisticReportDTO(String county, String municipality, long count, long amount) {
        this.county = county;
        this.municipality = municipality;
        this.count = count;
        this.amount = amount;
    }

    public String getCounty() {
        return county;
    }

    public String getMunicipality() {
        return municipality;
    }

    public long getAmount() {
        return amount;
    }

    public long getCount() {
        return count;
    }
}
