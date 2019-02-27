package se.vgregion.glasogonbidrag.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = "prototype")
public class DateUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DateUtil.class);

    public List<Date> getDatesOfThisWeek(Date today) {

        ArrayList<Date> datesOfWeek = new ArrayList<Date>();

        Calendar now = Calendar.getInstance();
        now.setTime(today);

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday

        now.add(Calendar.DAY_OF_MONTH, delta );

        for (int i = 0; i < 7; i++) {
            datesOfWeek.add(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return datesOfWeek;
    }


}
