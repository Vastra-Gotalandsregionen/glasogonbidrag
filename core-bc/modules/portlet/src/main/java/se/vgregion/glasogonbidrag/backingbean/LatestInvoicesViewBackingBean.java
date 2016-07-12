package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Martin Lind - Monator Technologies AB
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "latestInvoicesViewBackingBean")
@Scope(value = "request")
public class LatestInvoicesViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LatestInvoicesViewBackingBean.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FacesUtil facesUtil;

    private List<Invoice> invoices;
    private Locale locale;
    private PrettyTime prettyTime; //TODO: Do we need getter and setter?

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public Locale getLocale() {
        return locale;
    }

    public PrettyTime getPrettyTime() {
        return prettyTime;
    }

    public void setPrettyTime(PrettyTime prettyTime) {
        this.prettyTime = prettyTime;
    }

    // Public view formatting code

    public String formatPrettyTime(Date date) {
        return prettyTime.format(date);
    }

    // Initializer.

    @PostConstruct
    protected void init() {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        locale = themeDisplay.getLocale();
        long userId = themeDisplay.getUserId();

        //TODO: Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        fetchInvoices(userId);
        configurePrettyTime();
    }

    private void fetchInvoices(long userId) {
        int first = 0;
        int results = 10;
        invoices = invoiceRepository
                .findAllOrderByModificationDate(userId, first, results);
    }

    private void configurePrettyTime() {
        prettyTime = new PrettyTime(locale);
    }

}
