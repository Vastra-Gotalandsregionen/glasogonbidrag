package se.vgregion.service.glasogonbidrag.types;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StatisticSearchDateInterval {
    private static final DateFormat FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final boolean singleDate;
    private final Date[] dates;

    public StatisticSearchDateInterval(Date date) {
        singleDate = true;

        dates = new Date[1];
        dates[0] = date;
    }

    public StatisticSearchDateInterval(Date start, Date end) {
        singleDate = false;

        dates = new Date[2];
        dates[0] = start;
        dates[1] = end;
    }

    public boolean isInterval() {
        return !singleDate;
    }

    public String getDate() {
        if (singleDate) {
            return FORMAT.format(dates[0]);
        } else {
            throw new IllegalStateException(
                    "May not call getDate() on interval object");
        }
    }

    public String getStart() {
        if (!singleDate) {
            return FORMAT.format(dates[0]);
        } else {
            throw new IllegalStateException(
                    "May not call getStart() on single date object");
        }
    }

    public String getEnd() {
        if (!singleDate) {
            return FORMAT.format(dates[1]);
        } else {
            throw new IllegalStateException(
                    "May not call getEnd() on single date object");
        }
    }
}
