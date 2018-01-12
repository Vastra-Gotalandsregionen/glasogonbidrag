package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.dto.ImportDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.MigrationService;
import se.vgregion.service.glasogonbidrag.local.api.GrantImportService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "migrationViewBackingBean")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MigrationViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MigrationViewBackingBean.class);

    private Part file;

    private Boolean canMigrate = null;

    @Autowired
    private GrantImportService service;

    @Autowired
    private MigrationService migrationService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FacesUtil util;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public Boolean getCanMigrate() {
        if (canMigrate == null) {
            canMigrate = !migrationService.hasMigrations();
        }

        return canMigrate;
    }

    public void importFile() throws IOException {
        // Read data into the byte-array.
        byte[] data;
        try (InputStream input = file.getInputStream()) {
            data = new byte[(int)file.getSize()];
            DataInputStream stream = new DataInputStream(input);
            stream.readFully(data);
            stream.close();
        } catch (IOException e) {
            Locale facesLocale = util.getLocale();
            String localizedMessage = messageSource
                    .getMessage("import-error-supplied-file-reading",
                            new Object[0], facesLocale);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            localizedMessage, localizedMessage);
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);

            LOGGER.warn("Error reading file from request!");

            return;
        }

        String currentYear = Integer.toString(
                new GregorianCalendar().get(Calendar.YEAR));

        // We should fetch this from the request? We only support swedish
        // so this is not really that prioritized.
        Locale locale = Locale.forLanguageTag("sv-se");

        ImportDTO excelData;
        try {
            excelData = service.importData(data, currentYear, locale);
        } catch (Exception e) {
            Locale facesLocale = util.getLocale();
            String localizedMessage = messageSource
                    .getMessage("import-error-supplied-file-format",
                            new Object[0], facesLocale);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            localizedMessage, localizedMessage);
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);

            LOGGER.warn("Error parsing data in supplied excel file!");

            return;
        }

        // Fetch data from the request to set correct
        // user, group and company id.
        ThemeDisplay themeDisplay = util.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        try {
            migrationService.importData(userId, groupId, companyId, excelData);
        } catch (Exception e) {
            Locale facesLocale = util.getLocale();
            String localizedMessage = messageSource
                    .getMessage("import-error-database-write",
                            new Object[0], facesLocale);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            localizedMessage, localizedMessage);
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);

            LOGGER.warn("Exception importing data into database!");
        }
    }
}
