package se.vgregion.portal.glasogonbidrag.domain.dto;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StatisticReportDTO {
    private final long count;
    private final long amount;
    private final String data;

    public StatisticReportDTO(long count, long amount, String data) {
        this.count = count;
        this.amount = amount;
        this.data = data;
    }

    public long getAmount() {
        return amount;
    }

    public long getCount() {
        return count;
    }

    public String getData() {
        return data;
    }
}
