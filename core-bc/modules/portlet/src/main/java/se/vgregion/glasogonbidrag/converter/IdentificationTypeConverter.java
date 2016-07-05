package se.vgregion.glasogonbidrag.converter;

import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@FacesConverter("se.vgregion.IdentificationType")
public class IdentificationTypeConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context,
                              UIComponent component,
                              String value) {
        if (value == null) {
            return null;
        }

        for (IdentificationType type : IdentificationType.values()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext context,
                              UIComponent component,
                              Object value) {
        if (value instanceof IdentificationType) {
            IdentificationType type = (IdentificationType)value;

            return type.toString();
        }

        return null;
    }
}
