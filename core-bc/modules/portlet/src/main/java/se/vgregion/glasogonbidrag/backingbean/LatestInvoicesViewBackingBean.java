package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

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
    private InvoiceService invoiceService;

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

    private List<Invoice> invoices;
    private PrettyTime prettyTime; //TODO: Do we need getter and setter?

    public List<Invoice> getInvoices() {
        return invoices;
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
        long userId = themeDisplay.getUserId();

        fetchInvoices(userId);
        configurePrettyTime();
    }

    private void fetchInvoices(long userId) {
        int first = 0;
        int results = 10;
        invoices = invoiceService
                .findAllOrderByModificationDate(userId, first, results);
    }

    private void configurePrettyTime() {
        prettyTime = new PrettyTime(liferayUtil.getLocale());
    }

}
