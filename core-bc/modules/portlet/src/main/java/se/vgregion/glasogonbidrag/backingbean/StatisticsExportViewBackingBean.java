package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.viewobject.ExportVO;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "statisticsExportViewBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsExportViewBackingBean {


    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticsExportViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    // Attributes

    private Date minDate;

    private Date maxDate;

    private ExportVO exportVO;

    // Getters and setters


    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public ExportVO getExportVO() {
        return exportVO;
    }

    public void setExportVO(ExportVO exportVO) {
        this.exportVO = exportVO;
    }

    // Listeners

    public void changeSettingsListener() {
    }

    // Actions
    public void exportStatistics() {
        LOGGER.info("exportStatistics");
    }


    @PostConstruct
    protected void init() {
        LOGGER.info("init");

        exportVO = new ExportVO();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);

        exportVO.setStartDate(calendar.getTime());
        exportVO.setEndDate(today);

        calendar.set(2016, Calendar.JANUARY, 1);
        minDate = calendar.getTime();

        maxDate = today;

        exportVO.setIncludeAllFields(true);

        String[] includedFields = {
                "field-1", "field-2", "field-3", "field-4", "field-5", "field-6"
        };

        exportVO.setIncludedFields(includedFields);
    }


}
