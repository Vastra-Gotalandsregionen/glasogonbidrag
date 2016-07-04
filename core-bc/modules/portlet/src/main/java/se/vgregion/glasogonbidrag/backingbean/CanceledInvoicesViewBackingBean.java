package se.vgregion.glasogonbidrag.backingbean;


import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Locale;

@Component(value = "canceledInvoicesViewBackingBean")
@Scope(value = "request")
public class CanceledInvoicesViewBackingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CanceledInvoicesViewBackingBean.class);

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
//        HttpServletRequest request = themeDisplay.getRequest();
        long groupId = themeDisplay.getScopeGroupId();
        locale = themeDisplay.getLocale();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        invoices = invoiceRepository.findAllWithStatus(InvoiceStatus.CANCELED);
    }
}
