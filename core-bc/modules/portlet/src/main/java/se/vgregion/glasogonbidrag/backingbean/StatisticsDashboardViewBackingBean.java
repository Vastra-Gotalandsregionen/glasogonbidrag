package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.mock.StatisticsMockUtil;
import se.vgregion.glasogonbidrag.util.DateUtil;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "statisticsDashboardViewBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsDashboardViewBackingBean {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticsDashboardViewBackingBean.class);

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

    @Autowired
    private StatisticsMockUtil statisticsMockUtil;

    @Autowired
    private GrantService grantService;

    private String daysOfWeekJSONString;

    private int grantCountToday;

    private String grantsCountOfWeekDaysJSONString;

    private BigDecimal progressToday;

    private String progressOfWeekJSONString;

    public String getDaysOfWeekJSONString() {
        return daysOfWeekJSONString;
    }

    public int getGrantCountToday() {
        return grantCountToday;
    }

    public String getGrantsCountOfWeekDaysJSONString() {
        return grantsCountOfWeekDaysJSONString;
    }

    public BigDecimal getProgressToday() {
        return progressToday;
    }

    public String getProgressOfWeekJSONString() {
        return progressOfWeekJSONString;
    }

    public void changeStatisticsFilterListener() {
    }

    @PostConstruct
    protected void init() {
        Locale locale = liferayUtil.getLocale();
        KronaCalculationUtil currency = new KronaCalculationUtil();
        SimpleDateFormat format = new SimpleDateFormat("EEE", locale);

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();

        // Grants count today
        grantCountToday = (int)grantService.countByDate(today);

        // Progress today
        long progressTodayCents = grantService.currentProgressByDate(today);
        progressToday = currency.calculatePartsAsKrona(progressTodayCents);

        JSONArray daysOfWeekJSON = JSONFactoryUtil.createJSONArray();
        JSONArray countOfWeekDaysJSON = JSONFactoryUtil.createJSONArray();
        JSONArray progressOfWeekJSON = JSONFactoryUtil.createJSONArray();

        List<Date> datesOfThisWeek = dateUtil.getDatesOfThisWeek(today);
        for(Date date : datesOfThisWeek) {
            // Days of week
            String dateString = format.format(date);
            daysOfWeekJSON.put(dateString);

        // Grants counts of week days
            int count = (int)grantService.countByDate(date);
            countOfWeekDaysJSON.put(count);

        // Progress of week
            long dateProgressRaw = grantService.currentProgressByDate(date);
            BigDecimal dateProgress = currency.calculatePartsAsKrona(dateProgressRaw);
            progressOfWeekJSON.put(dateProgress.intValueExact());
        }

        daysOfWeekJSONString = daysOfWeekJSON.toString();
        grantsCountOfWeekDaysJSONString = countOfWeekDaysJSON.toString();
        progressOfWeekJSONString = progressOfWeekJSON.toString();
        }
}
