package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.DateUtil;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.glasogonbidrag.viewobject.StatisticsVO;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private GrantService grantService;

    KronaCalculationUtil currency;

    private String daysOfWeekJSONString;

    private int grantCountToday;

    private String grantsCountOfWeekDaysJSONString;

    private BigDecimal progressToday;

    private String progressOfWeekJSONString;

    private String statisticsGrouping;

    private String statisticsTimePeriod;

    private Date statisticsTimePeriodStartDate;

    private Date statisticsTimePeriodEndDate;

    private List<StatisticsVO> statisticsVOs;

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

    public String getStatisticsGrouping() {
        return statisticsGrouping;
    }

    public void setStatisticsGrouping(String statisticsGrouping) {
        this.statisticsGrouping = statisticsGrouping;
    }

    public String getStatisticsTimePeriod() {
        return statisticsTimePeriod;
    }

    public void setStatisticsTimePeriod(String statisticsTimePeriod) {
        this.statisticsTimePeriod = statisticsTimePeriod;
    }

    public Date getStatisticsTimePeriodStartDate() {
        return statisticsTimePeriodStartDate;
    }

    public void setStatisticsTimePeriodStartDate(Date statisticsTimePeriodStartDate) {
        this.statisticsTimePeriodStartDate = statisticsTimePeriodStartDate;
    }

    public Date getStatisticsTimePeriodEndDate() {
        return statisticsTimePeriodEndDate;
    }

    public void setStatisticsTimePeriodEndDate(Date statisticsTimePeriodEndDate) {
        this.statisticsTimePeriodEndDate = statisticsTimePeriodEndDate;
    }

    public List<StatisticsVO> getStatisticsVOs() {
        return statisticsVOs;
    }

    public void setStatisticsVOs(List<StatisticsVO> statisticsVOs) {
        this.statisticsVOs = statisticsVOs;
    }

    public void changeStatisticsTimePeriodListener() {
        // Don't do anything.
    }

    public void searchStatistics() {
        LOGGER.info("searchStatistics");

        String labelPrefix = "Kommun";
        int numberOfHits = 20;

        // TODO: Ugly code
        if(statisticsGrouping.equals("age")) {
            labelPrefix = "Alder";
            numberOfHits = 60;
        } else if(statisticsGrouping.equals("gender")) {
            labelPrefix = "Kon";
            numberOfHits = 3;
        } else if(statisticsGrouping.equals("grantType")) {
            labelPrefix = "Bidragstyp";
            numberOfHits = 6;
        }

        statisticsVOs = getDummyStatisticsVO(labelPrefix, numberOfHits);
    }

    @PostConstruct
    protected void init() {
        currency = new KronaCalculationUtil();

        Date today = new Date();

        // Progress today
        long progressTodayRaw = grantService.currentProgressByDate(today);
        progressToday = currency.calculatePartsAsKrona(progressTodayRaw);

        // Grants today
        List<Grant> todaysGrants = grantService.findByDate(today);
        grantCountToday = todaysGrants.size();

        // Days of week
        List<Date> datesOfThisWeek = dateUtil.getDatesOfThisWeek();

        JSONArray daysOfWeekJSON = JSONFactoryUtil.createJSONArray();
        for(Date date : datesOfThisWeek) {
            SimpleDateFormat format = new SimpleDateFormat("EEE", liferayUtil.getLocale());
            String dateString = format.format(date);
            daysOfWeekJSON.put(dateString);
        }

        daysOfWeekJSONString = daysOfWeekJSON.toString();

        // Grants counts of week days
        //getGrantsCountOfWeekDaysJSONString
        JSONArray getGrantsCountOfWeekDaysJSON = JSONFactoryUtil.createJSONArray();
        for(Date curDate : datesOfThisWeek) {
            List<Grant> grantsOfDate = grantService.findByDate(curDate);
            getGrantsCountOfWeekDaysJSON.put(grantsOfDate.size());
        }
        grantsCountOfWeekDaysJSONString = getGrantsCountOfWeekDaysJSON.toString();

        // Progress of week
        JSONArray progressOfWeekJSON = JSONFactoryUtil.createJSONArray();

        for(Date curDate : datesOfThisWeek) {
            long dateProgressRaw;

            try {
                dateProgressRaw = grantService.currentProgressByDate(curDate);
            } catch(NullPointerException e) {
                dateProgressRaw = new Long(0);
            }

            BigDecimal dateProgress = currency.calculatePartsAsKrona(dateProgressRaw);
            progressOfWeekJSON.put(dateProgress.intValueExact());
        }

        progressOfWeekJSONString = progressOfWeekJSON.toString();

        // Dummy data for statistics
        statisticsVOs = new ArrayList<StatisticsVO>();
        statisticsGrouping = "";
        statisticsTimePeriod = "";
    }

    private List<StatisticsVO> getDummyStatisticsVO(String labelPrefix, int numberOfHits) {
        ArrayList<StatisticsVO> statisticsItems = new ArrayList<StatisticsVO>();

        for (int i = 1; i <= numberOfHits; i++) {
            StatisticsVO item = new StatisticsVO();
            item.setLabel(labelPrefix + " " + i);
            item.setNumberOfGrants(5*i);
            item.setGrantsSum(200*i);

            statisticsItems.add(item);
        }

        return statisticsItems;
    }


}
