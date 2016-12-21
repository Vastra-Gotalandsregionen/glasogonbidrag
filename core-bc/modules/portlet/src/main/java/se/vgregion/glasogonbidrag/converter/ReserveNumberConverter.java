package se.vgregion.glasogonbidrag.converter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Erik Andersson - Monator Technologies AB
 */

@FacesConverter(value="se.vgregion.ReserveNumberConverter")
public class ReserveNumberConverter implements Converter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MultiDateConverter.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        String number = formatReserveNumber(value);

        if(!testIsValidReserveNumber(number)) {
            throw new ConverterException(new FacesMessage("Invalid LmaNumber format."));
        }

        return number;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        return (String)value;
    }

    private String formatReserveNumber(String number) {
        String formattedNumber = number;

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if(number.length() == 13) {
            // Pattern candidate yyyyMMdd-XXXX
        } else if(number.length() == 12) {
            // Pattern candidate yyyyMMddXXXX
            formattedNumber = addDashToNumber(number, 8);
        } else if(number.length() == 11) {
            // Pattern candidate yyMMdd-XXXX
            if(textHasOnlyNumbers(number.substring(0, 2))) {
                formattedNumber = addCenturyToNumber(number, currentYear);
            }
        } else if(number.length() == 10) {
            // Pattern candidate yyMMddXXXX
            if(textHasOnlyNumbers(number.substring(0, 2))) {
                number = addCenturyToNumber(number, currentYear);
            }
            formattedNumber = addDashToNumber(number, 8);
        }

        return formattedNumber;
    }

    private boolean testIsValidReserveNumber(String number) {
        boolean isValid = false;

        if(number.length() == 13) {
            Date date = parseDate(number.substring(0, 8), "yyyyMMdd");

            String separator = number.substring(8, 9);
            String lastNumbers = number.substring(9, 13);

            // If separator is not a dash
            if(!"-".equals(separator)) {
                isValid = false;
                return isValid;
            }

            // If lastNumbers are not alphaNumeric
            if(!StringUtils.isAlphanumeric(lastNumbers)) {
                isValid = false;
                return isValid;
            }

            if(date != null) {
                Calendar cal = new GregorianCalendar();

                // Get current year
                int currentYear = cal.get(Calendar.YEAR);

                // Get reserve number year
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);

                // Year cannot be in the future nor older than system initial birth year for beneficiary
                if(year <= currentYear && year >= Beneficiary.SYSTEM_INITIAL_BIRTH_YEAR) {
                    isValid = true;
                }
            }
        }

        return isValid;
    }

    private String addCenturyToNumber(String reserveNumber, int currentYear) {
        String yearStr = reserveNumber.substring(0, 2);

        String centuryString;

        int decade = Integer.parseInt(yearStr.substring(0, 2));

        String currentYearStr = String.valueOf(currentYear);
        int currentYearCentury = Integer.parseInt(currentYearStr.substring(0, 2));
        int currentYearDecade = Integer.parseInt(currentYearStr.substring(2, 4));

        if(decade <= currentYearDecade) {
            centuryString = String.valueOf(currentYearCentury);
        } else {
            centuryString = String.valueOf(currentYearCentury-1);
        }

        String reserveNumberConverted = centuryString + reserveNumber;

        return reserveNumberConverted;
    }

    private String addDashToNumber(String number, int position) {
        StringBuilder sb = new StringBuilder(number);
        sb.insert(position, "-");

        return sb.toString();
    }

    private Date parseDate(String value, String pattern) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            date = sdf.parse(value);
        } catch (ParseException ignore) {
        }

        return date;
    }

    private boolean textHasOnlyNumbers(String text) {
        return text.matches("[0-9]+");
    }

}
