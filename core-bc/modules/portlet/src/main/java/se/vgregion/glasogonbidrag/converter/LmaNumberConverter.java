package se.vgregion.glasogonbidrag.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.CharacterConverter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Erik Andersson - Monator Technologies AB
 */

@FacesConverter(value="se.vgregion.LmaNumberConverter")
public class LmaNumberConverter implements Converter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MultiDateConverter.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        boolean isValidLmaNumber = false;

        String number = value;

        // Check length of number
        if(number.length() == 8) {
            // Pattern candidate 00000000
            if(textHasOnlyNumbers(number)) {
                isValidLmaNumber = true;

                // Format LMA-number
                StringBuilder sb = new StringBuilder(number);
                sb.insert(2, "-");

                number = sb.toString();
            }
        } else if(number.length() == 9) {
            // Pattern candidate 00-000000
            String thirdChar = Character.toString(number.charAt(2));

            // Third character must be "-" when length is 9
            if("-".equals(thirdChar)) {
                StringBuilder sb = new StringBuilder(value);
                sb.deleteCharAt(2);
                String trimmedValue = sb.toString();

                // The rest of the characters must be numbers
                if(textHasOnlyNumbers(trimmedValue)) {
                    isValidLmaNumber = true;
                }
            }

        }

        if(!isValidLmaNumber) {
            throw new ConverterException(new FacesMessage("Invalid LmaNumber format."));
        }

        return number;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        return (String)value;
    }

    private boolean textHasOnlyNumbers(String text) {
        return text.matches("[0-9]+");
    }

}
