package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.api.data.SupplierRepository;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * List of suppliers that will be used to populate the
     * select menu.
     */
    private List<Supplier> suppliers;

    // Form inputs

    private String verificationNumber;
    private String supplier;
    private String invoiceNumber;
    private String amountWithVat;

    // Getter and setters for form inputs

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getAmountWithVat() {
        return amountWithVat;
    }

    public void setAmountWithVat(String amountWithVat) {
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
                    supplier.getName(),
                    supplier.getName());
            items.add(item);
        }

        return items;
    }

    public String register() {
        LOGGER.info("CreateInvoiceViewBackingBean - register()");

        LOGGER.info("Verification: {}, Supplier: {}, Invoice: {}, Amount: {}",
                new Object[] {
                        getVerificationNumber(),
                        getSupplier(),
                        getInvoiceNumber(),
                        getAmountWithVat()});

        String verificationNumber = getVerificationNumber();
        if (verificationNumber == null || verificationNumber.trim().isEmpty()) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            LOGGER.info("Return to view. verificaiton number Not filled in");

            return "view?faces-redirect=true";
        }

        String supplierName = getSupplier();
        if (supplierName == null || supplierName.trim().isEmpty()) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            LOGGER.info("Return to view. supplier Not filled in");

            return "view?faces-redirect=true";

        }

        String invoiceNumber = getInvoiceNumber();

        String amountWithVat = getAmountWithVat();
        if (amountWithVat == null || amountWithVat.trim().isEmpty()) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            LOGGER.info("Return to view. amount Not filled in");

            return "view?faces-redirect=true";
        }

        BigDecimal amountWithVatDecimal;
        try {
            amountWithVatDecimal = new BigDecimal(amountWithVat);
        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            return "view?faces-redirect=true";
        }

        BigDecimal centWithVatDecimal = amountWithVatDecimal.multiply(new BigDecimal("100"));
        BigDecimal centDecimal = centWithVatDecimal.multiply(new BigDecimal("0.8"));
        BigDecimal vatDecimal = centWithVatDecimal.subtract(centDecimal);

        int amount = centDecimal.intValue();
        int vat = vatDecimal.intValue();

        Supplier s1 = supplierRepository.find(supplierName);

        Invoice invoice = new Invoice();
        invoice.setVerificationNumber(verificationNumber);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setAmount(amount);
        invoice.setVat(vat);
        invoice.setSupplier(s1);

        invoiceService.create(invoice);

        LOGGER.info("Persisted invoice, got id: {}", invoice.getId());

        addGrantBackingBean.init(invoice);

        return "add_grant?&faces-redirect=true" +
                    "&includeViewParams=true";
    }

    public String cancel() {
        LOGGER.info("CreateInvoiceViewBackingBean - cancel()");

        return "view?faces-redirect=true";
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("CreateInvoiceViewBackingBean - init()");

        suppliers = supplierRepository.findAll();
    }
}
