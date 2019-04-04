package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "manageInvoiceViewBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManageInvoiceViewBackingBean {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ManageInvoiceViewBackingBean.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private FacesUtil facesUtil;

    // Main objects
    private Invoice invoice;

    // Form inputs

    private String verificationNumber;
    private long supplier;
    private String invoiceNumber;
    private BigDecimal amount;

    /**
     * List of suppliers that will be used to populate the
     * select menu.
     */
    private List<Supplier> suppliers;

    // Getters and setters

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public long getSupplier() {
        return supplier;
    }

    public void setSupplier(long supplier) {
        this.supplier = supplier;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // View methods

    /**
     * Transforms the list of suppliers into a list of select items
     * for the use in the view.
     *
     * @return return list of select items for the supplier list.
     */
    public List<SelectItem> getSuppliers() {
        List<SelectItem> items = new ArrayList<>(suppliers.size());

        for (Supplier supplier : suppliers) {
            SelectItem item = new SelectItem(
                    supplier.getId(), supplier.getName());
            items.add(item);
        }

        return items;
    }

    // Actions

    public String save() {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        String caseWorker = themeDisplay.getUser().getScreenName();


        invoice.setVerificationNumber(verificationNumber);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setAmountAsKrona(amount);

        // Only update supplier if this have changed
        if (supplier != invoice.getSupplier().getId()) {
            Supplier s = supplierService.find(supplier);
            invoice.setSupplier(s);
        }

        invoiceService.update(caseWorker, invoice);

        return String.format(
                "view_invoice?invoiceId=%d" +
                        "&faces-redirect=true" +
                        "&includeViewParams=true",
                invoice.getId());
    }

    public String cancel() {
        return String.format(
                "view_invoice?invoiceId=%d" +
                        "&faces-redirect=true" +
                        "&includeViewParams=true",
                invoice.getId());
    }

    // Initializer

    @PostConstruct
    public void init() {
        long invoiceId = facesUtil.fetchId("invoiceId");
        invoice = invoiceService.findWithParts(invoiceId);

        suppliers = supplierService.findAllActive();

        verificationNumber = invoice.getVerificationNumber();
        invoiceNumber = invoice.getInvoiceNumber();
        supplier = invoice.getSupplier().getId();
        amount = invoice.getAmountAsKrona();
    }
}
