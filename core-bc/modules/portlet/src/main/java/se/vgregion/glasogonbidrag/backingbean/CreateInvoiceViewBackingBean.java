package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.data.SupplierRepository;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "createInvoiceViewBackingBean")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateInvoiceViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CreateInvoiceViewBackingBean.class);

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CreateInvoiceAddGrantBackingBean addGrantBackingBean;

    @Autowired
    private FacesUtil util;

    @Autowired
    private MessageSource messageSource;

    /**
     * List of suppliers that will be used to populate the
     * select menu.
     */
    private List<Supplier> suppliers;

    // Form inputs

    private String verificationNumber;
    private long supplier;
    private String invoiceNumber;
    private BigDecimal amountWithVat;

    // Getter and setters for form inputs

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

    public BigDecimal getAmountWithVat() {
        return amountWithVat;
    }

    public void setAmountWithVat(BigDecimal amountWithVat) {
        this.amountWithVat = amountWithVat;
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

    public String register() {
        LOGGER.info("CreateInvoiceViewBackingBean - register()");

        ThemeDisplay themeDisplay = util.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        Supplier s = supplierRepository.find(supplier);

        Invoice invoice = new Invoice();

        invoice.setVerificationNumber(verificationNumber);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setAmountIncludingVatAsKrona(amountWithVat);
        invoice.setSupplier(s);
        invoice.setStatus(InvoiceStatus.IN_PROGRESS);

        try {
            invoiceService.create(userId, groupId, companyId, invoice);
        } catch (PersistenceException e) {
            Locale locale = util.getLocale();
            String localizedMessage = messageSource
                    .getMessage("reg-invoice-error-verification-duplicate",
                            new Object[0], locale);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            localizedMessage, localizedMessage);
            FacesContext.getCurrentInstance()
                    .addMessage("verificationNumber", message);

            return null;
        }

        LOGGER.info("Persisted invoice, got id: {}", invoice.getId());

        return String.format(
                "add_grant?invoiceId=%d" +
                        "&faces-redirect=true" +
                        "&includeViewParams=true",
                invoice.getId());
    }

    public String cancel() {
        LOGGER.info("CreateInvoiceViewBackingBean - cancel()");

        //TODO: Should redirect to start page?

        return "view?faces-redirect=true";
    }

    // Initializer

    @PostConstruct
    protected void init() {
        LOGGER.info("CreateInvoiceViewBackingBean - init()");

        suppliers = supplierRepository.findAllActive();
    }

}
