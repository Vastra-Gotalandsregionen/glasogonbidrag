package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.InvoiceStatus;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;
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

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public Locale getLocale() {
        return locale;
    }

    @PostConstruct
    protected void init() {

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        locale = themeDisplay.getLocale();
        long userId = themeDisplay.getUserId();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        invoices = invoiceRepository.findAllOrderByModificationDate(userId);
    }
}
