package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.faces.portlet.component.resourceurl.ResourceURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.viewobject.ExportVO;
import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticExportDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticExportService;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "statisticsExportViewBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsExportViewBackingBean {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticsExportViewBackingBean.class);

    public static final String EXCEL_MIME_TYPE =
            "application/vnd.openxmlformats-officedocument" +
                    ".spreadsheetml.sheet";

    @Autowired
    private StatisticExportService service;

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
    public void exportStatistics() throws IOException {
        LOGGER.trace("exportStatistics");

        // This don't work...
        // Need to do some JSF + Liferay magic.
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ByteArrayOutputStream output = service.export(minDate, maxDate);

        ec.responseReset();
        ec.setResponseContentType(EXCEL_MIME_TYPE);
        ec.setResponseContentLength(output.size());
        ec.setResponseHeader("Content-Disposition",
                "attachment; filename=\"Export.xlsx\"");

        OutputStream browserDownload = ec.getResponseOutputStream();

        output.writeTo(browserDownload);

        output.close();

        fc.responseComplete();
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
