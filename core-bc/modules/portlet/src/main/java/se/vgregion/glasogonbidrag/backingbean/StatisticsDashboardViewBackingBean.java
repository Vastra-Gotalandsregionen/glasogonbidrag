package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.DateUtil;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.domain.api.data.GrantRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "statisticsDashboardViewBackingBean")
@Scope(value = "view")
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
    private GrantRepository grantRepository;

    KronaCalculationUtil currency;

    private String daysOfWeekJSONString;

    private Locale locale;

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

    public Locale getLocale() {
        return locale;
    }

    public BigDecimal getProgressToday() {
        return progressToday;
    }

    public String getProgressOfWeekJSONString() {
        return progressOfWeekJSONString;
    }

    @PostConstruct
    protected void init() {
        currency = new KronaCalculationUtil();

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();

        locale = themeDisplay.getLocale();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        Date today = new Date();

        // Progress today
        long progressTodayRaw = grantRepository.currentProgressByDate(today);
        progressToday = currency.calculatePartsAsKrona(progressTodayRaw);

        // Grants today
        List<Grant> todaysGrants = grantRepository.findByDate(today);
        grantCountToday = todaysGrants.size();

        // Days of week
        List<Date> datesOfThisWeek = dateUtil.getDatesOfThisWeek();

        JSONArray daysOfWeekJSON = JSONFactoryUtil.createJSONArray();
        for(Date date : datesOfThisWeek) {
            SimpleDateFormat format = new SimpleDateFormat("EEE", locale);
            String dateString = format.format(date);
            daysOfWeekJSON.put(dateString);
        }

        daysOfWeekJSONString = daysOfWeekJSON.toString();

        // Grants counts of week days
        //getGrantsCountOfWeekDaysJSONString
        JSONArray getGrantsCountOfWeekDaysJSON = JSONFactoryUtil.createJSONArray();
        for(Date curDate : datesOfThisWeek) {
            List<Grant> grantsOfDate = grantRepository.findByDate(curDate);
            getGrantsCountOfWeekDaysJSON.put(grantsOfDate.size());
        }
        grantsCountOfWeekDaysJSONString = getGrantsCountOfWeekDaysJSON.toString();

        // Progress of week
        JSONArray progressOfWeekJSON = JSONFactoryUtil.createJSONArray();

        for(Date curDate : datesOfThisWeek) {
            long dateProgressRaw;

            try {
                dateProgressRaw = grantRepository.currentProgressByDate(curDate);
            } catch(NullPointerException e) {
                dateProgressRaw = new Long(0);
            }

            BigDecimal dateProgress = currency.calculatePartsAsKrona(dateProgressRaw);
            progressOfWeekJSON.put(dateProgress.intValueExact());
        }

        progressOfWeekJSONString = progressOfWeekJSON.toString();


    }
}
