package se.vgregion.glasogonbidrag.backingbean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "createInvoiceViewInvoiceBackingBean")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateInvoiceViewInvoiceBackingBean {
    @Autowired
    private InvoiceRepository invoiceRepository;

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

    public String addNewGrant() {
        Long invoiceId = facesUtil.fetchId("invoiceId");

        System.out.println("Invoice id: " + invoiceId);
        //invoice = invoiceRepository.findWithParts(invoiceId);

        //addGrantBackingBean.init(invoice);

        String returnView = String.format("add_grant?invoiceId=%d&faces-redirect=true&includeViewParams=true", invoiceId);

        return returnView;
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
