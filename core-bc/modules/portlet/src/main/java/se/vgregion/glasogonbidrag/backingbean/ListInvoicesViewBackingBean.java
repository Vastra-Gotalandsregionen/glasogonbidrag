package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactory;
import com.liferay.portlet.PortletURLFactoryUtil;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.jsf.PrettyTimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.InvoiceStatus;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component(value = "listInvoicesViewBackingBean")
@Scope(value = "request")
public class ListInvoicesViewBackingBean {

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

    // Initilizer.

    @PostConstruct
    protected void init() {

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        locale = themeDisplay.getLocale();
        long userId = themeDisplay.getUserId();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        prettyTime = new PrettyTime(locale);

        invoices = invoiceRepository.findAllOrderByModificationDate(userId);
    }
}
