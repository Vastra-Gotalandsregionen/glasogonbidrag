package se.vgregion.glasogonbidrag.jsf.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticExportService;
import se.vgregion.service.glasogonbidrag.local.internal.StaticApplicationContext;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StatisticReportResource extends Resource {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticReportResource.class);

    private static final String EXCEL_MIME_TYPE =
            "application/vnd.openxmlformats-officedocument" +
                    ".spreadsheetml.sheet";
    private static final String PARAM_NAME_START_DATE = "startDate";
    private static final String PARAM_NAME_END_DATE = "endDate";

    private static final String[] KEYS_TO_LOCALIZE = new String[] {
            "excel-file-amount-header",
            "excel-file-sex-header",
            "excel-file-diagnose-type-header",
            "excel-file-birth-date-header",
            "excel-file-delivery-date-header",
            "excel-file-create-date-header",
            "excel-file-county-header",
            "excel-file-municipality-header",
            "excel-file-responsibility-code-header",
            "excel-file-free-code-header",
            "sex-male",
            "sex-unknown",
            "sex-female",
            "diagnose-type-aphakia",
            "diagnose-type-keratoconus",
            "diagnose-type-special",
            "diagnose-type-child"
    };


    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("yyyyMMdd");

    private String requestPath = null;

    private MessageSource messageSource;

    private StatisticExportService service;

    private Date startDate = null;
    private Date endDate = null;

    public StatisticReportResource(String filename) {
        setResourceName(filename);
        setLibraryName(StatisticReportResourceHandler.LIBRARY_NAME);

        setContentType(EXCEL_MIME_TYPE);

        service = StaticApplicationContext
                .getContext().getBean(StatisticExportService.class);

        messageSource = StaticApplicationContext
                .getContext().getBean(MessageSource.class);
    }

    public StatisticReportResource(Date start, Date end) {
        this.startDate = start;
        this.endDate = end;

        setResourceName(generateFileName());
        setLibraryName(StatisticReportResourceHandler.LIBRARY_NAME);

        setContentType(EXCEL_MIME_TYPE);
    }

    /**
     * We want to return true value, because even if the
     * resource is dynamically generated it will never be persisted.
     *
     * @param context ignored context
     * @return true since we always update.
     */
    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        // Dynamic generated file will always need update.
        return true;
    }

    /**
     * A <code>Map&lt;String, String&gt;</code> that is returned
     * as headers in the response.
     *
     * The header includes file name and file size of the dynamically
     * generated statistic report.
     *
     * @return <code>Map&lt;String, String&gt;</code> of headers
     * that as headers for this response.
     */
    @Override
    public Map<String, String> getResponseHeaders() {
        Map<String, String> headers = new HashMap<>();

//        String filename = "export-" + generateFileName() + ".xlsx";

        headers.put("Content-Disposition",
                "attachment; filename=" + getResourceName());

        return headers;
    }

    /**
     *
     * @return return null.
     */
    @Override
    public URL getURL() {
        // Ignore this method
        return null;
    }

    @Override
    public String getRequestPath() {
        if (requestPath == null) {
            requestPath = generateRequestPath();
        }

        return requestPath;
    }

    /**
     * Service that will return a input stream to the export document.
     *
     * @return Input stream from a byte array.
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> requestMap = facesContext
                .getExternalContext().getRequestParameterMap();

        Locale locale = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestLocale();

        String startString = requestMap.get(PARAM_NAME_START_DATE);
        String endString = requestMap.get(PARAM_NAME_END_DATE);

        Date start;
        Date end;
        try {
            start = FORMAT.parse(startString);
            end = FORMAT.parse(endString);
        } catch (ParseException e) {
            throw new FacesException("Failed to parse supplied date.", e);
        }

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(start);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(end);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 99);

        Map<String, String> localizedKeys = new HashMap<>();
        for (String key : KEYS_TO_LOCALIZE) {
            String localizedValue =
                    messageSource.getMessage(key, new Object[0], locale);
            localizedKeys.put(key, localizedValue);
        }

        byte[] data = service.export(
                startCalendar.getTime(), endCalendar.getTime(), localizedKeys);

        return new ByteArrayInputStream(data);
    }

    /**
     * Include start date in the request url.
     *
     * @return the request path that should be called to fetch this resource.
     */
    private String generateRequestPath() {
        if (startDate != null && endDate != null) {
            return ResourceHandler.RESOURCE_IDENTIFIER +
                    "/" + getResourceName() +
                    "?ln=" + getLibraryName() +
                    "&" + PARAM_NAME_START_DATE + "=" + FORMAT.format(startDate) +
                    "&" + PARAM_NAME_END_DATE + "=" + FORMAT.format(endDate);
        } else {
            return ResourceHandler.RESOURCE_IDENTIFIER +
                    "/" + getResourceName() +
                    "?ln=" + getLibraryName();
        }
    }

    private String generateFileName() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        Calendar cal = new GregorianCalendar();
        Date now = cal.getTime();

        return "gb_export_" + dateTimeFormat.format(now) + ".xlsx";
    }

    private String generateHash() {
        String uuid = UUID.randomUUID().toString();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("Couldn't find the message digest " +
                    "with algorithm \"SHA-1\"");
            throw new RuntimeException(e);
        }

        byte[] uuidBytes;
        try {
            uuidBytes = uuid.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Couldn't use encoding UTF-8");
            throw new RuntimeException(e);
        }

        String digest = byteArrayToHexString(md.digest(uuidBytes));

        return digest.substring(0, 5).toLowerCase();
    }

    private static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (byte a : b) {
            result += Integer.toString((a & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
