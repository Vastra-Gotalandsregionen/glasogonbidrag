package se.vgregion.glasogonbidrag.backingbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
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
    private Part file; // +getter+setter

    @Autowired
    private GrantImportService service;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void save() throws IOException {
        byte[] data;

        try (InputStream input = file.getInputStream()) {
            data = new byte[(int)file.getSize()];

            DataInputStream stream = new DataInputStream(input);

            stream.readFully(data);

            stream.close();
        }
        catch (IOException e) {
            FacesMessage message = new FacesMessage();
            FacesContext.getCurrentInstance().addMessage("", message);

            return;
        }

        String currentYear = Integer.toString(
                new GregorianCalendar().get(Calendar.YEAR));

        Locale locale = Locale.forLanguageTag("sv-se");

        service.importData(data, currentYear, locale);
    }
}
