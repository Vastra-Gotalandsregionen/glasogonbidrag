package se.vgregion.glasogonbidrag.converter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;
import se.vgregion.service.glasogonbidrag.local.internal.StaticApplicationContext;

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

@FacesConverter(value="se.vgregion.PersonNumberConverter")
public class PersonNumberConverter implements Converter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MultiDateConverter.class);

    private PersonalNumberService numberService;

    public PersonNumberConverter() {
        ApplicationContext context = StaticApplicationContext.getContext();
        numberService = context.getBean(PersonalNumberService.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        String number = formatPersonNumber(value);

        if(!testIsValidPersonNumber(number)) {
            throw new ConverterException(new FacesMessage("Invalid PersonalNumber format."));
        }

        return number;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        return (String)value;
    }

    private String formatPersonNumber(String number) {
        String formattedNumber = number;

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Strip dash
        formattedNumber = formattedNumber.replace("-", "");

        // Length should now be either 12 or 10
        if(formattedNumber.length() == 10) {
            if(StringUtils.isNumeric(formattedNumber)) {
                formattedNumber = addCenturyToNumber(formattedNumber, currentYear);
            }
        }

        return formattedNumber;
    }

    private boolean testIsValidPersonNumber(String number) {
        boolean isValid = false;

        if(number.length() == 12 && StringUtils.isNumeric(number)) {

            isValid = numberService.validate(number);

//            Date date = parseDate(number.substring(0, 8), "yyyyMMdd");
//
//            if(date != null) {
//                Calendar cal = new GregorianCalendar();
//
//                // Get current year
//                int currentYear = cal.get(Calendar.YEAR);
//
//                // Get person number year
//                cal.setTime(date);
//                int year = cal.get(Calendar.YEAR);
//
//                // Year cannot be in the future nor older than system initial birth year for beneficiary
//                if(year <= currentYear && year >= Beneficiary.SYSTEM_INITIAL_BIRTH_YEAR) {
//                    isValid = true;
//                }
//            }

        }

        return isValid;
    }

    private String addCenturyToNumber(String personNumber, int currentYear) {
        String yearStr = personNumber.substring(0, 2);

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

        String personNumberConverted = centuryString + personNumber;

        return personNumberConverted;
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

}
