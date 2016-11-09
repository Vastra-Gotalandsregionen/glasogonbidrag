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
@Component(value = "statisticsReportViewBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsReportViewBackingBean {


    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticsReportViewBackingBean.class);

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

    @Autowired
    private StatisticsMockUtil statisticsMockUtil;

    // Attributes

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

    private int grantCountTotal;

    private int grantSumTotal;

    // Getters and Setters

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

    public int getGrantCountTotal() {
        return grantCountTotal;
    }

    public void setGrantCountTotal(int grantCountTotal) {
        this.grantCountTotal = grantCountTotal;
    }

    public int getGrantSumTotal() {
        return grantSumTotal;
    }

    public void setGrantSumTotal(int grantSumTotal) {
        this.grantSumTotal = grantSumTotal;
    }

    // Listeners

    public void changeStatisticsFilterListener() {
    }

    public void searchStatistics() {
        LOGGER.info("searchStatistics");

        statisticsVOs = statisticsMockUtil.getStatistics(statisticsGrouping, statisticsFilterGender, statisticsFilterBirthYearStart, statisticsFilterBirthYearStop);

        for(StatisticsVO statisticsVO : statisticsVOs) {
            grantCountTotal += statisticsVO.getNumberOfGrants();
            grantSumTotal += statisticsVO.getGrantsSum();
        }
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("init");

        // Dummy data for statistics
        statisticsVOs = new ArrayList<StatisticsVO>();
        grantCountTotal = 0;
        grantSumTotal = 0;

        statisticsFilterGender = "";
        statisticsGrouping = "";
        statisticsTimePeriod = "today";

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        statisticsFilterBirthYearMax = currentYear;
        statisticsFilterBirthYearMin = currentYear - 130;

        statisticsFilterBirthYearStart = statisticsFilterBirthYearMin;
        statisticsFilterBirthYearStop = statisticsFilterBirthYearMax;
    }


}
