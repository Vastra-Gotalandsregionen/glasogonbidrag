package se.vgregion.glasogonbidrag.backingbean;


import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.constants.GbConstants;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.AccountingDistribution;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.local.api.AccountingDistributionCalculationService;

import javax.annotation.PostConstruct;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "createInvoiceViewInvoiceBackingBean")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateInvoiceViewInvoiceBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CreateInvoiceViewInvoiceBackingBean.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AccountingDistributionCalculationService accountingService;


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

    public void generateAccountingDistribution() {
        //TODO: Warn if the invoice is not completed. Call not finished invoiceValidationService.

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        AccountingDistribution distribution =
                accountingService.calculateFrom(invoice);

        invoiceService.updateAddAccountingDistribution(
                userId, groupId, companyId, invoice, distribution);
    }

    @PostConstruct
    protected void init() {
        Long invoiceId = facesUtil.fetchId("invoiceId");

        if (invoiceId != null) {
            invoice = invoiceService.findWithParts(invoiceId);
        } else {
            invoice = new Invoice();
        }
    }
}
