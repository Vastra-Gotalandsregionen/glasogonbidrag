package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticReportService;
import se.vgregion.service.glasogonbidrag.local.api.AreaCodeLookupService;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchDateInterval;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchIntegerInterval;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchRequest;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchResponse;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchType;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

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
    private MessageSource messageSource;

    @Autowired
    private StatisticsMockUtil statisticsMockUtil;

    @Autowired
    private StatisticReportService service;

    @Autowired
    private AreaCodeLookupService areaCodeLookupService;

    // Attributes

    private boolean searchHasBeenMade;

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


    public boolean isSearchHasBeenMade() {
        return searchHasBeenMade;
    }

    public void setSearchHasBeenMade(boolean searchHasBeenMade) {
        this.searchHasBeenMade = searchHasBeenMade;
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

    // Helpers

    // Helpers
    private final KronaCalculationUtil currency =
            new KronaCalculationUtil();

    public BigDecimal getGrantSumTotalAsKrona() {
        return currency.calculatePartsAsKrona(grantSumTotal);
    }

    // Listeners

    public void changeStatisticsFilterListener() {
    }

    public void changeStatisticsGroupByListener() {
        LOGGER.info("changeStatisticsGroupByListener");
        // TODO: Reset filters here.
        resetFilters();
    }


    public void searchStatistics() {
        LOGGER.info("searchStatistics");

        Locale locale = facesUtil.getLocale();

        StatisticSearchRequest request = new StatisticSearchRequest();
        request.setType(findSearchType());
        request.setInterval(findInterval());

        if (statisticsFilterGender != null) {
            switch (statisticsFilterGender) {
                case "man":
                    request.setSex(SexType.MALE);
                    break;
                case "woman":
                    request.setSex(SexType.FEMALE);
                    break;
                case "not-available":
                    request.setSex(SexType.UNKNOWN);
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

        // Filter on a birth date interval
        request.setBirthYearInterval(
                new StatisticSearchIntegerInterval(
                        statisticsFilterBirthYearStart,
                        statisticsFilterBirthYearStop));

        // Send request.
        StatisticSearchResponse response = service.search(request);

        Converter converter = new NullConverter();
        if (response.getSearchType() == StatisticSearchType.MUNICIPALITY) {
            converter = new MunicipalityConverter(areaCodeLookupService);
        } else if(response.getSearchType() == StatisticSearchType.GRANT_TYPE) {
            converter = new DiagnoseTypeConverter(messageSource, locale);
        }

        statisticsVOs = new ArrayList<>();
        for (StatisticReportDTO dto : response.getResultList()) {
            statisticsVOs.add(converter.apply(dto));
        }

        grantCountTotal = 0;
        grantSumTotal = 0;
        for(StatisticsVO statisticsVO : statisticsVOs) {
            grantCountTotal += statisticsVO.getNumberOfGrants();
            grantSumTotal += statisticsVO.getGrantsSum();
        }

        searchHasBeenMade = true;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("init");

        searchHasBeenMade = false;

        // Dummy data for statistics
        statisticsVOs = new ArrayList<StatisticsVO>();
        grantCountTotal = 0;
        grantSumTotal = 0;

        statisticsGrouping = "";
        statisticsTimePeriod = "today";

        resetFilters();
    }

    private void resetFilters() {
        statisticsFilterGender = "";

        statisticsFilterBirthYearMax =
                Calendar.getInstance().get(Calendar.YEAR);
        statisticsFilterBirthYearMin = Beneficiary.SYSTEM_INITIAL_BIRTH_YEAR;

        statisticsFilterBirthYearStart = statisticsFilterBirthYearMin;
        statisticsFilterBirthYearStop = statisticsFilterBirthYearMax;
    }

    private StatisticSearchType findSearchType() {
        switch (statisticsGrouping) {
            case "municipality":
                return StatisticSearchType.MUNICIPALITY;
            case "birthday":
                return StatisticSearchType.BIRTH_YEAR;
            case "gender":
                return StatisticSearchType.SEX;
            case "grantType":
                return StatisticSearchType.GRANT_TYPE;
            default:
                //TODO: Throw faces exception
                return null;
        }
    }

    private StatisticSearchDateInterval findInterval() {
        Calendar calStart = new GregorianCalendar();
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);

        Calendar calStop = new GregorianCalendar();
        calStop.set(Calendar.HOUR_OF_DAY, 23);
        calStop.set(Calendar.MINUTE, 59);
        calStop.set(Calendar.SECOND, 59);
        calStop.set(Calendar.MILLISECOND, 99);

        switch (statisticsTimePeriod) {
            case "today":
                return new StatisticSearchDateInterval(calStart.getTime());
            case "yesterday":
                calStart.add(Calendar.DAY_OF_MONTH, -1);
                return new StatisticSearchDateInterval(calStart.getTime());
            case "thisWeek": {
                calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date start = calStart.getTime();

                calStop.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calStop.add(Calendar.DAY_OF_WEEK, +1);
                Date stop = calStop.getTime();

                return new StatisticSearchDateInterval(start, stop);
            }
            case "previousWeek": {
                calStart.add(Calendar.WEEK_OF_YEAR, -1);
                calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date start = calStart.getTime();

                calStop.add(Calendar.WEEK_OF_YEAR, -1);
                calStop.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calStop.add(Calendar.DAY_OF_WEEK, +1);
                Date stop = calStop.getTime();

                return new StatisticSearchDateInterval(start, stop);
            }
            case "thisMonth": {
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date start = calStart.getTime();

                calStop.set(Calendar.DAY_OF_MONTH,
                        calStop.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date stop = calStop.getTime();

                return new StatisticSearchDateInterval(start, stop);
            }
            case "previousMonth": {
                calStart.add(Calendar.MONTH, -1);
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date start = calStart.getTime();

                calStop.add(Calendar.MONTH, -1);
                calStop.set(Calendar.DAY_OF_MONTH,
                        calStop.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date stop = calStop.getTime();

                return new StatisticSearchDateInterval(start, stop);
            }
            case "thisYear": {
                calStart.set(Calendar.MONTH, Calendar.JANUARY);
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date start = calStart.getTime();

                calStop.set(Calendar.MONTH, Calendar.DECEMBER);
                calStop.set(Calendar.DAY_OF_MONTH,
                        calStop.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date stop = calStop.getTime();

                return new StatisticSearchDateInterval(start, stop);
            }
            case "previousYear": {
                calStart.add(Calendar.YEAR, -1);
                calStart.set(Calendar.MONTH, Calendar.JANUARY);
                calStart.set(Calendar.DAY_OF_MONTH, 1);
                Date start = calStart.getTime();

                calStop.add(Calendar.YEAR, -1);
                calStop.set(Calendar.MONTH, Calendar.DECEMBER);
                calStop.set(Calendar.DAY_OF_MONTH,
                        calStop.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date stop = calStop.getTime();

                return new StatisticSearchDateInterval(start, stop);
            }
            case "customPeriod":
                Calendar calStartUserInput = new GregorianCalendar();
                calStartUserInput.setTime(statisticsTimePeriodStartDate);
                calStartUserInput.set(Calendar.HOUR_OF_DAY, 0);
                calStartUserInput.set(Calendar.MINUTE, 0);
                calStartUserInput.set(Calendar.SECOND, 0);
                calStartUserInput.set(Calendar.MILLISECOND, 0);

                Calendar calStopUserInput = new GregorianCalendar();
                calStopUserInput.setTime(statisticsTimePeriodEndDate);
                calStopUserInput.set(Calendar.HOUR_OF_DAY, 23);
                calStopUserInput.set(Calendar.MINUTE, 59);
                calStopUserInput.set(Calendar.SECOND, 59);
                calStopUserInput.set(Calendar.MILLISECOND, 99);

                return new StatisticSearchDateInterval(
                        calStartUserInput.getTime(),
                        calStopUserInput.getTime());
            default:
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
                code = "Okänd"; // TODO: Fix language key for this.
            }

            return new StatisticsVO(
                    code,
                    new Long(dto.getCount()).intValue(),
                    dto.getAmount());
        }
    }


    private static class DiagnoseTypeConverter implements Converter {
        private final MessageSource messageSource;
        private final Locale locale;

        public DiagnoseTypeConverter(MessageSource messageSource, Locale locale) {
            this.messageSource = messageSource;
            this.locale = locale;
        }

        public StatisticsVO apply(StatisticReportDTO dto) {
            String label = "";
            String dtoData = dto.getData();

            switch(dtoData) {
                case "a" :
                    label = messageSource.getMessage(DiagnoseType.APHAKIA.getLanguageKey(),new Object[0], locale);
                    break;
                case "k" :
                    label = messageSource.getMessage(DiagnoseType.KERATOCONUS.getLanguageKey(),new Object[0], locale);
                    break;
                case "s" :
                    label = messageSource.getMessage(DiagnoseType.SPECIAL.getLanguageKey(),new Object[0], locale);
                    break;
                case "n" :
                    label = messageSource.getMessage(DiagnoseType.NONE.getLanguageKey(),new Object[0], locale);
                    break;
            }

            return new StatisticsVO(
                    label,
                    new Long(dto.getCount()).intValue(),
                    dto.getAmount());
        }
    }



}
