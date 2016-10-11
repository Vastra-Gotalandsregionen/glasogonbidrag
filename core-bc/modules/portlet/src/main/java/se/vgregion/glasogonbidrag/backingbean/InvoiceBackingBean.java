package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "invoiceBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class InvoiceBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceBackingBean.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FacesUtil facesUtil;

    // Main objects
    private Invoice invoice;

    // Getter and Setters for Main objects
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public boolean checkMayNewGrantsBeAdded(Invoice curInvoice) {
        LOGGER.info("InvoiceBackingBean - checkMayNewGrantsBeAdded()");

        boolean mayNewGrantsBeAdded = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);
        boolean isStillAmountLeft = (curInvoice.calculateDifferenceExcludingVatAsKrona().compareTo(BigDecimal.ZERO) != 0);

        mayNewGrantsBeAdded = isStatusInProgress && isStillAmountLeft;

        return mayNewGrantsBeAdded;
    }

    public boolean checkMayInvoiceBeChanged(Invoice curInvoice) {
        LOGGER.info("InvoiceBackingBean - checkMayInvoiceBeChanged()");

        boolean mayInvoiceBeChanged = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);

        mayInvoiceBeChanged = isStatusInProgress;

        return mayInvoiceBeChanged;
    }

    public boolean checkMayInvoicesBeMarkedCompleted(Invoice curInvoice) {
        LOGGER.info("InvoiceBackingBean - checkMayInvoicesBeMarkedCompleted()");

        boolean mayInvoicesBeMarkedCompleted = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);
        boolean isStillAmountLeft = (curInvoice.calculateDifferenceExcludingVatAsKrona().compareTo(BigDecimal.ZERO) != 0);

        mayInvoicesBeMarkedCompleted = isStatusInProgress && !isStillAmountLeft;

        return mayInvoicesBeMarkedCompleted;
    }


    // Initializer

    @PostConstruct
    public void init() {
        LOGGER.info("InvoiceBackingBean - init()");

        long invoiceId = facesUtil.fetchId("invoiceId");
        invoice = invoiceService.findWithParts(invoiceId);
    }

}
