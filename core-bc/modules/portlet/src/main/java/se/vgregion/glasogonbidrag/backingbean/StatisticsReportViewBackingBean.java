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
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.SexType;
import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticReportDTO;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticReportService;
import se.vgregion.service.glasogonbidrag.local.api.AreaCodeLookupService;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchDateInterval;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchRequest;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchResponse;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchType;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    @Autowired
    private StatisticReportService service;

    @Autowired
    private AreaCodeLookupService areaCodeLookupService;

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

        StatisticSearchRequest request = new StatisticSearchRequest();
        request.setType(findSearchType());
        request.setInterval(findInterval());

        if (statisticsFilterGender != null) {
            switch (statisticsFilterGender) {
                case "man":
                    request.setSex(SexType.MALE);
                    break;
                case "woman":
                    request.setSex(SexType.MALE);
                    break;
                default:
                    // TODO: Set to unknown.
                    request.setSex(null);
                    break;
            }
        }

        if (statisticsFilterGrantType != null) {
            switch (statisticsFilterGrantType) {
                case "children":
                    request.setDiagnoseType(DiagnoseType.NONE);
                    break;
                case "aphakia":
                    request.setDiagnoseType(DiagnoseType.APHAKIA);
                    break;
                case "keratoconus":
                    request.setDiagnoseType(DiagnoseType.KERATOCONUS);
                    break;
                case "special":
                    request.setDiagnoseType(DiagnoseType.SPECIAL);
                    break;
                default:
                    request.setDiagnoseType(null);
                    break;
            }
        }

        if (statisticsFilterIdentification != null) {
            switch (statisticsFilterIdentification) {
                case "personalnumber":
                    request.setIdentificationType(IdentificationType.PERSONAL);
                    break;
                case "protected":
                    request.setIdentificationType(IdentificationType.PROTECTED);
                    break;
                case "lma":
                    request.setIdentificationType(IdentificationType.LMA);
                    break;
                default:
                    break;
            }
        }

        // TODO: Handle birth year

        StatisticSearchResponse response = service.search(request);

        Converter converter = new NullConverter();
        if (response.getSearchType() == StatisticSearchType.MUNICIPALITY) {
            converter = new MunicipalityConverter(areaCodeLookupService);
        }

        statisticsVOs = new ArrayList<>();
        for (StatisticReportDTO dto : response.getResultList()) {
            statisticsVOs.add(converter.apply(dto));
        }

//        statisticsVOs = statisticsMockUtil.getStatistics(statisticsGrouping, statisticsFilterGender, statisticsFilterBirthYearStart, statisticsFilterBirthYearStop);

        grantCountTotal = 0;
        grantSumTotal = 0;
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
        statisticsFilterBirthYearMin = Beneficiary.SYSTEM_INITIAL_BIRTH_YEAR;

        statisticsFilterBirthYearStart = statisticsFilterBirthYearMin;
        statisticsFilterBirthYearStop = statisticsFilterBirthYearMax;
    }

    private StatisticSearchType findSearchType() {
        if ("municipality".equals(statisticsGrouping)) {
            return StatisticSearchType.MUNICIPALITY;
        } else if ("age".equals(statisticsGrouping)) {
            return StatisticSearchType.BIRTH_YEAR;
        } else if ("gender".equals(statisticsGrouping)) {
            return StatisticSearchType.SEX;
        } else if ("grantType".equals(statisticsGrouping)) {
            return StatisticSearchType.GRANT_TYPE;
        } else {
            // Throw faces exception
            return null;
        }
    }

    private StatisticSearchDateInterval findInterval() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if ("today".equals(statisticsTimePeriod)) {
            return new StatisticSearchDateInterval(cal.getTime());
        } else if ("yesterday".equals(statisticsTimePeriod)) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            return new StatisticSearchDateInterval(cal.getTime());
        } else if ("thisWeek".equals(statisticsTimePeriod)) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date start = cal.getTime();

            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date stop = cal.getTime();

            return new StatisticSearchDateInterval(start, stop);
        } else if ("previousWeek".equals(statisticsTimePeriod)) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);

            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date start = cal.getTime();

            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date stop = cal.getTime();

            return new StatisticSearchDateInterval(start, stop);
        } else if ("thisMonth".equals(statisticsTimePeriod)) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date start = cal.getTime();

            cal.set(Calendar.DAY_OF_MONTH,
                    cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date stop = cal.getTime();

            return new StatisticSearchDateInterval(start, stop);
        } else if ("previousMonth".equals(statisticsTimePeriod)) {
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date start = cal.getTime();

            cal.set(Calendar.DAY_OF_MONTH,
                    cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date stop = cal.getTime();

            return new StatisticSearchDateInterval(start, stop);
        } else if ("thisYear".equals(statisticsTimePeriod)) {
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date start = cal.getTime();

            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_WEEK_IN_MONTH);
            Date stop = cal.getTime();

            return new StatisticSearchDateInterval(start, stop);
        } else if ("previousYear".equals(statisticsTimePeriod)) {
            cal.add(Calendar.YEAR, -1);

            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date start = cal.getTime();

            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.DAY_OF_MONTH,
                    cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date stop = cal.getTime();

            return new StatisticSearchDateInterval(start, stop);
        } else if ("customPeriod".equals(statisticsTimePeriod)) {
            return new StatisticSearchDateInterval(
                    statisticsTimePeriodStartDate,
                    statisticsTimePeriodEndDate);
        } else {
            return null;
        }
    }

    private static interface Converter {
        StatisticsVO apply(StatisticReportDTO dto);
    }

    private static class NullConverter implements Converter {
        public StatisticsVO apply(StatisticReportDTO dto) {
            return new StatisticsVO(
                    dto.getData(),
                    new Long(dto.getCount()).intValue(),
                    dto.getAmount());
        }
    }

    private static class MunicipalityConverter implements Converter {
        private final AreaCodeLookupService lookupService;

        public MunicipalityConverter(AreaCodeLookupService lookupService) {
            this.lookupService = lookupService;
        }

        public StatisticsVO apply(StatisticReportDTO dto) {
            String code = lookupService
                    .lookupMunicipalityFromCode(dto.getData());
            if (code == null) {
                code = "Unknown"; // TODO: Fix language key for this.
            }

            return new StatisticsVO(
                    code,
                    new Long(dto.getCount()).intValue(),
                    dto.getAmount());
        }
    }
}
