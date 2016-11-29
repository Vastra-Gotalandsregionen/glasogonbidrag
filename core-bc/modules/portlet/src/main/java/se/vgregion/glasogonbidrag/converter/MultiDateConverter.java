package se.vgregion.glasogonbidrag.converter;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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

@FacesConverter(value="se.vgregion.MultiDateConverter")
public class MultiDateConverter implements Converter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MultiDateConverter.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        List<String> patterns = getPatterns(component);
        Date date = null;

        for (String pattern : patterns) {

            LOGGER.info("pattern: " + pattern);

            // Allow empty pattern (return null value for date)
            if("".equals(pattern)) {
                return null;
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false); // Don't parse dates like 33-33-3333.

            try {
                date = sdf.parse(value);
                break;
            } catch (ParseException ignore) {
                //
            }
        }

        if (date == null) {
            throw new ConverterException(new FacesMessage("Invalid date format, must match either of " + patterns));
        }

        return date;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        return new SimpleDateFormat(getPatterns(component).get(0)).format((Date) value);
    }

    private static List<String> getPatterns(UIComponent component) {
        List<String> patterns = new ArrayList<String>();

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String pattern = (String) component.getAttributes().get("pattern" + i);

            if (pattern != null) {
                patterns.add(pattern);
            } else {
                break;
            }
        }

        if (patterns.isEmpty()) {
            throw new IllegalArgumentException("Please provide <f:attribute name=\"patternX\"> where X is the order number");
        }

        return patterns;
    }

}
