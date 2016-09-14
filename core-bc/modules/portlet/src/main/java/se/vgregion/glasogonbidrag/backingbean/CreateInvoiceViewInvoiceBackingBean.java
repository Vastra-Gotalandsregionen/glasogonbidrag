package se.vgregion.glasogonbidrag.backingbean;


import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.service.glasogonbidrag.domain.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "createInvoiceViewInvoiceBackingBean")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateInvoiceViewInvoiceBackingBean {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;


    @Autowired
    private CreateInvoiceAddGrantBackingBean addGrantBackingBean;

    @Autowired
    private FacesUtil facesUtil;

    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    // Actions

    public void markPaid() {
        invoice.setStatus(InvoiceStatus.COMPLETED);
        invoiceService.update(invoice);
    }

    public void markUnpaid() {
        invoice.setStatus(InvoiceStatus.IN_PROGRESS);
        invoiceService.update(invoice);
    }

    public void markCanceled() {
        invoice.setStatus(InvoiceStatus.CANCELED);
        invoiceService.update(invoice);
    }


    @PostConstruct
    protected void init() {
        Long invoiceId = facesUtil.fetchId("invoiceId");

        if (invoiceId != null) {
            invoice = invoiceRepository.findWithParts(invoiceId);
        } else {
            invoice = new Invoice();
        }
    }
}
