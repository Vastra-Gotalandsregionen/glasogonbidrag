package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Component(value = "dbViewViewInvoiceBean")
@Scope(value = "request")
public class DBViewViewInvoiceBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DBViewViewInvoiceBackingBean.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FacesUtil util;

    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public String deleteGrant() {
        long grantId = util.fetchId("grantId");

        invoiceService.updateDeleteGrant(invoice, grantId);

        return "view_invoice?invoiceId=" + invoice.getId() +
                    "&faces-redirect=true";
    }

    public String deleteGrantAdjustment() {
        invoice.setAdjustment(null);

        invoiceService.update(invoice);

        return "view_invoice?invoiceId=" + invoice.getId() +
                    "&faces-redirect=true";
    }

    public String deleteInvoice() {
        if (invoice.getGrants().size() > 0) {
            FacesMessage message = new FacesMessage(
                    "Cannot remove an invoice with grants",
                    "You need to remove all grants from an invoice before " +
                            "removing the invoice.");
            FacesContext.getCurrentInstance().addMessage(null, message);

            return "view_invoice?invoiceId=" + invoice.getId() +
                    "&faces-redirect=true";
        }
        
        invoiceService.delete(invoice.getId());

        return "view?faces-redirect=true";
    }

    public void markPaid() {
        invoice.setStatus(InvoiceStatus.PAID);
        invoiceService.update(invoice);
    }

    public void markUnpaid() {
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoiceService.update(invoice);
    }

    public void markCancled() {
        invoice.setStatus(InvoiceStatus.CANCELED);
        invoiceService.update(invoice);
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("DBViewViewInvoiceBackingBean - init()");

        Long id = util.fetchId("invoiceId");

        invoice = invoiceRepository.findWithParts(id);

        LOGGER.info("Invoice: {}", invoice);
        if (invoice.getGrants().size() > 0) {
            LOGGER.info("Grant[1]: {}", invoice.getGrants().get(0));
        }
    }
}
