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
import se.vgregion.glasogonbidrag.viewobject.StatisticsVO;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private StatisticsMockUtil statisticsMockUtil;


    @Autowired
    private GrantService grantService;

    KronaCalculationUtil currency;

    private String daysOfWeekJSONString;

    private int grantCountToday;

    private String grantsCountOfWeekDaysJSONString;

    private BigDecimal progressToday;

    private String progressOfWeekJSONString;

    private int statisticsFilterBirthYearMin;

    private int statisticsFilterBirthYearMax;

    private int statisticsFilterBirthYearStart;

    private int statisticsFilterBirthYearStop;

    private String statisticsFilterGender;

    private String statisticsFilterGrantType;

    private String statisticsFilterIdentification;

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

    public int getStatisticsFilterBirthYearMin() {
        return statisticsFilterBirthYearMin;
    }

    public void setStatisticsFilterBirthYearMin(int statisticsFilterBirthYearMin) {
        this.statisticsFilterBirthYearMin = statisticsFilterBirthYearMin;
    }

    public int getStatisticsFilterBirthYearMax() {
        return statisticsFilterBirthYearMax;
    }

    public void setStatisticsFilterBirthYearMax(int statisticsFilterBirthYearMax) {
        this.statisticsFilterBirthYearMax = statisticsFilterBirthYearMax;
    }

    public int getStatisticsFilterBirthYearStart() {
        return statisticsFilterBirthYearStart;
    }

    public void setStatisticsFilterBirthYearStart(int statisticsFilterBirthYearStart) {
        this.statisticsFilterBirthYearStart = statisticsFilterBirthYearStart;
    }

    public int getStatisticsFilterBirthYearStop() {
        return statisticsFilterBirthYearStop;
    }

    public void setStatisticsFilterBirthYearStop(int statisticsFilterBirthYearStop) {
        this.statisticsFilterBirthYearStop = statisticsFilterBirthYearStop;
    }

    public String getStatisticsFilterGender() {
        return statisticsFilterGender;
    }

    public void setStatisticsFilterGender(String statisticsFilterGender) {
        this.statisticsFilterGender = statisticsFilterGender;
    }

    public String getStatisticsFilterGrantType() {
        return statisticsFilterGrantType;
    }

    public void setStatisticsFilterGrantType(String statisticsFilterGrantType) {
        this.statisticsFilterGrantType = statisticsFilterGrantType;
    }

    public String getStatisticsFilterIdentification() {
        return statisticsFilterIdentification;
    }

    public void setStatisticsFilterIdentification(String statisticsFilterIdentification) {
        this.statisticsFilterIdentification = statisticsFilterIdentification;
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

    public void changeStatisticsFilterListener() {
    }

    public void searchStatistics() {
        statisticsVOs = statisticsMockUtil.getStatistics(statisticsGrouping, statisticsFilterGender, statisticsFilterBirthYearStart, statisticsFilterBirthYearStop);

        // TODO: Code below is not used. Should be removed. This requires that functionality is tested again.
        String labelPrefix = "Kommun";
        int numberOfHits = 20;

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

        //statisticsVOs = getDummyStatisticsVO(labelPrefix, numberOfHits);
    }

    @PostConstruct
    protected void init() {
        currency = new KronaCalculationUtil();

        Date today = new Date();

        // Progress today
        long progressTodayRaw = grantService.currentProgressByDate(today);
        progressToday = currency.calculatePartsAsKrona(progressTodayRaw);

        // Grants today
        List<Grant> todaysGrants = grantService.findAllByDate(today);
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
            List<Grant> grantsOfDate = grantService.findAllByDate(curDate);
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
        statisticsFilterGender = "";
        statisticsGrouping = "";
        statisticsTimePeriod = "today";

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        statisticsFilterBirthYearMax = currentYear;
        statisticsFilterBirthYearMin = currentYear - 130;

        statisticsFilterBirthYearStart = statisticsFilterBirthYearMin;
        statisticsFilterBirthYearStop = statisticsFilterBirthYearMax;

        // Temp
        //statisticsVOs = getDummyStatisticsVO("Kommun", 20);
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
