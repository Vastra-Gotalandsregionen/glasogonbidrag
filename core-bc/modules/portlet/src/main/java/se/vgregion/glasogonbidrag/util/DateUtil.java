package se.vgregion.glasogonbidrag.util;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import org.primefaces.component.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Scope(value = "prototype")
public class DateUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DateUtil.class);

    public List<Date> getDatesOfThisWeek() {

        ArrayList<Date> datesOfWeek = new ArrayList<Date>();

        Calendar now = Calendar.getInstance();

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday

        now.add(Calendar.DAY_OF_MONTH, delta );

        for (int i = 0; i < 7; i++) {
            datesOfWeek.add(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return datesOfWeek;
    }


}
