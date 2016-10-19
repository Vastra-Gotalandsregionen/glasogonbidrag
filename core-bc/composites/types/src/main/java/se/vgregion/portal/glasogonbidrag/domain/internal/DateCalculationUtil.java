package se.vgregion.portal.glasogonbidrag.domain.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility that will extract numbers from swedish national identification
 * numbers and reserve numbers.
 * It also converts a string into a actual Java Date object.
 *
 * @author Martin Lind - Monator Technologies AB
 */
public class DateCalculationUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DateCalculationUtil.class);

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

    /**
     * Method that will parse a string and return a date object at the
     * time specified in the string.
     *
     * Accept a string in the format <code>yyyyMMdd</code>.
     *
     * @param date a date that should be converted to Date object.
     * @return A date without time.
     * @throws IllegalArgumentException will be thrown if the format of
     *         the date is wrong.
     */
    public Date dateFromString(String date) {
        try {
            return FORMAT.parse(date);
        } catch (ParseException e) {
            LOGGER.warn("Error parsing date string. Format must be yyyyMMdd");
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Will take a personal number string in the local format and
     * extract the first six numbers, which is the birth day in
     * the format <code>yyyyMMdd</code>.
     *
     * @param localFormat personal number in local format.
     * @return Return the date part from a personal number.
     */
    public String extractDateFromPersonalNumbers(String localFormat) {
        return localFormat.substring(0, 6);
    }
}
