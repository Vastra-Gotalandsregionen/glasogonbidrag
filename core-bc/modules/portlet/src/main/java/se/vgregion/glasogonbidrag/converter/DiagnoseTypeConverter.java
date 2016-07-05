package se.vgregion.glasogonbidrag.converter;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@FacesConverter("se.vgregion.DiagnoseType")
public class DiagnoseTypeConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context,
                              UIComponent component,
                              String value) {
        if (value == null) {
            return null;
        }

        for (DiagnoseType type : DiagnoseType.values()) {
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
        if (value instanceof DiagnoseType) {
            DiagnoseType type = (DiagnoseType)value;

            return type.toString();
        }

        return null;
    }
}
