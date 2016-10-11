package se.vgregion.glasogonbidrag.backingbean;


import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import java.util.List;

@Component(value = "canceledInvoicesViewBackingBean")
@Scope(value = "request")
public class CanceledInvoicesViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CanceledInvoicesViewBackingBean.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FacesUtil facesUtil;

    private List<Invoice> invoices;

    public List<Invoice> getInvoices() {
        return invoices;
    }

    @PostConstruct
    protected void init() {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();

        fetchInvoices(userId);
    }

    private void fetchInvoices(long userId) {
        int first = 0;
        int results = 10;
        invoices = invoiceService
                .findAllWithStatusOrderByModificationDate(
                        InvoiceStatus.CANCELED, first, results);
    }
}
